package com.example.ui

import com.example.data.DeviceTelemetry
import com.example.data.InstalledAppsStatus
import com.example.data.SystemProfile

data class GamingUiState(
  val isGamingModeActive: Boolean = false,
  val sessionDurationSeconds: Long = 0L,
  val isDndActive: Boolean = false,
  val hasDndPermission: Boolean = false,
  val keepScreenAwake: Boolean = true,
  val telemetry: DeviceTelemetry = DeviceTelemetry(),
  val installedApps: InstalledAppsStatus = InstalledAppsStatus(),
  val systemProfile: SystemProfile? = null,
  val notificationMessage: String? = null,
  val showDndPermissionSheet: Boolean = false
)
