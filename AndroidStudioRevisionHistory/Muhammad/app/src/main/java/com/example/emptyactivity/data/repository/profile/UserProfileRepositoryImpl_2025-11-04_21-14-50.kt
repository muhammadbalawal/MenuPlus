package com.example.emptyactivity.data.repository.profile

class UserProfileRepositoryImpl @Inject constructor() : UserProfileRepository {
    private val supabase = SupabaseClientProvider.client

    companion object {
        private const val TAG = "UserProfileRepository"
        private const val TABLE_USER_PROFILE = "userProfile"
        private const val TABLE_LANGUAGE = "language"

    }

}