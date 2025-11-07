package com.example.moneytalks.Bars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import com.example.moneytalks.ui.theme.LilyScriptOne
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.navigation.NavController
import com.example.moneytalks.Navigation.Destination
import com.example.moneytalks.Navigation.NavIcon
import com.example.moneytalks.Pages.NotificationPage
import com.example.moneytalks.ui.theme.Black
import com.example.moneytalks.ui.theme.blueDebtFreeV2
import com.example.moneytalks.ui.theme.gradient


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController, scrollBehavior: TopAppBarScrollBehavior) {

    CenterAlignedTopAppBar(
        modifier = Modifier.background(gradient).statusBarsPadding(),
        colors = TopAppBarDefaults.topAppBarColors(
            //containerColor = Color.Red,
            containerColor = Color.Transparent,
            titleContentColor = Black
        ),
        title = {
            Text(
                text = "Money Talk",
                fontFamily = LilyScriptOne,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = { navController.navigate(Destination.NOTIFICATIONS.route) }) {
                Icon(
                    imageVector = NavIcon.NOTIFICATIONS.icon,
                    contentDescription = NavIcon.NOTIFICATIONS.destination.contentDescription
                )
            }
        },
        scrollBehavior = scrollBehavior,
    )
}
