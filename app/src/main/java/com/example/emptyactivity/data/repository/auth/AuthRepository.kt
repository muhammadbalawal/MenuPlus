package com.example.emptyactivity.data.repository.auth

import com.example.emptyactivity.domain.model.User
import com.example.emptyactivity.util.Result
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for authentication operations.
 *
 * This repository abstracts authentication functionality, providing methods for user login,
 * registration, logout, and session management. It follows the repository pattern to decouple
 * the domain layer from the specific authentication implementation (Supabase).
 *
 * The repository exposes reactive flows for observing authentication state changes, allowing
 * the UI layer to automatically update when users log in or out.
 */
interface AuthRepository {
    /**
     * Observes the current authentication state as a reactive Flow.
     *
     * This Flow emits the current User when authenticated, or null when not authenticated.
     * The Flow automatically updates whenever the authentication state changes (login, logout, etc.).
     *
     * @return A Flow that emits the current User or null based on authentication state.
     */
    fun observeAuthState(): Flow<User?>

    /**
     * Retrieves the currently authenticated user synchronously.
     *
     * This method returns the current user from the active session, or null if no user
     * is currently authenticated. Unlike observeAuthState(), this is a one-time fetch.
     *
     * @return The current User if authenticated, null otherwise.
     */
    suspend fun getCurrentUser(): User?

    /**
     * Authenticates a user with email and password.
     *
     * This method attempts to sign in the user using Supabase authentication.
     * On success, it returns the authenticated User wrapped in Result.Success.
     *
     * @param email The user's email address.
     * @param password The user's password.
     * @return A Result containing the authenticated User on success, or an Error on failure.
     */
    suspend fun login(email: String, password: String): Result<User>

    /**
     * Registers a new user account.
     *
     * This method creates a new user account with the provided credentials and metadata.
     * The user's onboarding status is set to false by default.
     *
     * @param email The user's email address.
     * @param password The user's password.
     * @param name The user's display name.
     * @return A Result containing the newly created User on success, or an Error on failure.
     */
    suspend fun register(email: String, password: String, name: String): Result<User>

    /**
     * Signs out the currently authenticated user.
     *
     * This method invalidates the current session and logs out the user.
     *
     * @return A Result indicating success or failure of the logout operation.
     */
    suspend fun logout(): Result<Unit>
}
