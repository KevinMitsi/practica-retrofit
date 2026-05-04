package com.example.pokedex.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.domain.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConnectivityViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _isConnected = MutableStateFlow(true)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    private val _showOnlineBanner = MutableStateFlow(false)
    val showOnlineBanner: StateFlow<Boolean> = _showOnlineBanner.asStateFlow()

    init {
        var wasOffline = false
        repository.observeConnectivity()
            .onEach { connected ->
                _isConnected.value = connected
                if (connected && wasOffline) {
                    _showOnlineBanner.value = true
                    viewModelScope.launch {
                        delay(3000)
                        _showOnlineBanner.value = false
                    }
                }
                if (!connected) {
                    wasOffline = true
                }
            }
            .launchIn(viewModelScope)
    }
}
