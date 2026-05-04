package com.example.pokedex.data.remote.dto

data class TypeDto(
    val id: Int,
    val name: String,
    val pokemon: List<TypePokemonSlotDto>
)

data class TypePokemonSlotDto(
    val pokemon: PokemonResourceDto
)
