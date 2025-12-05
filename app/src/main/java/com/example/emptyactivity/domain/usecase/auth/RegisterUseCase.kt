package com.example.emptyactivity.domain.usecase.auth

import android.util.Patterns
import com.example.emptyactivity.data.repository.auth.AuthRepository
import com.example.emptyactivity.domain.model.User
import com.example.emptyactivity.util.Result
import javax.inject.Inject

/**
 * Use case for user registration.
 *
 * This use case handles the business logic for user registration, including comprehensive
 * input validation (name, email format, password requirements, password confirmation)
 * before delegating to the authentication repository.
 *
 * @param authRepository The authentication repository used to perform registration.
 *
 * Mostly created by: Malik
 */
class RegisterUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository,
    ) {
        /**
         * Invokes the registration use case with user details.
         *
         * This method validates all input parameters:
         * - Name must not be blank
         * - Email must be in valid format
         * - Password must be at least 6 characters
         * - Password and confirmPassword must match
         *
         * Returns Result.Error if validation fails, otherwise returns the result
         * from the repository.
         *
         * @param email The user's email address.
         * @param password The user's password.
         * @param confirmPassword The password confirmation (must match password).
         * @param name The user's display name.
         * @return Result.Success with User on successful registration, Result.Error on failure.
         */
        suspend operator fun invoke(
            email: String,
            password: String,
            confirmPassword: String,
            name: String,
        ): Result<User> {
            if (name.isBlank()) {
                return Result.Error("Name cannot be empty")
            }

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

            if (password != confirmPassword) {
                return Result.Error("Passwords do not match")
            }

            return authRepository.register(email, password, name)
        }
    }
