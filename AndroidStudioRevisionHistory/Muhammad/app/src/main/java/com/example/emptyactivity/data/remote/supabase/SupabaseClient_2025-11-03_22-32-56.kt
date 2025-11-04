package com.example.emptyactivity.data.remote.supabase

import com.example.emptyactivity.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

/**
 * Supabase client singleton.
 */
object SupabaseClientProvider {
    
    val client: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseUrl = "https://sofybacejzvsmohggujv.supabase.co",
            supabaseKey = BuildConfig.SUPABASE_KEY
        ){
            install(Auth){
                alwaysAutoRefresh = true
                autoLoadFromStorage = true
            }
            install(Postgrest)
            install(Storage)
        }
    }
}