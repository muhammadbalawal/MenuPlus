package com.example.emptyactivity.domain.model

data class User(
    val id: String,
    val email: String,
    val name: String? = null,
    val hasCompletedOnboarding: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)