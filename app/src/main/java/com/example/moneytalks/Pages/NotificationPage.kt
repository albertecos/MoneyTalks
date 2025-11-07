package com.example.moneytalks.Pages

import android.app.Notification
import android.widget.Space
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moneytalks.Notifications.GroupInviteNotify
import com.example.moneytalks.Notifications.PaymentNotify
import com.example.moneytalks.Notifications.ReceivementNotify


@Composable
fun NotificationPage(
    navController: NavController,
    modifier: Modifier = Modifier
){
    Column (
        modifier = modifier.padding(horizontal = 12.dp).fillMaxSize()
    ) {
        Text(
            text ="Group Invites",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        GroupInviteNotify("New Years", "10.10.2002")

        Spacer(Modifier.height(16.dp))
        Text(
            text ="History",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        PaymentNotify("150", "Weekend","10.10.2002", navController)
        ReceivementNotify("2000", "Paartyyy","05.11.2025")

    }
}

@Preview
@Composable
fun NotificationPreview(){
    //NotificationPage(navController)
}
