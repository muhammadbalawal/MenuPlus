package com.example.emptyactivity.domain.usecase.ocr

import com.example.emptyactivity.data.repository.ocr.OcrRepository
import com.example.emptyactivity.util.Result
import javax.inject.Inject

/**
 * Use case for extracting text from images using OCR.
 *
 * This use case handles the business logic for OCR text extraction, including
 * input validation and error handling. It wraps the repository call with
 * proper error handling and returns a Result type for consistent state management.
 *
 * @param ocrRepository The OCR repository used to extract text from images.
 */
class ExtractTextUseCase
    @Inject
    constructor(
        private val ocrRepository: OcrRepository,
    ) {
        /**
         * Extracts text lines from a base64-encoded image.
         *
         * This method validates the input and delegates to the OCR repository.
         * It wraps any exceptions in a Result.Error for consistent error handling.
         *
         * @param base64Image The image data encoded as a base64 string (without data URI prefix).
         * @return Result.Success with List<String> of extracted text lines on success,
         *         Result.Error on failure.
         */
        suspend operator fun invoke(base64Image: String): Result<List<String>> {
            if (base64Image.isBlank()) {
                return Result.Error("Image data cannot be empty")
            }

            return try {
                val lines = ocrRepository.extractLines(base64Image)
                if (lines.isEmpty()) {
                    Result.Error("No text detected in the image")
                } else {
                    Result.Success(lines)
                }
            } catch (e: Exception) {
                Result.Error(
                    message = "Failed to extract text: ${e.message ?: "Unknown error"}",
                    exception = e,
                )
            }
        }
    }