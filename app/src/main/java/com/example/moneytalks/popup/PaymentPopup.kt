package com.example.moneytalks.popup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.moneytalks.ui.theme.blueDebtFree

@Preview
@Composable
fun PaymentPopupPreview() {
    PaymentPopup(
        onDismiss = {},
        onConfirm = {},
        30.00
    )
}

@Composable
fun PaymentPopup(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    value: Double
    ) {
    Dialog(onDismissRequest = onDismiss) {
        val formattedPrice = String.format("%.2f", Math.abs(value))

        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {

                if (value > 0) {
                    Text(
                        text = "Pay your part to the group?",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Text(
                        text = "$formattedPrice.-",
                        fontWeight = FontWeight.ExtraBold,
                        color = blueDebtFree,
                        fontSize = 40.sp,
                        modifier = Modifier
                            .padding(10.dp)
                    )
                } else {
                    Text(
                        text = "You don't owe anything to the group!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(10.dp)
                    )
                }

                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterHorizontally),
                )  {
                    Text(
                        text = "Cancel",
                        fontSize = 15.sp,
                        modifier = Modifier
                            .padding(5.dp)
                            .clickable{onDismiss() }
                    )

                    Text(
                        text = "Confirm",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(5.dp)
                            .clickable{onConfirm() }
                    )
                }
            }
        }
    }
}
