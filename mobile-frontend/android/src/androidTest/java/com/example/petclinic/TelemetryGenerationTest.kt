package com.example.petclinic

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Before
import org.junit.After

/**
 * Comprehensive instrumented test that generates telemetry by interacting with all UI elements.
 * This test navigates through all screens, clicks buttons, performs searches, and interacts
 * with various UI components to generate comprehensive telemetry data.
 * 
 * The ANR and Crash buttons are saved for last as they will terminate the test.
 */
@RunWith(AndroidJUnit4::class)
class TelemetryGenerationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun generateComprehensiveTelemetry() {
        // Wait for the app to load
        composeTestRule.waitForIdle()

        navigateToOwnersScreen()
        
        navigateToVetsScreen()
        
        navigateToHomeScreen()
        
        testUIJankFeature()
        
        performFinalNavigationRound()
        
        performDestructiveTests()

        Thread.sleep(30000)
    }

    @Test()
    fun generateCrashTelemetry() {
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("⚠️ Testing & Monitoring").performScrollTo()
        composeTestRule.onNodeWithText("💥 Trigger App Crash").performScrollTo()
        composeTestRule.onNodeWithText("💥 Trigger App Crash").performClick()
        composeTestRule.waitForIdle()
    }

    private fun navigateToOwnersScreen() {
        // Click on the Owners tab in bottom navigation
        composeTestRule.onNodeWithText("Owners").performClick()
        composeTestRule.waitForIdle()
    }

    private fun navigateToVetsScreen() {
        // Click on the Vets tab in bottom navigation
        composeTestRule.onNodeWithText("Veterinarians").performClick()
        composeTestRule.waitForIdle()
    }

    private fun navigateToHomeScreen() {
        // Click on the Home tab in bottom navigation
        composeTestRule.onNodeWithText("Home").performClick()
        composeTestRule.waitForIdle()
    }

    private fun testUIJankFeature() {
        // First, scroll to the testing section at the bottom of the home screen
        composeTestRule.onNodeWithText("⚠️ Testing & Monitoring").performScrollTo()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("🐌 Start UI Jank").performScrollTo()
        // Click the UI Jank button to start jank (use exact text)
        composeTestRule.onNodeWithText("🐌 Start UI Jank").performClick()
        composeTestRule.waitForIdle()

        // Let jank run for a few seconds to generate telemetry
        Thread.sleep(5000)

        composeTestRule.onNodeWithText("✅ Stop UI Jank").performScrollTo()
        
        // Try to stop the jank - look for either the stop button or start button
        composeTestRule.onNodeWithText("✅ Stop UI Jank").performClick()
        composeTestRule.waitForIdle()

    }

    private fun performFinalNavigationRound() {
        // Do another round of navigation to generate more telemetry
        
        // Go to Owners
        composeTestRule.onNodeWithText("Owners").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(2000)
        
        // Go to Vets
        composeTestRule.onNodeWithText("Veterinarians").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(2000)
        
        // Go back to Home
        composeTestRule.onNodeWithText("Home").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(1000)
    }

    private fun performDestructiveTests() {
        // Scroll to the testing section
        composeTestRule.onNodeWithText("⚠️ Testing & Monitoring").performScrollTo()
        
        // Test ANR first (less destructive than crash)
        try {
            composeTestRule.onNodeWithText("⏰ Trigger ANR (10s block)").performScrollTo()
            composeTestRule.onNodeWithText("⏰ Trigger ANR (10s block)").performClick()
            // This will cause ANR, so the test might not continue from here
            Thread.sleep(15000) // Wait longer than the ANR timeout
        } catch (e: Exception) {
            // ANR occurred, which is expected
        }
    }
}
