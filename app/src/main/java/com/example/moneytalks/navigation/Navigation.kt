package com.example.moneytalks.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class Destination(
    val route: String,
    val label: String,
    val contentDescription: String

){
    HOME("home", "Home", "Home"),
    SETTINGS("settings", "Settings", "Settings"),
    NOTIFICATIONS("notifications", "Notifications", "Notifications"),
    CREATEGROUP("createGroup", "CreateGroup", "Create a group"),
    EDITGROUP("editGroup", "EditGroup", "EditGroup"),
    GROUPVIEW("groupview", "Groupview", "Groupview"),
    ADDEXPENSE("addexpense", "AddExpense", "AddExpense"),
    LOGIN("login", "Login", "Login"),
    CREATEACCOUNT("createAccount", "CreateAccount", "Create Account")
}

enum class NavIcon(
    var destination: Destination,
    val icon: ImageVector,
){
    HOME(Destination.HOME, Icons.Outlined.Home),
    SETTINGS(Destination.SETTINGS, Icons.Outlined.Settings),
    NOTIFICATIONS(Destination.NOTIFICATIONS, Icons.Outlined.Notifications),
    EDITGROUP(Destination.EDITGROUP, Icons.Outlined.Edit)
}

