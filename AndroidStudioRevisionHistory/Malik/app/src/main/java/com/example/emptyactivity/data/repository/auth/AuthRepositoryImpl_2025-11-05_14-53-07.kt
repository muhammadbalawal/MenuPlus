package com.example.emptyactivity.data.repository.auth

import android.util.Log
import com.example.emptyactivity.data.remote.supabase.SupabaseClientProvider
import com.example.emptyactivity.domain.model.User
import com.example.emptyactivity.util.Result
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl
    @Inject
    constructor() : AuthRepository {
        private val supabase = SupabaseClientProvider.client
        private val auth = supabase.auth

        companion object {
            private const val TAG = "AuthRepository"
        }

        override fun observeAuthState(): Flow<User?> =
            auth.sessionStatus.map { status ->
                when (status) {
                    is SessionStatus.Authenticated -> {
                        status.session.user?.toUser()
                    }

                    else -> null
                }
            }

        override suspend fun getCurrentUser(): User? =
            try {
                auth.currentSessionOrNull()?.user?.toUser()
            } catch (e: Exception) {
                Log.e(TAG, "Error getting current user", e)
                null
            }

        override suspend fun login(email: String, password: String): Result<User> =
            try {
                Log.d(TAG, "Login attempt for: $email")

                auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }

                val user = getCurrentUser()
                if (user != null) {
                    Result.Success(user)
                } else {
                    Result.Error("Failed to get user after login")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Login error", e)
                Result.Error(e.message ?: "Login failed", e)
            }

        override suspend fun register(
            email: String,
            password: String,
            name: String,
        ): Result<User> =
            try {
                Log.d(TAG, "Registration attempt for: $email")

                auth.signUpWith(Email) {
                    this.email = email
                    this.password = password
                    data =
                        buildJsonObject {
                            put("name", name)
                            put("onboarding_completed", false)
                        }
                }

                val user = getCurrentUser()
                if (user != null) {
                    Result.Success(user.copy(name = name))
                } else {
                    Result.Error("Failed to create user")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Registration error", e)
                Result.Error(e.message ?: "Registration failed", e)
            }

        override suspend fun logout(): Result<Unit> =
            try {
                auth.signOut()
                Result.Success(Unit)
            } catch (e: Exception) {
                Log.e(TAG, "Logout error", e)
                Result.Error(e.message ?: "Logout failed", e)
            }

        private fun UserInfo.toUser(): User {
            val metadata = this.userMetadata
            val name = metadata?.get("name")?.jsonPrimitive?.content
            val onboardingCompleted =
                metadata
                    ?.get("onboarding_completed")
                    ?.jsonPrimitive
                    ?.content
                    ?.toBoolean() ?: false

            return User(
                id = this.id,
                email = this.email ?: "",
                name = name,
                hasCompletedOnboarding = onboardingCompleted,
                createdAt = this.createdAt?.toEpochMilliseconds() ?: 0L,
            )
        }
    }
