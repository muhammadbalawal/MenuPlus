package com.example.emptyactivity.domain.usecase.profile

import android.util.Log
import com.example.emptyactivity.data.repository.auth.AuthRepository
import com.example.emptyactivity.data.repository.profile.UserProfileRepository
import com.example.emptyactivity.domain.model.UserProfile
import com.example.emptyactivity.util.Result
import javax.inject.Inject

/**
 * Use case for saving or updating a user's dietary profile.
 *
 * This use case orchestrates the complete profile save workflow:
 * 1. Validates that a language has been selected
 * 2. Checks if a profile already exists for the user
 * 3. Creates a new profile or updates the existing one
 * 4. Marks onboarding as complete in the user's auth metadata
 * 5. Refreshes the session to apply the onboarding status change
 *
 * The use case handles errors at each step and returns appropriate Result types
 * for the UI layer to handle (success, error, loading).
 *
 * @param userProfileRepository Repository for saving/updating user profile data.
 * @param authRepository Repository for updating user authentication metadata.
 */
class SaveUserProfileUseCase
    @Inject
    constructor(
        private val userProfileRepository: UserProfileRepository,
        private val authRepository: AuthRepository,
    ) {
        companion object {
            private const val TAG = "SaveUserProfileUseCase"
        }

        suspend operator fun invoke(
            userId: String,
            preferredLanguageId: String,
            allergies: List<String>,
            dietaryRestrictions: List<String>,
            dislikes: List<String>,
            preferences: List<String>,
        ): Result<UserProfile> {
            // Validation
            if (preferredLanguageId.isBlank()) {
                return Result.Error("Please select a language")
            }

            Log.d(TAG, "Saving user profile for userId: $userId")

            // Check if profile exists
            val existingProfile =
                when (val result = userProfileRepository.getUserProfile(userId)) {
                    is Result.Success -> result.data
                    is Result.Error -> return result
                    is Result.Loading -> null
                }

            // Create or update profile
            val profileResult =
                if (existingProfile == null) {
                    Log.d(TAG, "Creating new profile")
                    userProfileRepository.createUserProfile(
                        userId = userId,
                        preferredLanguageId = preferredLanguageId,
                        allergies = allergies,
                        dietaryRestrictions = dietaryRestrictions,
                        dislikes = dislikes,
                        preferences = preferences,
                    )
                } else {
                    Log.d(TAG, "Updating existing profile")
                    userProfileRepository.updateUserProfile(
                        userId = userId,
                        preferredLanguageId = preferredLanguageId,
                        allergies = allergies,
                        dietaryRestrictions = dietaryRestrictions,
                        dislikes = dislikes,
                        preferences = preferences,
                    )
                }

            if (profileResult is Result.Success) {
                Log.d(TAG, "Profile saved successfully, marking onboarding complete")

                // Mark onboarding as complete in auth metadata
                val onboardingResult = authRepository.completeOnboarding()

                if (onboardingResult is Result.Error) {
                    Log.e(TAG, "Failed to mark onboarding complete: ${onboardingResult.message}")
                    return Result.Error("Profile saved but failed to complete onboarding: ${onboardingResult.message}")
                }

                Log.d(TAG, "Refreshing session to apply onboarding status")
                val refreshResult = authRepository.refreshSession()

                if (refreshResult is Result.Error) {
                    Log.e(TAG, "Failed to refresh session: ${refreshResult.message}")
                    return Result.Error("Profile saved but failed to refresh session: ${refreshResult.message}")
                }

                Log.d(TAG, "Onboarding complete and session refreshed successfully")
            }

            return profileResult
        }
    }
