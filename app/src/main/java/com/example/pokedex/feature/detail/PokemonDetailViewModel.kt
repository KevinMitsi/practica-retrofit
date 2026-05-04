package com.example.pokedex.feature.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.domain.model.PokemonDetail
import com.example.pokedex.domain.usecase.GetPokemonDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PokemonDetailUiState>(PokemonDetailUiState.Loading)
    val uiState: StateFlow<PokemonDetailUiState> = _uiState.asStateFlow()

    fun loadPokemon(pokemonIdentifier: String) {
        viewModelScope.launch {
            _uiState.value = PokemonDetailUiState.Loading
            getPokemonDetailUseCase(pokemonIdentifier)
                .onSuccess { detail ->
                    _uiState.value = PokemonDetailUiState.Success(detail)
                }
                .onFailure { error ->
                    _uiState.value = PokemonDetailUiState.Error(error.message ?: "Unknown error")
                }
        }
    }
}

sealed interface PokemonDetailUiState {
    object Loading : PokemonDetailUiState
    data class Success(val pokemon: PokemonDetail) : PokemonDetailUiState
    data class Error(val message: String) : PokemonDetailUiState
}
