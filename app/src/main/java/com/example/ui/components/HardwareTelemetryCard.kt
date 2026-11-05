package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.BatteryStd
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DeviceTelemetry
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardDark
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.PitchDark
import com.example.ui.theme.StatusActiveGreen
import com.example.ui.theme.StatusAlertRed
import com.example.ui.theme.StatusWarningOrange
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

@Composable
fun HardwareTelemetryCard(
  telemetry: DeviceTelemetry,
  onTrimMemory: () -> Unit,
  modifier: Modifier = Modifier
) {
  val totalGb = telemetry.totalRamBytes / (1024.0 * 1024.0 * 1024.0)
  val usedGb = telemetry.usedRamBytes / (1024.0 * 1024.0 * 1024.0)
  val availGb = telemetry.availRamBytes / (1024.0 * 1024.0 * 1024.0)

  val animatedProgress by animateFloatAsState(
    targetValue = (telemetry.ramUsagePercent / 100f).coerceIn(0f, 1f),
    animationSpec = tween(600),
    label = "ramProgress"
  )

  Surface(
    modifier = modifier.fillMaxWidth(),
    color = PitchDark,
    border = BorderStroke(1.dp, CardBorder),
    shape = RoundedCornerShape(20.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(18.dp)
    ) {
      // Header
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Memory,
            contentDescription = "RAM & Hardware",
            tint = NeonCyan,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = "HARDWARE TELEMETRY",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            color = TextPrimary
          )
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(CardDark)
            .border(BorderStroke(0.5.dp, Color(0xFF1E3A5F)), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Text(
            text = "REAL-TIME",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = NeonCyan
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // RAM Section
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(14.dp))
          .background(CardDark)
          .padding(14.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "SYSTEM RAM",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextSecondary
          )
          Text(
            text = String.format(Locale.US, "%.2f GB / %.2f GB", usedGb, totalGb),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = TextPrimary
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // RAM Usage Bar
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(10.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(Color(0xFF070B11))
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth(animatedProgress)
              .fillMaxHeight()
              .clip(RoundedCornerShape(5.dp))
              .background(
                Brush.horizontalGradient(
                  colors = if (telemetry.ramUsagePercent > 85) {
                    listOf(StatusWarningOrange, StatusAlertRed)
                  } else {
                    listOf(ElectricBlue, NeonCyan)
                  }
                )
              )
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = "Available: ${String.format(Locale.US, "%.2f GB", availGb)}",
            fontSize = 11.sp,
            color = TextSecondary
          )
          Text(
            text = "Load: ${telemetry.ramUsagePercent}%",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (telemetry.ramUsagePercent > 85) StatusAlertRed else NeonCyan
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Thermals & Battery Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        // Temperature Block
        val tempCelsius = telemetry.batteryTempCelsius
        val tempFahrenheit = (tempCelsius * 9 / 5) + 32
        val tempStatus = when {
          tempCelsius > 42f -> "Thermal Alert"
          tempCelsius > 38f -> "Warm"
          else -> "Optimal Cool"
        }
        val tempStatusColor = when {
          tempCelsius > 42f -> StatusAlertRed
          tempCelsius > 38f -> StatusWarningOrange
          else -> StatusActiveGreen
        }

        Column(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(14.dp))
            .background(CardDark)
            .padding(12.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Thermostat,
              contentDescription = "Temperature",
              tint = tempStatusColor,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "DEVICE TEMP",
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold,
              color = TextSecondary
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = String.format(Locale.US, "%.1f°C", tempCelsius),
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = FontFamily.Monospace,
            color = TextPrimary
          )

          Spacer(modifier = Modifier.height(2.dp))

          Text(
            text = "${String.format(Locale.US, "%.1f°F", tempFahrenheit)} • $tempStatus",
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = tempStatusColor
          )
        }

        // Battery Block
        Column(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(14.dp))
            .background(CardDark)
            .padding(12.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = if (telemetry.isCharging) Icons.Default.BatteryChargingFull else Icons.Default.BatteryStd,
              contentDescription = "Battery",
              tint = if (telemetry.isCharging) StatusActiveGreen else NeonCyan,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "BATTERY",
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold,
              color = TextSecondary
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "${telemetry.batteryLevelPercent}%",
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = FontFamily.Monospace,
            color = TextPrimary
          )

          Spacer(modifier = Modifier.height(2.dp))

          Text(
            text = if (telemetry.isCharging) "Charging" else "Discharging",
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = if (telemetry.isCharging) StatusActiveGreen else TextSecondary
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Trim Cache Button (Transparent, honest Android behavior)
      OutlinedButton(
        onClick = onTrimMemory,
        modifier = Modifier
          .fillMaxWidth()
          .testTag("trim_cache_button"),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Color(0xFF1E3A5F)),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonCyan)
      ) {
        Icon(
          imageVector = Icons.Default.CleaningServices,
          contentDescription = null,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Trim Memory Buffers (GC)",
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold
        )
      }

      Spacer(modifier = Modifier.height(6.dp))

      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 4.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Info,
          contentDescription = null,
          tint = TextSecondary,
          modifier = Modifier.size(12.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "Authentic OS metrics. No fake overclocking or simulated boosters.",
          fontSize = 10.sp,
          color = TextSecondary
        )
      }
    }
  }
}
