package com.example.emptyactivity.data.repository.profile

import com.example.emptyactivity.domain.model.Language
import com.example.emptyactivity.domain.model.UserProfile
import com.example.emptyactivity.util.Result
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for user profile data operations.
 *
 * This repository abstracts user profile persistence operations, allowing the app to manage
 * user dietary profiles (allergies, preferences, restrictions, language) without directly
 * depending on the specific database implementation (currently Supabase).
 *
 * The repository pattern allows us to:
 * - Switch database providers in the future without changing business logic
 * - Easily mock this interface for testing
 * - Keep database implementation details separate from the domain layer
 *
 * All operations return Result types for consistent error handling, and the repository
 * provides a reactive Flow for observing profile changes.
 */
interface UserProfileRepository {
    /**
     * Retrieves all available languages from the database.
     *
     * Languages are used for menu translation and UI personalization. This method fetches
     * the complete list of supported languages.
     *
     * @return Result containing a list of Language objects, or an error if the operation fails.
     */
    suspend fun getAllLanguages(): Result<List<Language>>

    /**
     * Retrieves a user's dietary profile by their user ID.
     *
     * @param userId The unique identifier of the user whose profile should be retrieved.
     * @return Result containing the UserProfile if found, null if not found, or an error
     *         if the operation fails.
     */
    suspend fun getUserProfile(userId: String): Result<UserProfile?>

    /**
     * Creates a new user profile in the database.
     *
     * This method is used during onboarding to create a user's initial dietary profile.
     *
     * @param userId The unique identifier of the user.
     * @param preferredLanguageId The ID of the user's preferred language.
     * @param allergies List of allergens the user must avoid.
     * @param dietaryRestrictions List of dietary restrictions (vegan, halal, kosher, etc.).
     * @param dislikes List of foods the user prefers not to eat.
     * @param preferences List of foods the user enjoys.
     * @return Result containing the created UserProfile, or an error if the operation fails.
     */
    suspend fun createUserProfile(
        userId: String,
        preferredLanguageId: String,
        allergies: List<String>,
        dietaryRestrictions: List<String>,
        dislikes: List<String>,
        preferences: List<String>,
    ): Result<UserProfile>

    /**
     * Updates an existing user profile in the database.
     *
     * This method is used when a user edits their dietary profile from the ProfileScreen.
     *
     * @param userId The unique identifier of the user whose profile should be updated.
     * @param preferredLanguageId The ID of the user's preferred language.
     * @param allergies List of allergens the user must avoid.
     * @param dietaryRestrictions List of dietary restrictions (vegan, halal, kosher, etc.).
     * @param dislikes List of foods the user prefers not to eat.
     * @param preferences List of foods the user enjoys.
     * @return Result containing the updated UserProfile, or an error if the operation fails.
     */
    suspend fun updateUserProfile(
        userId: String,
        preferredLanguageId: String,
        allergies: List<String>,
        dietaryRestrictions: List<String>,
        dislikes: List<String>,
        preferences: List<String>,
    ): Result<UserProfile>

    /**
     * Observes a user's profile as a reactive Flow.
     *
     * This Flow emits the current UserProfile whenever it changes, allowing the UI to
     * automatically update when profile data is modified.
     *
     * @param userId The unique identifier of the user whose profile should be observed.
     * @return A Flow that emits the current UserProfile or null if not found.
     */
    fun observeUserProfile(userId: String): Flow<UserProfile?>
}


