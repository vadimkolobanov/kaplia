package com.kaplia.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

// Kaplia is always dark — the deep-space aesthetic is core to the brand.
// Dynamic Material You colours would override this intentional palette.
private val KaplaColorScheme =
    darkColorScheme(
        primary = KapliaBlue,
        onPrimary = DeepSpace,
        primaryContainer = KapliaBlueDark,
        onPrimaryContainer = KapliaBlueLight,
        secondary = KapliaLavender,
        onSecondary = DeepSpace,
        secondaryContainer = KapliaLavenderDark,
        onSecondaryContainer = KapliaLavenderLight,
        background = DeepSpace,
        onBackground = TextPrimary,
        surface = SpaceSurface,
        onSurface = TextPrimary,
        surfaceVariant = SpaceSurfaceVariant,
        onSurfaceVariant = TextSecondary,
        error = DangerRed,
        onError = DeepSpace,
    )

@Composable
fun KapliaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = KaplaColorScheme,
        typography = Typography,
        content = content,
    )
}
