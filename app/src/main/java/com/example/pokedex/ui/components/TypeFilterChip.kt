package com.example.pokedex.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pokedex.R

@Composable
fun TypeFilterChip(
    type: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorResId = when (type.lowercase()) {
        "normal" -> R.color.type_normal
        "fire" -> R.color.type_fire
        "water" -> R.color.type_water
        "electric" -> R.color.type_electric
        "grass" -> R.color.type_grass
        "ice" -> R.color.type_ice
        "fighting" -> R.color.type_fighting
        "poison" -> R.color.type_poison
        "ground" -> R.color.type_ground
        "flying" -> R.color.type_flying
        "psychic" -> R.color.type_psychic
        "bug" -> R.color.type_bug
        "rock" -> R.color.type_rock
        "ghost" -> R.color.type_ghost
        "dragon" -> R.color.type_dragon
        "steel" -> R.color.type_steel
        "fairy" -> R.color.type_fairy
        "dark" -> R.color.type_dark
        else -> R.color.black
    }

    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = {
            Text(
                text = type.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.labelLarge,
                color = if (isSelected) Color.White else colorResource(id = colorResId)
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = colorResource(id = colorResId),
            containerColor = Color.Transparent
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = isSelected,
            borderColor = colorResource(id = colorResId),
            selectedBorderColor = Color.Transparent,
            borderWidth = 1.dp
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.padding(horizontal = 4.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun TypeFilterChipPreview() {
    TypeFilterChip(type = "fire", isSelected = true, onClick = {})
}
