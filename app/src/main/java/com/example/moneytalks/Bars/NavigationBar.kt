package com.example.moneytalks.Bars

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.moneytalks.Navigation.NavIcon



@Composable
fun NavBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavIcon.HOME,
        NavIcon.PROFILE,
        NavIcon.SETTINGS
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(modifier = modifier) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.destination.route,
                onClick = {
                    navController.navigate(item.destination.route)
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.destination.contentDescription
                    )
                },
                label = { Text(item.destination.label) }
            )
        }
    }
}
