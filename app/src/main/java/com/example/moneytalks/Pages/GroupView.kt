package com.example.moneytalks.Pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.moneytalks.Cards.BalanceBox
import com.example.moneytalks.Cards.BalanceStatus
import com.example.moneytalks.ui.theme.DarkGrey

@Preview
@Composable
fun GroupView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {

        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 40.dp, bottom = 20.dp)
        )
        BalanceBox(0.20) //TODO - skal kalde API :)

        // Transactions
        Column(modifier = modifier
            .weight(1f)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
        ) {
            FriendsBubble("Alberte", "Paid ", 500)
            FriendsBubble("Asta", "Paid ", 200)
            OwnBubble("Paid ",150)
            FriendsBubble("Maria", "Added expense of ", 200)
            FriendsBubble("Bernard", "Added expense of ", 300)
            FriendsBubble("Idriis", "Paid ", 500)
            OwnBubble("Added expense of ",300)
            OwnBubble("Added expense of ",300)
            OwnBubble("Added expense of ",300)
            OwnBubble("Added expense of ",300)

        }

        AllButtonsBar()
    }
}

@Composable
fun OwnBubble(text: String, value: Int) {
    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Column(modifier = Modifier
            .padding(8.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text("You", fontWeight = FontWeight.Bold)
            Text(text + value + ".-", color = DarkGrey)
        }
    }
}

@Composable
fun FriendsBubble(username: String, text: String, value: Int) {
    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Column(modifier = Modifier
            .align(Alignment.CenterVertically)
            .padding(8.dp),
        ) {
            Text(username, fontWeight = FontWeight.Bold)
            Text(text + value + ".-", color = DarkGrey)
        }
    }
}

@Composable
fun AllButtonsBar() {
    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp),
    )  {
        AddExpenseButton()
        TransactionButton()
    }
}

@Composable
fun TransactionButton() {
    var buttonText by remember { mutableStateOf("Pay your part to the group?") }
    Button(
        onClick = { buttonText = "Payed!" }
    ) {
        Text(buttonText)
    }
}

@Composable
fun AddExpenseButton() {
    var buttonText by remember { mutableStateOf("Add expense to group?") }
    Button(
        onClick = { buttonText = "Added!" }
    ) {
        Text(buttonText)
    }
}

