package com.example.pokedex.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pokedex.data.local.dao.PokemonDao
import com.example.pokedex.data.local.entity.PokemonDetailEntity
import com.example.pokedex.data.local.entity.PokemonEntity
import com.example.pokedex.data.local.entity.RemoteKeysEntity

@Database(
    entities = [PokemonEntity::class, PokemonDetailEntity::class, RemoteKeysEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}
