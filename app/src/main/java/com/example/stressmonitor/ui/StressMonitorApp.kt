// app/src/main/java/com/example/stressmonitor/ui/StressMonitorApp.kt
package com.example.stressmonitor.ui

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.stressmonitor.ui.screens.HomeScreen
import com.example.stressmonitor.ui.screens.DevicesScreen
import com.example.stressmonitor.ui.screens.AccountScreen
import com.example.stressmonitor.ui.viewmodel.StressViewModel

sealed class Screen(val route: String, val icon: ImageVector, val label: String) {
    object Devices : Screen("devices", Icons.Default.Settings, "Devices")
    object Main    : Screen("main",    Icons.Default.Home,     "Home")
    object Account : Screen("account", Icons.Default.Person,   "Account")
}

@Composable
fun StressMonitorApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val current = navController.currentBackStackEntryAsState().value?.destination?.route
                listOf(Screen.Devices, Screen.Main, Screen.Account).forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = current == screen.route,
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
    ) { padding ->
        NavHost(
            navController,
            startDestination = Screen.Main.route,
            Modifier.padding(padding)
        ) {
            composable(Screen.Devices.route) {
                DevicesScreen()
            }
            composable(Screen.Main.route) {
                // передаём ViewModel через hiltViewModel()
                val vm: StressViewModel = hiltViewModel()
                HomeScreen(viewModel = vm)
            }
            composable(Screen.Account.route) {
                AccountScreen()
            }
        }
    }
}
