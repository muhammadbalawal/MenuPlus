package com.example.emptyactivity.domain.usecase.menu

import com.example.emptyactivity.data.remote.gemini.GeminiClient
import com.example.emptyactivity.data.repository.profile.UserProfileRepository
import com.example.emptyactivity.util.Result
import javax.inject.Inject

class AnalyzeMenuUseCase @Inject constructor(
    private val geminiClient: GeminiClient,
    private val userProfileRepository: UserProfileRepository
) {
    suspend operator fun invoke(
        userId: String,
        menuText: String
    ): Result<String> {
        return try {
            // Get user profile
            val profileResult = userProfileRepository.getUserProfile(userId)

            if (profileResult is Result.Error) {
                return Result.Error("Failed to load user profile: ${profileResult.message}")
            }

            val profile = (profileResult as Result.Success).data
                ?: return Result.Error("User profile not found")

            val analysis = geminiClient.analyzeMenu(
                menuText = menuText,
                userLanguage = profile.preferredLanguage,
                userAllergies = profile.allergies,
                userDietaryRestrictions = profile.dietaryRestrictions,
                userDislikes = profile.dislikes,
                userPreferences = profile.preferences
            )

            Result.Success(analysis)
        } catch (e: Exception) {
            Result.Error("Failed to analyze menu: ${e.message}", e)
        }
    }
}