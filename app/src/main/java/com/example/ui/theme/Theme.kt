package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = GoldAccent,
    onPrimary = NavyDark,
    primaryContainer = NavyCard,
    onPrimaryContainer = GoldLight,
    secondary = OrangeAccent,
    onSecondary = Color.White,
    background = NavyDark,
    onBackground = Color.White,
    surface = NavyPrimary,
    onSurface = Color.White,
    surfaceVariant = NavyCard,
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = NavyBorder
)

private val LightColorScheme = lightColorScheme(
    primary = NavyPrimary,
    onPrimary = Color.White,
    primaryContainer = NavyCard,
    onPrimaryContainer = GoldAccent,
    secondary = OrangeAccent,
    onSecondary = Color.White,
    background = SlateLight,
    onBackground = TextDark,
    surface = Color.White,
    onSurface = TextDark,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = TextMuted,
    outline = SlateBorder
)

@Composable
fun MSZenoMartTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
      colorScheme = colorScheme,
      typography = Typography,
      content = content
  )
}

