package com.tva.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.tva.app.ui.navigation.TvaNavHost
import com.tva.app.ui.onboarding.OnboardingScreen
import com.tva.app.ui.theme.TvaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TvaTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    TvaApp()
                }
            }
        }
    }
}

@Composable
private fun TvaApp() {
    // Phase 2: in-memory flag. Phase 3 persists this in UserSettings/DataStore
    // and gates it on the real PACKAGE_USAGE_STATS grant, not just "seen onboarding".
    var onboardingComplete by remember { mutableStateOf(false) }

    if (onboardingComplete) {
        TvaNavHost()
    } else {
        OnboardingScreen(onFinished = { onboardingComplete = true })
    }
}
