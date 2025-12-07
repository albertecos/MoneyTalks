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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import com.example.moneytalks.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.moneytalks.apisetup.RetrofitClient
import com.example.moneytalks.cards.BalanceBox
import com.example.moneytalks.cards.GroupMembersListCard
import com.example.moneytalks.dataclasses.Group
import com.example.moneytalks.dataclasses.GroupMember
import com.example.moneytalks.navigation.Destination
import com.example.moneytalks.pages.PossibleActions.*
import com.example.moneytalks.popup.PaymentPopup
import com.example.moneytalks.ui.theme.DarkBlue
import com.example.moneytalks.ui.theme.DarkGrey
import com.example.moneytalks.ui.theme.GreyColor
import com.example.moneytalks.ui.theme.LilyScriptOne
import com.example.moneytalks.ui.theme.MoneyTalksTheme
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
    var showMemberCard by remember { mutableStateOf(false) }
    val currentUser = userVm.currentUser //for own bubble
    val currentUserId = currentUser.value?.id ?: "00cacc5b-55a3-4958-b551-b07668168ca6"
    val expenses = expenseVM.expenseHistory.value

    LaunchedEffect(group.id) {
        expenseVM.getExpenseHistoryByGroupId(group.id)
        balanceVm.fetchBalance(group.id, currentUserId)
    }

    val balanceNum = balanceVm.memberBalances.get(currentUserId)
    var balance = - (balanceNum ?: 0.0)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Row (
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp)
                .height(60.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            BalanceBox(balance, modifier = Modifier.weight(1f))

            IconButton(
                onClick = {
                    showMemberCard = true
                },
                modifier = Modifier.weight(0.2f)
            ) {
                Icon(
                    imageVector = Icons.Outlined.AccountBox,
                    contentDescription = "Group Member List",
                    tint = DarkBlue,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        if(showMemberCard) {
            Box(
                modifier = Modifier
                    .height(350.dp)
                    .width(350.dp)
                    .background(Color.Transparent),
                contentAlignment = Alignment.Center
            ){
                GroupMembersListCard(
                    navController = navController,
                    group = group,
                    balanceVm,
                    onClose = { showMemberCard = false }
                )
            }

        }

        // Transactions
        Column(modifier = modifier
            .weight(1f)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
        ) {
            expenses.forEach { expense ->

                val membersToFullName = group.members.associate { it.userId to it.full_name }
                val membersToPfp = group.members.associate { it.userId to it.profile_picture }
                val ifMyself = expense.userId == currentUserId
                val actionEnum = mapActionToEnum(expense.action)

                fun getMemberByAllIds(id: String): GroupMember {
                    return group.members.firstOrNull {|it.userId == id}
                }

                if (ifMyself) {
                    OwnBubble(actionEnum, expense.amount)
                } else {
                    FriendsBubble(
                        membersToFullName[expense.userId] ?:"Unknown user",
                        membersToPfp[expense.userId] ?: "337d5322-930a-472e-8c0f-ebd04cc9b3ef.jpg", //the default img?
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
                    balanceVm.payOwed(
                        groupId = group.id,
                        userId = currentUserId
                    )
                    showPaymentPopup = false
                }
            )
        }
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
fun FriendsBubble(username: String, pfpString: String, action: PossibleActions, value: Double) {
    val formattedPrice = String.format("%.2f", Math.abs(value))

    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = "${RetrofitClient.BASE_URL}image?path=${pfpString}",
            contentDescription = "Profile picture",
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
        Image(painter = painterResource(R.drawable.payment), "payment icon")
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

@Preview(
    name = "GroupView preview",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun GroupViewPreview() {
    val navController = rememberNavController()

    val members = listOf(
        GroupMember(
            id = "user_1",
            userId = "userId_1",
            username = "alice",
            full_name = "Alice Andersen",
            email = "alice@example.com",
            password = "",
            profile_picture = "",
            accepted = true
        ),
        GroupMember(
            id = "user_2",
            userId = "userId_2",
            username = "bob",
            full_name = "Bob Hansen",
            email = "bob@example.com",
            password = "",
            profile_picture = "",
            accepted = true
        ),
        GroupMember(
            id = "user_3",
            userId = "userId_3",
            username = "charlie",
            full_name = "Charlie Jensen",
            email = "charlie@example.com",
            password = "",
            profile_picture = "",
            accepted = false
        ),
        GroupMember(
            id = "user_4",
            userId = "userId_4",
            username = "bob",
            full_name = "Bob Hansen",
            email = "bob@example.com",
            password = "",
            profile_picture = "",
            accepted = true
        ),
        GroupMember(
            id = "user_5",
            userId = "userId_5",
            username = "charlie",
            full_name = "Charlie Jensen",
            email = "charlie@example.com",
            password = "",
            profile_picture = "",
            accepted = false
        )
    )

    val previewGroup = Group(
        id = "group_123",
        name = "Holiday Trip",
        members = members
    )

    // If your VMs have default constructors / Hilt preview support,
    // this will work. Otherwise replace with your own fake / stub implementations.
    val userVm: UserViewModel = viewModel()
    val expenseVm: ExpenseViewModel = viewModel()
    val balanceVm: BalanceViewModel = viewModel()

    MoneyTalksTheme {
        GroupView(
            navController = navController,
            group = previewGroup,
            userVm = userVm,
            expenseVM = expenseVm,
            balanceVm = balanceVm
        )
    }
}

