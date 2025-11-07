package com.example.moneytalks.Bars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.moneytalks.Navigation.NavIcon
import com.example.moneytalks.ui.theme.gradient


@Composable
fun NavBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavIcon.PROFILE,
        NavIcon.HOME,
        NavIcon.SETTINGS
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Box(modifier = modifier.background(gradient)) {
        NavigationBar(
            containerColor = Color.Transparent
        ) {
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

}
