package com.example.emptyactivity.ui.components.menu

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.emptyactivity.domain.model.MenuItem
import com.example.emptyactivity.domain.model.SafetyRating
import com.example.emptyactivity.ui.theme.RoyalGold
import com.google.accompanist.flowlayout.FlowRow

/**
 * Composable that displays a scrollable list of menu items.
 *
 * This component renders a LazyColumn containing menu item cards. Each item is displayed
 * as a card with color-coded borders and backgrounds based on safety ratings. The list
 * is optimized for performance using LazyColumn's lazy loading.
 *
 * @param menuItems The list of menu items to display. Each item will be rendered as a
 *                  MenuItemCard with appropriate styling based on its safety rating.
 * @param modifier Optional Modifier to apply to the LazyColumn container. Useful for
 *                 applying padding, sizing, or other layout constraints.
 */
@Composable
fun MenuItemList(
    menuItems: List<MenuItem>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        items(menuItems) { item ->
            MenuItemCard(item = item)
        }
    }
}

/**
 * Composable that displays a single menu item as a card with safety rating styling.
 *
 * This component renders a Material3 Card with color-coded borders and backgrounds:
 * - RED border/background: Items containing allergies or violating dietary restrictions
 * - YELLOW border/background: Items containing dislikes (caution recommended)
 * - GREEN border/background: Safe items that match user preferences
 *
 * Each card displays:
 * - Item name and price (if available)
 * - Description
 * - Color-coded tags for allergies (red), restrictions (orange), dislikes (yellow), preferences (green)
 * - Personalized recommendation text (if available)
 *
 * @param item The MenuItem to display. Contains all analysis information including
 *             safety rating, tags, and recommendations from Gemini AI.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MenuItemCard(item: MenuItem) {
    val borderColor =
        when (item.safetyRating) {
            SafetyRating.RED -> Color(0xFFE53935)
            SafetyRating.YELLOW -> Color(0xFFFDD835)
            SafetyRating.GREEN -> Color(0xFF43A047)
        }

    val backgroundColor =
        when (item.safetyRating) {
            SafetyRating.RED -> Color(0xFFE53935).copy(alpha = 0.1f)
            SafetyRating.YELLOW -> Color(0xFFFDD835).copy(alpha = 0.1f)
            SafetyRating.GREEN -> Color(0xFF43A047).copy(alpha = 0.1f)
        }

    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(16.dp),
                ),
        colors =
            CardDefaults.cardColors(
                containerColor = backgroundColor,
            ),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Name and Price Row
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = item.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.weight(1f),
                )

                if (item.price != null) {
                    Text(
                        text = item.price,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RoyalGold,
                    )
                }
            }

            // Description
            Text(
                text =
                    if (item.description.isBlank()) {
                        "No description"
                    } else {
                        item.description
                    },
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.8f),
            )

            // Tags Row
            if (item.allergies.isNotEmpty() ||
                item.dietaryRestrictions.isNotEmpty() ||
                item.dislikes.isNotEmpty() ||
                item.preferences.isNotEmpty()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    // Allergies (Red)
                    item.allergies.forEach { allergy ->
                        ReadOnlyTagChip(
                            text = allergy,
                            color = Color(0xFFE53935),
                        )
                    }

                    // Dietary Restrictions (Orange)
                    item.dietaryRestrictions.forEach { restriction ->
                        ReadOnlyTagChip(
                            text = restriction,
                            color = Color(0xFFFF6F00),
                        )
                    }

                    // Dislikes (Yellow)
                    item.dislikes.forEach { dislike ->
                        ReadOnlyTagChip(
                            text = dislike,
                            color = Color(0xFFFDD835),
                        )
                    }

                    // Preferences (Green)
                    item.preferences.forEach { preference ->
                        ReadOnlyTagChip(
                            text = preference,
                            color = Color(0xFF43A047),
                        )
                    }
                }
            }

            // Recommendation
            if (item.recommendation != null) {
                Text(
                    text = item.recommendation,
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    fontStyle = FontStyle.Italic,
                )
            }
        }
    }
}

/**
 * Composable that displays a read-only tag chip with color-coded styling.
 *
 * This component is used to display tags for allergies, dietary restrictions, dislikes,
 * and preferences on menu item cards. The chips are non-interactive (read-only) and
 * use color coding to indicate the type of information:
 * - Red: Allergies (critical safety information)
 * - Orange: Dietary restrictions
 * - Yellow: Dislikes
 * - Green: Preferences
 *
 * @param text The text to display in the tag chip.
 * @param color The color to use for the tag. Determines both the border and text color,
 *              with a semi-transparent background for visual distinction.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReadOnlyTagChip(
    text: String,
    color: Color,
) {
    InputChip(
        selected = false,
        onClick = { },
        label = { Text(text, fontWeight = FontWeight.Medium) },
        colors =
            InputChipDefaults.inputChipColors(
                containerColor = color.copy(alpha = 0.15f),
                labelColor = color,
            ),
        border =
            InputChipDefaults.inputChipBorder(
                borderColor = color.copy(alpha = 0.5f),
                borderWidth = 1.5.dp,
            ),
        shape = RoundedCornerShape(20.dp),
    )
}

