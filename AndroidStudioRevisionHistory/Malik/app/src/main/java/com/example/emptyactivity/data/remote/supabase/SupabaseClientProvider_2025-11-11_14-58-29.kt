package com.example.emptyactivity.data.remote.supabase

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

/**
 * Provider for the Supabase client singleton instance.
 *
 * This object provides lazy initialization of the Supabase client with all required
 * plugins (Auth, PostgREST, Storage). The client is configured with auto-refresh
 * and auto-load from storage enabled for seamless authentication state management.
 *
 * The client is initialized lazily on first access, ensuring it's only created when needed.
 */
object SupabaseClientProvider {
    /**
     * The Supabase client instance with Auth, PostgREST, and Storage plugins.
     *
     * This client is configured with:
     * - Auth plugin with auto-refresh and auto-load from storage
     * - PostgREST plugin for database operations
     * - Storage plugin for file storage operations
     *
     * The client is initialized lazily on first access.
     */
    val client: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseUrl = "https://sofybacejzvsmohggujv.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InNvZnliYWNlanp2c21vaGdndWp2Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjExNTQ1NjcsImV4cCI6MjA3NjczMDU2N30.Rrp-gCof7TeolieiS5RHCPg38pD5gP_ksOWx8X74NnI",
        ) {
            install(Auth) {
                alwaysAutoRefresh = true
                autoLoadFromStorage = true
            }
            install(Postgrest)
            install(Storage)
        }
    }
}
