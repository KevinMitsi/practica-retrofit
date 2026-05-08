package com.example.pokedex.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.pokedex.data.local.db.AppDatabase
import com.example.pokedex.data.local.entity.PokemonEntity
import com.example.pokedex.data.local.entity.RemoteKeysEntity
import com.example.pokedex.data.mapper.toEntity
import com.example.pokedex.data.remote.api.PokeApiService
import java.io.IOException
import retrofit2.HttpException

@OptIn(ExperimentalPagingApi::class)
class PokemonRemoteMediator(
    private val apiService: PokeApiService,
    private val database: AppDatabase
) : RemoteMediator<Int, PokemonEntity>() {

    override suspend fun initialize(): RemoteMediator.InitializeAction {
        return if (database.pokemonDao().getPokemonCount() > 0) {
            RemoteMediator.InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            RemoteMediator.InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(state.config.pageSize) ?: 0
            }
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
                nextKey
            }
        }

        try {
            val apiResponse = apiService.getPokemonList(
                limit = state.config.pageSize,
                offset = page
            )

            val pokemonList = apiResponse.results
            val endOfPaginationReached = pokemonList.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.pokemonDao().clearAll()
                    database.pokemonDao().clearRemoteKeys()
                }
                val prevKey = if (page == 0) null else page - state.config.pageSize
                val nextKey = if (endOfPaginationReached) null else page + state.config.pageSize
                
                val entities = pokemonList.map { it.toEntity(page) }
                val keys = entities.map {
                    RemoteKeysEntity(pokemonId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                
                database.pokemonDao().insertAllRemoteKeys(keys)
                database.pokemonDao().insertAll(entities)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, PokemonEntity>): RemoteKeysEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { pokemon -> database.pokemonDao().getRemoteKeysId(pokemon.id) }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, PokemonEntity>): RemoteKeysEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.pokemonDao().getRemoteKeysId(id)
            }
        }
    }
}
