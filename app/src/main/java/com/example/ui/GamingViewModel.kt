package com.example.ui

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.GamingMonitor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class GamingViewModel(application: Application) : AndroidViewModel(application) {

  private val monitor = GamingMonitor(application)
  private val _uiState = MutableStateFlow(GamingUiState())
  val uiState: StateFlow<GamingUiState> = _uiState.asStateFlow()

  private var timerJob: Job? = null
  private var telemetryJob: Job? = null

  init {
    val profile = monitor.getSystemProfile()
    val apps = monitor.checkInstalledApps()
    val hasDnd = monitor.hasDndPermission()
    val initialTelemetry = monitor.getTelemetrySnapshot()

    _uiState.update {
      it.copy(
        systemProfile = profile,
        installedApps = apps,
        hasDndPermission = hasDnd,
        telemetry = initialTelemetry
      )
    }

    startTelemetryPolling()
  }

  fun refreshStatus() {
    val apps = monitor.checkInstalledApps()
    val hasDnd = monitor.hasDndPermission()
    val telemetry = monitor.getTelemetrySnapshot()

    _uiState.update {
      it.copy(
        installedApps = apps,
        hasDndPermission = hasDnd,
        telemetry = telemetry
      )
    }
  }

  private fun startTelemetryPolling() {
    telemetryJob?.cancel()
    telemetryJob = viewModelScope.launch {
      while (isActive) {
        val snapshot = monitor.getTelemetrySnapshot()
        _uiState.update { it.copy(telemetry = snapshot) }
        delay(2000L)
      }
    }
  }

  fun toggleGamingMode() {
    val currentlyActive = _uiState.value.isGamingModeActive
    val nextState = !currentlyActive
    vibrateFeedback(if (nextState) 80L else 40L)

    if (nextState) {
      // Activating Gaming Mode
      val hasDnd = monitor.hasDndPermission()
      var dndApplied = false
      if (hasDnd) {
        dndApplied = monitor.setDndMode(true)
      }

      startSessionTimer()

      val message = if (hasDnd && dndApplied) {
        "Gaming Mode ON • Do Not Disturb & Screen Wake Active"
      } else if (!hasDnd) {
        "Gaming Mode ON • Grant DND access to block popups"
      } else {
        "Gaming Mode ON • Screen Lock Engaged"
      }

      _uiState.update {
        it.copy(
          isGamingModeActive = true,
          isDndActive = dndApplied,
          hasDndPermission = hasDnd,
          notificationMessage = message,
          showDndPermissionSheet = !hasDnd
        )
      }
    } else {
      // Deactivating Gaming Mode
      stopSessionTimer()
      if (_uiState.value.isDndActive && monitor.hasDndPermission()) {
        monitor.setDndMode(false)
      }

      _uiState.update {
        it.copy(
          isGamingModeActive = false,
          isDndActive = false,
          sessionDurationSeconds = 0L,
          notificationMessage = "Gaming Mode Standby • Sound Restored"
        )
      }
    }
  }

  fun toggleDndManually() {
    val hasDnd = monitor.hasDndPermission()
    if (!hasDnd) {
      _uiState.update { it.copy(showDndPermissionSheet = true) }
      return
    }

    val currentDnd = _uiState.value.isDndActive
    val nextDnd = !currentDnd
    vibrateFeedback(50L)

    val success = monitor.setDndMode(nextDnd)
    if (success) {
      _uiState.update {
        it.copy(
          isDndActive = nextDnd,
          notificationMessage = if (nextDnd) "Do Not Disturb Activated" else "Normal Sound Mode Restored"
        )
      }
    }
  }

  fun toggleScreenAwake() {
    vibrateFeedback(30L)
    _uiState.update {
      val next = !it.keepScreenAwake
      it.copy(
        keepScreenAwake = next,
        notificationMessage = if (next) "Screen Keep-Awake ON" else "Screen Keep-Awake OFF"
      )
    }
  }

  fun trimAppMemory() {
    vibrateFeedback(60L)
    System.gc()
    val updated = monitor.getTelemetrySnapshot()
    _uiState.update {
      it.copy(
        telemetry = updated,
        notificationMessage = "Local buffers trimmed • JVM garbage collection requested"
      )
    }
  }

  fun dismissNotificationMessage() {
    _uiState.update { it.copy(notificationMessage = null) }
  }

  fun dismissDndPermissionSheet() {
    _uiState.update { it.copy(showDndPermissionSheet = false) }
  }

  private fun startSessionTimer() {
    timerJob?.cancel()
    timerJob = viewModelScope.launch {
      var seconds = 0L
      while (isActive) {
        delay(1000L)
        seconds++
        _uiState.update { it.copy(sessionDurationSeconds = seconds) }
      }
    }
  }

  private fun stopSessionTimer() {
    timerJob?.cancel()
    timerJob = null
  }

  private fun vibrateFeedback(milliseconds: Long) {
    try {
      val context = getApplication<Application>()
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        val vibrator = vibratorManager?.defaultVibrator
        vibrator?.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE))
      } else {
        @Suppress("DEPRECATION")
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          vibrator?.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
          @Suppress("DEPRECATION")
          vibrator?.vibrate(milliseconds)
        }
      }
    } catch (_: Exception) {
      // Vibration not available or permission issue
    }
  }

  override fun onCleared() {
    super.onCleared()
    timerJob?.cancel()
    telemetryJob?.cancel()
  }
}
