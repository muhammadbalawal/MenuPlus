package com.example.emptyactivity.data.repository.profile

import android.util.Log
import com.example.emptyactivity.data.remote.supabase.SupabaseClientProvider
import com.example.emptyactivity.data.remote.supabase.dto.CreateUserProfileDto
import com.example.emptyactivity.data.remote.supabase.dto.LanguageDto
import com.example.emptyactivity.data.remote.supabase.dto.UserProfileDto
import com.example.emptyactivity.domain.model.Language
import com.example.emptyactivity.domain.model.UserProfile
import com.example.emptyactivity.util.Result
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserProfileRepositoryImpl @Inject constructor() : UserProfileRepository {

    private val supabase = SupabaseClientProvider.client

    companion object {
        private const val TAG = "UserProfileRepository"
        private const val TABLE_USER_PROFILE = "userProfile"
        private const val TABLE_LANGUAGE = "language"
    }

    override suspend fun getAllLanguages(): Result<List<Language>> {
        return try {
            Log.d(TAG, "Fetching all languages")

            val languages = supabase
                .from(TABLE_LANGUAGE)
                .select()
                .decodeList<LanguageDto>()

            Log.d(TAG, "Fetched ${languages.size} languages")

            val languageModels = languages.map { dto ->
                Language(
                    id = dto.languageId,
                    name = dto.languageName
                )
            }

            Result.Success(languageModels)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching languages", e)
            Result.Error("Failed to load languages: ${e.message}", e)
        }
    }

    override suspend fun getUserProfile(userId: String): Result<UserProfile?> {
        return try {
            Log.d(TAG, "Fetching user profile for: $userId")

            val profiles = supabase
                .from(TABLE_USER_PROFILE)
                .select(
                    columns = Columns.raw("""
                        user_id,
                        preferred_language,
                        user_allergies,
                        user_dietary_restrictions,
                        user_dislikes,
                        user_preferences,
                        language:preferred_language(language_id, language_name)
                    """.trimIndent())
                ) {
                    filter {
                        eq("user_id", userId)
                    }
                }
                .decodeList<UserProfileDto>()

            if (profiles.isEmpty()) {
                Log.d(TAG, "No profile found for user: $userId")
                return Result.Success(null)
            }

            val profileDto = profiles.first()
            val userProfile = profileDto.toDomain()

            Log.d(TAG, "User profile fetched successfully")
            Result.Success(userProfile)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching user profile", e)
            Result.Error("Failed to load profile: ${e.message}", e)
        }
    }

    override suspend fun createUserProfile(
        userId: String,
        preferredLanguageId: String,
        allergies: List<String>,
        dietaryRestrictions: List<String>,
        dislikes: List<String>,
        preferences: List<String>
    ): Result<UserProfile> {
        return try {
            Log.d(TAG, "Creating user profile for: $userId")

            val createDto = CreateUserProfileDto(
                userId = userId,
                preferredLanguageId = preferredLanguageId,
                userAllergies = allergies.joinToString(","),
                userDietaryRestrictions = dietaryRestrictions.joinToString(","),
                userDislikes = dislikes.joinToString(","),
                userPreferences = preferences.joinToString(",")
            )

            supabase
                .from(TABLE_USER_PROFILE)
                .insert(createDto)
            Log.d(TAG, "Insert successful")


            Log.d(TAG, "User profile created successfully")

            Result.Success(
                UserProfile(
                    userId = userId,
                    preferredLanguageId = preferredLanguageId,
                    allergies = allergies,
                    dietaryRestrictions = dietaryRestrictions,
                    dislikes = dislikes,
                    preferences = preferences
                )
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error creating user profile", e)
            Result.Error("Failed to create profile: ${e.message}", e)
        }
    }

    override suspend fun updateUserProfile(
        userId: String,
        preferredLanguageId: String,
        allergies: List<String>,
        dietaryRestrictions: List<String>,
        dislikes: List<String>,
        preferences: List<String>
    ): Result<UserProfile> {
        return try {
            Log.d(TAG, "Updating user profile for: $userId")

            val updateDto = CreateUserProfileDto(
                userId = userId,
                preferredLanguageId = preferredLanguageId,
                userAllergies = allergies.joinToString(","),
                userDietaryRestrictions = dietaryRestrictions.joinToString(","),
                userDislikes = dislikes.joinToString(","),
                userPreferences = preferences.joinToString(",")
            )

            supabase
                .from(TABLE_USER_PROFILE)
                .update(updateDto) {
                    filter {
                        eq("user_id", userId)
                    }
                }

            Log.d(TAG, "User profile updated successfully")

            Result.Success(
                UserProfile(
                    userId = userId,
                    preferredLanguageId = preferredLanguageId,
                    allergies = allergies,
                    dietaryRestrictions = dietaryRestrictions,
                    dislikes = dislikes,
                    preferences = preferences
                )
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error updating user profile", e)
            Result.Error("Failed to update profile: ${e.message}", e)
        }
    }

    override fun observeUserProfile(userId: String): Flow<UserProfile?> = flow {
        try {
            when (val result = getUserProfile(userId)) {
                is Result.Success -> emit(result.data)
                is Result.Error -> {
                    Log.e(TAG, "Error observing profile: ${result.message}")
                    emit(null)
                }
                is Result.Loading -> emit(null)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in observeUserProfile", e)
            emit(null)
        }
    }

    private fun UserProfileDto.toDomain(): UserProfile {
        return UserProfile(
            userId = this.userId,
            preferredLanguageId = this.preferredLanguageId,
            allergies = this.userAllergies?.split(",")?.filter { it.isNotBlank() } ?: emptyList(),
            dietaryRestrictions = this.userDietaryRestrictions?.split(",")?.filter { it.isNotBlank() } ?: emptyList(),
            dislikes = this.userDislikes?.split(",")?.filter { it.isNotBlank() } ?: emptyList(),
            preferences = this.userPreferences?.split(",")?.filter { it.isNotBlank() } ?: emptyList()
        )
    }
}