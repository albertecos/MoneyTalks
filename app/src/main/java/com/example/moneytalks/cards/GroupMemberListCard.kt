package com.example.moneytalks.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.example.moneytalks.viewmodel.UserViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.moneytalks.dataclasses.Group
import com.example.moneytalks.dataclasses.GroupMember
import com.example.moneytalks.dataclasses.User
import com.example.moneytalks.ui.theme.DarkBlue
import com.example.moneytalks.viewmodel.BalanceViewModel


@Composable
fun GroupMembersListCard(
    navController: NavController,
    group: Group? = null,
    balanceVm: BalanceViewModel,
    onClose: () -> Unit
) {
    val groupMembers = group?.members

    LaunchedEffect(group?.id) {
        groupMembers?.forEach { member ->
            balanceVm.fetchBalance(group.id, member.id)
        }
    }



    if (group == null) {
        navController.navigateUp()
        return
    }
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = {
                        onClose()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Close",
                        tint = DarkBlue
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                for (member in group.members) {
                    val memberBalance = balanceVm.memberBalances[member.id] ?: 0.0
                    GroupMemberCard(member, group.id, memberBalance)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GroupMembersListCardPreview() {
    val navController = rememberNavController()

    val fakeUser = User(
        id = "user_1",
        username = "alice",
        profile_picture = "",
        full_name = "Alice Andersen",
        email = "alice@example.com",
        password = ""
    )

    val members = listOf(
        GroupMember(
            id = "user_1",
            userId = "userId_1",
            username = "alice",
            full_name = "Alice Andersen",
            email = "alice@example.com",
            password = "",
            profile_picture = "",
            accepted = true
        ),
        GroupMember(
            id = "user_2",
            userId = "userId_2",
            username = "bob",
            full_name = "Bob Hansen",
            email = "bob@example.com",
            password = "",
            profile_picture = "",
            accepted = true
        ),
        GroupMember(
            id = "user_3",
            userId = "userId_3",
            username = "charlie",
            full_name = "Charlie Jensen",
            email = "charlie@example.com",
            password = "",
            profile_picture = "",
            accepted = false
        )
    )

    val group = Group(
        id = "group_123",
        name = "Holiday Trip",
        members = members
    )

    val userVM = remember {
        UserViewModel().apply {
            currentUser.value = fakeUser
        }
    }
/*
    GroupMembersListCard(
        navController = navController,
        group = group,
        onClose = { true }
    )

 */
}
