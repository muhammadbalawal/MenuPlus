package com.example.emptyactivity.data.remote.vision

import com.squareup.moshi.Json

// ---- Request ----

data class VisionRequest(
    val requests: List<AnnotateImageRequest>
)

data class AnnotateImageRequest(
    val image: VisionImage,
    val features: List<VisionFeature> = listOf(
        VisionFeature(type = "DOCUMENT_TEXT_DETECTION")
    )
)

data class VisionImage(
    @Json(name = "content") val base64: String
)

data class VisionFeature(
    val type: String
)

// ---- Response (only fields we read) ----

data class VisionResponse(
    val responses: List<AnnotateImageResponse> = emptyList()
)

data class AnnotateImageResponse(
    val fullTextAnnotation: FullTextAnnotation?
)

data class FullTextAnnotation(
    val text: String?
)