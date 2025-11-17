package com.example.moneytalks.Pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.moneytalks.Cards.GroupCard
import com.example.moneytalks.Navigation.Destination
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
                groupId = group.id,
                memberId = memberId,
                navController = navController
            )
        }

        if(groups.isEmpty()){
            Text(
                text = "No groups yet..."
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        FloatingActionButton(
            onClick = {
                navController.navigate(Destination.CREATEGROUP.route)
            },
            modifier = Modifier
                .align(androidx.compose.ui.Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create Group"
            )
        }
    }

}