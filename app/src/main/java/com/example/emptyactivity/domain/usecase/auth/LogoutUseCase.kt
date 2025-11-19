package com.example.emptyactivity.domain.usecase.auth

import com.example.emptyactivity.data.repository.auth.AuthRepository
import com.example.emptyactivity.util.Result
import javax.inject.Inject

/**
 * Use case for user logout.
 *
 * This use case handles the business logic for signing out the current user.
 * It delegates to the authentication repository to invalidate the current session.
 *
 * @param authRepository The authentication repository used to perform logout.
 */
class LogoutUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository,
    ) {
        suspend operator fun invoke(): Result<Unit> = authRepository.logout()
    }
