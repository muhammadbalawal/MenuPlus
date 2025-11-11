package com.example.emptyactivity.di

import com.example.emptyactivity.data.remote.supabase.SupabaseClientProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import javax.inject.Singleton

/**
 * Hilt module for providing network-related dependencies.
 *
 * This module provides the Supabase client instance as a singleton, making it available
 * for injection throughout the application. The client is obtained from SupabaseClientProvider
 * which handles lazy initialization and configuration.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    /**
     * Provides the Supabase client instance as a singleton.
     *
     * This method returns the configured Supabase client with Auth, PostgREST, and Storage
     * plugins enabled. The client is provided as a singleton to ensure a single instance
     * is used throughout the application lifecycle.
     *
     * @return The configured Supabase client instance.
     */
    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient = SupabaseClientProvider.client
}
