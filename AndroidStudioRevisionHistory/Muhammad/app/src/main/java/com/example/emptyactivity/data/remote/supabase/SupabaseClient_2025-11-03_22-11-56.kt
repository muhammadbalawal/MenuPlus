package com.example.emptyactivity.data.remote.supabase

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient


object SupabaseClientProvider {
    
    val client: SupabaseClientProvider{
        createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_KEY
        ){
            

            
        }
    }
}