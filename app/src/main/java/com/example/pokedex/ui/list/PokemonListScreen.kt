package com.example.pokedex.ui.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.pokedex.R
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.ui.components.PokemonCard
import com.example.pokedex.ui.components.SearchBar
import com.example.pokedex.ui.components.TypeFilterChip

@Composable
fun PokemonListScreen(
    onPokemonClick: (String) -> Unit,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedType by viewModel.selectedType.collectAsStateWithLifecycle()
    val filteredPokemonList by viewModel.filteredPokemonList.collectAsStateWithLifecycle()
    val pagingItems = viewModel.pokemonPagingData.collectAsLazyPagingItems()

    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(
            query = searchQuery,
            onQueryChange = viewModel::onSearchQueryChange
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 8.dp)
        ) {
            items(viewModel.pokemonTypes) { type ->
                TypeFilterChip(
                    type = type,
                    isSelected = selectedType == type,
                    onClick = { viewModel.onTypeSelect(type) }
                )
            }
        }

        // Capturar el valor del estado en una variable local antes de entrar al lambda de LazyVerticalGrid.
        // Si se lee el estado delegado (filteredPokemonList) dentro del lambda lazy, Compose lo invalida
        // cuando cambia a null ANTES de recomponer el if externo, produciendo NPE en filteredPokemonList!!
        val currentFilteredList = filteredPokemonList

        val totalCount = if (currentFilteredList != null) {
            currentFilteredList.size
        } else {
            pagingItems.itemCount
        }

        Text(
            text = stringResource(R.string.showing_x_pokemon, totalCount),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (currentFilteredList != null) {
            // Show filtered results (Search or Type filter)
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(currentFilteredList) { pokemon ->
                    PokemonCard(
                        pokemon = pokemon,
                        onClick = { onPokemonClick(pokemon.name) }
                    )
                }
            }
        } else {
            // Show paginated list
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(pagingItems.itemCount) { index ->
                    pagingItems[index]?.let { pokemon ->
                        PokemonCard(
                            pokemon = pokemon,
                            onClick = { onPokemonClick(pokemon.name) }
                        )
                    }
                }

                pagingItems.apply {
                    when {
                        loadState.refresh is LoadState.Loading -> {
                            item { LoadingIndicator() }
                        }
                        loadState.append is LoadState.Loading -> {
                            item { LoadingIndicator() }
                        }
                        loadState.refresh is LoadState.Error -> {
                            val e = pagingItems.loadState.refresh as LoadState.Error
                            item { ErrorView(message = e.error.localizedMessage ?: "Error", onRetry = { retry() }) }
                        }
                        loadState.append is LoadState.Error -> {
                            val e = pagingItems.loadState.append as LoadState.Error
                            item { ErrorView(message = e.error.localizedMessage ?: "Error", onRetry = { retry() }) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorView(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message, color = MaterialTheme.colorScheme.error)
        Button(onClick = onRetry) {
            Text(text = stringResource(R.string.retry))
        }
    }
}
