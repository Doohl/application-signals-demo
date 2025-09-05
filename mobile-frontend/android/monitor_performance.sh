#!/bin/bash

# Performance monitoring script for instrumented tests
# Usage: ./monitor_performance.sh com.yourapp.package

if [ $# -eq 0 ]; then
    echo "Usage: $0 <package_name>"
    echo "Example: $0 com.yourapp"
    exit 1
fi

PACKAGE_NAME=$1
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
OUTPUT_DIR="performance_results_${TIMESTAMP}"

echo "Starting performance monitoring for $PACKAGE_NAME"
echo "Results will be saved to $OUTPUT_DIR/"

# Create output directory
mkdir -p $OUTPUT_DIR

# Standardize device settings for consistent testing
echo "Standardizing device settings..."
adb shell settings put global airplane_mode_on 0
adb shell settings put global wifi_on 1
adb shell settings put system screen_brightness 128
adb shell settings put system screen_off_timeout 600000  # 10 min

# Reset battery stats
echo "Resetting battery stats..."
adb shell dumpsys batterystats --reset

# Start monitoring functions
monitor_cpu() {
    echo "Starting CPU monitoring..."
    echo "Timestamp,PID,CPU%,Memory" > $OUTPUT_DIR/cpu_usage.log
    while true; do
        # Get PID first, then get CPU usage
        PID=$(adb shell pidof $PACKAGE_NAME)
        if [ ! -z "$PID" ]; then
            # Extract CPU% and memory from top output, clean ANSI codes
            CPU_DATA=$(adb shell top -p $PID -n 1 | tail -n 1 | sed 's/\x1b\[[0-9;]*m//g' | awk '{print $9","$6}')
            echo "$(date +"%H:%M:%S"),$PID,$CPU_DATA" >> $OUTPUT_DIR/cpu_usage.log
        else
            echo "$(date +"%H:%M:%S"),N/A,0.0,0M" >> $OUTPUT_DIR/cpu_usage.log
        fi
        sleep 5
    done
}

monitor_memory() {
    echo "Starting memory monitoring..."
    while true; do
        echo "$(date): $(adb shell dumpsys meminfo $PACKAGE_NAME | grep 'TOTAL')" >> $OUTPUT_DIR/memory_usage.log
        sleep 10
    done
}

monitor_frames() {
    echo "Starting frame monitoring..."
    echo "Timestamp,Total_Frames,Janky_Frames,Jank_Percentage" > $OUTPUT_DIR/frame_stats.log
    while true; do
        # Get frame stats and extract key metrics
        FRAME_DATA=$(adb shell dumpsys gfxinfo $PACKAGE_NAME | grep -A 1 "Total frames rendered:")
        if [ ! -z "$FRAME_DATA" ]; then
            TOTAL=$(echo "$FRAME_DATA" | grep "Total frames" | awk '{print $4}')
            JANKY=$(echo "$FRAME_DATA" | grep "Janky frames" | awk '{print $3}')
            if [ ! -z "$TOTAL" ] && [ ! -z "$JANKY" ] && [ "$TOTAL" -gt 0 ]; then
                JANK_PCT=$(echo "scale=2; $JANKY * 100 / $TOTAL" | bc -l 2>/dev/null || echo "0")
                echo "$(date +"%H:%M:%S"),$TOTAL,$JANKY,$JANK_PCT%" >> $OUTPUT_DIR/frame_stats.log
            fi
        fi
        sleep 15
    done
}

# Start background monitoring
monitor_cpu &
CPU_PID=$!

monitor_memory &
MEMORY_PID=$!

monitor_frames &
FRAMES_PID=$!

echo "Monitoring started. Running instrumented tests..."
echo "CPU monitoring PID: $CPU_PID"
echo "Memory monitoring PID: $MEMORY_PID"
echo "Frame monitoring PID: $FRAMES_PID"

# Run the instrumented tests
./gradlew connectedAndroidTest

echo "Tests completed. Collecting final stats..."

# Stop monitoring
kill $CPU_PID $MEMORY_PID $FRAMES_PID 2>/dev/null

# Collect battery stats
echo "Collecting battery statistics..."
adb shell dumpsys batterystats $PACKAGE_NAME > $OUTPUT_DIR/battery_stats.txt

# Collect final memory snapshot
adb shell dumpsys meminfo $PACKAGE_NAME > $OUTPUT_DIR/final_memory.txt

# Collect final frame stats
adb shell dumpsys gfxinfo $PACKAGE_NAME > $OUTPUT_DIR/final_frame_stats.txt

# Generate summary
echo "=== Performance Test Summary ===" > $OUTPUT_DIR/summary.txt
echo "Package: $PACKAGE_NAME" >> $OUTPUT_DIR/summary.txt
echo "Timestamp: $TIMESTAMP" >> $OUTPUT_DIR/summary.txt
echo "Test Duration: $(grep -c "$(date +%Y-%m-%d)" $OUTPUT_DIR/memory_usage.log) samples" >> $OUTPUT_DIR/summary.txt

echo ""
echo "Performance monitoring completed!"
echo "Results saved to: $OUTPUT_DIR/"
echo ""
echo "Files generated:"
echo "  - cpu_usage.log: CPU usage over time"
echo "  - memory_usage.log: Memory usage over time"  
echo "  - frame_stats.log: UI performance and jank metrics"
echo "  - battery_stats.txt: Battery consumption details"
echo "  - final_memory.txt: Final memory state"
echo "  - final_frame_stats.txt: Complete frame statistics"
echo "  - summary.txt: Test summary"
