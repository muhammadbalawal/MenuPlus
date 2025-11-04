package com.example.emptyactivity.data.remote.vision
import android.util.Log


class VisionClient {
    fun debugLogKey() {
        Log.d("VisionKey", BuildConfig.GCP_VISION_KEY)
    }
}