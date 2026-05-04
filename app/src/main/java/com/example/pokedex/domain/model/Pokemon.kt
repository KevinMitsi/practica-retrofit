package com.example.pokedex.domain.model

data class Pokemon(
    val id: Int,
    val name: String,
    val url: String,
    val spriteUrl: String,
    val types: List<String>
)
