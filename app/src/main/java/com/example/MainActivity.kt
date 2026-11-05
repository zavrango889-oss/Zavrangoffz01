package com.example

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.GamingScreen
import com.example.ui.GamingViewModel
import com.example.ui.theme.ZavrangoffzTheme

class MainActivity : ComponentActivity() {

  private val viewModel: GamingViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      ZavrangoffzTheme {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        // Sync keep screen on flag with window
        LaunchedEffect(uiState.isGamingModeActive, uiState.keepScreenAwake) {
          if (uiState.isGamingModeActive && uiState.keepScreenAwake) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
          } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
          }
        }

        GamingScreen(
          uiState = uiState,
          onToggleGamingMode = { viewModel.toggleGamingMode() },
          onToggleDnd = { viewModel.toggleDndManually() },
          onToggleScreenAwake = { viewModel.toggleScreenAwake() },
          onTrimMemory = { viewModel.trimAppMemory() },
          onRefresh = { viewModel.refreshStatus() },
          onDismissNotification = { viewModel.dismissNotificationMessage() },
          onDismissDndSheet = { viewModel.dismissDndPermissionSheet() }
        )
      }
    }
  }

  override fun onResume() {
    super.onResume()
    // Re-check DND permission and installed packages when user returns from Settings or Play Store
    viewModel.refreshStatus()
  }
}
