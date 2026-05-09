package com.example.pokedex.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.domain.usecase.FilterByTypeUseCase
import com.example.pokedex.domain.usecase.GetPokemonListUseCase
import com.example.pokedex.domain.usecase.SearchPokemonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val getPokemonListUseCase: GetPokemonListUseCase,
    private val searchPokemonUseCase: SearchPokemonUseCase,
    private val filterByTypeUseCase: FilterByTypeUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedType = MutableStateFlow<String?>(null)
    val selectedType: StateFlow<String?> = _selectedType.asStateFlow()

    val pokemonPagingData: Flow<PagingData<Pokemon>> = getPokemonListUseCase()
        .cachedIn(viewModelScope)

    val pokemonTypes = listOf(
        "normal", "fire", "water", "electric", "grass", "ice", "fighting",
        "poison", "ground", "flying", "psychic", "bug", "rock", "ghost",
        "dragon", "steel", "fairy", "dark"
    )

    val filteredPokemonList: StateFlow<List<Pokemon>?> =
        combine(_searchQuery, _selectedType) { query, type ->
            Pair(query, type)
        }.flatMapLatest { (query, type) ->
            flow {
                if (query.isEmpty() && type == null) {
                    emit(null)
                } else {
                    // Solo debounce al buscar por texto sin filtro de tipo activo
                    if (query.isNotEmpty() && type == null) {
                        delay(300L)
                    }
                    try {
                        val results = if (type != null) {
                            filterByTypeUseCase(type).filter { it.name.contains(query, ignoreCase = true) }
                        } else {
                            searchPokemonUseCase(query)
                        }
                        emit(results)
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        emit(emptyList())
                    }
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onTypeSelect(type: String) {
        _selectedType.value = if (_selectedType.value == type) null else type
    }
}
