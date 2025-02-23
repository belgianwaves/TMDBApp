package com.bw.tmdb

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import com.bw.tmdb.db.Movie
import com.bw.tmdb.ui.MyApplicationTheme
import com.bw.tmdb.ui.Navigation
import platform.UIKit.UIViewController

object SharedViewControllers {
    fun navigationViewController(
        onOpenYoutube: (Movie) -> Unit,
        onOpenBrowser: (Movie) -> Unit
    ): UIViewController = ComposeUIViewController {
        MyApplicationTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Navigation(
                    onOpenYoutube = onOpenYoutube,
                    onOpenBrowser = onOpenBrowser
                )
            }
        }
    }
}