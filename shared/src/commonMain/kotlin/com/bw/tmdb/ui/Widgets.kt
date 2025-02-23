package com.bw.tmdb.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.bw.tmdb.transport.Movie
import com.skydoves.landscapist.coil3.CoilImage

@Composable
fun MoviePosterImage(movie: Movie, width: Int = 154, onClick: (Movie) -> Unit) {
    val imageUrl = "https://image.tmdb.org/t/p/w$width${movie.poster_path}"

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