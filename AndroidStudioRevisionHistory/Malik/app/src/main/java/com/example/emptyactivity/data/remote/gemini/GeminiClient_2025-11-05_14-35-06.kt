package com.example.emptyactivity.data.remote.gemini

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.ai
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiClient @Inject constructor() {

    companion object {
        private const val TAG = "GeminiClient"
        private const val MODEL_NAME = "gemini-2.5-flash"
    }

    private val model = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel(MODEL_NAME)

    suspend fun analyzeMenu(
        menuText: String,
        userLanguage: String?,
        userAllergies: List<String>,
        userDietaryRestrictions: List<String>,
        userDislikes: List<String>,
        userPreferences: List<String>,
    ): String {
        return try {
            val prompt = buildMenuAnalysisPrompt(
                menuText = menuText,
                userLanguage = userLanguage,
                allergies = userAllergies,
                dietaryRestrictions = userDietaryRestrictions,
                dislikes = userDislikes,
                preferences = userPreferences
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
    }

    private fun buildMenuAnalysisPrompt(
        menuText: String,
        userLanguage: String?,
        allergies: List<String>,
        dietaryRestrictions: List<String>,
        dislikes: List<String>,
        preferences: List<String>
    ): String {
        return """
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
            1. Convert the menu into user language.
            2. Analyze each menu item for safety based on the user's profile
            3. Classify items into three categories:
               - RED (Dangerous): Contains allergies or violates Dietary restrictions
               - YELLOW (Caution): Contains dislikes or minor concerns
               - GREEN (Safe): No concerns, good choice
            
            3. For each item, provide:
               - Safety rating (RED/YELLOW/GREEN)
               - Clear reason for the rating
               - Any ingredients of concern
               - Suggestions or alternatives if applicable
            
            4. At the end, provide:
               - Summary of safest options
               - Items to absolutely avoid
               - Recommended dishes based on user preferences
            
            FORMAT:
            Please structure your response clearly with easy-to-read formatting.
        """.trimIndent()
    }
}