package com.example.moneytalks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.moneytalks.Bars.NavBar
import com.example.moneytalks.Bars.TopBar
import com.example.moneytalks.Navigation.Destination
import com.example.moneytalks.Pages.CreateAccount
import com.example.moneytalks.Pages.GroupView
import com.example.moneytalks.Pages.HomePage
import com.example.moneytalks.Pages.NotificationPage
import com.example.moneytalks.Pages.ProfilePage
import com.example.moneytalks.Pages.SettingsPage
import com.example.moneytalks.Pages.EditGroupPage
import com.example.moneytalks.Pages.CreateGroup
import com.example.moneytalks.Pages.LoginScreen
import com.example.moneytalks.ui.theme.MoneyTalksTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoneyTalksTheme {
                MoneyTalksApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewScreenSizes
@Composable
fun MoneyTalksApp() {
    val navController = rememberNavController()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val startMemberID = "68661b1b-6586-4f3c-ac0b-a02fa35d60f7"

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { TopBar(navController, scrollBehavior) },
        bottomBar = {
            if (currentRoute != Destination.LOGIN.route &&
                currentRoute != Destination.CREATEACCOUNT.route
            ) {
                NavBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Destination.LOGIN.route,
            modifier = Modifier.padding(innerPadding).consumeWindowInsets(innerPadding)
        ){
            composable(Destination.PROFILE.route) { ProfilePage() }
            composable(Destination.HOME.route) { HomePage(startMemberID, navController) }
            composable(Destination.SETTINGS.route) { SettingsPage() }
            composable(Destination.NOTIFICATIONS.route) { NotificationPage(navController) }
            composable(Destination.EDITGROUP.route) { EditGroupPage() }
            composable(Destination.CREATEGROUP.route) { CreateGroup() }
            composable(Destination.GROUPVIEW.route) { GroupView() }
            composable(Destination.LOGIN.route) { LoginScreen(navController) }
            composable(Destination.CREATEACCOUNT.route) { CreateAccount(navController) }
        }
    }

}
