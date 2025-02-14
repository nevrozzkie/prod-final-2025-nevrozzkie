package view.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

@Composable
private fun Color.animate(animationSpec: AnimationSpec<Color>): Color {
    return animateColorAsState(this, animationSpec, label = "colorAnimation").value
}

@Composable
fun ColorScheme.animated(animationSpec: AnimationSpec<Color> = remember { spring(stiffness = Spring.StiffnessLow) }): ColorScheme {
    return this.copy(
        primary = this.primary.animate(animationSpec),
        primaryContainer = this.primaryContainer.animate(animationSpec),
        secondary = this.secondary.animate(animationSpec),
        secondaryContainer = this.secondaryContainer.animate(animationSpec),
        tertiary = this.tertiary.animate(animationSpec),
        tertiaryContainer = this.tertiaryContainer.animate(animationSpec),
        background = this.background.animate(animationSpec),
        surface = this.surface.animate(animationSpec),
        surfaceTint = this.surfaceTint.animate(animationSpec),
        surfaceBright = this.surfaceBright.animate(animationSpec),
        surfaceDim = this.surfaceDim.animate(animationSpec),
        surfaceContainer = this.surfaceContainer.animate(animationSpec),
        surfaceContainerHigh = this.surfaceContainerHigh.animate(animationSpec),
        surfaceContainerHighest = this.surfaceContainerHighest.animate(animationSpec),
        surfaceContainerLow = this.surfaceContainerLow.animate(animationSpec),
        surfaceContainerLowest = this.surfaceContainerLowest.animate(animationSpec),
        surfaceVariant = this.surfaceVariant.animate(animationSpec),
        error = this.error.animate(animationSpec),
        errorContainer = this.errorContainer.animate(animationSpec),
        onPrimary = this.onPrimary.animate(animationSpec),
        onPrimaryContainer = this.onPrimaryContainer.animate(animationSpec),
        onSecondary = this.onSecondary.animate(animationSpec),
        onSecondaryContainer = this.onSecondaryContainer.animate(animationSpec),
        onTertiary = this.onTertiary.animate(animationSpec),
        onTertiaryContainer = this.onTertiaryContainer.animate(animationSpec),
        onBackground = this.onBackground.animate(animationSpec),
        onSurface = this.onSurface.animate(animationSpec),
        onSurfaceVariant = this.onSurfaceVariant.animate(animationSpec),
        onError = this.onError.animate(animationSpec),
        onErrorContainer = this.onErrorContainer.animate(animationSpec),
        inversePrimary = this.inversePrimary.animate(animationSpec),
        inverseSurface = this.inverseSurface.animate(animationSpec),
        inverseOnSurface = this.inverseOnSurface.animate(animationSpec),
        outline = this.outline.animate(animationSpec),
        outlineVariant = this.outlineVariant.animate(animationSpec),
        scrim = this.scrim.animate(animationSpec),
    )
}
val LightColorScheme = ColorScheme(
    primary = Color(0xFFFFEB3B), // Yellow 600
    onPrimary = Color(0xFF212121), // Black
    primaryContainer = Color(0xFFF9A825), // Yellow 800
    onPrimaryContainer = Color(0xFFFFFFFF), // White
    inversePrimary = Color(0xFFFFF176), // Yellow 300
    secondary = Color(0xFF64B5F6), // Blue 300
    onSecondary = Color(0xFFFFFFFF), // White
    secondaryContainer = Color(0xFFBBDEFB), // Blue 100
    onSecondaryContainer = Color(0xFF212121), // Black
    tertiary = Color(0xFF81C784), // Green 300
    onTertiary = Color(0xFFFFFFFF), // White
    tertiaryContainer = Color(0xFFC8E6C9), // Green 100
    onTertiaryContainer = Color(0xFF212121), // Black
    background = Color(0xFFFAFAFA), // Light Gray
    onBackground = Color(0xFF212121), // Black
    surface = Color(0xFFFFFFFF), // White
    onSurface = Color(0xFF212121), // Black
    surfaceVariant = Color(0xFFEEEEEE), // Light Gray
    onSurfaceVariant = Color(0xFF757575), // Gray
    surfaceTint = Color(0xFFF9A825), // Yellow 800
    inverseSurface = Color(0xFF212121), // Black
    inverseOnSurface = Color(0xFFFFFFFF), // White
    error = Color(0xFFE53935), // Red 600
    onError = Color(0xFFFFFFFF), // White
    errorContainer = Color(0xFFFFCDD2), // Red 100
    onErrorContainer = Color(0xFF212121), // Black
    outline = Color(0xFFBDBDBD), // Gray 400
    outlineVariant = Color(0xFFEEEEEE), // Light Gray
    scrim = Color(0x99000000), // Black with 60% opacity
    surfaceBright = Color(0xFFFFFFFF), // White
    surfaceDim = Color(0xFFF5F5F5), // Light Gray
    surfaceContainer = Color(0xFFEEEEEE), // Light Gray
    surfaceContainerHigh = Color(0xFFE0E0E0), // Gray 200
    surfaceContainerHighest = Color(0xFFBDBDBD), // Gray 400
    surfaceContainerLow = Color(0xFFF5F5F5), // Light Gray
    surfaceContainerLowest = Color(0xFFFFFFFF) // White
)

// Dark Colors
// Dark Colors
val DarkColorScheme = ColorScheme(
    primary = Color(0xFFFFEB3B), // Yellow 600
    onPrimary = Color(0xFF212121), // Black
    primaryContainer = Color(0xFFF9A825), // Yellow 800
    onPrimaryContainer = Color(0xFFFFFFFF), // White
    inversePrimary = Color(0xFFF9A825), // Yellow 800
    secondary = Color(0xFF42A5F5), // Blue 400
    onSecondary = Color(0xFF212121), // Black
    secondaryContainer = Color(0xFF64B5F6), // Blue 300
    onSecondaryContainer = Color(0xFFFFFFFF), // White
    tertiary = Color(0xFF66BB6A), // Green 400
    onTertiary = Color(0xFF212121), // Black
    tertiaryContainer = Color(0xFF81C784), // Green 300
    onTertiaryContainer = Color(0xFFFFFFFF), // White
    background = Color(0xFF121212), // Dark Gray
    onBackground = Color(0xFFFFFFFF), // White
    surface = Color(0xFF1E1E1E), // Dark Gray
    onSurface = Color(0xFFFFFFFF), // White
    surfaceVariant = Color(0xFF2E2E2E), // Dark Gray
    onSurfaceVariant = Color(0xFFBDBDBD), // Gray 400
    surfaceTint = Color(0xFFFFEB3B), // Yellow 600
    inverseSurface = Color(0xFFFFFFFF), // White
    inverseOnSurface = Color(0xFF212121), // Black
    error = Color(0xFFEF5350), // Red 400
    onError = Color(0xFF212121), // Black
    errorContainer = Color(0xFFE53935), // Red 600
    onErrorContainer = Color(0xFFFFFFFF), // White
    outline = Color(0xFF757575), // Gray 600
    outlineVariant = Color(0xFF424242), // Dark Gray
    scrim = Color(0x99000000), // Black with 60% opacity
    surfaceBright = Color(0xFF2E2E2E), // Dark Gray
    surfaceDim = Color(0xFF121212), // Dark Gray
    surfaceContainer = Color(0xFF1E1E1E), // Dark Gray
    surfaceContainerHigh = Color(0xFF2E2E2E), // Dark Gray
    surfaceContainerHighest = Color(0xFF424242), // Dark Gray
    surfaceContainerLow = Color(0xFF1E1E1E), // Dark Gray
    surfaceContainerLowest = Color(0xFF121212) // Dark Gray
)