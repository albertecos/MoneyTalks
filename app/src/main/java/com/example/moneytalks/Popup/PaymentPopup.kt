package com.example.moneytalks.Popup

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

@Preview
@Composable
fun PaymentPopupPreview() {
    PaymentPopup(
        onDismiss = {},
        onConfirm = {}
    )
}

@Composable
fun PaymentPopup(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
    ) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Pay your part to the group?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterHorizontally),
                )  {
                    Text(
                        text = "Cancel",
                        fontSize = 15.sp,
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable{onDismiss() }
                    )

                    Text(
                        text = "Confirm",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable{onDismiss() }
                    )
                }
            }
        }
    }
}
