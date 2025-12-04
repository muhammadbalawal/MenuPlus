package com.example.emptyactivity.data.remote.gemini

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Client for interacting with Google's Gemini AI model via Firebase AI SDK.
 *
 * This client handles menu analysis by sending menu text and user dietary profile
 * to Gemini AI and receiving personalized recommendations. The analysis includes:
 * - Safety ratings (RED/YELLOW/GREEN) for each menu item
 * - Allergy warnings and ingredient concerns
 * - Dietary restriction violations
 * - Personalized recommendations based on preferences
 * - Menu translation to user's preferred language
 *
 * The client uses Firebase AI SDK which provides a unified interface to Google's
 * generative AI models. It's configured as a singleton to ensure efficient resource usage.
 */
@Singleton
class GeminiClient
    @Inject
    constructor() {
        companion object {
            private const val TAG = "GeminiClient"
            private const val MODEL_NAME = "gemini-2.5-flash"
        }

        private val model =
            Firebase
                .ai(backend = GenerativeBackend.googleAI())
                .generativeModel(MODEL_NAME)

        /**
         * Analyzes a menu using Gemini AI based on the user's dietary profile.
         *
         * This method builds a comprehensive prompt that includes the menu text and
         * all user dietary information, then sends it to Gemini for analysis. The AI
         * returns a formatted analysis with safety ratings and recommendations.
         *
         * @param menuText The complete menu text extracted from OCR, typically a multi-line string.
         * @param userLanguage The user's preferred language for menu translation (e.g., "English", "French").
         * @param userAllergies List of allergens the user must avoid (critical safety information).
         * @param userDietaryRestrictions List of dietary restrictions (vegan, halal, kosher, etc.).
         * @param userDislikes List of foods the user prefers not to eat.
         * @param userPreferences List of foods the user enjoys.
         * @return Formatted analysis string with safety ratings, warnings, and recommendations.
         *         Returns an error message string if the analysis fails.
         */
        suspend fun analyzeMenu(
            menuText: String,
            userLanguage: String?,
            userAllergies: List<String>,
            userDietaryRestrictions: List<String>,
            userDislikes: List<String>,
            userPreferences: List<String>,
        ): String =
            try {
                val prompt =
                    buildMenuAnalysisPrompt(
                        menuText = menuText,
                        userLanguage = userLanguage,
                        allergies = userAllergies,
                        dietaryRestrictions = userDietaryRestrictions,
                        dislikes = userDislikes,
                        preferences = userPreferences,
                    )

                Log.d(TAG, "Sending prompt to Gemini")
                val response = model.generateContent(prompt)

                val result = response.text ?: "No response from AI"
                Log.d(TAG, "Received response from Gemini: ${result.length} characters")

                result
            } catch (e: Exception) {
                Log.e(TAG, "Error analyzing menu with Gemini", e)
                "Error analyzing menu: ${e.message}"
            }

        private fun buildMenuAnalysisPrompt(
    menuText: String,
    userLanguage: String?,
    allergies: List<String>,
    dietaryRestrictions: List<String>,
    dislikes: List<String>,
    preferences: List<String>,
): String =
    """
    You are MenuPlus AI, an expert food safety assistant. Analyze this restaurant menu based on the user's dietary profile.
    
    USER PROFILE:
    - Allergies (CRITICAL - these can cause serious health reactions): ${allergies.joinToString(", ").ifEmpty { "None" }}
    - Dietary Restrictions (must avoid): ${dietaryRestrictions.joinToString(", ").ifEmpty { "None" }}
    - Dislikes (prefers not to eat): ${dislikes.joinToString(", ").ifEmpty { "None" }}
    - Preferences (enjoys eating): ${preferences.joinToString(", ").ifEmpty { "None" }}
    - Language: $userLanguage
    
    MENU:
    $menuText
    
    INSTRUCTIONS:
    1. Convert menu to user language if needed
    2. Analyze each menu item and classify safety: RED (contains allergies/restrictions), YELLOW (contains dislikes), GREEN (safe)
    3. Rank items by what's BEST for the user (considering preferences, safety, and match quality)
    4. Return ONLY valid JSON in this exact format (no markdown, no code blocks):
    
    {
      "menuItems": [
        {
          "name": "Item Name",
          "description": "Item description in user's language",
          "price": "Price if available (e.g., '$15.99' or null)",
          "safetyRating": "GREEN|YELLOW|RED",
          "allergies": ["allergy1", "allergy2"],
          "dietaryRestrictions": ["restriction1"],
          "dislikes": ["dislike1"],
          "preferences": ["preference1"],
          "recommendation": "Why this item is recommended or concerns",
          "rank": 1
        }
      ]
    }
    
    IMPORTANT:
    - Return ONLY the JSON object, no other text
    - Order items by rank (1 = best for user, higher numbers = less ideal)
    - Include ALL menu items
    - Extract price from menu text if available
    - Be specific about which allergies/restrictions/dislikes/preferences apply to each item
    """.trimIndent()
    }
