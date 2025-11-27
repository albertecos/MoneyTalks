package com.example.moneytalks.pages

import android.annotation.SuppressLint
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.moneytalks.cards.BalanceBox
import com.example.moneytalks.dataclasses.Group
import com.example.moneytalks.navigation.Destination
import com.example.moneytalks.pages.PossibleActions.*
import com.example.moneytalks.popup.PaymentPopup
import com.example.moneytalks.ui.theme.DarkGrey
import com.example.moneytalks.ui.theme.GreyColor
import com.example.moneytalks.ui.theme.LilyScriptOne
import com.example.moneytalks.ui.theme.blueDebtFree
import com.example.moneytalks.ui.theme.blueDebtFreeV2
import com.example.moneytalks.viewmodel.BalanceViewModel
import com.example.moneytalks.viewmodel.ExpenseViewModel
import com.example.moneytalks.viewmodel.UserViewModel
import kotlin.Unit

@Composable
fun GroupView(
    navController: NavController,
    group: Group,
    userVm: UserViewModel,
    expenseVM: ExpenseViewModel = viewModel(),
    balanceVm: BalanceViewModel = viewModel(),
    modifier: Modifier = Modifier
) {

    var showPaymentPopup by remember { mutableStateOf(false) }
    val currentUser = userVm.currentUser //for own bubble
    val currentUserId = currentUser.value?.id ?: "00cacc5b-55a3-4958-b551-b07668168ca6"
    val expenses = expenseVM.expenseHistory.value

    LaunchedEffect(group.id) {
        expenseVM.getExpenseHistoryByGroupId(group.id)
        balanceVm.fetchBalance(group.id, currentUserId)
    }

    val balanceNum = balanceVm.balance.value
    var balance = balanceNum?.balance?.toDouble() ?: 0.0

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        GroupBar(group.name)
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(4.dp)
        )
        BalanceBox(balance)

        // Transactions
        Column(modifier = modifier
            .weight(1f)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
        ) {
            expenses.forEach { expense ->

                val ifMyself = expense.userId == currentUserId
                val actionEnum = mapActionToEnum(expense.action)

                if (ifMyself) {
                    OwnBubble(actionEnum, expense.amount)
                } else {
                    FriendsBubble(
                        //TODO: Display correct userID, is this through.... group members?
                        "PLAYER",
                        R.drawable.babygator,
                        actionEnum,
                        expense.amount)
                }
            }
        }

        AllButtonsBar(
            navController,
            onPayClick = {showPaymentPopup = true },
            group
        )

        if (showPaymentPopup) {
            PaymentPopup(
                value = balance,
                onDismiss = {showPaymentPopup = false},
                onConfirm = {
                    if (balance > 0.0) {
                        balance = 0.0
                    }
                    showPaymentPopup = false
                }
            )
        }
    }
}

@Composable
fun GroupBar(groupName: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(blueDebtFree)
            .padding(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally

        ) {
        Text(groupName, color = blueDebtFreeV2, fontFamily = LilyScriptOne)
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun OwnBubble(action: PossibleActions, value: Double) {
    val formattedPrice = String.format("%.2f", Math.abs(value))

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
            Text(returnTextForAction(action) + formattedPrice + ".-", color = DarkGrey)
        }
    }
}


@SuppressLint("DefaultLocale")
@Composable
fun FriendsBubble(username: String, pfpResID: Int, action: PossibleActions, value: Double) {
    val formattedPrice = String.format("%.2f", Math.abs(value))

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
            Text(returnTextForAction(action) + formattedPrice + ".-", color = DarkGrey)
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
            navController.navigate(Destination.ADDEXPENSE.route)
        } //TODO, give her the needed parameters?
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

fun mapActionToEnum(action: String): PossibleActions {
    return when (action.uppercase()) {
        "PAYMENT" -> PAID
        "EXPENSE" -> ADD_EXPENSE
        "REMOVE_EXPENSE" -> REMOVE_EXPENSE //whats the name of this one...
        else -> {ADD_EXPENSE}
    }
}

enum class PossibleActions {
    PAID,
    ADD_EXPENSE,
    REMOVE_EXPENSE
}

