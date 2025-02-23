package com.bw.tmdb.ui.movies

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bw.tmdb.db.Movie
import com.bw.tmdb.ui.RatingWidget
import com.skydoves.landscapist.coil3.CoilImage
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import org.jetbrains.compose.resources.stringResource
import tmdbapp.shared.generated.resources.Res
import tmdbapp.shared.generated.resources.back
import tmdbapp.shared.generated.resources.open_browser
import tmdbapp.shared.generated.resources.open_youtube
import tmdbapp.shared.generated.resources.toggle_liked

@Composable
fun MovieDetails(
    navController: NavController,
    id: Int,
    onOpenYoutube: (Movie) -> Unit,
    onOpenBrowser: (Movie) -> Unit) {
    val viewModel: MovieDetailsViewModel = viewModel { MovieDetailsViewModel() }
    val state by viewModel.state.collectAsState()

    val movie = state.movie

    if (movie != null) {
        Box {
            Backdrop(movie)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(Res.string.back),
                        modifier = Modifier
                            .clickable {
                                navController.navigateUp()
                            }
                            .padding(end = 16.dp)
                    )
                    Column {
                        Text(
                            movie.title,
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 1
                        )
                        Text(
                            movie.original_title,
                            style = MaterialTheme.typography.titleSmall,
                            maxLines = 1
                        )
                    }
                }

                LazyColumn {
                    item {
                        MovieHeader(movie)
                    }
                    item {
                        MovieActions(
                            movie,
                            onOpenYoutube,
                            onOpenBrowser, {
                                viewModel.setLiked(it.id, !it.liked)
                            }
                        )
                    }
                    item {
                        MovieOverview(movie)
                    }
                }
            }
        }
    }

    LaunchedEffect(id) {
        viewModel.fetchDetails(id)
    }
}


@Composable
private fun MovieHeader(movie: Movie) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        MoviePosterImage(movie)
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(movie.release_date)
            Text(movie.status)

            Text(
                movie.genres,
                modifier = Modifier
                    .padding(bottom = 8.dp)
            )

            RatingWidget(movie.vote_average)
        }
    }
}

@Composable
private fun MovieActions(
    movie: Movie,
    onOpenYoutube: (Movie) -> Unit,
    onOpenBrowser: (Movie) -> Unit,
    onToggleLike: (Movie) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
            .padding(horizontal = 32.dp, vertical = 16.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Movie,
            contentDescription = stringResource(Res.string.open_youtube),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = if (movie.videos.isBlank()) 0.2f else 1.0f),
            modifier = Modifier
                .clickable {
                    onOpenYoutube(movie)
                }
                .size(32.dp)
        )
        Icon(
            imageVector = Icons.Filled.OpenInBrowser,
            contentDescription = stringResource(Res.string.open_browser),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = if (movie.homepage.isNullOrBlank()) 0.2f else 1.0f),
            modifier = Modifier
                .clickable {
                    onOpenBrowser(movie)
                }
                .size(32.dp)
        )
        Icon(
            imageVector = if (movie.liked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            contentDescription = stringResource(Res.string.toggle_liked),
            modifier = Modifier
                .clickable {
                    onToggleLike(movie)
                }
                .size(32.dp)
        )
    }
}

@Composable
private fun MovieOverview(movie: Movie) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        if (movie.tagline.isNotBlank()) {
            Text(
                movie.tagline,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 8.dp)
            )
        }
        Text(
            movie.overview
        )
    }
}

@Composable
private fun Backdrop(movie: Movie) {
    val hazeState = remember { HazeState() }

    val imageUrl = "https://image.tmdb.org/t/p/w342${movie.poster_path}"

    CoilImage(
        imageModel = { imageUrl },
        modifier = Modifier
            .fillMaxSize()
            .hazeSource(state = hazeState)
    )

    Surface(
        modifier = Modifier
            .hazeEffect(
                state = hazeState,
                style = hazeMaterial(MaterialTheme.colorScheme.surface, 0.35f)
            )
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
    ) {
    }
}

@Composable
private fun MoviePosterImage(movie: Movie) {
    val imageUrl = "https://image.tmdb.org/t/p/w154${movie.poster_path}"

    Box(
        modifier = Modifier
            .width(150.dp)
            .wrapContentHeight()
            .padding(8.dp)
    ) {
        CoilImage(
            imageModel = { imageUrl },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
        )
    }
}

private fun hazeMaterial(
    containerColor: Color,
    alpha: Float
): HazeStyle = HazeStyle(
    blurRadius = 24.dp,
    backgroundColor = containerColor,
    tint = HazeTint(
        containerColor.copy(alpha = alpha),
    ),
)