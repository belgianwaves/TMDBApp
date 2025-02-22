package com.bw.tmdb

import com.bw.tmdb.db.Database
import com.russhwolf.settings.Settings

object Graph {
    val api = Api(Config.VERBOSE, Config.LANGUAGE)
    lateinit var database: Database
        private set
    val settings = Settings()

    fun init(driverFactory: DriverFactory) {
        database = Database(driverFactory.createDriver())
    }
}