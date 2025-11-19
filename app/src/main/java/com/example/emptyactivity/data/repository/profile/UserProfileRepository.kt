package com.example.emptyactivity.data.repository.profile

import com.example.emptyactivity.domain.model.Language
import com.example.emptyactivity.domain.model.UserProfile
import com.example.emptyactivity.util.Result
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for user profile operations.
 *
 * This repository abstracts user profile functionality, providing methods for retrieving,
 * creating, and updating user dietary profiles. It follows the repository pattern to decouple
 * the domain layer from the specific data source implementation (Supabase).
 *
 * The repository handles:
 * - User dietary preferences (allergies, restrictions, dislikes, preferences)
 * - Language preferences
 * - Reactive observation of profile changes via Flow
 */
interface UserProfileRepository {
    suspend fun getAllLanguages(): Result<List<Language>>

    suspend fun getUserProfile(userId: String): Result<UserProfile?>

    suspend fun createUserProfile(
        userId: String,
        preferredLanguageId: String,
        allergies: List<String>,
        dietaryRestrictions: List<String>,
        dislikes: List<String>,
        preferences: List<String>,
    ): Result<UserProfile>

    suspend fun updateUserProfile(
        userId: String,
        preferredLanguageId: String,
        allergies: List<String>,
        dietaryRestrictions: List<String>,
        dislikes: List<String>,
        preferences: List<String>,
    ): Result<UserProfile>

    fun observeUserProfile(userId: String): Flow<UserProfile?>
}


