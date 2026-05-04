package com.example.pokedex.domain.model

data class PokemonDetail(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<String>,
    val stats: List<Stat>,
    val spriteUrl: String,
    val spriteShinyUrl: String
)

data class Stat(
    val name: String,
    val value: Int
)
