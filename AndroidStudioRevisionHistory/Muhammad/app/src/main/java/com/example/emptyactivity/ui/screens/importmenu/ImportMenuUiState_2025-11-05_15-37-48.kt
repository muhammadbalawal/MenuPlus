package com.example.emptyactivity.ui.screens.importmenu

data class ImportMenuUiState(
    val menuText: String = FAKE_MENU_TEXT,
    val isAnalyzing: Boolean = false,
    val analysisResult: String? = null,
    val errorMessage: String? = null,
)

private const val FAKE_MENU_TEXT = """
    🍽 RESTAURANT MENU

APPETIZERS
- Caesar Salad - Fresh romaine, parmesan, croutons, caesar dressing
- Bruschetta - Tomatoes, basil, garlic on toasted bread
- Shrimp Cocktail - Fresh shrimp with cocktail sauce
- Chicken Wings - Buffalo or BBQ sauce
- Mozzarella Sticks - Served with marinara sauce

MAIN COURSES
- Grilled Salmon - With lemon butter sauce, rice, and vegetables
- Chicken Parmesan - Breaded chicken, marinara, mozzarella, pasta
- Beef Burger - Angus beef, lettuce, tomato, onion, fries
- Vegetable Stir Fry - Mixed vegetables, tofu, soy sauce, rice
- Penne Pasta - Tomato sauce with fresh basil
- Steak & Potatoes - 8oz ribeye with mashed potatoes
- Fish & Chips - Beer battered cod with fries
- Margherita Pizza - Tomato, mozzarella, basil

SEAFOOD
- Lobster Tail - Grilled with drawn butter
- Tuna Steak - Sesame crusted, rare, with wasabi
- Crab Cakes - Maryland style with remoulade
- Seafood Pasta - Shrimp, mussels, clams in white wine sauce

DESSERTS
- Chocolate Cake - Rich chocolate ganache
- Cheesecake - Classic New York style
- Ice Cream - Vanilla, chocolate, or strawberry
- Tiramisu - Coffee-soaked ladyfingers, mascarpone

DRINKS
- Soft Drinks
- Fresh Juices
- Coffee & Tea
- Wine Selection
"""
