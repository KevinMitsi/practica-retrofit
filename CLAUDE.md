# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Comandos de desarrollo

```bash
# Compilar
./gradlew build

# APK de release
./gradlew assembleRelease

# Tests unitarios
./gradlew test

# Tests instrumentados (requiere emulador/dispositivo)
./gradlew connectedAndroidTest

# Lint
./gradlew lint

# Limpiar
./gradlew clean
```

## Arquitectura

App Android de Pokédex construida con Clean Architecture + MVVM. Consume [PokeAPI](https://pokeapi.co/api/v2/).

**Capas:**

- **`domain/`** — modelos de negocio, interfaz `PokemonRepository`, y casos de uso (`GetPokemonListUseCase`, `GetPokemonDetailUseCase`, `SearchPokemonUseCase`, `FilterByTypeUseCase`)
- **`data/`** — implementación del repositorio, DTOs de Retrofit, entidades de Room, `PokemonRemoteMediator` (Paging 3), y mappers DTO↔Entity↔Domain
- **`ui/`** — pantallas Compose + ViewModels, navegación (`PokedexNavHost`), componentes compartidos
- **`feature/detail/`** — pantalla de detalle con estado sellado (`PokemonDetailUiState`)
- **`di/`** — módulos Hilt: `NetworkModule`, `DatabaseModule`, `RepositoryModule`

**Flujo de datos para la lista:** `PokemonListViewModel` → `GetPokemonListUseCase` → `PokemonRepositoryImpl` → `Pager` con `PokemonRemoteMediator` → `PokemonDao` (Room). Los datos se obtienen primero desde Room y se actualizan vía API cuando es necesario.

**Flujo de datos para el detalle:** `PokemonDetailViewModel` → `GetPokemonDetailUseCase` → repositorio que revisa Room primero, luego API, y cachea el resultado.

## Decisiones técnicas clave

- **Paging 3 + RemoteMediator**: La lista paginada usa `PokemonRemoteMediator` para orquestar la carga remota y el caché local. La tabla `remote_keys` gestiona el estado de paginación.
- **Room con converters JSON**: Los campos de tipo lista (`types`, `stats`) se serializan a JSON mediante `Converters.kt` para almacenarse en Room.
- **Búsqueda vs. paginación**: El `PokemonListViewModel` mantiene dos estados: `pagedPokemonList` (Paging 3) y `filteredPokemonList` (lista simple). La UI muestra uno u otro según si hay filtro activo.
- **Debounce en búsqueda**: El `_searchQuery` usa `debounce(300ms)` antes de disparar la búsqueda.
- **Conectividad**: `ConnectivityViewModel` observa cambios de red y muestra un banner de 3 segundos al reconectarse.

## Stack

- **UI**: Jetpack Compose + Material 3, Navigation Compose
- **DI**: Hilt (KSP)
- **Red**: Retrofit 2 + OkHttp 4 + Gson
- **BD local**: Room 2.6 (con paging)
- **Paginación**: Paging 3
- **Imágenes**: Coil Compose
- **Config**: minSdk 26, targetSdk 35, namespace `com.example.pokedex`
