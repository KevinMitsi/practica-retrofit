package com.example.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.pokedex.core.theme.DemoAppTheme
import com.example.pokedex.ui.ConnectivityViewModel
import com.example.pokedex.ui.PokedexNavHost
import com.example.pokedex.ui.components.ConnectivityBanner
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DemoAppTheme {
                val navController = rememberNavController()
                val connectivityViewModel: ConnectivityViewModel = hiltViewModel()
                val isConnected by connectivityViewModel.isConnected.collectAsStateWithLifecycle()
                val showOnlineBanner by connectivityViewModel.showOnlineBanner.collectAsStateWithLifecycle()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        ConnectivityBanner(
                            isConnected = isConnected,
                            showOnlineBanner = showOnlineBanner
                        )
                        PokedexNavHost(navController = navController)
                    }
                }
            }
        }
    }
}
