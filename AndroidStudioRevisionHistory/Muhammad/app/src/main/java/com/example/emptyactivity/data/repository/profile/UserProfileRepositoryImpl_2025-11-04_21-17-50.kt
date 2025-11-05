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

            val language = supabase
                .from(TABLE_LANGUAGE)
                .select("")
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
}