package com.example.moneytalks.Pages

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.moneytalks.Cards.GroupCard


@Composable
fun HomePage(
    navController: NavController,
    modifier: Modifier = Modifier
){
    Column {
        GroupCard(
            groupName = "Holiday 2026",
            payment = 90,
            navController = navController
        )
        GroupCard(
            groupName = "Anya Birthday",
            payment = 0,
            navController = navController
        )
        GroupCard(
            groupName = "Concert",
            payment = -1000,
            navController = navController
        )
    }


}