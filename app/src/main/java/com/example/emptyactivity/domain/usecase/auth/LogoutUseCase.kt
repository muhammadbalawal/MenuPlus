package com.example.emptyactivity.domain.usecase.auth

import com.example.emptyactivity.data.repository.auth.AuthRepository
import com.example.emptyactivity.util.Result
import javax.inject.Inject

/**
 * Use case for signing out the current user.
 *
 * This use case handles the business logic for user logout. It delegates to the
 * authentication repository to invalidate the current session and sign out the user.
 *
 * After successful logout, the authentication state will be updated, which triggers
 * navigation to the unauthenticated screens (Landing/Login) via the MenuPlusAppViewModel.
 *
 * @param authRepository The authentication repository used to perform logout.
 *
 * Mostly created by: Malik
 */
class LogoutUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository,
    ) {
        /**
         * Signs out the currently authenticated user.
         *
         * This method invalidates the current session and logs out the user. After successful
         * logout, the authentication state will be updated automatically, triggering navigation
         * to the unauthenticated screens.
         *
         * @return Result indicating success (Unit) or an error if the logout operation fails.
         */
        suspend operator fun invoke(): Result<Unit> = authRepository.logout()
    }
