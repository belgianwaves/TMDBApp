package com.bw.tmdb.transport

import kotlinx.serialization.Serializable

@Serializable
data class MoviesResult(
    val results: List<Movie>
)

@Serializable
data class Genre(
    val id: Int,
    val name: String
)

@Serializable
data class Movie(
    val id: Int,
    val title: String,
    val poster_path: String,
    val backdrop_path: String,
    val vote_average: Float
)

@Serializable
data class MovieDetails(
    val id: Int,
    val title: String,
    val original_title: String,
    val overview: String,
    val tagline: String,
    val status: String,
    val release_date: String,
    val genres: List<Genre>,
    val poster_path: String,
    val backdrop_path: String,
    val vote_average: Float
)
