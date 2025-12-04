data class MenuItem(
    val name: String,
    val description: String,
    val price: String? = null,
    val safetyRating: SafetyRating,
    val allergies: List<String> = emptyList(),
    val dietaryRestrictions: List<String> = emptyList(),
    val dislikes: List<String> = emptyList(),
    val preferences: List<String> = emptyList(),
    val recommendation: String? = null,
    val rank: Int? = null, 
)