package com.tva.app.domain.model

import java.time.LocalDate
import java.time.LocalTime

/** One tracked app's usage on a single day. sessions is null when the OS/OEM
 *  doesn't reliably expose session counts — never fabricated. */
data class AppUsage(
    val packageName: String,
    val displayName: String,
    val minutes: Int,
    val sessions: Int? = null
)

/** Aggregate usage for a single day across all tracked apps. */
data class DailyUsage(
    val date: LocalDate,
    val totalMinutes: Int,
    val byApp: List<AppUsage>,
    val goalMinutes: Int?
)

data class UserSettings(
    val dailyGoalMinutes: Int = 60,
    val wakeTime: LocalTime? = LocalTime.of(7, 0),
    val sleepTime: LocalTime? = LocalTime.of(23, 0),
    val trackedApps: List<String> = listOf("com.instagram.android", "com.zhiliaoapp.musically", "com.google.android.youtube"),
    val notificationsEnabled: Boolean = true,
    val aiInsightsEnabled: Boolean = false,
    val theme: String = "SYSTEM"
)

enum class GoalState { UNDER, APPROACHING, OVER }

data class GoalProgress(
    val goalMinutes: Int,
    val currentMinutes: Int,
    val remainingMinutes: Int,
    val progressFraction: Float, // 0f..1f+ (can exceed 1 when over)
    val state: GoalState
)

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val unlockedDate: LocalDate?
)

data class StreakInfo(
    val currentStreak: Int,
    val longestStreak: Int,
    val brokeYesterday: Boolean
)

data class Insight(val text: String)
