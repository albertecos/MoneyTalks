package com.example.moneytalks.Bars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.moneytalks.Navigation.NavIcon
import com.example.moneytalks.ui.theme.DarkBlue
import com.example.moneytalks.ui.theme.LightBlue
import com.example.moneytalks.ui.theme.LightLightBlue
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
    Box(modifier = modifier.background(LightBlue)) {
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
                            contentDescription = item.destination.contentDescription,
                            tint = DarkBlue,
                            modifier = Modifier.size(40.dp)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.White
                    )
                )
            }
        }

    }

}
