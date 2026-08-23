package com.tva.app.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private data class OnboardingPage(val eyebrow: String, val title: String, val body: String)

private val pages = listOf(
    OnboardingPage("01", "Where does your time go?", "TVA helps you understand how much time you spend scrolling."),
    OnboardingPage("02", "See the numbers", "Track your daily and weekly usage."),
    OnboardingPage("03", "Build awareness", "Set goals and watch your habits change.")
)

@Composable
fun OnboardingScreen(onFinished: () -> Unit) {
    var pageIndex by remember { mutableStateOf(0) }
    val isPermissionScreen = pageIndex == pages.size

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(Modifier.height(48.dp))

        if (!isPermissionScreen) {
            val page = pages[pageIndex]
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
                Text(page.eyebrow, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(16.dp))
                Text(page.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(12.dp))
                Text(page.body, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
                Text("04", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(16.dp))
                Text("Give TVA usage access", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(12.dp))
                Text(
                    "TVA needs usage access to measure how much time you spend in supported apps. " +
                        "TVA does not need access to your messages, passwords, photos, or private conversations.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Column {
            Row(
                Modifier.fillMaxWidth().padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pages.size + 1) { i ->
                    val active = i == pageIndex
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (active) 10.dp else 8.dp)
                            .background(
                                if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                shape = androidx.compose.foundation.shape.CircleShape
                            )
                    )
                }
            }

            Button(
                onClick = {
                    if (isPermissionScreen) {
                        // Phase 3: launches Settings.ACTION_USAGE_ACCESS_SETTINGS here.
                        onFinished()
                    } else {
                        pageIndex++
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text(if (isPermissionScreen) "Grant Access" else "Continue")
            }
        }
    }
}
