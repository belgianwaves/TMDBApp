package com.bw.tmdb.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bw.tmdb.transport.Movie
import com.skydoves.landscapist.coil3.CoilImage
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.placeholder.shimmer.Shimmer
import com.skydoves.landscapist.placeholder.shimmer.ShimmerPlugin
import kotlin.math.roundToInt

@Composable
fun MoviePosterImage(movie: Movie, width: Int = 154, onClick: (Movie) -> Unit) {
    val imageUrl = "https://image.tmdb.org/t/p/w$width${movie.poster_path}"

    Box(
        modifier = Modifier
            .width(150.dp)
            .heightIn(min = 150.dp)
            .wrapContentHeight()
            .padding(8.dp)
    ) {
        CoilImage(
            imageModel = { imageUrl },
            component = rememberImageComponent {
                 +ShimmerPlugin(
                    Shimmer.Flash(
                        baseColor = Color.White,
                        highlightColor = Color.LightGray,
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .clickable {
                    onClick(movie)
                }
        )
    }
}

@Composable
fun RatingWidget(rating: Double) {
    val percentage = (rating * 10).roundToInt()
    val progress = percentage / 100f

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(50.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = Color.Gray.copy(alpha = 0.3f),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 6.dp.toPx())
            )
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = if (percentage >= 70) Color.Green else if (percentage >= 60) Color.Yellow else Color.Red,
                startAngle = -90f,
                sweepAngle = progress * 360f,
                useCenter = false,
                style = Stroke(width = 6.dp.toPx())
            )
        }

        Text(
            text = "$percentage%",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}