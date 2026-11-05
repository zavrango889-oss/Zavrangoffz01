package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.DeviceSpecCard
import com.example.ui.components.DndPermissionCard
import com.example.ui.components.GameLauncherCard
import com.example.ui.components.GamingHeader
import com.example.ui.components.GamingModeHUD
import com.example.ui.components.HardwareTelemetryCard
import com.example.ui.theme.CardDark
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.PitchDark
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamingScreen(
  uiState: GamingUiState,
  onToggleGamingMode: () -> Unit,
  onToggleDnd: () -> Unit,
  onToggleScreenAwake: () -> Unit,
  onTrimMemory: () -> Unit,
  onRefresh: () -> Unit,
  onDismissNotification: () -> Unit,
  onDismissDndSheet: () -> Unit,
  modifier: Modifier = Modifier
) {
  val snackbarHostState = remember { SnackbarHostState() }

  LaunchedEffect(uiState.notificationMessage) {
    uiState.notificationMessage?.let { msg ->
      snackbarHostState.showSnackbar(
        message = msg,
        duration = SnackbarDuration.Short
      )
      onDismissNotification()
    }
  }

  Scaffold(
    modifier = modifier
      .fillMaxSize()
      .background(ObsidianBlack),
    containerColor = ObsidianBlack,
    snackbarHost = {
      SnackbarHost(
        hostState = snackbarHostState,
        modifier = Modifier
          .padding(16.dp)
          .windowInsetsPadding(WindowInsets.navigationBars)
      ) { data ->
        Surface(
          color = CardDark,
          shape = RoundedCornerShape(12.dp),
          shadowElevation = 8.dp,
          modifier = Modifier.clip(RoundedCornerShape(12.dp))
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Info,
              contentDescription = null,
              tint = NeonCyan,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = data.visuals.message,
              fontSize = 12.sp,
              color = TextPrimary,
              fontWeight = FontWeight.Medium
            )
          }
        }
      }
    }
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .windowInsetsPadding(WindowInsets.statusBars),
      contentAlignment = Alignment.TopCenter
    ) {
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .widthIn(max = 640.dp)
          .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 48.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        // App Header with white bear mascot
        item {
          GamingHeader(
            isGamingModeActive = uiState.isGamingModeActive,
            onRefresh = onRefresh
          )
        }

        // Master Gaming Mode HUD
        item {
          GamingModeHUD(
            isGamingModeActive = uiState.isGamingModeActive,
            sessionDurationSeconds = uiState.sessionDurationSeconds,
            isDndActive = uiState.isDndActive,
            hasDndPermission = uiState.hasDndPermission,
            keepScreenAwake = uiState.keepScreenAwake,
            batteryTempCelsius = uiState.telemetry.batteryTempCelsius,
            ramUsagePercent = uiState.telemetry.ramUsagePercent,
            onToggleGamingMode = onToggleGamingMode,
            onToggleScreenAwake = onToggleScreenAwake,
            onDndChipClicked = onToggleDnd
          )
        }

        // Hardware Telemetry: RAM & Thermals
        item {
          HardwareTelemetryCard(
            telemetry = uiState.telemetry,
            onTrimMemory = onTrimMemory
          )
        }

        // Game Launchers: Free Fire MAX & Samsung Game Booster
        item {
          GameLauncherCard(
            installedApps = uiState.installedApps,
            onNotify = { /* Handled via ViewModel or snackbar */ }
          )
        }

        // Do Not Disturb Setup & Control
        item {
          DndPermissionCard(
            hasDndPermission = uiState.hasDndPermission,
            isDndActive = uiState.isDndActive,
            onToggleDnd = onToggleDnd
          )
        }

        // Device Architecture & Fair Play Disclaimers
        item {
          DeviceSpecCard(
            profile = uiState.systemProfile
          )
        }
      }
    }
  }

  // DND Permission Sheet if user clicks while ungranted
  if (uiState.showDndPermissionSheet) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
      onDismissRequest = onDismissDndSheet,
      sheetState = sheetState,
      containerColor = PitchDark
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(24.dp)
          .windowInsetsPadding(WindowInsets.navigationBars)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "DND Permission Setup",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = NeonCyan
          )
          IconButton(onClick = onDismissDndSheet) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close",
              tint = TextSecondary
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        DndPermissionCard(
          hasDndPermission = uiState.hasDndPermission,
          isDndActive = uiState.isDndActive,
          onToggleDnd = onToggleDnd
        )

        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}
