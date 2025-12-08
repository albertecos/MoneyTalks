package com.example.moneytalks.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
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
import androidx.navigation.compose.rememberNavController
import com.example.moneytalks.navigation.Destination

@Composable
fun PaymentNotify(
    payment: String,
    groupName: String,
    date: String,
    navController: NavController,
    onClick: () -> Unit = { navController.navigate(Destination.GROUPVIEW.route) },
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
                        text = payment,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFDC143C)
                    )
                    Text(
                        text = " added expense",
                        fontWeight = FontWeight.SemiBold
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

@Composable
fun ExpensePaidNotify(
    payment: String,
    groupName: String,
    date: String,
    navController: NavController,
    onClick: () -> Unit = { navController.navigate(Destination.GROUPVIEW.route) },
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
                        text = payment,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF32CD32) // LimeGreen
                    )
                    Text(
                        text = " was paid",
                        fontWeight = FontWeight.SemiBold
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
    val navController = rememberNavController()
    PaymentNotify(
        payment = "150 kr",
        groupName = "Weekend Trip",
        date = "10.10.2024",
        navController = navController
    )
}

@Preview
@Composable
fun ExpensePaidNotifyPreview() {
    val navController = rememberNavController()
    ExpensePaidNotify(
        payment = "250 kr",
        groupName = "Cinema",
        date = "12.10.2024",
        navController = navController
    )
}
