package com.example.emptyactivity.data.repository.auth

import com.example.emptyactivity.domain.model.User
import com.example.emptyactivity.util.Result
import kotlinx.coroutines.flow.Flow


interface AuthRepository {
    fun observeAuthState(): Flow<User?>

    suspend fun getCurrentUser(): User?

    suspend fun login(email: String, password: String): Result<User>

    suspend fun register(email: String, password: String, name: String): Result<User>

    suspend fun logout(): Result<Unit>

    suspend fun hasCompletedOnboarding(): Boolean
    suspend fun completeOnboarding(): Result<Unit>
    suspend fun refreshSession(): Result<User?>
}
