package com.bw.tmdb

import io.ktor.client.HttpClient

interface Platform {
    val name: String

    fun isIOS() = name.lowercase().contains("ios")
}

expect fun getPlatform(): Platform

expect fun createHttpClient(verbose: Boolean = true): HttpClient