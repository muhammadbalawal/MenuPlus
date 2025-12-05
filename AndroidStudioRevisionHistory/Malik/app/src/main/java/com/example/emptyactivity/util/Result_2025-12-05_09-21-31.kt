package com.example.emptyactivity.util

/**
 * Sealed class representing the result of an asynchronous operation.
 *
 * This is a common pattern for handling async operations in Kotlin. It provides type-safe
 * handling of three possible states: success, error, and loading. This eliminates the need
 * for nullable return types and provides clear state management throughout the app.
 *
 * Usage example:
 * ```
 * when (val result = someAsyncOperation()) {
 *     is Result.Success -> handleSuccess(result.data)
 *     is Result.Error -> handleError(result.message)
 *     is Result.Loading -> showLoadingIndicator()
 * }
 * ```
 *
 * @param T The type of data returned on success. Use Unit if no data is returned.
 *
 * Mostly created by: Muhammad
 */
sealed class Result<out T> {
    /**
     * Represents a successful operation with data.
     *
     * @param data The result data from the successful operation.
     */
    data class Success<out T>(
        val data: T,
    ) : Result<T>()

    /**
     * Represents a failed operation with an error message.
     *
     * @param message Human-readable error message describing what went wrong.
     * @param exception Optional exception that caused the error. Useful for logging
     *                  and debugging, but not always necessary for UI display.
     */
    data class Error(
        val message: String,
        val exception: Throwable? = null,
    ) : Result<Nothing>()

    /**
     * Represents an operation that is currently in progress.
     *
     * This state is useful for showing loading indicators in the UI while
     * an async operation is executing.
     */
    data object Loading : Result<Nothing>()
}
