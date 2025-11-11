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

/**
 * Implementation of [AuthRepository] using Supabase authentication.
 *
 * This class provides the concrete implementation of authentication operations using Supabase's
 * Auth service. It handles user login, registration, logout, and session management.
 *
 * The implementation uses Supabase's session status Flow to observe authentication state changes
 * and automatically converts Supabase UserInfo objects to domain User models.
 *
 * All authentication operations are wrapped in try-catch blocks to handle errors gracefully
 * and return Result types for consistent error handling.
 *
 * @constructor Creates an instance of AuthRepositoryImpl with Supabase client injection.
 */
@Singleton
class AuthRepositoryImpl
    @Inject
    constructor() : AuthRepository {
        private val supabase = SupabaseClientProvider.client
        private val auth = supabase.auth

        companion object {
            private const val TAG = "AuthRepository"
        }

        /**
         * Observes authentication state by mapping Supabase session status to User.
         *
         * This implementation maps the Supabase session status Flow to emit User objects
         * when authenticated, or null when not authenticated. The Flow automatically updates
         * when the session changes.
         *
         * @return A Flow that emits User when authenticated, null otherwise.
         */
        override fun observeAuthState(): Flow<User?> =
            auth.sessionStatus.map { status ->
                when (status) {
                    is SessionStatus.Authenticated -> {
                        status.session.user?.toUser()
                    }

                    else -> null
                }
            }

        /**
         * Retrieves the current user from the active Supabase session.
         *
         * This method fetches the current session and converts the Supabase UserInfo
         * to a domain User model. Returns null if no session exists or an error occurs.
         *
         * @return The current User if a session exists, null otherwise.
         */
        override suspend fun getCurrentUser(): User? =
            try {
                auth.currentSessionOrNull()?.user?.toUser()
            } catch (e: Exception) {
                Log.e(TAG, "Error getting current user", e)
                null
            }

        /**
         * Authenticates a user using email and password via Supabase.
         *
         * This method attempts to sign in the user with the provided credentials.
         * After successful authentication, it retrieves the user information and
         * returns it wrapped in a Result.Success. Any errors are caught and returned
         * as Result.Error.
         *
         * @param email The user's email address.
         * @param password The user's password.
         * @return Result.Success with User on successful login, Result.Error on failure.
         */
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

        /**
         * Registers a new user account with Supabase authentication.
         *
         * This method creates a new user account with the provided credentials and stores
         * additional metadata (name and onboarding status) in the user's metadata. The
         * onboarding status is set to false by default for new users.
         *
         * @param email The user's email address.
         * @param password The user's password.
         * @param name The user's display name.
         * @return Result.Success with User on successful registration, Result.Error on failure.
         */
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

        /**
         * Signs out the currently authenticated user.
         *
         * This method invalidates the current Supabase session and logs out the user.
         * Any errors during logout are caught and returned as Result.Error.
         *
         * @return Result.Success on successful logout, Result.Error on failure.
         */
        override suspend fun logout(): Result<Unit> =
            try {
                auth.signOut()
                Result.Success(Unit)
            } catch (e: Exception) {
                Log.e(TAG, "Logout error", e)
                Result.Error(e.message ?: "Logout failed", e)
            }

        /**
         * Extension function to convert Supabase UserInfo to domain User model.
         *
         * This function extracts user data from Supabase's UserInfo object, including
         * metadata fields like name and onboarding_completed status. It safely handles
         * missing or null values with default fallbacks.
         *
         * @return A domain User object with data extracted from UserInfo.
         */
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
