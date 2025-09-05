package com.example.petclinic

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserInteractionTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun simulateRealisticUserSession() {
        composeTestRule.waitForIdle()

        (1..200).forEach { _ ->
            // Home screen interaction (3-4 seconds)
            stayAndInteractOnScreen(3500) {
                scrollUpAndDown()
                clickSafeTestingButtons()
            }

            // Navigate to Owners and interact
            navigateToOwnersScreen()
            stayAndInteractOnScreen(4000) {
                scrollUpAndDown()
                testAddOwnerFlow()
            }

            // Navigate to Vets and interact
            navigateToVetsScreen()
            stayAndInteractOnScreen(2500) {
                scrollUpAndDown()
            }

            // Return to Home for final interaction
            navigateToHomeScreen()
            stayAndInteractOnScreen(2000) {
                scrollUpAndDown()
            }
        }
    }

    private fun stayAndInteractOnScreen(durationMs: Long, interactions: () -> Unit) {
        val startTime = System.currentTimeMillis()
        
        while (System.currentTimeMillis() - startTime < durationMs) {
            interactions()
            Thread.sleep(500) // Brief pause between interactions
        }
    }

    private fun scrollUpAndDown() {
        // Smooth scrolling simulation
        composeTestRule.onRoot().performTouchInput {
            swipeUp(startY = centerY + 200, endY = centerY - 200, durationMillis = 800)
        }
        Thread.sleep(300)
        
        composeTestRule.onRoot().performTouchInput {
            swipeDown(startY = centerY - 200, endY = centerY + 200, durationMillis = 800)
        }
        Thread.sleep(300)
    }

    private fun clickSafeTestingButtons() {
        try {
            composeTestRule.onNodeWithText("🌐 HTTP 500 Error").performClick()
            Thread.sleep(800)
        } catch (e: Exception) {
            // Continue if buttons not found
        }
    }

    private fun navigateToOwnersScreen() {
        composeTestRule.onNodeWithText("Owners").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(300)
    }

    private fun navigateToVetsScreen() {
        composeTestRule.onNodeWithText("Veterinarians").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(300)
    }

    private fun navigateToHomeScreen() {
        composeTestRule.onNodeWithText("Home").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(300)
    }

    private fun testAddOwnerFlow() {
        try {
            composeTestRule.onNodeWithContentDescription("Add Owner").performClick()
            Thread.sleep(1000)
            composeTestRule.onNodeWithContentDescription("Back").performClick()
            Thread.sleep(500)
        } catch (e: Exception) {
            // Continue if Add Owner button not found
        }
    }
}
