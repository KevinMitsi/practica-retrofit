package com.example.pokedex.data.mapper

import com.example.pokedex.data.local.entity.PokemonDetailEntity
import com.example.pokedex.data.local.entity.PokemonEntity
import com.example.pokedex.data.remote.dto.PokemonDetailDto
import com.example.pokedex.data.remote.dto.PokemonResourceDto
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.domain.model.PokemonDetail
import com.example.pokedex.domain.model.Stat

fun PokemonResourceDto.toEntity(pageOffset: Int): PokemonEntity {
    val id = url.split("/").filter { it.isNotEmpty() }.last().toInt()
    return PokemonEntity(
        id = id,
        name = name,
        url = url,
        spriteUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png",
        types = emptyList(), // Types are usually not in the list endpoint, will be updated or kept empty
        pageOffset = pageOffset
    )
}

fun PokemonEntity.toDomain(): Pokemon {
    return Pokemon(
        id = id,
        name = name,
        url = url,
        spriteUrl = spriteUrl,
        types = types
    )
}

fun PokemonDetailDto.toEntity(): PokemonDetailEntity {
    return PokemonDetailEntity(
        id = id,
        name = name,
        height = height,
        weight = weight,
        types = types.map { it.type.name },
        stats = stats.map { Stat(it.stat.name, it.baseStat) },
        spriteUrl = sprites.other?.officialArtwork?.frontDefault ?: sprites.frontDefault ?: "",
        spriteShinyUrl = sprites.frontShiny ?: ""
    )
}

fun PokemonDetailEntity.toDomain(): PokemonDetail {
    return PokemonDetail(
        id = id,
        name = name,
        height = height,
        weight = weight,
        types = types,
        stats = stats,
        spriteUrl = spriteUrl,
        spriteShinyUrl = spriteShinyUrl
    )
}
