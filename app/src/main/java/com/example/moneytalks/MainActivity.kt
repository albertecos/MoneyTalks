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
import com.example.moneytalks.bars.NavBar
import com.example.moneytalks.bars.TopBar
import com.example.moneytalks.navigation.Destination
import com.example.moneytalks.pages.AddExpensePage
import com.example.moneytalks.pages.CreateAccount
import com.example.moneytalks.pages.GroupView
import com.example.moneytalks.pages.HomePage
import com.example.moneytalks.pages.NotificationPage
import com.example.moneytalks.pages.ProfilePage
import com.example.moneytalks.pages.SettingsPage
import com.example.moneytalks.pages.EditGroupPage
import com.example.moneytalks.pages.CreateGroup
import com.example.moneytalks.pages.LoginScreen
import com.example.moneytalks.ui.theme.MoneyTalksTheme
import com.example.moneytalks.viewmodel.NotificationViewModel
import com.example.moneytalks.viewmodel.UserViewModel


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
    val userViewModel: UserViewModel = viewModel()
    val notificationViewModel: NotificationViewModel = viewModel()

    val startMemberID = "c4d21a74-c59c-4a4b-8dea-9eb519428543"

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
            composable(Destination.SETTINGS.route) { SettingsPage(userViewModel = userViewModel, notificationViewModel = notificationViewModel) }
            composable(Destination.NOTIFICATIONS.route) { NotificationPage(startMemberID, navController) }
            composable(Destination.EDITGROUP.route) {
                val group = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<com.example.moneytalks.dataclasses.Group>("group")
                
                if (group != null) {
                    EditGroupPage(group, navController)
                } else {
                    androidx.compose.material3.Text("Group not found")
                }
            }
            composable(Destination.CREATEGROUP.route) { CreateGroup(navController) }
            composable(Destination.GROUPVIEW.route) { GroupView(navController) }
            composable(Destination.ADDEXPENSE.route) { AddExpensePage(navController) }
            composable(Destination.LOGIN.route) { LoginScreen(navController) }
            composable(Destination.CREATEACCOUNT.route) { CreateAccount(navController) }
        }
    }

}
