package com.example.moneytalks

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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
import kotlin.jvm.java

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoneyTalksTheme {
                MoneyTalksApp()
            }
        }

        createNotificationChannel()
        requestNotificationPermission()

    }

    companion object {
        private const val CHANNEL_ID = "notification_channel"
    }


    private var requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (!isGranted) {
                requestNotificationPermission()
                Log.d("POST_NOTIFICATION_PERMISSION", "USER DENIED PERMISSION")
            } else {
                sendNotification("title", "text")
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

//    var builder = NotificationCompat.Builder(this, CHANNEL_ID)
//        .setSmallIcon(R.drawable.notification_prompt)
//        .setContentTitle(/*textTitle*/ "")
//        .setContentText(/*textContent*/"")
//        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Channel"
            val descriptionText = "The notification channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

    }


    fun sendNotification(title: String, text: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(1, builder.build())
        }
    }

    private fun showNotification(title: String? = "Hello", message: String? = "World") {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, "CHANNEL", NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewScreenSizes
@Composable
fun MoneyTalksApp() {
    val navController = rememberNavController()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

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
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {
            composable(Destination.HOME.route) { HomePage(startMemberID, navController) }
            composable(Destination.SETTINGS.route) { SettingsPage() }
            composable(Destination.NOTIFICATIONS.route) {
                NotificationPage(
                    startMemberID,
                    navController
                )
            }
            composable(Destination.EDITGROUP.route) {
                val group = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<Group>("group")

                if (group != null) {
                    EditGroupPage(group, navController)
                } else {
                    Text("Group not found")
                }
            }
            composable(Destination.CREATEGROUP.route) { CreateGroup(navController) }
            composable(Destination.GROUPVIEW.route) {
                val group = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<Group>("group")

                if (group != null) {
                    GroupView(navController, group)
                } else {
                    Text("Group not found for GroupView")
                }
            }
            composable(Destination.ADDEXPENSE.route) {
                val group = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<Group>("group")

                if (group != null) {
                    AddExpensePage(navController, group)
                } else {
                    Text("Group not found for AddExpense")
                }
            }
            composable(Destination.LOGIN.route) { LoginScreen(navController) }
            composable(Destination.CREATEACCOUNT.route) { CreateAccount(navController) }
        }
    }
}


