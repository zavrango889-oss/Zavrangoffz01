package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DoNotDisturbOn
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardDark
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.PitchDark
import com.example.ui.theme.StatusActiveGreen
import com.example.ui.theme.StatusWarningOrange
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun DndPermissionCard(
  hasDndPermission: Boolean,
  isDndActive: Boolean,
  onToggleDnd: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

  Surface(
    modifier = modifier.fillMaxWidth(),
    color = PitchDark,
    border = BorderStroke(1.dp, if (!hasDndPermission) StatusWarningOrange.copy(alpha = 0.6f) else CardBorder),
    shape = RoundedCornerShape(20.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(18.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.DoNotDisturbOn,
            contentDescription = "Do Not Disturb",
            tint = if (isDndActive) NeonCyan else TextSecondary,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = "DO NOT DISTURB (DND)",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            color = TextPrimary
          )
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (hasDndPermission) Color(0xFF003820) else Color(0xFF3E2700))
            .border(
              BorderStroke(0.5.dp, if (hasDndPermission) StatusActiveGreen else StatusWarningOrange),
              RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Text(
            text = if (hasDndPermission) "AUTHORIZED" else "SETUP NEEDED",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = if (hasDndPermission) StatusActiveGreen else StatusWarningOrange
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      if (hasDndPermission) {
        // Controls when permission is already granted
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(CardDark)
            .padding(14.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = if (isDndActive) "DND Silence Active" else "DND Silence Inactive",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = if (isDndActive) NeonCyan else TextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = if (isDndActive)
                "Incoming alerts and calls muted to prevent FPS stutters"
              else
                "Automatically engages when Gaming Mode is switched ON",
              fontSize = 11.sp,
              color = TextSecondary
            )
          }

          Spacer(modifier = Modifier.width(8.dp))

          Switch(
            checked = isDndActive,
            onCheckedChange = { onToggleDnd() },
            colors = SwitchDefaults.colors(
              checkedThumbColor = Color(0xFF050B14),
              checkedTrackColor = NeonCyan,
              uncheckedThumbColor = TextSecondary,
              uncheckedTrackColor = Color(0xFF1B2738)
            ),
            modifier = Modifier.testTag("dnd_manual_switch")
          )
        }
      } else {
        // Guide & Grant Action when permission is needed
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(CardDark)
            .padding(14.dp)
        ) {
          Text(
            text = "Notification Policy Access Needed",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = StatusWarningOrange
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "To suppress calls, banner notifications, and vibration interrupts during intense Free Fire MAX matches, grant Android DND access in system settings.",
            fontSize = 11.sp,
            color = TextSecondary,
            lineHeight = 16.sp
          )

          Spacer(modifier = Modifier.height(14.dp))

          Button(
            onClick = {
              openDndSettings(context)
            },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("grant_dnd_permission_button"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = StatusWarningOrange,
              contentColor = Color.Black
            )
          ) {
            Icon(
              imageVector = Icons.Default.Security,
              contentDescription = null,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "GRANT DND PERMISSION",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 0.5.sp
            )
          }
        }
      }
    }
  }
}

private fun openDndSettings(context: Context) {
  val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS).apply {
    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
  }
  try {
    context.startActivity(intent)
  } catch (_: Exception) {
    val fallback = Intent(Settings.ACTION_SETTINGS).apply {
      addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(fallback)
  }
}
