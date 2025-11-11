package com.example.emptyactivity.domain.usecase.auth

import android.util.Patterns
import com.example.emptyactivity.data.repository.auth.AuthRepository
import com.example.emptyactivity.domain.model.User
import com.example.emptyactivity.util.Result
import javax.inject.Inject

/**
 * Use case for user authentication.
 *
 * This use case handles the business logic for user login, including input validation
 * and delegation to the authentication repository. It validates email format and
 * password requirements before attempting authentication.
 *
 * @param authRepository The authentication repository used to perform login.
 */
class LoginUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository,
    ) {
        /**
         * Invokes the login use case with email and password.
         *
         * This method validates the input parameters (email format, password length)
         * before delegating to the repository. Returns Result.Error if validation fails,
         * otherwise returns the result from the repository.
         *
         * @param email The user's email address.
         * @param password The user's password.
         * @return Result.Success with User on successful login, Result.Error on failure.
         */
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
