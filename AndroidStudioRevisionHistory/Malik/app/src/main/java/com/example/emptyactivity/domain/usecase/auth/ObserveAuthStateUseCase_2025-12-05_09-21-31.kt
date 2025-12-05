package com.example.emptyactivity.domain.usecase.auth

import com.example.emptyactivity.data.repository.auth.AuthRepository
import com.example.emptyactivity.domain.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for observing authentication state changes.
 *
 * This use case provides a reactive Flow that emits the current User when authenticated,
 * or null when not authenticated. It delegates to the AuthRepository to observe
 * authentication state changes in real-time.
 *
 * The use case follows the clean architecture pattern by encapsulating the business logic
 * of observing authentication state, making it reusable across the application.
 *
 * @param authRepository The authentication repository used to observe auth state.
 *
 * Mostly created by: Muhammad
 */
class ObserveAuthStateUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository,
    ) {
        /**
         * Invokes the use case to observe authentication state.
         *
         * This method returns a Flow that emits User objects when authenticated,
         * or null when not authenticated. The Flow automatically updates when
         * authentication state changes.
         *
         * @return A Flow that emits the current User or null based on auth state.
         */
        operator fun invoke(): Flow<User?> = authRepository.observeAuthState()
    }
