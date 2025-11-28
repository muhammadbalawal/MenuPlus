data class Menu(
    val menuId: String,
    val userId: String,
    val menuText: String,
    val safeMenuContent: String? = null,
    val bestMenuContent: String? = null,
    val fullMenuContent: String? = null,
    val imageUri: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
)