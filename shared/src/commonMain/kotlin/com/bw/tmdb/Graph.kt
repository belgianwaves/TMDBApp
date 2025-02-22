package com.bw.tmdb

import com.russhwolf.settings.Settings

object Graph {
    val api = Api(Config.VERBOSE, Config.LANGUAGE)
    val settings = Settings()
}