package com.example.emptyactivity.domain.usecase.auth

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        confirmPassword: String,
        name: String
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