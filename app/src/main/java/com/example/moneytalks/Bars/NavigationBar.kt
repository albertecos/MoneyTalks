package com.example.moneytalks.Bars

import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.getValue
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.moneytalks.Navigation.Destination
import com.example.moneytalks.Navigation.NavIcon
import com.example.moneytalks.Pages.HomePage
import com.example.moneytalks.Pages.NotificationPage
import com.example.moneytalks.Pages.ProfilePage
import com.example.moneytalks.Pages.SettingsPage


@Composable
fun NavBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar(modifier = modifier) {
        NavIcon.entries.forEach { destination ->
            NavigationBarItem(
                selected = currentRoute == destination.destination.route,
                onClick = {
                    if (currentRoute != destination.destination.route) {
                        navController.navigate(destination.destination.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        destination.icon,
                        contentDescription = destination.destination.contentDescription
                    )
                },
                label = { Text(destination.destination.label) }
            )
        }
    }
}

@Composable
fun NavigateNavBar(
    navController: NavHostController,
    startDestination: Destination,
    modifier: Modifier = Modifier
){
    NavHost (
        navController,
        startDestination = startDestination.route,
        modifier = modifier
    ){
        Destination.entries.forEach { destination ->
            composable(destination.route){
                when(destination){
                    Destination.PROFILE -> ProfilePage()
                    Destination.HOME -> HomePage()
                    Destination.SETTINGS -> SettingsPage()
                    Destination.NOTIFICATIONS -> NotificationPage()
                }
            }
        }
    }
}
