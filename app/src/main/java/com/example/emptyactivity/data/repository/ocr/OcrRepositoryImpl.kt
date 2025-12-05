package com.example.emptyactivity.data.repository.ocr

import com.example.emptyactivity.data.remote.vision.*

/**
 * Implementation of OcrRepository using Google Cloud Vision API.
 *
 * This class handles the actual communication with Google's Vision API to perform OCR on images.
 * It converts the raw API response into a more usable format (list of text lines) for the rest
 * of the application.
 *
 * The implementation:
 * - Wraps the base64 image in the Vision API request format
 * - Calls the Vision API with document text detection
 * - Extracts the raw text from the response
 * - Splits the text into individual lines and filters out empty lines
 *
 * @param api The VisionApi instance configured with API key and base URL. Injected via dependency injection.
 *
 * Mostly created by: Muhammad
 */
class OcrRepositoryImpl(
    private val api: VisionApi,
) : OcrRepository {
    /**
     * Extracts text lines from a base64-encoded image using Google Cloud Vision API.
     *
     * This method performs the complete OCR workflow:
     * 1. Builds a Vision API request with the image and DOCUMENT_TEXT_DETECTION feature
     * 2. Sends the request to Google Cloud Vision API
     * 3. Extracts the full text annotation from the response
     * 4. Splits the text by newlines and processes each line
     * 5. Returns a clean list of non-empty, trimmed text lines
     *
     * @param base64Image Base64-encoded image string (without data URI prefix).
     * @return List of text lines extracted from the image. Empty lines are removed,
     *         and each line is trimmed of leading/trailing whitespace.
     * @throws Exception If the API call fails (network error, invalid API key, etc.)
     */
    override suspend fun extractLines(base64Image: String): List<String> {
        val req =
            VisionRequest(
                requests =
                    listOf(
                        AnnotateImageRequest(
                            image = VisionImage(base64Image),
                        ),
                    ),
            )
        val resp = api.annotate(req)
        val raw =
            resp.responses
                .firstOrNull()
                ?.fullTextAnnotation
                ?.text
                .orEmpty()
        return raw.split('\n').map { it.trim() }.filter { it.isNotBlank() }
    }
}
