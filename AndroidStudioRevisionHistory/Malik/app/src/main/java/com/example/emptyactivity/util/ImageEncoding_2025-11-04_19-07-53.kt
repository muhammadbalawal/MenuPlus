package com.example.emptyactivity.util

import android.content.Context
import android.net.Uri
import android.util.Base64

object ImageEncoding {
    fun uriToBase64(context: Context, uri: Uri): String {
        val bytes =
            context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                ?: error("Unable to read image stream")
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }
}
