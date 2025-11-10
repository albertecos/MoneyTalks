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
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import com.example.moneytalks.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.moneytalks.Cards.BalanceBox
import com.example.moneytalks.Pages.PossibleActions.*
import com.example.moneytalks.ui.theme.DarkGrey
import com.example.moneytalks.ui.theme.GreyColor
import com.example.moneytalks.ui.theme.blueDebtFree
import com.example.moneytalks.ui.theme.blueDebtFreeV2

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
                .padding(10.dp)
        )
        BalanceBox(0.20) //TODO - skal kalde API :)

        // Transactions
        Column(modifier = modifier
            .weight(1f)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
        ) {
            FriendsBubble("Alberte", R.drawable.babygator,PAY, 500)
            FriendsBubble("Asta", R.drawable.arghhh,PAY, 200)
            OwnBubble(REMOVE_EXPENSE,150)
            FriendsBubble("Maria", R.drawable.batman,ADD_EXPENSE, 200)
            OwnBubble(ADD_EXPENSE,300)
            OwnBubble(PAY,300)
        }

        AllButtonsBar()
    }
}

@Composable
fun OwnBubble(action: PossibleActions, value: Int) {
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
            Text(returnTextForAction(action) + value + ".-", color = DarkGrey)
        }
    }
}


@Composable
fun FriendsBubble(username: String, pfpResID: Int, action: PossibleActions, value: Int) {
    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(pfpResID),
            "lulul",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
            )
        Column(modifier = Modifier
            .align(Alignment.CenterVertically)
            .padding(8.dp),
        ) {
            Text(username, fontWeight = FontWeight.Bold)
            Text(returnTextForAction(action) + value + ".-", color = DarkGrey)
        }
    }
}

@Composable
fun AllButtonsBar() {
    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp),
        horizontalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterHorizontally),
    )  {
        AddExpenseButton()
        TransactionButton()
    }
}

@Composable
fun TransactionButton() {
    Button(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .border(
                3.dp,
                Brush.horizontalGradient(0f to (blueDebtFreeV2), 1.0f to blueDebtFree),
                CircleShape
            ),
        colors = ButtonDefaults.buttonColors(GreyColor),
        onClick = { "Payment!" } //TODO
    ) {
        Image(painter = painterResource(R.drawable.payment),
            "payment icon")
    }
}

@Composable
fun AddExpenseButton() {
    //var buttonText by remember { mutableStateOf("Add expense to group?") }
    Button(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .border(
                3.dp,
                Brush.horizontalGradient(0f to (blueDebtFreeV2), 1.0f to blueDebtFree),
                CircleShape
            ),
        colors = ButtonDefaults.buttonColors(GreyColor),
        onClick = { "Added Expense!" } //TODO
    ) {
        Image(painter = painterResource(R.drawable.add),
            "Add expense icon"
        )
    }
}

fun returnTextForAction(value: PossibleActions): String {
    return when (value) {
        PAY -> "Payed "
        ADD_EXPENSE -> "Added expense of "
        REMOVE_EXPENSE -> "Removed expense of "
    }
}

enum class PossibleActions {
    PAY,
    ADD_EXPENSE,
    REMOVE_EXPENSE
}

