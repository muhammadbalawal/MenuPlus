package com.example.emptyactivity.util

/**
 * Sealed class representing the result of an operation.
 *
 * This is a generic result wrapper used throughout the application to handle
 * asynchronous operations with three possible states: Success, Error, or Loading.
 * It provides type-safe error handling and eliminates the need for try-catch blocks
 * in many cases.
 *
 * @param T The type of data contained in a Success result.
 */
sealed class Result<out T> {
    /**
     * Represents a successful operation with data.
     *
     * @param data The result data of the operation.
     */
    data class Success<out T>(
        val data: T,
    ) : Result<T>()

    /**
     * Represents a failed operation with an error message.
     *
     * @param message A human-readable error message describing what went wrong.
     * @param exception Optional exception that caused the error, for debugging purposes.
     */
    data class Error(
        val message: String,
        val exception: Throwable? = null,
    ) : Result<Nothing>()

    /**
     * Represents an operation that is currently in progress.
     *
     * This state is typically used to show loading indicators in the UI.
     */
    data object Loading : Result<Nothing>()
}
