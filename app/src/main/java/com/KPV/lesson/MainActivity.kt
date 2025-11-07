package com.kpv.lesson

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kpv.lesson.navigation.NavigationIds
import com.kpv.lesson.navscreens.edit.EditScreen
import com.kpv.lesson.navscreens.messages.MessagesScreen
import com.kpv.lesson.navscreens.settings.SettingsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val screens = listOf(
                NavigationIds.SETTINGS,
                NavigationIds.EDIT,
                NavigationIds.MESSAGES
            )
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            Scaffold(
                bottomBar = {
                    NavigationBar {
                        screens.forEachIndexed { index, screen ->
                            NavigationBarItem(
                                icon = {},
                                label = { Text(screen.getTitle()) },
                                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                onClick = {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = NavigationIds.SETTINGS.route,
                    modifier = androidx.compose.ui.Modifier.padding(innerPadding)
                ) {
                    composable(NavigationIds.SETTINGS.route) {
                        SettingsScreen()
                    }
                    composable(NavigationIds.EDIT.route) {
                        EditScreen()
                    }
                    composable(NavigationIds.MESSAGES.route) {
                        MessagesScreen()
                    }
                }
            }
        }
    }
}