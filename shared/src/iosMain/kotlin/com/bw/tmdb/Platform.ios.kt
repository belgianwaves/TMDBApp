package com.bw.tmdb

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.bw.tmdb.db.Database
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.AcceptAllCookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual fun createHttpClient(verbose: Boolean): HttpClient {
    return HttpClient(Darwin) {
        install(HttpTimeout) {
            requestTimeoutMillis = 30000
            connectTimeoutMillis = 30000
        }
        this.followRedirects = true
        install(HttpCookies) {
            this.storage = AcceptAllCookiesStorage()
        }
        install(ContentNegotiation) {
            json(Json {
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            level = if (verbose) LogLevel.HEADERS else LogLevel.NONE
        }
        install(ContentEncoding) {
            gzip()
        }
        install(UserAgent) {
            agent = "TMDBApp/1.0"
        }
    }
}

actual class DriverFactory() {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(Database.Schema, "movies.db", maxReaderConnections = 4)
    }
}