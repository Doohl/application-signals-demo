package com.example.petclinic

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OwnerDetailNavigationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testNavigationToOwnerDetail() {
        // Wait for initial screen to load
        composeTestRule.waitForIdle()
        Thread.sleep(1000)

        // Step 1: Verify we're on the home screen
        composeTestRule.onNodeWithText("Home").assertExists()
        Thread.sleep(1000)

        // Step 2: Navigate to Owners screen
        composeTestRule.onNodeWithText("Owners").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(3000) // Wait for owners list to load

        // Step 3: Click on the first owner card to navigate to owner detail
        // Wait for owners to load and find the first owner card

        composeTestRule.onAllNodesWithText("Phone:", substring = true)
            .onFirst()
            .performClick()
        
        composeTestRule.waitForIdle()
        Thread.sleep(5000)
        println("Navigation succeeded without crash")
    }


    fun testNavigationToOwnerDetailMultipleTimes() {
        composeTestRule.waitForIdle()
        
        // Test expects crash to occur - if it does, test passes
        try {
            // Repeat the navigation flow 5 times to catch intermittent crashes
            repeat(5) { iteration ->
                println("Iteration ${iteration + 1}")
                
                // Navigate to Owners screen
                composeTestRule.onNodeWithText("Owners").performClick()
                composeTestRule.waitForIdle()
                Thread.sleep(1500)

                // Click on first owner - crash should occur here
                composeTestRule.onAllNodesWithText("Phone:", substring = true)
                    .onFirst()
                    .performClick()
                
                composeTestRule.waitForIdle()
                Thread.sleep(5000)
                
                println("Iteration ${iteration + 1}: Navigation succeeded without crash")
            }
            
            // If we reach here, no crash occurred in any iteration - test should fail
            throw AssertionError("Test FAILED: Expected app to crash when navigating to owner detail, but no crash occurred in any of the 5 iterations")
            
        } catch (e: AssertionError) {
            // Re-throw assertion errors (our own failure message)
            throw e
        } catch (e: Exception) {
            // Expected crash occurred - test passes
            println("Expected crash occurred: ${e.message}")
            // Test passes by returning normally
        }
    }
}
