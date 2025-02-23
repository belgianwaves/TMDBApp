package com.bw.tmdb.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bw.tmdb.Api
import com.bw.tmdb.DebugLog
import com.bw.tmdb.Graph
import com.bw.tmdb.transport.Movie
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(private val api: Api = Graph.api): ViewModel() {
    private val _state = MutableStateFlow(SearchViewState())

    private var searchJob: Job? = null

    val state: StateFlow<SearchViewState>
        get() = _state

    fun searchMovies(query: String) {
        _state.value = _state.value.copy(query = query)

        if (query.isBlank()) return

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)

            try {
                _state.value =  _state.value.copy(movies = api.searchMovies(query).filter { it.poster_path != null })
            } catch (e: Exception) {
                DebugLog.e("failed getting search results for query $query", e)
            }
        }
    }
}

data class SearchViewState(
    val query: String = "",
    val movies: List<Movie> = emptyList()
)