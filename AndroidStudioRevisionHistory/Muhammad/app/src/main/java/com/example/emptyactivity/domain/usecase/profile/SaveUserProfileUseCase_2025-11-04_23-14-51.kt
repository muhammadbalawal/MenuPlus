package com.example.emptyactivity.domain.usecase.profile

import com.example.emptyactivity.data.repository.auth.AuthRepository
import com.example.emptyactivity.data.repository.profile.UserProfileRepository
import com.example.emptyactivity.domain.model.UserProfile
import com.example.emptyactivity.util.Result
import javax.inject.Inject

class SaveUserProfileUseCase @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        userId: String,
        preferredLanguageId: String,
        allergies: List<String>,
        dietaryRestrictions: List<String>,
        dislikes: List<String>,
        preferences: List<String>
    ): Result<UserProfile> {

        // Validation
        if (preferredLanguageId.isBlank()) {
            return Result.Error("Please select a language")
        }

        // Check if profile already exists
        val existingProfile = when (val result = userProfileRepository.getUserProfile(userId)) {
            is Result.Success -> result.data
            is Result.Error -> return result
            is Result.Loading -> null
        }

        return if (existingProfile == null) {
            val result = userProfileRepository.createUserProfile(
                userId = userId,
                preferredLanguageId = preferredLanguageId,
                allergies = allergies,
                dietaryRestrictions = dietaryRestrictions,
                dislikes = dislikes,
                preferences = preferences
            )

            if (result is Result.Success) {
//                authRepository.completeOnboarding()
            }

            result
        } else {
            userProfileRepository.updateUserProfile(
                userId = userId,
                preferredLanguageId = preferredLanguageId,
                allergies = allergies,
                dietaryRestrictions = dietaryRestrictions,
                dislikes = dislikes,
                preferences = preferences
            )
        }
    }
}