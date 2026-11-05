package com.example

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.GamingMonitor
import com.example.ui.GamingViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context verifies ZAVRANGOFFZ app name`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("ZAVRANGOFFZ", appName)
  }

  @Test
  fun `gaming monitor provides valid system profile and telemetry`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val monitor = GamingMonitor(context)

    val profile = monitor.getSystemProfile()
    assertNotNull(profile)
    assertNotNull(profile.displayBrand)

    val telemetry = monitor.getTelemetrySnapshot()
    assertNotNull(telemetry)
    assertTrue("Total RAM should be >= 0", telemetry.totalRamBytes >= 0L)
    assertTrue("RAM usage percent should be in range 0-100", telemetry.ramUsagePercent in 0..100)
    assertTrue("Battery temperature should be a realistic float", telemetry.batteryTempCelsius > 0f)
  }

  @Test
  fun `gaming viewmodel initializes and toggles gaming mode correctly`() {
    val application = ApplicationProvider.getApplicationContext<Application>()
    val viewModel = GamingViewModel(application)

    assertFalse("Gaming mode should start inactive", viewModel.uiState.value.isGamingModeActive)

    viewModel.toggleGamingMode()
    assertTrue("Gaming mode should be active after toggle", viewModel.uiState.value.isGamingModeActive)

    viewModel.toggleScreenAwake()
    assertFalse("Screen awake should toggle to false", viewModel.uiState.value.keepScreenAwake)

    viewModel.trimAppMemory()
    assertNotNull(viewModel.uiState.value.notificationMessage)

    viewModel.toggleGamingMode()
    assertFalse("Gaming mode should return to standby", viewModel.uiState.value.isGamingModeActive)
  }
}
