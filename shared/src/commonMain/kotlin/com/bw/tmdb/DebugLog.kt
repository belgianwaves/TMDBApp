package com.bw.tmdb

import co.touchlab.kermit.Logger

object DebugLog {
    var verbose = false

    private const val tag = "tmdb_app"

    fun e(message: String) {
        Logger.e(tag) { "🔴 $message" }
    }

    fun e(message: String, t: Throwable?) {
        Logger.e(tag, t) { "🔴 $message" }
    }

    fun w(message: String) {
        Logger.w(tag) { message }
    }

    fun w(message: String, t: Throwable?) {
        Logger.w(tag, t) { message }
    }

    fun i(message: String) {
        Logger.i(tag) { message }
    }

    fun i(message: String, t: Throwable?) {
        Logger.i(tag, t) { message }
    }

    fun d(message: String) {
        if (verbose) Logger.d(tag) { message }
    }

    fun d(message: String, t: Throwable?) {
        if (verbose) Logger.d(tag, t) { message }
    }

    fun v(message: String) {
        if (verbose) Logger.v(tag) { message }
    }

    fun v(message: String, t: Throwable?) {
        if (verbose) Logger.v(tag, t) { message }
    }
}