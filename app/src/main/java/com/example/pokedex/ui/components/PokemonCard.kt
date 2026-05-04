package com.example.pokedex.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.pokedex.R
import com.example.pokedex.domain.model.Pokemon

@Composable
fun PokemonCard(
    pokemon: Pokemon,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(pokemon.spriteUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = pokemon.name,
                modifier = Modifier
                    .size(120.dp),
                contentScale = ContentScale.Fit
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = pokemon.name.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                pokemon.types.forEach { type ->
                    TypeBadge(type = type)
                }
            }
        }
    }
}

@Composable
fun TypeBadge(type: String) {
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
    
    Surface(
        color = colorResource(id = colorResId),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = type.uppercase(),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            color = Color.White
        )
    }
}

@Preview
@Composable
fun PokemonCardPreview() {
    PokemonCard(
        pokemon = Pokemon(
            id = 1,
            name = "Bulbasaur",
            url = "",
            spriteUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png",
            types = listOf("grass", "poison")
        ),
        onClick = {}
    )
}
