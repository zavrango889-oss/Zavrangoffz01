package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.GamingMonitor
import com.example.data.InstalledAppsStatus
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardDark
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.GamerAzure
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.PitchDark
import com.example.ui.theme.StatusActiveGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun GameLauncherCard(
  installedApps: InstalledAppsStatus,
  onNotify: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

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
          imageVector = Icons.Default.SportsEsports,
          contentDescription = "Game Launchers",
          tint = NeonCyan,
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
          text = "GAME LAUNCHERS & TOOLS",
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp,
          color = TextPrimary
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Free Fire MAX Launcher Block
      val isFfInstalled = installedApps.isFreeFireMaxInstalled || installedApps.isFreeFireStandardInstalled
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(14.dp))
          .background(CardDark)
          .border(
            BorderStroke(1.dp, if (isFfInstalled) Color(0xFF1E3A5F) else CardBorder),
            shape = RoundedCornerShape(14.dp)
          )
          .padding(14.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Brush.linearGradient(listOf(Color(0xFFE65100), Color(0xFFFF9100)))),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.RocketLaunch,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(22.dp)
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = "Free Fire MAX",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
              )
              Text(
                text = if (isFfInstalled) "Ready to Launch" else "Not detected on device",
                fontSize = 11.sp,
                color = if (isFfInstalled) StatusActiveGreen else TextSecondary
              )
            }
          }

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(if (isFfInstalled) Color(0xFF003820) else Color(0xFF263238))
              .border(
                BorderStroke(0.5.dp, if (isFfInstalled) StatusActiveGreen else Color(0xFF546E7A)),
                RoundedCornerShape(6.dp)
              )
              .padding(horizontal = 8.dp, vertical = 3.dp)
          ) {
            Text(
              text = if (isFfInstalled) "INSTALLED" else "STORE",
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = if (isFfInstalled) StatusActiveGreen else TextSecondary
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (isFfInstalled) {
          Button(
            onClick = {
              launchFreeFire(context, onNotify)
            },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("launch_free_fire_button"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = NeonCyan,
              contentColor = Color(0xFF050B14)
            )
          ) {
            Icon(
              imageVector = Icons.Default.Launch,
              contentDescription = null,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "LAUNCH FREE FIRE MAX",
              fontSize = 12.sp,
              fontWeight = FontWeight.ExtraBold,
              letterSpacing = 0.5.sp
            )
          }
        } else {
          OutlinedButton(
            onClick = {
              openPlayStoreForFreeFire(context)
            },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("get_free_fire_button"),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, NeonCyan),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonCyan)
          ) {
            Icon(
              imageVector = Icons.Default.OpenInNew,
              contentDescription = null,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "GET FREE FIRE MAX (PLAY STORE)",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Samsung Game Booster Block
      val isSamsungBoosterDetected = installedApps.isSamsungGameBoosterInstalled || installedApps.isSamsungGamingHubInstalled
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(14.dp))
          .background(CardDark)
          .border(
            BorderStroke(1.dp, if (isSamsungBoosterDetected) Color(0xFF1E3A5F) else CardBorder),
            shape = RoundedCornerShape(14.dp)
          )
          .padding(14.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Brush.linearGradient(listOf(Color(0xFF0D47A1), ElectricBlue))),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Gamepad,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(22.dp)
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = "Samsung Game Booster",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
              )
              Text(
                text = if (isSamsungBoosterDetected) "One UI Gaming Suite Ready" else "Samsung Gaming Hub / Settings",
                fontSize = 11.sp,
                color = TextSecondary
              )
            }
          }

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(Color(0xFF002244))
              .border(BorderStroke(0.5.dp, ElectricBlue), RoundedCornerShape(6.dp))
              .padding(horizontal = 8.dp, vertical = 3.dp)
          ) {
            Text(
              text = "ONE UI",
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = ElectricBlue
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
          onClick = {
            launchSamsungGameBooster(context, installedApps.samsungGamePackage, onNotify)
          },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("launch_game_booster_button"),
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF122338),
            contentColor = NeonCyan
          ),
          border = BorderStroke(1.dp, Color(0xFF1E3A5F))
        ) {
          Icon(
            imageVector = Icons.Default.Launch,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "OPEN SAMSUNG GAME BOOSTER",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
          )
        }
      }
    }
  }
}

private fun launchFreeFire(context: Context, onNotify: (String) -> Unit) {
  val pm = context.packageManager
  val intent = pm.getLaunchIntentForPackage(GamingMonitor.PKG_FREE_FIRE_MAX)
    ?: pm.getLaunchIntentForPackage(GamingMonitor.PKG_FREE_FIRE_STANDARD)

  if (intent != null) {
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    try {
      context.startActivity(intent)
      onNotify("Launching Free Fire MAX...")
    } catch (e: Exception) {
      onNotify("Error launching Free Fire: ${e.localizedMessage}")
    }
  } else {
    openPlayStoreForFreeFire(context)
  }
}

private fun openPlayStoreForFreeFire(context: Context) {
  val pkg = GamingMonitor.PKG_FREE_FIRE_MAX
  val marketIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$pkg")).apply {
    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
  }
  try {
    context.startActivity(marketIntent)
  } catch (_: Exception) {
    val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$pkg")).apply {
      addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(webIntent)
  }
}

private fun launchSamsungGameBooster(context: Context, detectedPkg: String?, onNotify: (String) -> Unit) {
  val pm = context.packageManager

  // 1. Try launching detected Samsung package
  if (detectedPkg != null) {
    val intent = pm.getLaunchIntentForPackage(detectedPkg)
    if (intent != null) {
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      try {
        context.startActivity(intent)
        onNotify("Opening Samsung Gaming Suite...")
        return
      } catch (_: Exception) {}
    }
  }

  // 2. Try common Samsung game packages
  val candidates = listOf(
    GamingMonitor.PKG_SAMSUNG_GAME_BOOSTER,
    GamingMonitor.PKG_SAMSUNG_GAMING_HUB,
    GamingMonitor.PKG_SAMSUNG_GOS
  )
  for (pkg in candidates) {
    val intent = pm.getLaunchIntentForPackage(pkg)
    if (intent != null) {
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      try {
        context.startActivity(intent)
        onNotify("Opening Samsung Game Hub...")
        return
      } catch (_: Exception) {}
    }
  }

  // 3. Try Samsung Game Tools Action Intent or Android Game Settings
  try {
    val samsungIntent = Intent("com.samsung.android.game.gametools.action.GAME_TOOLS_SETTINGS").apply {
      addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(samsungIntent)
    onNotify("Opening Game Booster Settings...")
    return
  } catch (_: Exception) {}

  // 4. Try Android 12+ Game Dashboard Settings
  try {
    val gameSettingsIntent = Intent("android.settings.GAME_SETTINGS").apply {
      addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(gameSettingsIntent)
    onNotify("Opening Device Gaming Dashboard...")
    return
  } catch (_: Exception) {}

  // 5. Fallback to general system settings
  try {
    val settingsIntent = Intent(Settings.ACTION_SETTINGS).apply {
      addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(settingsIntent)
    onNotify("Opening Settings...")
  } catch (e: Exception) {
    onNotify("Could not open settings: ${e.localizedMessage}")
  }
}
