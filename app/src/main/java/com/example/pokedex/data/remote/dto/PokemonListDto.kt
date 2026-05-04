package com.example.pokedex.data.remote.dto

data class PokemonListDto(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonResourceDto>
)

data class PokemonResourceDto(
    val name: String,
    val url: String
)
