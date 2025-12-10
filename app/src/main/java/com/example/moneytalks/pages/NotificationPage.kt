package com.example.moneytalks.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.moneytalks.notifications.GroupInviteNotify
import com.example.moneytalks.notifications.PaymentNotify
import com.example.moneytalks.notifications.ReceivementNotify
import com.example.moneytalks.notifications.ReminderNotify
import com.example.moneytalks.notifications.ExpensePaidNotify
import com.example.moneytalks.viewmodel.NotificationViewModel
import kotlinx.coroutines.delay


@Composable
fun NotificationPage(
    userId: String,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val vm: NotificationViewModel = viewModel()

    LaunchedEffect(userId) {
        vm.fetchNotifications(userId)
    }

    val notifications = vm.notifications

    Column(
        modifier = modifier
            .padding(horizontal = 12.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Group Invites",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        val inviteNotification = notifications.filter { it.action == "INVITE" }.sortedByDescending { it.date }

        if (inviteNotification.isEmpty()) {
            Text("No invites at the moment...")
        } else {
            inviteNotification.forEach { notification ->
                GroupInviteNotify(
                    groupName = notification.groupName,
                    date = notification.date.take(10),
                    onAccept = { vm.acceptInvite(notification)},
                    onDecline = {vm.declineInvite(notification)}
                )
                Spacer(Modifier.height(16.dp))
            }
        }

        Text(
            text = "History",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        val expenseNotifications = notifications.filter {
            it.action == "PAYMENT" || it.action == "RECEIVEMENT" || it.action == "EXPENSE" || it.action == "REMINDER"
        }.sortedByDescending { it.date }

        if(expenseNotifications.isEmpty()){
            Text("No notifications yet...")
        } else {
            expenseNotifications.forEach { notification ->
                when (notification.action){
                    "PAYMENT" -> PaymentNotify(
                        payment = notification.amount?.toString() ?: "0",
                        groupName = notification.groupName,
                        date = notification.date.take(10),
                        navController = navController
                    )

                    "EXPENSE" -> ExpensePaidNotify(
                        payment = notification.amount?.toString() ?: "0",
                        groupName = notification.groupName,
                        date = notification.date.take(10),
                        navController = navController
                    )

                    "RECEIVEMENT" -> ReceivementNotify(
                        payment = notification.amount?.toString() ?: "0",
                        groupName = notification.groupName,
                        date = notification.date.take(10),
                        navController = navController
                    )
                    "REMINDER" -> ReminderNotify(
                        payment = notification.amount?.toString() ?: "0",
                        groupName = notification.groupName,
                        date = notification.date.take(10),
                        navController = navController
                    )
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Preview
@Composable
fun NotificationPreview() {
    //NotificationPage(navController)
}
