package com.example.pokedex.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PokemonDetailDto(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<TypeSlotDto>,
    val stats: List<StatSlotDto>,
    val sprites: SpritesDto
)

data class TypeSlotDto(
    val slot: Int,
    val type: TypeResourceDto
)

data class TypeResourceDto(
    val name: String,
    val url: String
)

data class StatSlotDto(
    @SerializedName("base_stat")
    val baseStat: Int,
    val effort: Int,
    val stat: StatResourceDto
)

data class StatResourceDto(
    val name: String,
    val url: String
)

data class SpritesDto(
    @SerializedName("front_default")
    val frontDefault: String?,
    @SerializedName("front_shiny")
    val frontShiny: String?,
    val other: OtherSpritesDto?
)

data class OtherSpritesDto(
    @SerializedName("official-artwork")
    val officialArtwork: OfficialArtworkDto?
)

data class OfficialArtworkDto(
    @SerializedName("front_default")
    val frontDefault: String?
)
