package com.example.moneytalks.Pages

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.moneytalks.Cards.GroupCard


@Composable
fun HomePage(
    navController: NavController,
    modifier: Modifier = Modifier
){
    GroupCard(
        groupName = "Holiday 2026",
        navController = navController
    )

}