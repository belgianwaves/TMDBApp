package com.bw.tmdb.ui.movies

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bw.tmdb.MPBackHandler
import com.bw.tmdb.transport.Movie
import com.bw.tmdb.ui.Destinations
import com.bw.tmdb.ui.MovieGrid
import com.bw.tmdb.ui.MoviePosterImage
import org.jetbrains.compose.resources.stringResource
import tmdbapp.shared.generated.resources.Res
import tmdbapp.shared.generated.resources.back
import tmdbapp.shared.generated.resources.movies
import tmdbapp.shared.generated.resources.now_playing
import tmdbapp.shared.generated.resources.popular
import tmdbapp.shared.generated.resources.top_rated
import tmdbapp.shared.generated.resources.upcoming

@Composable
fun Movies(navController: NavController) {
    val viewModel: MoviesViewModel = viewModel { MoviesViewModel() }
    val state by viewModel.state.collectAsState()

    val labels = mapOf(
        "top_rated" to stringResource(Res.string.top_rated),
        "popular" to stringResource(Res.string.popular),
        "now_playing" to stringResource(Res.string.now_playing),
        "upcoming" to stringResource(Res.string.upcoming)
    )

    MPBackHandler(state.scope != null) {
        viewModel.fallbackToLanding()
    }

    if (state.topRated.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val scope = state.scope
    if (scope != null) {
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
                            viewModel.fallbackToLanding()
                        }
                        .padding(end = 16.dp)
                )
                Text(
                    labels[scope] ?: stringResource(Res.string.movies),
                    style = MaterialTheme.typography.titleLarge
                )
            }

            MovieGrid(
                state.all, {
                    viewModel.fetchMore()
                }
            ) {
                navController.navigate("${Destinations.MovieDetails}/${it.id}")
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                MovieRow(labels["top_rated"] ?: "Top Rated", state.topRated, {
                    navController.navigate("${Destinations.MovieDetails}/${it.id}")
                }) {
                    viewModel.switchToAll("top_rated")
                }
            }
            item {
                MovieRow(labels["popular"] ?: "Popular", state.popular, {
                    navController.navigate("${Destinations.MovieDetails}/${it.id}")
                }) {
                    viewModel.switchToAll("popular")
                }
            }
            item {
                MovieRow(labels["now_playing"] ?: "Now Playing", state.nowPlaying, {
                    navController.navigate("${Destinations.MovieDetails}/${it.id}")
                }) {
                    viewModel.switchToAll("now_playing")
                }
            }
            item {
                MovieRow(labels["upcoming"] ?: "Upcoming", state.upcoming, {
                    navController.navigate("${Destinations.MovieDetails}/${it.id}")
                }) {
                    viewModel.switchToAll("upcoming")
                }
            }
        }
    }
}

@Composable
private fun MovieRow(
    label: String,
    movies: List<Movie>,
    onClick: (Movie) -> Unit,
    onAll: () -> Unit) {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .padding(bottom = 16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp)
        ) {
            Text(
                label,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .semantics {
                        contentDescription = ""
                    }
            )
            Text(
                ">>",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clip(RoundedCornerShape(25.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable {
                        onAll()
                    }
                    .semantics {
                        contentDescription = label
                        role = Role.Button
                    }
            )
        }
        LazyRow{
            items(movies.size) { index ->
                val movie = movies[index]
                MoviePosterImage(movie, onClick = onClick)
            }
        }
    }
}
