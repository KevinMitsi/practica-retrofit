package com.example.pokedex.data.local.db

import androidx.room.TypeConverter
import com.example.pokedex.domain.model.Stat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromStatList(value: List<Stat>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStatList(value: String): List<Stat> {
        val listType = object : TypeToken<List<Stat>>() {}.type
        return gson.fromJson(value, listType)
    }
}
