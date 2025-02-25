package com.bw.tmdb.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.sqldelight.coroutines.asFlow
import com.bw.tmdb.DebugLog
import com.bw.tmdb.Graph
import com.bw.tmdb.db.Database
import com.bw.tmdb.db.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val database: Database = Graph.database
): ViewModel() {

    private val _state = MutableStateFlow(FavoritesViewState())

    val state: StateFlow<FavoritesViewState>
        get() = _state

    init {
        viewModelScope.launch {
            val queries = database.moviesQueries
            queries.selectFavoriteMovies().asFlow().map { query ->
                FavoritesViewState(
                    query.executeAsList()
                )
            }.collect { _state.value = it }
        }
    }

    fun deleteFavorite(id: Long) {
        viewModelScope.launch {
            try {
                val queries = database.moviesQueries
                queries.deleteMovie(id)
            } catch (e: Exception) {
                DebugLog.e("failed deleting favorite $id", e)
            }
        }
    }
}


data class FavoritesViewState(
    val favorites: List<Movie> = emptyList()
)