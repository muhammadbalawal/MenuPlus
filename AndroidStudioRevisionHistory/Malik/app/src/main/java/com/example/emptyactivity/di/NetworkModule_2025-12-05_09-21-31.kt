package com.example.emptyactivity.di

import com.example.emptyactivity.data.remote.gemini.GeminiClient
import com.example.emptyactivity.data.remote.supabase.SupabaseClientProvider
import com.example.emptyactivity.data.remote.vision.VisionApi
import com.example.emptyactivity.data.remote.vision.VisionClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import javax.inject.Singleton

/**
 * Hilt module for providing network-related dependencies.
 *
 * This module configures and provides all external API clients used by the application:
 * - Supabase client for authentication and database operations
 * - Google Cloud Vision API client for OCR text extraction
 * - Gemini AI client for menu analysis
 *
 * All clients are provided as singletons, meaning a single instance is shared across
 * the entire application lifecycle. This is efficient and ensures consistent configuration.
 *
 * Mostly created by: Muhammad
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    /**
     * Provides the Supabase client singleton.
     *
     * Supabase is used for:
     * - User authentication (login, register, logout)
     * - User profile storage (dietary preferences, allergies, etc.)
     * - Language data storage
     *
     * @return Configured SupabaseClient instance with authentication, Postgres, and Storage enabled.
     */
    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient =
        SupabaseClientProvider.client

    /**
     * Provides the Google Cloud Vision API client for OCR operations.
     *
     * This client is configured with:
     * - API key authentication (added automatically via interceptor)
     * - Base URL pointing to Google Vision API
     * - JSON serialization using Moshi
     *
     * Note: The API key is currently hardcoded. In production, this should be stored
     * securely (e.g., in local.properties or a secure configuration service).
     *
     * @return Configured VisionApi instance ready to perform OCR on images.
     * @throws IllegalArgumentException If the API key is blank or missing.
     */
    @Provides
    @Singleton
    fun provideVisionApi(): VisionApi {
        val key = "AIzaSyCdzFMUclnND1fZzBCe93IpOMqdyBQIjw0"

        require(key.isNotBlank()) {
            "GCP_VISION_KEY is missing. Make sure local.properties contains GCP_VISION_KEY=YOUR_KEY and buildFeatures.buildConfig=true."
        }
        return VisionClient.create(key)
    }

    /**
     * Provides the Gemini AI client for menu analysis.
     *
     * Gemini AI is used to analyze menu text and provide personalized recommendations
     * based on user dietary profiles. The client uses Firebase AI SDK to interact with
     * Google's Gemini model.
     *
     * @return Configured GeminiClient instance ready to analyze menu text.
     */
    @Provides
    @Singleton
    fun provideGeminiClient(): GeminiClient = GeminiClient()
}
