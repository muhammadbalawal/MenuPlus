package com.example.emptyactivity.data.repository.ocr

/**
 * Repository interface for OCR (Optical Character Recognition) operations.
 *
 * This repository abstracts the OCR functionality, allowing the app to extract text from images
 * without directly depending on the specific OCR service implementation (currently Google Cloud Vision API).
 *
 * The repository pattern allows us to:
 * - Switch OCR providers in the future without changing business logic
 * - Easily mock this interface for testing
 * - Keep OCR implementation details separate from the UI layer
 */
interface OcrRepository {
    /**
     * Extracts text lines from a base64-encoded image.
     *
     * This method takes an image encoded as a base64 string, sends it to the OCR service,
     * and returns the detected text split into individual lines. Empty lines are filtered out.
     *
     * @param base64Image The image data encoded as a base64 string (without data URI prefix).
     *                    The image should be encoded using Base64.NO_WRAP for best compatibility.
     * @return List of non-empty text lines extracted from the image. Lines are trimmed of
     *         whitespace. Returns an empty list if no text is detected or if an error occurs.
     * @throws Exception If the OCR service is unavailable, the API key is invalid, or the
     *                   image format is unsupported.
     */
    suspend fun extractLines(base64Image: String): List<String>
}
