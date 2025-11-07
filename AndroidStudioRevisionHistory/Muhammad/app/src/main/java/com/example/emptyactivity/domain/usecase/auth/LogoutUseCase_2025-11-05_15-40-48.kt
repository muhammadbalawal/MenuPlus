package com.example.emptyactivity.domain.usecase.auth

import com.example.emptyactivity.data.repository.auth.AuthRepository
import com.example.emptyactivity.util.Result
import javax.inject.Inject

class LogoutUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository,
    ) {
        suspend operator fun invoke(): Result<Unit> = authRepository.logout()
    }
