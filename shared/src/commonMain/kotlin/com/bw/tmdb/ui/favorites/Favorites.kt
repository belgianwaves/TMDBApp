package com.bw.tmdb.ui.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bw.tmdb.db.Movie
import com.bw.tmdb.ui.Destinations
import com.skydoves.landscapist.coil3.CoilImage
import org.jetbrains.compose.resources.stringResource
import tmdbapp.shared.generated.resources.Res
import tmdbapp.shared.generated.resources.empty_favorites

@Composable
fun Favorites(navController: NavController) {
    val viewModel: FavoritesViewModel = viewModel { FavoritesViewModel() }
    val state by viewModel.state.collectAsState()

    if (state.favorites.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                stringResource(Res.string.empty_favorites),
                textAlign = TextAlign.Center
            )
        }
        return
    }

    BoxWithConstraints {
        LazyVerticalGrid(
            columns = GridCells.Fixed(if (maxWidth > maxHeight) 4 else 2),
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
}

@Composable
private fun MoviePosterImage(movie: Movie, width: Int = 154, onClick: (Movie) -> Unit) {
    val imageUrl = "https://image.tmdb.org/t/p/w$width${movie.poster_path}"

    Box(
        modifier = Modifier
            .width(150.dp)
            .wrapContentHeight()
            .padding(8.dp)
    ) {
        CoilImage(
            imageModel = { imageUrl },
            modifier = Modifier
                .clickable {
                    onClick(movie)
                }
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .semantics {
                    contentDescription = movie.title
                }
        )
    }
}