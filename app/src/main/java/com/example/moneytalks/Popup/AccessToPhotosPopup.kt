package com.example.moneytalks.Popup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Preview
@Composable
fun AccessPhotosPopupPreview() {
    AccessPhotosPopup(
        onDismiss = {},
        onDeny = {},
        onAllow = {}
    )
}

@Composable
fun AccessPhotosPopup(
    onDismiss: () -> Unit,
    onDeny: () -> Unit,
    onAllow: () -> Unit,
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
                Text(text = "Allow MoneyTalk to access and manage your photo album?", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Row (modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterHorizontally),
                )  {
                    Button(onClick = onDeny) {Text("Deny")}
                    Button(onClick = onAllow) {Text("Allow")}
                }
            }
        }
    }
}
