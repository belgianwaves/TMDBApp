package com.bw.tmdb.ui.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bw.tmdb.ui.Destinations
import com.bw.tmdb.ui.MovieGrid
import com.bw.tmdb.ui.MoviePosterImage
import org.jetbrains.compose.resources.stringResource
import tmdbapp.shared.generated.resources.Res
import tmdbapp.shared.generated.resources.search_movies

@Composable
fun Search(navController: NavController) {
    val viewModel: SearchViewModel = viewModel { SearchViewModel() }
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = state.query,
            onValueChange = {
                viewModel.searchMovies(it)
            },
            label = { Text(stringResource(Res.string.search_movies)) },
            modifier = Modifier.fillMaxWidth()
        )

        MovieGrid(
            state.movies, {
            }
        ) {
            navController.navigate("${Destinations.MovieDetails}/${it.id}")
        }
    }
}