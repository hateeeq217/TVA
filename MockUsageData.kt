package com.tva.app.data.mock

import com.tva.app.domain.model.Achievement
import com.tva.app.domain.model.AppUsage
import com.tva.app.domain.model.DailyUsage
import com.tva.app.domain.model.UserSettings
import java.time.LocalDate

/**
 * Phase 2 stand-in for the real UsageStatsRepository (Phase 3).
 * Shape matches the real repository's return types exactly, so swapping this
 * out later is a one-file change — no UI or ViewModel code should need to move.
 */
object MockUsageData {

    val settings = UserSettings()

    private const val INSTAGRAM = "com.instagram.android"
    private const val TIKTOK = "com.zhiliaoapp.musically"
    private const val YOUTUBE = "com.google.android.youtube"

    fun appDisplayName(packageName: String): String = when (packageName) {
        INSTAGRAM -> "Instagram"
        TIKTOK -> "TikTok"
        YOUTUBE -> "YouTube"
        else -> packageName
    }

    /** 30 days of plausible, gently-declining usage ending today (2026-08-24). */
    val history: List<DailyUsage> by lazy {
        val today = LocalDate.of(2026, 8, 24)
        val rawMinutesNewestFirst = listOf(
            106, 137, 108, 133, 148, 128, 161,      // most recent 7 days
            139, 151, 132, 168, 142, 155, 171,
            149, 163, 158, 174, 166, 181, 159,
            177, 169, 185, 173, 190, 178, 195, 182, 163
        )
        rawMinutesNewestFirst.mapIndexed { index, total ->
            val date = today.minusDays(index.toLong())
            val igShare = 0.55
            val ttShare = 0.30
            val ytShare = 0.15
            DailyUsage(
                date = date,
                totalMinutes = total,
                byApp = listOf(
                    AppUsage(INSTAGRAM, appDisplayName(INSTAGRAM), (total * igShare).toInt(), sessions = (total / 9)),
                    AppUsage(TIKTOK, appDisplayName(TIKTOK), (total * ttShare).toInt(), sessions = (total / 6)),
                    AppUsage(YOUTUBE, appDisplayName(YOUTUBE), (total * ytShare).toInt(), sessions = (total / 12))
                ),
                goalMinutes = settings.dailyGoalMinutes
            )
        }
    }

    val today: DailyUsage get() = history.first()
    val yesterday: DailyUsage get() = history[1]
    val last7Days: List<DailyUsage> get() = history.take(7)
    val last30Days: List<DailyUsage> get() = history

    val achievements: List<Achievement> = listOf(
        Achievement("first_step", "First Step", "Complete your first day of tracking.", LocalDate.of(2026, 7, 26)),
        Achievement("under_one_hour", "Under One Hour", "Stay below one hour for a day.", null),
        Achievement("three_day_control", "Three-Day Control", "Stay under your personal goal for three consecutive days.", null),
        Achievement("seven_day_streak", "Seven-Day Streak", "Meet your goal for seven days.", null),
        Achievement("personal_best", "Personal Best", "Record your lowest usage day.", LocalDate.of(2026, 8, 20)),
        Achievement("improvement_25", "25% Improvement", "Reduce average usage by 25%.", null),
        Achievement("consistency_14", "Consistency", "Meet your target for 14 days.", null)
    )
}
