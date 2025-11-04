package com.example.emptyactivity.data.remote.supabase

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
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InNvZnliYWNlanp2c21vaGdndWp2Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjExNTQ1NjcsImV4cCI6MjA3NjczMDU2N30.Rrp-gCof7TeolieiS5RHCPg38pD5gP_ksOWx8X74NnI"
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