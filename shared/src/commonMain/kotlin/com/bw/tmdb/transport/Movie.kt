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
    val poster_path: String? = null,
    val backdrop_path: String? = null,
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
    val poster_path: String? = null,
    val backdrop_path: String? =  null,
    val homepage: String? = null,
    val vote_average: Double
)

@Serializable
data class VideosResult(
    val results: List<Video>
)

@Serializable
data class Video(
    val key: String,
    val site: String,
    val official: Boolean
)
