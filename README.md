# TVA — Time View Access

Phase 2 of the build: a navigable Compose UI prototype, wired to realistic
mock data shaped exactly like the real data Phase 3 will produce.

## What's in this drop

- **Onboarding** — 4-screen intro ending in the usage-access permission
  explanation (no dark patterns, no auto-launch of the system dialog).
- **Home** — today's total, % of day, % of waking time, day-over-day and
  7-day-average comparisons, goal ring, per-app breakdown.
- **History** — 7/30/90-day toggle, summary stats, a bar chart, daily list.
  (Mock data covers 30 days; the 90-day view degrades to what's available,
  which is exactly how the real version will behave with sparse history.)
- **Goals** — presets + custom target, live progress ring, neutral status
  copy ("under target" / "getting close" / "passed target" — never "failed").
- **Achievements** — streak summary + milestone list.
- **Settings** — tracked apps, wake/sleep, notifications toggle, AI-insights
  toggle (off by default, with an explicit note on what data it would send),
  privacy explanation, delete-all-data confirmation dialog.

## What's real vs. mock

- `domain/statistics/StatisticsEngine.kt` — **real**, final calculation logic
  (percentages, deltas, streaks, baseline). Framework-free, unit tested in
  `app/src/test/.../StatisticsEngineTest.kt`.
- `domain/model/UsageModels.kt` — **real**, final shape of the data layer.
- `data/mock/MockUsageData.kt` — **mock**, stands in for the Phase 3
  `UsageStatsRepository`. Returns the same types the real repository will,
  so replacing it is a one-file swap — no ViewModel or UI code should need
  to change.
- Everything under `ui/` — **real** UI, driving off the above.

## Not yet wired (by design, per the spec's phased plan)

- Real `UsageStatsManager` integration (Phase 3)
- Room persistence (Phase 4 — entities are modeled, not yet backed by DAOs)
- WorkManager snapshot job
- AI insights network call (Phase 6 — toggle exists, no backend call yet)
- Notification scheduling

## Opening the project

This is a standard Gradle Android project. Open the `TVA/` folder in
Android Studio (Koala or newer) and let it sync — it will pull dependencies
from Google/Maven Central on first sync, which requires network access on
your machine. minSdk 26, targetSdk/compileSdk 34.

Run unit tests with the “Run Tests” gutter icon on `StatisticsEngineTest`,
or `./gradlew testDebugUnitTest` from a terminal once synced.

## Getting a debug APK without Android Studio

A GitHub Actions workflow is included at
`.github/workflows/build-debug-apk.yml`. Push this project to a GitHub
repo (or fork/upload it), then either let it run automatically on a push
to `main`, or trigger it manually from the **Actions** tab
("Run workflow"). When it finishes, open the run and download the
**app-debug** artifact — that's `app-debug.apk`, installable on your
phone (enable "install unknown apps" for whatever app you use to open
the download, since it isn't from the Play Store).

The workflow provisions its own JDK 17 and Gradle 8.7 (the project has no
Gradle wrapper checked in), and installs the Android platform 34 /
build-tools 34.0.0 the project targets, matching `app/build.gradle.kts`.

## Next step

Phase 3: replace `MockUsageData` with a real `UsageStatsRepository` backed
by `UsageStatsManager`, add the Usage Access settings-intent flow, and back
`DailyUsage`/`AppUsage` with Room so history survives app restarts.
