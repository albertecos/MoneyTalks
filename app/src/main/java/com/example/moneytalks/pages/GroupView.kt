package com.example.moneytalks.pages

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import com.example.moneytalks.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moneytalks.cards.BalanceBox
import com.example.moneytalks.dataclasses.Group
import com.example.moneytalks.navigation.Destination
import com.example.moneytalks.pages.PossibleActions.*
import com.example.moneytalks.popup.PaymentPopup
import com.example.moneytalks.ui.theme.DarkGrey
import com.example.moneytalks.ui.theme.GreyColor
import com.example.moneytalks.ui.theme.blueDebtFree
import com.example.moneytalks.ui.theme.blueDebtFreeV2
import kotlin.Unit

@Composable
//TODO: API CALL TO GROUP
fun GroupView(
    navController: NavController,
    group: Group,
    modifier: Modifier = Modifier) {

    var showPaymentPopup by remember { mutableStateOf(false) }
    var expenseValue by remember { mutableStateOf(10.20) } //TODO: API CALL TO VALUE

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
        BalanceBox(expenseValue)

        // Transactions
        Column(modifier = modifier
            .weight(1f)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
        ) {
            //TODO: API CALL TO EXPENSE LOG
            FriendsBubble("Alberte", R.drawable.babygator,PAID, 500)
            FriendsBubble("Asta", R.drawable.arghhh,PAID, 200)
            OwnBubble(REMOVE_EXPENSE,150)
            FriendsBubble("Maria", R.drawable.batman,ADD_EXPENSE, 200)
            OwnBubble(ADD_EXPENSE,300)
            OwnBubble(PAID,300)
        }

        AllButtonsBar(
            navController,
            onPayClick = {showPaymentPopup = true },
            group
        )

        if (showPaymentPopup) {
            PaymentPopup(
                value = expenseValue,
                onDismiss = {showPaymentPopup = false},
                onConfirm = {
                    if (expenseValue > 0.0) {
                        expenseValue = 0.0;
                    }
                    showPaymentPopup = false
                }
            )
        }
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
fun AllButtonsBar(
    navController: NavController,
    onPayClick:() -> Unit,
    group: Group
) {
    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp),
        horizontalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterHorizontally),
    )  {
        AddExpenseButton(navController, group)
        TransactionButton(onPayClick)
    }
}

@Composable
fun TransactionButton(onPayClick:() -> Unit ) {
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
        onClick = { onPayClick() } //TODO
    ) {
        Image(painter = painterResource(R.drawable.payment),
            "payment icon")
    }
}

@Composable
fun AddExpenseButton(navController: NavController, group: Group) {
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
        onClick = {
            navController.currentBackStackEntry?.savedStateHandle?.set("group", group)
            navController.navigate(Destination.ADDEXPENSE.route)} //TODO, give her the needed parameters?
    ) {
        Image(painter = painterResource(R.drawable.add),
            "Add expense icon"
        )
    }
}

fun returnTextForAction(value: PossibleActions): String {
    return when (value) {
        PAID -> "Paid "
        ADD_EXPENSE -> "Added expense of "
        REMOVE_EXPENSE -> "Removed expense of "
    }
}

enum class PossibleActions {
    PAID,
    ADD_EXPENSE,
    REMOVE_EXPENSE
}

