package com.bw.tmdb.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bw.tmdb.ui.favorites.Favorites
import com.bw.tmdb.ui.movies.MovieDetails
import com.bw.tmdb.ui.movies.Movies
import com.bw.tmdb.ui.search.Search
import org.jetbrains.compose.resources.stringResource
import tmdbapp.shared.generated.resources.Res
import tmdbapp.shared.generated.resources.favorites
import tmdbapp.shared.generated.resources.movies
import tmdbapp.shared.generated.resources.search

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHostMain(
        navController = navController
    )
}

@Composable
fun NavHostMain(
    navController: NavHostController
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = backStackEntry?.destination

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController, currentScreen?.route ?: Destinations.Movies)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomBarScreen.Movies.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            }
        ) {
            composable(route = Destinations.Movies) {
                Movies(navController)
            }
            composable(route = Destinations.Favorites) {
                Favorites(navController)
            }
            composable(route = Destinations.Search) {
                Search(navController)
            }
            composable(route = "${Destinations.MovieDetails}/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: "0"
                MovieDetails(navController, id.toInt())
            }
        }
    }
}

sealed class BottomBarScreen(
    val route: String,
    val defaultIcon: ImageVector
) {
    data object Movies : BottomBarScreen(
        route = Destinations.Movies,
        defaultIcon = Icons.Filled.Movie
    )

    data object Favorites : BottomBarScreen(
        route = Destinations.Favorites,
        defaultIcon = Icons.Filled.Favorite
    )

    data object Search : BottomBarScreen(
        route = Destinations.Search,
        defaultIcon = Icons.Filled.Search
    )
}

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    currentRoute: String
) {
   val screens = listOf(
        BottomBarScreen.Movies,
        BottomBarScreen.Favorites,
        BottomBarScreen.Search
    )

    val labels = mapOf(
        Destinations.Movies to stringResource(Res.string.movies),
        Destinations.Favorites to stringResource(Res.string.favorites),
        Destinations.Search to stringResource(Res.string.search)
    )

    AppBottomNavigationBar(show = !currentRoute.contains(Destinations.MovieDetails)) {
        screens.forEach { item ->
            AppBottomNavigationBarItem(
                icon = item.defaultIcon,
                label = labels[item.route] ?: "",
                onClick = {
                    navigateBottomBar(navController, item.route)
                },
                selected = currentRoute == item.route
            )
        }
    }
}

@Composable
fun AppBottomNavigationBar(
    modifier: Modifier = Modifier,
    show: Boolean,
    content: @Composable (RowScope.() -> Unit),
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        modifier = modifier.windowInsetsPadding(BottomAppBarDefaults.windowInsets)
    ) {
        if (show) {
            Column {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp)
                        .selectableGroup(),
                    verticalAlignment = Alignment.CenterVertically,
                    content = content
                )
            }
        }
    }
}

@Composable
fun RowScope.AppBottomNavigationBarItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    selected: Boolean,
) {
    Column(
        modifier = modifier
            .weight(1f)
            .clickable(
                onClick = onClick,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Image(
            painter = rememberVectorPainter(icon),
            contentDescription = label,
            contentScale = ContentScale.Crop,
            colorFilter = if (selected) {
                ColorFilter.tint(MaterialTheme.colorScheme.primary)
            } else {
                ColorFilter.tint(MaterialTheme.colorScheme.outline)
            },
            modifier = modifier.then(
                Modifier.clickable {
                    onClick()
                }
                    .size(24.dp)
            )
        )

        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (selected) {
                FontWeight.SemiBold
            } else {
                FontWeight.Normal
            },
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outline
            }
        )
    }
}

private fun navigateBottomBar(navController: NavController, destination: String) {
    navController.navigate(destination) {
        navController.graph.startDestinationRoute?.let { route ->
            popUpTo(Destinations.Movies) {
                saveState = true
            }
        }
        launchSingleTop = true
        restoreState = true
    }
}