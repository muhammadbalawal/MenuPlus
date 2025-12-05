package com.example.emptyactivity.domain.usecase.menu

import com.example.emptyactivity.data.remote.gemini.GeminiClient
import com.example.emptyactivity.data.repository.profile.UserProfileRepository
import com.example.emptyactivity.util.Result
import javax.inject.Inject

/**
 * Use case for analyzing menu text with Gemini AI.
 *
 * This use case orchestrates the menu analysis workflow:
 * 1. Fetches the user's dietary profile from the repository
 * 2. Sends the menu text and profile to Gemini AI for analysis
 * 3. Returns the personalized analysis result
 *
 * The use case handles errors at each step and returns appropriate Result types
 * for the UI layer to handle (success, error, loading).
 *
 * @param geminiClient The Gemini AI client for sending analysis requests.
 * @param userProfileRepository Repository for fetching user dietary profile data.
 *
 * Mostly created by: Muhammad
 */
class AnalyzeMenuUseCase
    @Inject
    constructor(
        private val geminiClient: GeminiClient,
        private val userProfileRepository: UserProfileRepository,
    ) {
        /**
         * Analyzes a menu for a specific user.
         *
         * This method fetches the user's dietary profile (allergies, restrictions, preferences)
         * and sends it along with the menu text to Gemini AI. The AI returns a personalized
         * analysis with safety ratings and recommendations.
         *
         * @param userId The unique identifier of the user requesting the analysis.
         * @param menuText The menu text extracted from OCR that needs to be analyzed.
         * @return Result containing the analysis string on success, or an error message
         *         if the profile can't be loaded or the analysis fails.
         */
        suspend operator fun invoke(
            userId: String,
            menuText: String,
        ): Result<String> {
            return try {
                // Get user profile
                val profileResult = userProfileRepository.getUserProfile(userId)

                if (profileResult is Result.Error) {
                    return Result.Error("Failed to load user profile: ${profileResult.message}")
                }

                val profile =
                    (profileResult as Result.Success).data
                        ?: return Result.Error("User profile not found")

                val analysis =
                    geminiClient.analyzeMenu(
                        menuText = menuText,
                        userLanguage = profile.preferredLanguageName,
                        userAllergies = profile.allergies,
                        userDietaryRestrictions = profile.dietaryRestrictions,
                        userDislikes = profile.dislikes,
                        userPreferences = profile.preferences,
                    )

                Result.Success(analysis)
            } catch (e: Exception) {
                Result.Error("Failed to analyze menu: ${e.message}", e)
            }
        }
    }
