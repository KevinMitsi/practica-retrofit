package com.example.pokedex.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.pokedex.feature.detail.PokemonDetailScreen
import com.example.pokedex.ui.list.PokemonListScreen

sealed class Screen(val route: String) {
    object PokemonList : Screen("pokemon_list")
    object PokemonDetail : Screen("pokemon_detail/{pokemonName}") {
        fun createRoute(pokemonName: String) = "pokemon_detail/$pokemonName"
    }
}

@Composable
fun PokedexNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.PokemonList.route,
        modifier = modifier
    ) {
        composable(Screen.PokemonList.route) {
            PokemonListScreen(
                onPokemonClick = { pokemonName ->
                    navController.navigate(Screen.PokemonDetail.createRoute(pokemonName))
                }
            )
        }
        composable(
            route = Screen.PokemonDetail.route,
            arguments = listOf(
                navArgument("pokemonName") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val pokemonName = backStackEntry.arguments?.getString("pokemonName") ?: ""
            PokemonDetailScreen(
                pokemonName = pokemonName,
                navController = navController
            )
        }
    }
}
