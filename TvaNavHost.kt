package com.tva.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tva.app.ui.achievements.AchievementsScreen
import com.tva.app.ui.goals.GoalsScreen
import com.tva.app.ui.history.HistoryScreen
import com.tva.app.ui.home.HomeScreen
import com.tva.app.ui.settings.SettingsScreen

private sealed class Destination(val route: String, val label: String, val icon: ImageVector) {
    data object Home : Destination("home", "Home", Icons.Filled.Home)
    data object History : Destination("history", "History", Icons.Filled.ShowChart)
    data object Goals : Destination("goals", "Goals", Icons.Filled.Flag)
    data object Achievements : Destination("achievements", "Achievements", Icons.Filled.EmojiEvents)
    data object Settings : Destination("settings", "Settings", Icons.Filled.Settings)
}

private val bottomNavItems = listOf(
    Destination.Home, Destination.History, Destination.Goals, Destination.Achievements, Destination.Settings
)

@Composable
fun TvaNavHost() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { TvaBottomBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Destination.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Destination.Home.route) { HomeScreen() }
            composable(Destination.History.route) { HistoryScreen() }
            composable(Destination.Goals.route) { GoalsScreen() }
            composable(Destination.Achievements.route) { AchievementsScreen() }
            composable(Destination.Settings.route) { SettingsScreen() }
        }
    }
}

@Composable
private fun TvaBottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        bottomNavItems.forEach { destination ->
            NavigationBarItem(
                selected = currentRoute == destination.route,
                onClick = {
                    navController.navigate(destination.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(destination.icon, contentDescription = destination.label) },
                label = { Text(destination.label) }
            )
        }
    }
}
