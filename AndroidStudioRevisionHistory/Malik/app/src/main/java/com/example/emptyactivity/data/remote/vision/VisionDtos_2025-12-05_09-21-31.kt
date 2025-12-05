package com.example.emptyactivity.data.remote.vision

import com.squareup.moshi.Json

/**
 * Top-level request wrapper for Vision API.
 *
 * The Vision API expects requests to be wrapped in a "requests" array, allowing batch processing
 * of multiple images. We typically send a single image per request.
 *
 * @param requests List of image annotation requests. Each request contains one image to analyze.
 *
 * Mostly created by: Muhammad
 */
data class VisionRequest(
    val requests: List<AnnotateImageRequest>,
)

/**
 * Individual image annotation request.
 *
 * Specifies which image to analyze and what type of analysis to perform.
 *
 * @param image The image to analyze, provided as a base64-encoded string.
 * @param features List of analysis features to perform. Defaults to document text detection,
 *                 which extracts all text from the image.
 */
data class AnnotateImageRequest(
    val image: VisionImage,
    val features: List<VisionFeature> =
        listOf(
            VisionFeature(type = "DOCUMENT_TEXT_DETECTION"),
        ),
)

/**
 * Image data for Vision API request.
 *
 * The image must be base64-encoded. The API accepts various image formats (JPEG, PNG, etc.)
 * as long as they are properly base64-encoded.
 *
 * @param base64 Base64-encoded image data without line breaks or data URI prefix.
 *               Use Base64.NO_WRAP encoding for best compatibility.
 */
data class VisionImage(
    @Json(name = "content") val base64: String,
)

/**
 * Feature type specification for Vision API.
 *
 * Defines what type of analysis to perform on the image. "DOCUMENT_TEXT_DETECTION" is optimized
 * for dense text documents like menus, receipts, and forms.
 *
 * @param type The feature type string. Valid values include "DOCUMENT_TEXT_DETECTION",
 *             "TEXT_DETECTION", "LABEL_DETECTION", etc.
 */
data class VisionFeature(
    val type: String,
)

// ---- Response DTOs ----

/**
 * Top-level response wrapper from Vision API.
 *
 * Contains the analysis results for all images in the request. Typically contains one response
 * per image submitted.
 *
 * @param responses List of annotation responses, one per image in the request.
 */
data class VisionResponse(
    val responses: List<AnnotateImageResponse> = emptyList(),
)

/**
 * Individual image annotation response.
 *
 * Contains the analysis results for a single image. May be null if the image couldn't be processed
 * or if no text was detected.
 *
 * @param fullTextAnnotation The complete text annotation containing all detected text from the image.
 *                           Null if no text was found or if processing failed.
 */
data class AnnotateImageResponse(
    val fullTextAnnotation: FullTextAnnotation?,
)

/**
 * Complete text annotation from Vision API.
 *
 * Contains the raw extracted text from the image. The text includes all detected characters
 * in the order they appear in the image, with newlines preserved where detected.
 *
 * @param text The complete extracted text as a single string, or null if no text was detected.
 *             This is the primary field we use - it contains all the menu text in one string.
 */
data class FullTextAnnotation(
    val text: String?,
)
