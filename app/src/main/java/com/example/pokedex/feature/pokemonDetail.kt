package com.example.pokedex.feature

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetail(
    pokemonId: String,
    navController: NavController,
    viewModel: PokemonDetailViewModel = hiltViewModel()
){

}