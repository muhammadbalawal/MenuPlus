package com.example.emptyactivity.data.remote.gemini

import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiClient @Inject constructor() {

    companion object {
        private const val TAG = "GeminiClient"
        private const val MODEL_NAME = "gemini-1.5-flash"
    }

    private val generativeModel = GenerativeModel(
        modelName = MODEL_NAME,
        apiKey = "",
        generationConfig = generationConfig {
            temperature = 0.7f
            topK = 40
            topP = 0.95f
            maxOutputTokens = 8192
        }
    )

    suspend fun analyzeMenu(
        menu: String,
        userAllergens: List<String>,
        userDietaryRestrictions: List<String>,
        userDislikes: List<String>,
        userPreferences: List<String>,
        userLanguage: String
    ) {
        
    }

}