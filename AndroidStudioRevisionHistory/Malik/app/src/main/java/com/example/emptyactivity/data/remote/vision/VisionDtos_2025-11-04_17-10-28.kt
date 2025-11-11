package com.example.emptyactivity.data.remote.vision

/**
 * Data Transfer Objects for Cloud Vision API requests and responses.
 */


data class VisionRequest(
    val requests: List<AnnotateImageRequest>,
)

data class AnnotateImageRequest(
    val image: VisionImage,
    val features: List<VisionFeature> = listOf(VisionFeature("DOCUMENT_TEXT_DETECTION")),
)

data class VisionImage(
    val content: String,
) // base64 encoded image

data class VisionFeature(
    val type: String,
)

// Response models
data class VisionResponse(
    val responses: List<VisionAnnotateResult> = emptyList(),
)

data class VisionAnnotateResult(
    val fullTextAnnotation: FullTextAnnotation? = null,
)

data class FullTextAnnotation(
    val text: String? = null,
)
