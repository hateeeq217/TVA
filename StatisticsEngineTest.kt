package com.tva.app.domain.statistics

import com.tva.app.domain.model.DailyUsage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class StatisticsEngineTest {

    @Test
    fun `percentageOfDay matches spec example`() {
        // 2h / 24h * 100 = 8.33%
        val result = StatisticsEngine.percentageOfDay(120)
        assertEquals(8.33f, StatisticsEngine.roundedPercent(result, 2), 0.01f)
    }

    @Test
    fun `percentageOfWakingTime matches spec example`() {
        // Wake 7:00, Sleep 23:00 -> 16h waking. 2h usage -> 12.5%
        val result = StatisticsEngine.percentageOfWakingTime(
            totalMinutes = 120,
            wake = LocalTime.of(7, 0),
            sleep = LocalTime.of(23, 0)
        )
        assertEquals(12.5f, result)
    }

    @Test
    fun `percentageOfWakingTime is null without configured schedule`() {
        assertNull(StatisticsEngine.percentageOfWakingTime(120, null, LocalTime.of(23, 0)))
    }

    @Test
    fun `wakingMinutesBetween handles overnight wrap`() {
        // Sleep before wake numerically (e.g. wake 7am, sleep 1am) still yields positive span
        val minutes = StatisticsEngine.wakingMinutesBetween(LocalTime.of(7, 0), LocalTime.of(1, 0))
        assertEquals(18 * 60, minutes)
    }

    @Test
    fun `minuteDelta and percentageChange`() {
        assertEquals(-31, StatisticsEngine.minuteDelta(106, 137))
        val pct = StatisticsEngine.percentageChange(currentMinutes = 106, previousMinutes = 137)
        assertEquals(-22.6f, StatisticsEngine.roundedPercent(pct!!, 1), 0.05f)
    }

    @Test
    fun `percentageChange is null when previous is zero`() {
        assertNull(StatisticsEngine.percentageChange(50, 0))
    }

    @Test
    fun `goalProgress reports correct state boundaries`() {
        val under = StatisticsEngine.goalProgress(currentMinutes = 30, goalMinutes = 60)
        assertEquals(com.tva.app.domain.model.GoalState.UNDER, under.state)
        assertEquals(30, under.remainingMinutes)

        val approaching = StatisticsEngine.goalProgress(currentMinutes = 52, goalMinutes = 60)
        assertEquals(com.tva.app.domain.model.GoalState.APPROACHING, approaching.state)

        val over = StatisticsEngine.goalProgress(currentMinutes = 75, goalMinutes = 60)
        assertEquals(com.tva.app.domain.model.GoalState.OVER, over.state)
        assertEquals(0, over.remainingMinutes)
    }

    @Test
    fun `currentStreak counts consecutive under-goal days backward from most recent`() {
        val today = LocalDate.of(2026, 8, 24)
        val history = listOf(
            DailyUsage(today, 40, emptyList(), goalMinutes = 60),
            DailyUsage(today.minusDays(1), 55, emptyList(), goalMinutes = 60),
            DailyUsage(today.minusDays(2), 70, emptyList(), goalMinutes = 60), // breaks streak
            DailyUsage(today.minusDays(3), 20, emptyList(), goalMinutes = 60)
        )
        assertEquals(2, StatisticsEngine.currentStreak(history))
    }

    @Test
    fun `longestStreak finds best run anywhere in history`() {
        val d0 = LocalDate.of(2026, 8, 1)
        val history = (0..5).map { offset ->
            val mins = when (offset) { 0, 1, 2 -> 40; 3 -> 90; 4, 5 -> 30; else -> 40 }
            DailyUsage(d0.plusDays(offset.toLong()), mins, emptyList(), goalMinutes = 60)
        }
        assertEquals(3, StatisticsEngine.currentStreak(history.reversed()).let {
            // current streak from most recent (day 5) walks back through day4,3(breaks)
            it
        })
        assertEquals(3, StatisticsEngine.longestStreak(history))
    }

    @Test
    fun `baseline requires minimum days and averages the earliest window`() {
        val d0 = LocalDate.of(2026, 8, 1)
        val history = (0..6).map { DailyUsage(d0.plusDays(it.toLong()), 100 + it, emptyList(), 60) }
        val baseline = StatisticsEngine.baseline(history, days = 7)
        assertEquals(103f, baseline!!, 0.01f) // avg of 100..106
    }

    @Test
    fun `baseline is null when not enough history yet`() {
        val d0 = LocalDate.of(2026, 8, 1)
        val history = (0..2).map { DailyUsage(d0.plusDays(it.toLong()), 100, emptyList(), 60) }
        assertNull(StatisticsEngine.baseline(history, days = 7))
    }
}
