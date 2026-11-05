package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.DoNotDisturb
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.StayCurrentPortrait
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.ripple
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardDark
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonCyanLight
import com.example.ui.theme.PitchDark
import com.example.ui.theme.StatusActiveGreen
import com.example.ui.theme.StatusAlertRed
import com.example.ui.theme.StatusWarningOrange
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GamingModeHUD(
  isGamingModeActive: Boolean,
  sessionDurationSeconds: Long,
  isDndActive: Boolean,
  hasDndPermission: Boolean,
  keepScreenAwake: Boolean,
  batteryTempCelsius: Float,
  ramUsagePercent: Int,
  onToggleGamingMode: () -> Unit,
  onToggleScreenAwake: () -> Unit,
  onDndChipClicked: () -> Unit,
  modifier: Modifier = Modifier
) {
  val infiniteTransition = rememberInfiniteTransition(label = "pulse")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 1.0f,
    targetValue = if (isGamingModeActive) 1.08f else 1.0f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulseScale"
  )

  val borderGlowColor by animateColorAsState(
    targetValue = if (isGamingModeActive) NeonCyan else Color(0xFF162536),
    animationSpec = tween(500),
    label = "glowColor"
  )

  Surface(
    modifier = modifier.fillMaxWidth(),
    color = PitchDark,
    border = BorderStroke(1.5.dp, borderGlowColor),
    shape = RoundedCornerShape(24.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Status Pill
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .clip(RoundedCornerShape(20.dp))
          .background(if (isGamingModeActive) Color(0xFF003847) else CardDark)
          .border(
            BorderStroke(1.dp, if (isGamingModeActive) NeonCyan else CardBorder),
            shape = RoundedCornerShape(20.dp)
          )
          .padding(horizontal = 14.dp, vertical = 6.dp)
      ) {
        Box(
          modifier = Modifier
            .size(10.dp)
            .clip(CircleShape)
            .background(if (isGamingModeActive) StatusActiveGreen else Color(0xFF78909C))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = if (isGamingModeActive) "GAMING MODE ACTIVE" else "GAMING MODE STANDBY",
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.5.sp,
          color = if (isGamingModeActive) NeonCyanLight else TextSecondary
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Central Pulse & One-Tap Action Button
      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(150.dp)
      ) {
        // Outer aura circle
        if (isGamingModeActive) {
          Box(
            modifier = Modifier
              .size(140.dp)
              .scale(pulseScale)
              .clip(CircleShape)
              .background(
                Brush.radialGradient(
                  colors = listOf(
                    NeonCyan.copy(alpha = 0.35f),
                    ElectricBlue.copy(alpha = 0.15f),
                    Color.Transparent
                  )
                )
              )
          )
        }

        // Action Core Button
        Box(
          contentAlignment = Alignment.Center,
          modifier = Modifier
            .size(116.dp)
            .clip(CircleShape)
            .background(
              Brush.linearGradient(
                colors = if (isGamingModeActive) {
                  listOf(Color(0xFF00B0FF), Color(0xFF00E5FF))
                } else {
                  listOf(Color(0xFF111923), Color(0xFF1E2B3C))
                }
              )
            )
            .border(
              BorderStroke(
                width = 3.dp,
                brush = Brush.sweepGradient(
                  colors = if (isGamingModeActive) {
                    listOf(NeonCyan, Color.White, ElectricBlue, NeonCyan)
                  } else {
                    listOf(Color(0xFF2C3E55), Color(0xFF162334), Color(0xFF2C3E55))
                  }
                )
              ),
              shape = CircleShape
            )
            .clickable(
              interactionSource = remember { MutableInteractionSource() },
              indication = ripple(bounded = true, color = NeonCyan),
              onClick = onToggleGamingMode
            )
            .testTag("gaming_mode_main_toggle")
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(
              imageVector = Icons.Default.PowerSettingsNew,
              contentDescription = "Toggle Gaming Mode",
              tint = if (isGamingModeActive) Color(0xFF050B14) else NeonCyan,
              modifier = Modifier.size(46.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = if (isGamingModeActive) "TAP OFF" else "TAP ON",
              fontSize = 11.sp,
              fontWeight = FontWeight.ExtraBold,
              letterSpacing = 1.sp,
              color = if (isGamingModeActive) Color(0xFF050B14) else TextPrimary
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Session Duration Display
      if (isGamingModeActive) {
        val hours = sessionDurationSeconds / 3600
        val minutes = (sessionDurationSeconds % 3600) / 60
        val seconds = sessionDurationSeconds % 60
        val timeFormatted = String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds)

        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(vertical = 4.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Timer,
            contentDescription = "Session Duration",
            tint = NeonCyan,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "SESSION TIME: $timeFormatted",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 1.sp,
            color = TextPrimary
          )
        }
      } else {
        Text(
          text = "Tap button to lock DND and stabilize gaming environment",
          fontSize = 12.sp,
          color = TextSecondary
        )
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Quick Status Chips Grid
      FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        // DND Chip
        val dndColor = when {
          isDndActive -> StatusActiveGreen
          hasDndPermission -> TextSecondary
          else -> StatusWarningOrange
        }
        val dndLabel = when {
          isDndActive -> "DND ON"
          hasDndPermission -> "DND OFF"
          else -> "DND PERM REQUIRED"
        }
        StatusChip(
          icon = Icons.Default.DoNotDisturb,
          label = dndLabel,
          accentColor = dndColor,
          onClick = onDndChipClicked,
          testTag = "hud_dnd_chip"
        )

        // Screen Awake Chip
        StatusChip(
          icon = Icons.Default.StayCurrentPortrait,
          label = if (keepScreenAwake) "SCREEN AWAKE" else "NORMAL SLEEP",
          accentColor = if (keepScreenAwake) NeonCyan else TextSecondary,
          onClick = onToggleScreenAwake,
          testTag = "hud_screen_awake_chip"
        )

        // Thermals Chip
        val tempColor = when {
          batteryTempCelsius > 42f -> StatusAlertRed
          batteryTempCelsius > 38f -> StatusWarningOrange
          else -> StatusActiveGreen
        }
        StatusChip(
          icon = Icons.Default.Thermostat,
          label = String.format(Locale.US, "%.1f°C", batteryTempCelsius),
          accentColor = tempColor,
          onClick = {},
          testTag = "hud_temp_chip"
        )

        // RAM Load Chip
        val ramColor = if (ramUsagePercent > 85) StatusAlertRed else NeonCyan
        StatusChip(
          icon = Icons.Default.Memory,
          label = "RAM $ramUsagePercent%",
          accentColor = ramColor,
          onClick = {},
          testTag = "hud_ram_chip"
        )
      }
    }
  }
}

@Composable
private fun StatusChip(
  icon: ImageVector,
  label: String,
  accentColor: Color,
  onClick: () -> Unit,
  testTag: String,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(10.dp))
      .background(CardDark)
      .border(BorderStroke(1.dp, CardBorder), shape = RoundedCornerShape(10.dp))
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = ripple(bounded = true, color = NeonCyan),
        onClick = onClick
      )
      .padding(horizontal = 10.dp, vertical = 7.dp)
      .testTag(testTag)
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        imageVector = icon,
        contentDescription = label,
        tint = accentColor,
        modifier = Modifier.size(15.dp)
      )
      Spacer(modifier = Modifier.width(6.dp))
      Text(
        text = label,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.5.sp,
        color = TextPrimary
      )
    }
  }
}
