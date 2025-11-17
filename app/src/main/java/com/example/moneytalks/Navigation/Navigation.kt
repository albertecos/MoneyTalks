package com.example.moneytalks.Navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moneytalks.Pages.HomePage
import com.example.moneytalks.Pages.NotificationPage
import com.example.moneytalks.Pages.ProfilePage
import com.example.moneytalks.Pages.SettingsPage

enum class Destination(
    val route: String,
    val label: String,
    val contentDescription: String

){
    PROFILE("profile", "Profile", "Profile"),
    HOME("home", "Home", "Home"),
    SETTINGS("settings", "Settings", "Settings"),
    NOTIFICATIONS("notifications", "Notifications", "Notifications"),
    CREATEGROUP("createGroup", "CreateGroup", "Create a group"),
    EDITGROUP("editGroup", "EditGroup", "EditGroup"),
    GROUPVIEW("groupview", "Groupview", "Groupview"),
    LOGIN("login", "Login", "Login"),
    CREATEACCOUNT("createAccount", "CreateAccount", "Create Account")
}

enum class NavIcon(
    var destination: Destination,
    val icon: ImageVector,
){
    PROFILE(Destination.PROFILE, Icons.Outlined.Person),
    HOME(Destination.HOME, Icons.Outlined.Home),
    SETTINGS(Destination.SETTINGS, Icons.Outlined.Settings),
    NOTIFICATIONS(Destination.NOTIFICATIONS, Icons.Outlined.Notifications),
    EDITGROUP(Destination.EDITGROUP, Icons.Outlined.Edit)
}

