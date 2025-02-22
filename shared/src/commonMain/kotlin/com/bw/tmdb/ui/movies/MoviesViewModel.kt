package com.bw.tmdb.ui.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bw.tmdb.Api
import com.bw.tmdb.DebugLog
import com.bw.tmdb.Graph
import com.bw.tmdb.transport.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MoviesViewModel(private val api: Api = Graph.api): ViewModel() {
    private val _state = MutableStateFlow(MoviesViewState())

    val state: StateFlow<MoviesViewState>
        get() = _state

    init {
        viewModelScope.launch {
            try {
                val fetched = fetchConcurrent(
                    api.getMovieScopes()
                )

                _state.value = MoviesViewState(
                    topRated = fetched["top_rated"] ?: emptyList(),
                    popular = fetched["popular"] ?: emptyList(),
                    nowPlaying = fetched["now_playing"] ?: emptyList(),
                    upcoming = fetched["upcoming"] ?: emptyList()
                )
            } catch (e: Exception) {
                DebugLog.e("failed fetching movies", e)
            }
        }
    }

    fun switchToAll(scope: String) {
        viewModelScope.launch {
            try {
                val all = api.getMovies(scope)

                _state.value = _state.value.copy(
                    scope = scope,
                    page = 1,
                    all = all
                )
            } catch (e: Exception) {
                DebugLog.e("failed fetching movies for scope $scope", e)
            }
        }
    }

    fun fetchMore() {
        viewModelScope.launch {
            val scope = _state.value.scope
            if (scope != null) {
                try {
                    val page = _state.value.page

                    val new = api.getMovies(scope, page = page + 1)
                    val all = _state.value.all.toMutableList()
                        all.addAll(new)
                    _state.value = _state.value.copy(
                        scope = scope,
                        page = page + 1,
                        all = all
                    )
                } catch (e: Exception) {
                    DebugLog.e("failed fetching more movies for scope $scope", e)
                }
            }
        }
    }

    fun fallbackToLanding() {
        _state.value = _state.value.copy(
            scope = null,
            page = 0,
            all = emptyList()
        )
    }

    private suspend fun fetchConcurrent(scopes: Iterable<String>): Map<String, List<Movie>> = coroutineScope {
        scopes.map { name -> async(Dispatchers.Main) { name to api.getMovies(name) } }
            .associate { it.await() }
    }
}

data class MoviesViewState(
    val topRated: List<Movie> = emptyList(),
    val popular: List<Movie> = emptyList(),
    val nowPlaying: List<Movie> = emptyList(),
    val upcoming: List<Movie> = emptyList(),
    val scope: String? = null,
    val page: Int = 0,
    val all: List<Movie> = emptyList()
)