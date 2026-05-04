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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
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

    private val _filteredPokemonList = MutableStateFlow<List<Pokemon>?>(null)
    val filteredPokemonList: StateFlow<List<Pokemon>?> = _filteredPokemonList.asStateFlow()

    val pokemonPagingData: Flow<PagingData<Pokemon>> = getPokemonListUseCase()
        .cachedIn(viewModelScope)

    val pokemonTypes = listOf(
        "normal", "fire", "water", "electric", "grass", "ice", "fighting",
        "poison", "ground", "flying", "psychic", "bug", "rock", "ghost",
        "dragon", "steel", "fairy", "dark"
    )

    init {
        combine(_searchQuery.debounce(300), _selectedType) { query, type ->
            Pair(query, type)
        }.onEach { (query, type) ->
            updateFilteredList(query, type)
        }.launchIn(viewModelScope)
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onTypeSelect(type: String) {
        _selectedType.value = if (_selectedType.value == type) null else type
    }

    private fun updateFilteredList(query: String, type: String?) {
        viewModelScope.launch {
            if (query.isEmpty() && type == null) {
                _filteredPokemonList.value = null
                return@launch
            }

            val results = if (type != null) {
                filterByTypeUseCase(type).filter { it.name.contains(query, ignoreCase = true) }
            } else {
                searchPokemonUseCase(query)
            }
            _filteredPokemonList.value = results
        }
    }
}
