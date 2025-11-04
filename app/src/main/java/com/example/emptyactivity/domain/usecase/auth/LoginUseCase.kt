package com.example.emptyactivity.domain.usecase.auth

import android.util.Patterns
import com.example.emptyactivity.data.repository.auth.AuthRepository
import com.example.emptyactivity.domain.model.User
import com.example.emptyactivity.util.Result
import javax.inject.Inject

class LoginUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository,
    ) {
        suspend operator fun invoke(email: String, password: String): Result<User> {
            if (email.isBlank()) {
                return Result.Error("Email cannot be empty")
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                return Result.Error("Invalid email format")
            }

            if (password.isBlank()) {
                return Result.Error("Password cannot be empty")
            }

            if (password.length < 6) {
                return Result.Error("Password must be at least 6 characters")
            }

            return authRepository.login(email, password)
        }
    }
