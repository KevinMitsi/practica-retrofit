package com.example.pokedex.domain.repository

import androidx.paging.PagingData
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.domain.model.PokemonDetail
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun getPokemonList(): Flow<PagingData<Pokemon>>
    suspend fun getPokemonDetail(name: String): Result<PokemonDetail>
    suspend fun searchPokemon(query: String): List<Pokemon>
    suspend fun getPokemonByType(typeName: String): List<Pokemon>
    fun observeConnectivity(): Flow<Boolean>

    suspend fun getPokemonById(id: Int): Pokemon?
}
