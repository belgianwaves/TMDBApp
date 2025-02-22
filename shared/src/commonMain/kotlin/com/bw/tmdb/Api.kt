package com.bw.tmdb

import com.bw.tmdb.transport.Movie
import com.bw.tmdb.transport.MovieDetails
import com.bw.tmdb.transport.MoviesResult
import com.bw.tmdb.transport.Video
import com.bw.tmdb.transport.VideosResult
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders

class Api(verbose: Boolean, private val language: String) {
    private val httpClient = createHttpClient(verbose)
    private val apiKey = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI0NWFjYzFiY2I4OTMzOTQyZTY5MjNjMGQxYmZmZDA4NCIsIm5iZiI6MTMyMTM1MDg3MC4wLCJzdWIiOiI0ZWMyMzZkNjVlNzNkNjQ5NGMwMDEwMGEiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.Cd_mKjEqpuNyELEF2muS65WD-8qVZHNiNu4iZRvOU7E"

    suspend fun getTopRatedMovies(page: Int = 1): List<Movie> {
        return getMovies("top_rated", page)
    }
    suspend fun getPopularMovies(page: Int = 1): List<Movie> {
        return getMovies("popular", page)
    }
    suspend fun getNowPlayingMovies(page: Int = 1): List<Movie> {
        return getMovies("now_playing", page)
    }
    suspend fun getUpcomingMovies(page: Int = 1): List<Movie> {
        return getMovies("upcoming", page)
    }

    fun getMovieScopes(): List<String> {
        return listOf(
            "top_rated",
            "popular",
            "now_playing",
            "upcoming"
        )
    }

    suspend fun getMovies(scope: String = "top_rated", page: Int = 1): List<Movie> {
        val url = "https://api.themoviedb.org/3/movie/$scope?language=$language&page=$page"

        val response: HttpResponse = httpClient.get(url) {
            headers {
                append(HttpHeaders.Authorization, "Bearer $apiKey")
            }
        }
        return response.body<MoviesResult>().results
    }

    suspend fun getMovieDetails(id: Int): MovieDetails {
        val url = "https://api.themoviedb.org/3/movie/$id?language=$language"

        val response: HttpResponse = httpClient.get(url) {
            headers {
                append(HttpHeaders.Authorization, "Bearer $apiKey")
            }
        }
        return response.body<MovieDetails>()
    }

    suspend fun getVideos(id: Int): List<Video> {
        val url = "https://api.themoviedb.org/3/movie/$id/videos?language=$language"

        val response: HttpResponse = httpClient.get(url) {
            headers {
                append(HttpHeaders.Authorization, "Bearer $apiKey")
            }
        }
        return response.body<VideosResult>().results
    }
}