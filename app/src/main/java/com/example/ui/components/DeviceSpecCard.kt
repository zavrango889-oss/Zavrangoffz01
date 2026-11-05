package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SystemProfile
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardDark
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.PitchDark
import com.example.ui.theme.StatusActiveGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun DeviceSpecCard(
  profile: SystemProfile?,
  modifier: Modifier = Modifier
) {
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
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.Devices,
          contentDescription = "Target Profile",
          tint = NeonCyan,
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
          text = "DEVICE ARCHITECTURE",
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp,
          color = TextPrimary
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      Column(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(14.dp))
          .background(CardDark)
          .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        SpecRow(label = "Optimized Device", value = "Samsung Galaxy M15 5G")
        SpecRow(label = "Hardware Memory", value = "6 GB LPDDR4X")
        SpecRow(label = "Platform OS", value = "Android 16 • One UI 8.5")
        SpecRow(label = "Detected Host", value = profile?.displayBrand ?: "Android Terminal")
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Safety & Anti-Cheat Guarantee
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(14.dp))
          .background(Color(0xFF07121C))
          .padding(12.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Security,
            contentDescription = null,
            tint = StatusActiveGreen,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "SAFE PLAY COMPLIANT",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = StatusActiveGreen
          )
        }

        Spacer(modifier = Modifier.height(6.dp))

        ComplianceBullet("No root access required")
        ComplianceBullet("Zero memory injection or game file tampering")
        ComplianceBullet("No fake FPS boosters or misleading claims")
        ComplianceBullet("No ads, accounts, or telemetry tracking")
      }
    }
  }
}

@Composable
private fun SpecRow(label: String, value: String) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = label,
      fontSize = 12.sp,
      color = TextSecondary
    )
    Text(
      text = value,
      fontSize = 12.sp,
      fontWeight = FontWeight.SemiBold,
      fontFamily = FontFamily.Monospace,
      color = TextPrimary
    )
  }
}

@Composable
private fun ComplianceBullet(text: String) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier.padding(vertical = 2.dp)
  ) {
    Icon(
      imageVector = Icons.Default.Check,
      contentDescription = null,
      tint = NeonCyan,
      modifier = Modifier.size(13.dp)
    )
    Spacer(modifier = Modifier.width(6.dp))
    Text(
      text = text,
      fontSize = 10.sp,
      color = TextSecondary
    )
  }
}
