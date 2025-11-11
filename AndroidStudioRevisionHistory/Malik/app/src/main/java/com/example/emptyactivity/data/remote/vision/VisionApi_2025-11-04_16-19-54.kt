package com.example.emptyactivity.data.remote.vision

import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Retrofit interface for the Google Cloud Vision API.
 * Currently uses DOCUMENT_TEXT_DETECTION for OCR text extraction.
 */
interface VisionApi {
    @POST("v1/images:annotate")
    suspend fun annotate(
        @Body body: VisionRequest,
    ): VisionResponse
}
