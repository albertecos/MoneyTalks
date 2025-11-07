package com.example.moneytalks.Cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavController
import com.example.moneytalks.Navigation.Destination
import com.example.moneytalks.ui.theme.DarkBlue
import com.example.moneytalks.ui.theme.LightLightBlue
import com.example.moneytalks.ui.theme.greenCreditor
import com.example.moneytalks.ui.theme.redInDebt
import java.util.Properties
import kotlin.toString

@Composable
fun ShowLeavePopup(
    groupName: String,
    payment: Int,
    navController: NavController,
    dialogState: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {
    val canLeave = payment >= 0

    if(dialogState.value){
        Popup(
            alignment = Alignment.TopStart,
            properties = PopupProperties()
        ){
            ElevatedCard(
                modifier = modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = LightLightBlue
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LeaveGroupText(payment, groupName)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterHorizontally)
                    ) {
                        Button(
                            onClick = { dialogState.value = false },
                            colors = ButtonDefaults.buttonColors(DarkBlue),
                        ) {
                            Text(
                                text = "Cancel"
                            )
                        }
                        Button(
                            onClick = { navController.navigate(Destination.EDITGROUP.route) },
                            enabled = canLeave,
                            colors = ButtonDefaults.buttonColors(DarkBlue)
                        ) {
                            Text(
                                text = "Leave"
                            )
                        }
                    }
                }
            }
        }
    }


}

@Composable
fun LeaveGroupText(payment: Int, groupName: String){
    val (message, amountColor) = when {
        payment > 0 -> "Sure you want to leave \"$groupName\"? You will not receive" to greenCreditor
        payment < 0 -> "You cannot leave $groupName, you have to pay" to redInDebt
        else -> "Sure you want to leave $groupName?" to greenCreditor
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message, color = DarkBlue, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(35.dp))
        Text(
            text = "${payment.toString()} KR",
            color = amountColor,
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LeaveGroupCardPreview() {
    //LeaveGroupCard("hi", 0, onCancel = {}, onLeave = {})
}



