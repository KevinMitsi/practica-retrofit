package com.example.pokedex.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.paging.*
import com.example.pokedex.data.local.db.AppDatabase
import com.example.pokedex.data.mapper.toDomain
import com.example.pokedex.data.mapper.toEntity
import com.example.pokedex.data.mapper.toPokemon
import kotlinx.coroutines.CancellationException
import com.example.pokedex.data.remote.PokemonRemoteMediator
import com.example.pokedex.data.remote.api.PokeApiService
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.domain.model.PokemonDetail
import com.example.pokedex.domain.repository.PokemonRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val apiService: PokeApiService,
    private val database: AppDatabase,
    @ApplicationContext private val context: Context
) : PokemonRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getPokemonList(): Flow<PagingData<Pokemon>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = PokemonRemoteMediator(apiService, database),
            pagingSourceFactory = { database.pokemonDao().getPokemonList() }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    override suspend fun getPokemonDetail(name: String): Result<PokemonDetail> {
        return try {
            val localDetail = database.pokemonDao().getPokemonDetail(name)
            if (localDetail != null) {
                Result.success(localDetail.toDomain())
            } else {
                val remoteDetail = apiService.getPokemonDetail(name)
                val entity = remoteDetail.toEntity()
                database.pokemonDao().insertDetail(entity)
                Result.success(entity.toDomain())
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchPokemon(query: String): List<Pokemon> {
        return try {
            val allPokemon = apiService.getAllPokemonNames().results
            allPokemon
                .filter { it.name.contains(query, ignoreCase = true) }
                .map { it.toEntity(0).toDomain() }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            database.pokemonDao().searchPokemon("%$query%").map { it.toDomain() }
        }
    }

    override suspend fun getPokemonByType(typeName: String): List<Pokemon> {
        return try {
            val typeResponse = apiService.getPokemonByType(typeName)
            typeResponse.pokemon.map { it.pokemon.toEntity(0).toDomain() }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            database.pokemonDao().getAllCachedDetails()
                .filter { typeName in it.types }
                .map { it.toPokemon() }
        }
    }

    override fun observeConnectivity(): Flow<Boolean> = callbackFlow {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(true)
            }
            override fun onLost(network: Network) {
                trySend(false)
            }
        }
        
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
            
        connectivityManager.registerNetworkCallback(request, callback)
        
        // Initial state
        val activeNetwork = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        trySend(capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true)
        
        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.distinctUntilChanged()
}
