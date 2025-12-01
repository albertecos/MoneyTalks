package com.example.moneytalks.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import com.example.moneytalks.viewmodel.UserViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.moneytalks.apisetup.RetrofitClient
import com.example.moneytalks.dataclasses.Expense
import com.example.moneytalks.dataclasses.Group
import com.example.moneytalks.dataclasses.GroupMember
import com.example.moneytalks.dataclasses.User
import com.example.moneytalks.navigation.Destination
import com.example.moneytalks.navigation.NavIcon
import com.example.moneytalks.ui.theme.redInDebt
import com.example.moneytalks.ui.theme.DarkBlue
import com.example.moneytalks.ui.theme.GreyColor


@Composable
fun GroupMembersListCard(
    navController: NavController,
    group: Group? = null,
    onClose: () -> Unit
) {
    if (group == null) {
        navController.navigateUp()
        return
    }
    ElevatedCard (
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(20.dp)
    ){
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ){
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
                    GroupMemberCard(member, group.id)
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
            username = "alice",
            full_name = "Alice Andersen",
            email = "alice@example.com",
            password = "",
            profile_picture = "",
            accepted = true
        ),
        GroupMember(
            id = "user_2",
            username = "bob",
            full_name = "Bob Hansen",
            email = "bob@example.com",
            password = "",
            profile_picture = "",
            accepted = true
        ),
        GroupMember(
            id = "user_3",
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

    GroupMembersListCard(
        navController = navController,
        group = group,
        onClose = { true }
    )
}
