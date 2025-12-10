package com.example.moneytalks

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.moneytalks.bars.NavBar
import com.example.moneytalks.bars.TopBar
import com.example.moneytalks.dataclasses.Group
import com.example.moneytalks.navigation.Destination
import com.example.moneytalks.pages.AddExpensePage
import com.example.moneytalks.pages.CreateAccount
import com.example.moneytalks.pages.GroupView
import com.example.moneytalks.pages.HomePage
import com.example.moneytalks.pages.NotificationPage
import com.example.moneytalks.pages.SettingsPage
import com.example.moneytalks.pages.EditGroupPage
import com.example.moneytalks.pages.CreateGroup
import com.example.moneytalks.pages.LoginScreen
import com.example.moneytalks.ui.theme.MoneyTalksTheme
import com.example.moneytalks.utilityclasses.NotificationUtil
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

        NotificationUtil.createNotificationChannel(this)
        requestNotificationPermission()

    }



    private var requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (!isGranted) {
                Log.d("POST_NOTIFICATION_PERMISSION", "USER DENIED PERMISSION")
            } else {
                Log.d("POST_NOTIFICATION_PERMISSION", "USER GRANTED PERMISSION")
            }
        }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            when {
                ContextCompat.checkSelfPermission(
                    this, permission
                ) == PackageManager.PERMISSION_GRANTED -> {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show()
                }

                shouldShowRequestPermissionRationale(permission) -> {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show()
                }

                else -> {
                    requestPermissionLauncher.launch(permission)
                }
            }
        } else {
            Toast.makeText(this, "No required permission", Toast.LENGTH_LONG).show()
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewScreenSizes
@Composable
fun MoneyTalksApp() {
    val navController = rememberNavController()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val userVM: UserViewModel = viewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            val title = if (currentRoute == Destination.GROUPVIEW.route || currentRoute == Destination.ADDEXPENSE.route) {
                val group = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<Group>("group")
                group?.name
            } else null

            TopBar(scrollBehavior, title)
        },
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
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {
            composable(Destination.HOME.route) {
                val user = userVM.currentUser.value
                if (user != null) {
                    HomePage(user.id, navController)
                }else{
                    println("User is null")
                }
            }
            composable(Destination.SETTINGS.route) {
                SettingsPage(navController, userVM)
            }
            composable(Destination.NOTIFICATIONS.route) {
                val user = userVM.currentUser.value
                if (user != null) {
                    NotificationPage(user.id, navController)
                }else{
                    println("User is null")
                }
            }
            composable(Destination.EDITGROUP.route) {
                val group = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<Group>("group")

                if (group != null) {
                    EditGroupPage(group, navController, userVM)
                } else {
                    Text("Group not found")
                }
            }
            composable(Destination.CREATEGROUP.route) {
                CreateGroup(navController, userVM)
            }
            composable(Destination.GROUPVIEW.route) {
                val group = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<Group>("group")

                if (group != null) {
                    GroupView(navController, group, userVM)
                } else {
                    Text("Group not found for GroupView")
                }
            }
            composable(Destination.ADDEXPENSE.route) {
                val group = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<Group>("group")

                if (group != null) {
                    AddExpensePage(navController, group, userVM)
                } else {
                    Text("Group not found for AddExpense")
                }
            }
            composable(Destination.LOGIN.route) {
                LoginScreen(navController, userVM)
            }
            composable(Destination.CREATEACCOUNT.route) {
                CreateAccount(navController, userVM)
            }
        }
    }
}


