package com.bw.tmdb.ui.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.sqldelight.coroutines.asFlow
import com.bw.tmdb.Api
import com.bw.tmdb.Graph
import com.bw.tmdb.db.Database
import com.bw.tmdb.db.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val api: Api = Graph.api,
    private val database: Database = Graph.database
): ViewModel() {

    private val _state = MutableStateFlow(MovieDetailsViewState())

    val state: StateFlow<MovieDetailsViewState>
        get() = _state

    fun fetchDetails(id: Int) {
        viewModelScope.launch {
            val queries = database.moviesQueries
            val stored = queries.selectMovie(id.toLong()).executeAsOneOrNull()
            if (stored == null) {
                val videos = api.getVideos(id)

                val fetched = api.getMovieDetails(id)
                    queries.insertMovie(
                        id.toLong(),
                        fetched.title,
                        fetched.original_title,
                        fetched.overview,
                        fetched.tagline,
                        fetched.status,
                        fetched.release_date,
                        fetched.genres.joinToString(", ") { it.name },
                        fetched.poster_path,
                        fetched.backdrop_path,
                        fetched.homepage,
                        fetched.vote_average,
                        videos.filter { it.site.lowercase().contains("youtube") }
                            .joinToString(", ") { it.key },
                        false
                    )
            }

            queries.selectMovie(id.toLong()).asFlow().map { query ->
                MovieDetailsViewState(
                    query.executeAsOneOrNull()
                )
            }.collect { _state.value = it }
        }
    }

    fun setLiked(id: Long, liked: Boolean) {
        viewModelScope.launch {
            val queries = database.moviesQueries
            queries.updateMovie(liked, id)
        }
    }
}

data class MovieDetailsViewState(
    val movie: Movie? = null
)