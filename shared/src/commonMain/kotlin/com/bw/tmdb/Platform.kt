package com.bw.tmdb

import androidx.compose.runtime.Composable
import app.cash.sqldelight.db.SqlDriver
import io.ktor.client.HttpClient

interface Platform {
    val name: String

    fun isIOS() = name.lowercase().contains("ios")
}

expect fun getPlatform(): Platform

expect fun getCurrentLocale(): String

expect fun createHttpClient(verbose: Boolean = true): HttpClient

expect class DriverFactory {
    fun createDriver(): SqlDriver
}

@Composable
expect fun MPBackHandler(enabled: Boolean = true, onBack: () -> Unit)