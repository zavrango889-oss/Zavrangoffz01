package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.CardBorderActive
import com.example.ui.theme.CardDark
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonCyanLight
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.PitchDark
import com.example.ui.theme.StatusActiveGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun GamingHeader(
  isGamingModeActive: Boolean,
  onRefresh: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier.fillMaxWidth(),
    color = PitchDark,
    border = BorderStroke(1.dp, Brush.horizontalGradient(listOf(Color(0xFF003B5C), NeonCyan, Color(0xFF003B5C)))),
    shape = RoundedCornerShape(16.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 14.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        // White Bear Mascot Logo Badge
        Box(
          modifier = Modifier
            .size(54.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(CardDark)
            .border(
              BorderStroke(
                width = if (isGamingModeActive) 2.dp else 1.dp,
                color = if (isGamingModeActive) NeonCyan else Color(0xFF1E3A5F)
              ),
              shape = RoundedCornerShape(14.dp)
            ),
          contentAlignment = Alignment.Center
        ) {
          Image(
            painter = painterResource(id = R.drawable.ic_white_bear_logo),
            contentDescription = "ZAVRANGOFFZ White Bear Mascot",
            modifier = Modifier
              .size(50.dp)
              .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
          )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column {
          Row(
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "ZAVRANGOFFZ",
              fontSize = 20.sp,
              fontWeight = FontWeight.ExtraBold,
              letterSpacing = 2.sp,
              color = NeonCyanLight,
              fontFamily = FontFamily.SansSerif
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
              modifier = Modifier
                .clip(CircleShape)
                .background(if (isGamingModeActive) StatusActiveGreen else Color(0xFF546E7A))
                .size(8.dp)
            )
          }

          Spacer(modifier = Modifier.height(2.dp))

          Text(
            text = "Galaxy M15 5G • One UI 8.5 • Android 16",
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = TextSecondary,
            letterSpacing = 0.5.sp
          )
        }
      }

      IconButton(
        onClick = onRefresh,
        modifier = Modifier.testTag("refresh_status_button")
      ) {
        Icon(
          imageVector = Icons.Default.Refresh,
          contentDescription = "Refresh telemetry",
          tint = NeonCyan
        )
      }
    }
  }
}
