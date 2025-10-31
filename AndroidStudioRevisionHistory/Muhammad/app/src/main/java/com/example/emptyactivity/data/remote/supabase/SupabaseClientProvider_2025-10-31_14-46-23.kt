package com.example.emptyactivity.data.remote.supabase

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupabaseClientProvider @Inject constructor() {
    val client: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseUrl ="",
            supabaseKey = ""
}