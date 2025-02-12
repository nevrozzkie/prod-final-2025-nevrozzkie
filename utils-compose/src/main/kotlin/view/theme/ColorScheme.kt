package view.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.prod2025.nevrozqIndividual.R

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

@Composable
fun greenLightColorScheme():
        ColorScheme
        = ColorScheme(
    primary =                   Colors.greenLightPrimary,
    onPrimary =                 Colors.greenLightOnPrimary,
    primaryContainer =          Colors.greenLightPrimaryContainer,
    onPrimaryContainer =        Colors.greenLightOnPrimaryContainer,
    inversePrimary =            Colors.greenLightInversePrimary,
    secondary =                 Colors.greenLightSecondary,
    onSecondary =               Colors.greenLightOnSecondary,
    secondaryContainer =        Colors.greenLightSecondaryContainer,
    onSecondaryContainer =      Colors.greenLightOnSecondaryContainer,
    tertiary =                  Colors.greenLightTertiary,
    onTertiary =                Colors.greenLightOnTertiary,
    tertiaryContainer =         Colors.greenLightTertiaryContainer,
    onTertiaryContainer =       Colors.greenLightOnTertiaryContainer,
    background =                Colors.greenLightBackground,
    onBackground =              Colors.greenLightOnBackground,
    surface =                   Colors.greenLightSurface,
    onSurface =                 Colors.greenLightOnSurface,
    surfaceVariant =            Colors.greenLightSurfaceVariant,
    onSurfaceVariant =          Colors.greenLightOnSurfaceVariant,
    surfaceTint =               Colors.greenLightSurfaceTint,
    inverseSurface =            Colors.greenLightInverseSurface,
    inverseOnSurface =          Colors.greenLightInverseOnSurface,
    error =                     Colors.greenLightError,
    onError =                   Colors.greenLightOnError,
    errorContainer =            Colors.greenLightErrorContainer,
    onErrorContainer =          Colors.greenLightOnErrorContainer,
    outline =                   Colors.greenLightOutline,
    outlineVariant =            Colors.greenLightOutlineVariant,
    scrim = Color.Black
)
@Composable
fun greenDarkColorScheme():
        ColorScheme
        = ColorScheme(
    primary =                   Colors.greenDarkPrimary,
    onPrimary =                 Colors.greenDarkOnPrimary,
    primaryContainer =          Colors.greenDarkPrimaryContainer,
    onPrimaryContainer =        Colors.greenDarkOnPrimaryContainer,
    inversePrimary =            Colors.greenDarkInversePrimary,
    secondary =                 Colors.greenDarkSecondary,
    onSecondary =               Colors.greenDarkOnSecondary,
    secondaryContainer =        Colors.greenDarkSecondaryContainer,
    onSecondaryContainer =      Colors.greenDarkOnSecondaryContainer,
    tertiary =                  Colors.greenDarkTertiary,
    onTertiary =                Colors.greenDarkOnTertiary,
    tertiaryContainer =         Colors.greenDarkTertiaryContainer,
    onTertiaryContainer =       Colors.greenDarkOnTertiaryContainer,
    background =                Colors.greenDarkBackground,
    onBackground =              Colors.greenDarkOnBackground,
    surface =                   Colors.greenDarkSurface,
    onSurface =                 Colors.greenDarkOnSurface,
    surfaceVariant =            Colors.greenDarkSurfaceVariant,
    onSurfaceVariant =          Colors.greenDarkOnSurfaceVariant,
    surfaceTint =               Colors.greenDarkSurfaceTint,
    inverseSurface =            Colors.greenDarkInverseSurface,
    inverseOnSurface =          Colors.greenDarkInverseOnSurface,
    error =                     Colors.greenDarkError,
    onError =                   Colors.greenDarkOnError,
    errorContainer =            Colors.greenDarkErrorContainer,
    onErrorContainer =          Colors.greenDarkOnErrorContainer,
    outline =                   Colors.greenDarkOutline,
    outlineVariant =            Colors.greenDarkOutlineVariant,
    scrim = Color.Black
)