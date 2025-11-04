package com.example.emptyactivity.data.repository.auth

@Singleton
class AuthRepositoryImpl @Inject constructor() : AuthRepository {

    private val supabase = SupabaseClientProvider.client
    private val auth = supabase.auth

    companion object {
        private const val TAG = "AuthRepository"
    }


}