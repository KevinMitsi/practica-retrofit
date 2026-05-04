package com.example.pokedex.data.remote.api

import com.example.pokedex.data.remote.dto.PokemonDetailDto
import com.example.pokedex.data.remote.dto.PokemonListDto
import com.example.pokedex.data.remote.dto.TypeDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokemonListDto

    @GET("pokemon/{name}")
    suspend fun getPokemonDetail(
        @Path("name") name: String
    ): PokemonDetailDto

    @GET("type/{name}")
    suspend fun getPokemonByType(
        @Path("name") name: String
    ): TypeDto

    @GET("pokemon")
    suspend fun getAllPokemonNames(
        @Query("limit") limit: Int = 100000,
        @Query("offset") offset: Int = 0
    ): PokemonListDto

    companion object {
        const val BASE_URL = "https://pokeapi.co/api/v2/"
    }
}
