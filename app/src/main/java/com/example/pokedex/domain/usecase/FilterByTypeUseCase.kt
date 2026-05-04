package com.example.pokedex.domain.usecase

import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.domain.repository.PokemonRepository
import javax.inject.Inject

class FilterByTypeUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(typeName: String): List<Pokemon> {
        return repository.getPokemonByType(typeName)
    }
}
