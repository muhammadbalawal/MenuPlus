package com.example.emptyactivity.di

import com.example.emptyactivity.BuildConfig
import com.example.emptyactivity.data.remote.supabase.SupabaseClientProvider
import com.example.emptyactivity.data.remote.vision.VisionApi
import com.example.emptyactivity.data.remote.vision.VisionClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    // --- Supabase client ---
    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient =
        SupabaseClientProvider.client

    // --- Vision API client (Google Cloud Vision / Gemini OCR) ---
    @Provides
    @Singleton
    fun provideVisionApi(): VisionApi {
        val key = "AIzaSyCdzFMUclnND1fZzBCe93IpOMqdyBQIjw0"

        require(key.isNotBlank()) {
            "GCP_VISION_KEY is missing. Make sure local.properties contains GCP_VISION_KEY=YOUR_KEY and buildFeatures.buildConfig=true."
        }
        return VisionClient.create(key)
    }
}
