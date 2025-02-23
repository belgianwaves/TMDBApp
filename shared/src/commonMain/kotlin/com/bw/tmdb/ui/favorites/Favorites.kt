package com.bw.tmdb.ui.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bw.tmdb.db.Movie
import com.bw.tmdb.ui.Destinations
import com.skydoves.landscapist.coil3.CoilImage

@Composable
fun Favorites(navController: NavController) {
    val viewModel: FavoritesViewModel = viewModel { FavoritesViewModel() }
    val state by viewModel.state.collectAsState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        itemsIndexed(state.favorites) { _, movie ->
            MoviePosterImage(movie) {
                navController.navigate("${Destinations.MovieDetails}/${it.id}")
            }
        }
    }
}

@Composable
private fun MoviePosterImage(movie: Movie, onClick: (Movie) -> Unit) {
    val imageUrl = "https://image.tmdb.org/t/p/w154${movie.poster_path}"

    Box(
        modifier = Modifier
            .width(150.dp)
            .wrapContentHeight()
    ) {
        CoilImage(
            imageModel = { imageUrl },
            modifier = Modifier
                .clickable {
                    onClick(movie)
                }
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .padding(8.dp)
        )
    }
}