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

