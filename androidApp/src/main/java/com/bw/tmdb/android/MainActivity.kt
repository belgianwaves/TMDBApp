package com.bw.tmdb.android

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.bw.tmdb.Config
import com.bw.tmdb.DebugLog
import com.bw.tmdb.DriverFactory
import com.bw.tmdb.Graph
import com.bw.tmdb.getCurrentLocale
import com.bw.tmdb.ui.MyApplicationTheme
import com.bw.tmdb.ui.Navigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Config.VERBOSE = 0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
        Config.LANGUAGE = getCurrentLocale()

        DebugLog.verbose = Config.VERBOSE

        Graph.init(DriverFactory(this))

        setContent {
            val context = LocalContext.current

            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation(
                        onOpenYoutube = {
                            val videoId = it.videos.split(", ")[0]

                            val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$videoId"))
                            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=$videoId"))

                            try {
                                context.startActivity(appIntent)
                            } catch (e: Exception) {
                                context.startActivity(webIntent)
                            }
                        },
                        onOpenBrowser = {
                            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it.homepage))

                            try {
                                context.startActivity(webIntent)
                            } catch (e: Exception) {
                                DebugLog.e("no browser available", e)
                            }
                        }
                    )
                }
            }
        }
    }
}