package com.tets.manhwa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.tets.manhwa.ui.theme.TetsReaderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TetsReaderTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val appContainer = (application as TetsApplication).appContainer
                    TetsApp(appContainer)
                }
            }
        }
    }
}
