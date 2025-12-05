package com.example.emptyactivity.domain.usecase.profile

import com.example.emptyactivity.data.repository.profile.UserProfileRepository
import com.example.emptyactivity.domain.model.UserProfile
import com.example.emptyactivity.util.Result
import javax.inject.Inject

/**
 * Use case for retrieving a user's profile.
 *
 * This use case handles fetching a user's dietary profile including allergies,
 * preferences, restrictions, and language settings.
 *
 * @param userProfileRepository The repository for accessing user profile data.
 *
 * Mostly created by: Malik
 */
class GetUserProfileUseCase
    @Inject
    constructor(
        private val userProfileRepository: UserProfileRepository,
    ) {
        /**
         * Retrieves the user profile for a given user ID.
         *
         * @param userId The unique identifier of the user.
         * @return Result.Success with UserProfile on success, Result.Error on failure.
         *         Returns null in data if profile doesn't exist.
         */
        suspend operator fun invoke(userId: String): Result<UserProfile?> {
            if (userId.isBlank()) {
                return Result.Error("User ID cannot be empty")
            }

            return userProfileRepository.getUserProfile(userId)
        }
    }
