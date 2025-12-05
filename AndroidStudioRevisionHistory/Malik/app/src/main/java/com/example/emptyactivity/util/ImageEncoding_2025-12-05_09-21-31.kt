package com.example.emptyactivity.util

import android.content.Context
import android.net.Uri
import android.util.Base64

/**
 * Utility object for encoding images to base64 format.
 *
 * This utility is used to convert Android image URIs (from gallery, camera, etc.) into
 * base64-encoded strings that can be sent to the Vision API. The encoding uses NO_WRAP
 * flag to ensure compatibility with Google Cloud Vision API requirements.
 *
 * Mostly created by: Malik
 */
object ImageEncoding {
    /**
     * Converts an Android image URI to a base64-encoded string.
     *
     * This method reads the image file from the provided URI, converts it to a byte array,
     * and then encodes it as a base64 string. The encoding is done without line breaks
     * (NO_WRAP) as required by the Vision API.
     *
     * @param context Android context needed to access the content resolver for reading the URI.
     * @param uri The Android URI pointing to the image file (from gallery picker, camera, etc.).
     * @return Base64-encoded string representation of the image, ready to be sent to Vision API.
     * @throws Error If the URI cannot be opened or read (file doesn't exist, permission denied, etc.).
     *               The error message will indicate "Unable to read image stream".
     */
    fun uriToBase64(context: Context, uri: Uri): String {
        val bytes =
            context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                ?: error("Unable to read image stream")
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }
}
