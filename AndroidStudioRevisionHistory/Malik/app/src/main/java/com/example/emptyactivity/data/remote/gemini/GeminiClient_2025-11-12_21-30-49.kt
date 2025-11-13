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

        /**
         * Builds the prompt sent to Gemini AI for menu analysis.
         *
         * This method constructs a detailed prompt that instructs Gemini on how to analyze
         * the menu. The prompt includes:
         * - User's complete dietary profile
         * - The menu text to analyze
         * - Detailed instructions on how to categorize items (RED/YELLOW/GREEN)
         * - Formatting requirements for the response
         *
         * @param menuText The menu text to analyze.
         * @param userLanguage User's preferred language for translation.
         * @param allergies List of user allergies.
         * @param dietaryRestrictions List of dietary restrictions.
         * @param dislikes List of food dislikes.
         * @param preferences List of food preferences.
         * @return Complete prompt string ready to send to Gemini AI.
         */
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
            Analyze each menu item and provide THREE DISTINCT SECTIONS in your response, separated by the markers below.
            
            1. Convert menu to user language if needed
            2. Classify items: RED (contains allergies/restrictions), YELLOW (contains dislikes), GREEN (safe)
            3. Structure your response EXACTLY as follows:
            
            === SAFE MENU START ===
            List ONLY the items marked as GREEN (completely safe for this user).
            Format: Item name, brief description, why it's safe.
            Focus on what they CAN eat without worry.
            === SAFE MENU END ===
            
            === BEST MENU START ===
            Provide TOP 5 personalized recommendations based on their preferences.
            Format: Ranked list with detailed reasoning.
            Explain why each recommendation matches their taste profile.
            Include flavor profiles and what makes each dish special.
            === BEST MENU END ===
            
            === FULL MENU START ===
            Complete menu with ALL items annotated with safety ratings.
            Format each item as:
            [RED/YELLOW/GREEN] Item Name
            - Description (translated)
            - Concerns: (allergies, restrictions, or dislikes present)
            - Recommendation: (safe to eat / avoid / ask about ingredients)
            
            Include summary at end:
            SUMMARY:
            ✅ Safest Options: [list]
            ⚠️ Items to Avoid: [list]
            💡 Ask Staff About: [items needing clarification]
            === FULL MENU END ===
            
            IMPORTANT: Use the EXACT markers shown above (=== SECTION NAME START/END ===) so the app can parse your response correctly.
            """.trimIndent()
    }
