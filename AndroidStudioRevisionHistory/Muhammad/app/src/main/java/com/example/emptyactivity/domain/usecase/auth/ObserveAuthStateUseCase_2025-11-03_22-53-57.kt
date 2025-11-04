package com.example.emptyactivity.domain.usecase.auth

import com.example.emptyactivity.data.repository.auth.AuthRepository
import com.example.emptyactivity.domain.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveAuthStateUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<User?> {
        return authRepository.observeAuthState()
    }
}