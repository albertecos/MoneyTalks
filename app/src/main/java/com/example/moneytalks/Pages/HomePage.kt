package com.example.moneytalks.Pages

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.moneytalks.Cards.GroupCard
import com.example.moneytalks.ViewModel.GroupsViewModel


@Composable
fun HomePage(
    memberId: String,
    navController: NavController,
    modifier: Modifier = Modifier
){
    val vm: GroupsViewModel = viewModel()

    LaunchedEffect(memberId) {
        vm.fetchGroups(memberId)
    }

    val groups = vm.groups
    Column(modifier = modifier) {

        groups.forEach { group ->
            GroupCard(
                groupName = group.name,
                payment = 90,
                navController = navController
            )
        }

        if(groups.isEmpty()){
            Text(
                text = "No groups yet..."
            )
        }
    }


}