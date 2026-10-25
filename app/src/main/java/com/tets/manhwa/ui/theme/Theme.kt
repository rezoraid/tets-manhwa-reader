package com.tets.manhwa.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColors = darkColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFFB11D2B),
    secondary = androidx.compose.ui.graphics.Color(0xFF141414),
    background = androidx.compose.ui.graphics.Color(0xFF080808),
    surface = androidx.compose.ui.graphics.Color(0xFF141414),
    onPrimary = androidx.compose.ui.graphics.Color.White,
    onBackground = androidx.compose.ui.graphics.Color(0xFFF5F5F7),
    onSurface = androidx.compose.ui.graphics.Color(0xFFF5F5F7)
)

private val LightColors = lightColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFFB11D2B),
    background = androidx.compose.ui.graphics.Color(0xFFF5F5F7),
    surface = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
    onBackground = androidx.compose.ui.graphics.Color(0xFF111111),
    onSurface = androidx.compose.ui.graphics.Color(0xFF111111)
)

@Composable
fun TetsReaderTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}
