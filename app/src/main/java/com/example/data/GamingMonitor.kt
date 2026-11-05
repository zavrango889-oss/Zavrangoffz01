package com.example.data

import android.app.ActivityManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.BatteryManager
import android.os.Build
import android.os.HardwarePropertiesManager
import android.os.SystemClock
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.Locale

data class DeviceTelemetry(
  val totalRamBytes: Long = 0L,
  val availRamBytes: Long = 0L,
  val usedRamBytes: Long = 0L,
  val ramUsagePercent: Int = 0,
  val isLowMemory: Boolean = false,
  val batteryTempCelsius: Float = 0f,
  val batteryLevelPercent: Int = 0,
  val isCharging: Boolean = false,
  val cpuTempCelsius: Float? = null,
  val timestamp: Long = System.currentTimeMillis()
)

data class InstalledAppsStatus(
  val isFreeFireMaxInstalled: Boolean = false,
  val isFreeFireStandardInstalled: Boolean = false,
  val isSamsungGameBoosterInstalled: Boolean = false,
  val isSamsungGamingHubInstalled: Boolean = false,
  val samsungGamePackage: String? = null
)

data class SystemProfile(
  val model: String,
  val manufacturer: String,
  val androidVersion: String,
  val sdkInt: Int,
  val isSamsungDevice: Boolean,
  val displayBrand: String
)

class GamingMonitor(private val context: Context) {

  private val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
  private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
  private val packageManager = context.packageManager

  companion object {
    const val PKG_FREE_FIRE_MAX = "com.dts.freefiremax"
    const val PKG_FREE_FIRE_STANDARD = "com.dts.freefireth"
    const val PKG_SAMSUNG_GAME_BOOSTER = "com.samsung.android.game.gametools"
    const val PKG_SAMSUNG_GAMING_HUB = "com.samsung.android.game.gamehome"
    const val PKG_SAMSUNG_GOS = "com.samsung.android.game.gos"
  }

  fun getSystemProfile(): SystemProfile {
    val manufacturer = Build.MANUFACTURER.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
    val model = Build.MODEL
    val isSamsung = manufacturer.equals("Samsung", ignoreCase = true) || Build.BRAND.equals("samsung", ignoreCase = true)
    val displayBrand = if (isSamsung) {
      if (model.contains("M15", ignoreCase = true)) "Samsung Galaxy M15 5G" else "Samsung $model"
    } else {
      "$manufacturer $model"
    }

    return SystemProfile(
      model = model,
      manufacturer = manufacturer,
      androidVersion = "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})",
      sdkInt = Build.VERSION.SDK_INT,
      isSamsungDevice = isSamsung,
      displayBrand = displayBrand
    )
  }

  fun hasDndPermission(): Boolean {
    return notificationManager.isNotificationPolicyAccessGranted
  }

  fun setDndMode(enabled: Boolean): Boolean {
    if (!hasDndPermission()) return false
    return try {
      if (enabled) {
        notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_PRIORITY)
      } else {
        notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
      }
      true
    } catch (_: Exception) {
      false
    }
  }

  fun checkInstalledApps(): InstalledAppsStatus {
    val isFfMax = isPackageInstalled(PKG_FREE_FIRE_MAX)
    val isFfStd = isPackageInstalled(PKG_FREE_FIRE_STANDARD)
    val isGameTools = isPackageInstalled(PKG_SAMSUNG_GAME_BOOSTER)
    val isGamingHub = isPackageInstalled(PKG_SAMSUNG_GAMING_HUB)
    val isGos = isPackageInstalled(PKG_SAMSUNG_GOS)

    val activeSamsungPkg = when {
      isGameTools -> PKG_SAMSUNG_GAME_BOOSTER
      isGamingHub -> PKG_SAMSUNG_GAMING_HUB
      isGos -> PKG_SAMSUNG_GOS
      else -> null
    }

    return InstalledAppsStatus(
      isFreeFireMaxInstalled = isFfMax,
      isFreeFireStandardInstalled = isFfStd,
      isSamsungGameBoosterInstalled = isGameTools || isGos,
      isSamsungGamingHubInstalled = isGamingHub,
      samsungGamePackage = activeSamsungPkg
    )
  }

  fun readMemoryTelemetry(): Pair<Long, Long> {
    val memoryInfo = ActivityManager.MemoryInfo()
    activityManager.getMemoryInfo(memoryInfo)
    return Pair(memoryInfo.totalMem, memoryInfo.availMem)
  }

  fun getTelemetrySnapshot(): DeviceTelemetry {
    val memoryInfo = ActivityManager.MemoryInfo()
    activityManager.getMemoryInfo(memoryInfo)

    val totalRam = memoryInfo.totalMem
    val availRam = memoryInfo.availMem
    val usedRam = (totalRam - availRam).coerceAtLeast(0L)
    val usagePercent = if (totalRam > 0) ((usedRam.toDouble() / totalRam.toDouble()) * 100).toInt() else 0

    // Read Battery Temp
    val batteryStatus: Intent? = context.registerReceiver(
      null,
      IntentFilter(Intent.ACTION_BATTERY_CHANGED)
    )
    val rawTemp = batteryStatus?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) ?: 0
    val batteryTemp = if (rawTemp > 0) rawTemp / 10.0f else 32.5f

    val rawLevel = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
    val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
    val batteryLevel = if (rawLevel >= 0 && scale > 0) ((rawLevel / scale.toFloat()) * 100).toInt() else 85

    val status = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
    val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL

    // Hardware CPU Temp where supported
    var cpuTemp: Float? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      try {
        val hwManager = context.getSystemService(Context.HARDWARE_PROPERTIES_SERVICE) as? HardwarePropertiesManager
        val temps = hwManager?.getDeviceTemperatures(
          HardwarePropertiesManager.DEVICE_TEMPERATURE_CPU,
          HardwarePropertiesManager.TEMPERATURE_CURRENT
        )
        if (temps != null && temps.isNotEmpty() && temps[0] > 0) {
          cpuTemp = temps[0]
        }
      } catch (_: Exception) {
        // Ignored, not all platforms expose hardware properties manager to third party apps
      }
    }

    return DeviceTelemetry(
      totalRamBytes = totalRam,
      availRamBytes = availRam,
      usedRamBytes = usedRam,
      ramUsagePercent = usagePercent,
      isLowMemory = memoryInfo.lowMemory,
      batteryTempCelsius = batteryTemp,
      batteryLevelPercent = batteryLevel,
      isCharging = isCharging,
      cpuTempCelsius = cpuTemp,
      timestamp = SystemClock.elapsedRealtime()
    )
  }

  fun observeTelemetry(intervalMs: Long = 2000L): Flow<DeviceTelemetry> = callbackFlow {
    var isRunning = true
    val runnable = object : Runnable {
      override fun run() {
        if (!isRunning) return
        trySend(getTelemetrySnapshot())
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(this, intervalMs)
      }
    }
    android.os.Handler(android.os.Looper.getMainLooper()).post(runnable)

    awaitClose {
      isRunning = false
    }
  }

  private fun isPackageInstalled(packageName: String): Boolean {
    return try {
      packageManager.getPackageInfo(packageName, 0)
      true
    } catch (_: PackageManager.NameNotFoundException) {
      false
    }
  }
}
