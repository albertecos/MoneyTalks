package com.example.moneytalks.pages
 
 import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.foundation.layout.Spacer
 import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Column
 import androidx.compose.foundation.layout.padding
 import androidx.compose.material3.Text
 import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
 import com.example.moneytalks.viewmodel.UserViewModel
import com.example.moneytalks.viewmodel.GroupsViewModel
import androidx.compose.ui.Modifier
 import androidx.compose.ui.unit.dp

@Composable
fun GroupMembersPage(navController: NavController, group: com.example.moneytalks.dataclasses.Group? = null) {
    if (group == null) {
        navController.navigateUp()
        return
    }
    val groupVM: GroupsViewModel = viewModel()
    val userVM: UserViewModel = viewModel()

    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Text(
            text = "Group Members"
        )
        Spacer(modifier = Modifier.height(16.dp))
        for (member in group.members) {
            if (member.id == userVM.currentUser.value?.id) continue
            MemberListElement(
                member = member,
                additionalActionIcon = Icons.Default.Notifications,
                onAdditionalActionClick = {
                    groupVM.sendReminder(
                        userId = member.id,
                        groupId = group.id
                    )
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}