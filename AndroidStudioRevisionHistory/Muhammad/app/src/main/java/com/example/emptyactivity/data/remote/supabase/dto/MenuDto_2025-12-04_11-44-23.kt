package com.example.emptyactivity.data.remote.supabase.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MenuDto(
    @SerialName("menu_id")
    val menuId: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("menu_text")
    val menuText: String,
    @SerialName("menu_items_json")
    val menuItemsJson: String? = null,
    @SerialName("image_uri")
    val imageUri: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null,
)

@Serializable
data class CreateMenuDto(
    @SerialName("user_id")
    val userId: String,
    @SerialName("menu_text")
    val menuText: String,
    @SerialName("menu_items_json")
    val menuItemsJson: String? = null,
    @SerialName("image_uri")
    val imageUri: String? = null,
)
