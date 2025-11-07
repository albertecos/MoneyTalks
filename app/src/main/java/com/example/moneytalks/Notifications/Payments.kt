package com.example.moneytalks.Notifications

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moneytalks.Navigation.Destination

@Composable
fun PaymentNotify(
    payment: String,
    groupName: String,
    date: String,
    navController: NavController,
    onClick: () -> Unit = { navController.navigate(Destination.GROUPVIEW.route) },
    onAccept: () -> Unit = {},
    onDecline: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(12.dp)
                .clickable(onClick = onClick),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "You owe ",
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = payment,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFDC143C)
                    )

                }
                Text(
                    text = "Group: $groupName",
                )

                Text(date, color = Color.DarkGray, fontStyle = FontStyle.Italic)
            }
        }
    }
}

@Preview
@Composable
fun PaymentNotifyPreview() {
    //PaymentNotify("150", "weekend","10.10.2002")
}