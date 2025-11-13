package com.example.emptyactivity.data.remote.vision

import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Retrofit interface for Google Cloud Vision API.
 *
 * This interface defines the HTTP endpoints for interacting with Google's Vision API service.
 * The Vision API provides OCR (Optical Character Recognition) capabilities to extract text
 * from images. This interface is used by Retrofit to generate the actual HTTP client implementation.
 *
 * @see VisionClient for the Retrofit client setup
 * @see VisionDtos for the request/response data models
 */
interface VisionApi {
    /**
     * Sends an image annotation request to Google Cloud Vision API.
     *
     * This endpoint performs document text detection on the provided image. The image must be
     * base64-encoded and included in the request body. The API will analyze the image and return
     * all detected text.
     *
     * @param request The vision request containing the base64-encoded image and feature configuration.
     *                The request should specify "DOCUMENT_TEXT_DETECTION" as the feature type.
     * @return VisionResponse containing the extracted text from the image, or an empty response
     *         if no text is detected or an error occurs.
     */
    @POST("v1/images:annotate")
    suspend fun annotate(
        @Body request: VisionRequest,
    ): VisionResponse
}
