package com.example.pokedex.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pokedex.domain.model.Stat

@Entity(tableName = "pokemon_detail")
data class PokemonDetailEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<String>,
    val stats: List<Stat>,
    val spriteUrl: String,
    val spriteShinyUrl: String
)
