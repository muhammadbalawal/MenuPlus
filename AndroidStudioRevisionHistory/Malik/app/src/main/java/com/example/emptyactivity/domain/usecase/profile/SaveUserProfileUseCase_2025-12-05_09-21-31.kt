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
 * This use case handles the complete workflow of saving a user's dietary profile, including:
 * - Validating that a language is selected
 * - Checking if a profile already exists (to determine create vs update)
 * - Creating a new profile or updating an existing one
 * - Marking onboarding as complete in the authentication metadata
 * - Refreshing the authentication session to apply the onboarding status
 *
 * The use case ensures that when a profile is saved during onboarding, the user's
 * authentication state is updated to reflect that onboarding is complete, which triggers
 * navigation to the authenticated screens.
 *
 * @param userProfileRepository The repository for accessing user profile data.
 * @param authRepository The repository for managing authentication state and metadata.
 *                       Both injected via Hilt.
 *
 * Mostly created by: Muhammad
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

        /**
         * Saves or updates a user's dietary profile.
         *
         * This method performs validation, checks for existing profiles, and either creates
         * a new profile or updates an existing one. After successful save, it marks onboarding
         * as complete and refreshes the authentication session.
         *
         * @param userId The unique identifier of the user whose profile is being saved.
         * @param preferredLanguageId The ID of the user's preferred language. Must not be blank.
         * @param allergies List of allergens the user must avoid. Critical safety information.
         * @param dietaryRestrictions List of dietary restrictions (vegan, halal, kosher, etc.).
         * @param dislikes List of foods the user prefers not to eat.
         * @param preferences List of foods the user enjoys.
         * @return Result containing the saved UserProfile on success, or an error message
         *         if validation fails, profile save fails, or onboarding completion fails.
         */
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
