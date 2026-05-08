package com.example.pokedex.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pokedex.data.local.entity.PokemonDetailEntity
import com.example.pokedex.data.local.entity.PokemonEntity
import com.example.pokedex.data.local.entity.RemoteKeysEntity

@Dao
interface PokemonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pokemon: List<PokemonEntity>)

    @Query("SELECT * FROM pokemon_list ORDER BY pageOffset ASC")
    fun getPokemonList(): PagingSource<Int, PokemonEntity>

    @Query("DELETE FROM pokemon_list")
    suspend fun clearAll()

    // Remote Keys
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRemoteKeys(remoteKey: List<RemoteKeysEntity>)

    @Query("SELECT * FROM remote_keys WHERE pokemonId = :id")
    suspend fun getRemoteKeysId(id: Int): RemoteKeysEntity?

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()

    // Detail
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetail(detail: PokemonDetailEntity)

    @Query("SELECT * FROM pokemon_detail WHERE name = :name")
    suspend fun getPokemonDetail(name: String): PokemonDetailEntity?
    
    @Query("SELECT * FROM pokemon_list WHERE name LIKE :query")
    suspend fun searchPokemon(query: String): List<PokemonEntity>

    @Query("SELECT COUNT(*) FROM pokemon_list")
    suspend fun getPokemonCount(): Int

    @Query("SELECT * FROM pokemon_detail")
    suspend fun getAllCachedDetails(): List<PokemonDetailEntity>
}
