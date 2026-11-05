package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ZavrangoffzColorScheme = darkColorScheme(
  primary = NeonCyan,
  onPrimary = ObsidianBlack,
  primaryContainer = CardDark,
  onPrimaryContainer = NeonCyanLight,
  secondary = ElectricBlue,
  onSecondary = ObsidianBlack,
  secondaryContainer = CardDark,
  onSecondaryContainer = TextPrimary,
  tertiary = GamerAzure,
  onTertiary = Color.White,
  background = ObsidianBlack,
  onBackground = TextPrimary,
  surface = PitchDark,
  onSurface = TextPrimary,
  surfaceVariant = CardDark,
  onSurfaceVariant = TextSecondary,
  outline = CardBorder,
  outlineVariant = CardBorderActive,
  error = StatusAlertRed,
  onError = Color.White
)

@Composable
fun ZavrangoffzTheme(
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = ZavrangoffzColorScheme,
    typography = Typography,
    content = content
  )
}
