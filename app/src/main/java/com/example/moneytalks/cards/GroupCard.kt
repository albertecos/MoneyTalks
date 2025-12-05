package com.example.moneytalks.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.moneytalks.dataclasses.Group
import com.example.moneytalks.navigation.Destination
import com.example.moneytalks.navigation.NavIcon
import com.example.moneytalks.popup.ShowLeavePopup
import com.example.moneytalks.ui.theme.DarkBlue
import com.example.moneytalks.ui.theme.GreyColor
import com.example.moneytalks.ui.theme.LightBlue
import com.example.moneytalks.ui.theme.blueDebtFree
import com.example.moneytalks.ui.theme.greenCreditor
import com.example.moneytalks.ui.theme.redInDebt
import com.example.moneytalks.viewmodel.BalanceViewModel


@Composable
fun GroupCard(
    group: Group,
    memberId: String,
    navController: NavController,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {
        navController.currentBackStackEntry?.savedStateHandle?.set("group", group)
        navController.navigate(Destination.GROUPVIEW.route)
    },
) {
    var showLeavePopup = remember { mutableStateOf(false) }

    val balanceVm: BalanceViewModel = viewModel(key = group.id)

    LaunchedEffect(group.id) {
        balanceVm.fetchBalance(group.id, memberId)
    }

    val balanceNum = balanceVm.memberBalances.get(memberId)
    var balance = - (balanceNum ?: 0.0)

    val balanceStatus = calculatingStatus(balance)

    val colorChange = when (balanceStatus) {
        BalanceStatus.OweMoney -> (redInDebt)
        BalanceStatus.RecieveMoney -> (greenCreditor)
        BalanceStatus.Clear -> (blueDebtFree)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(161.dp)
            .padding(16.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = LightBlue,
            contentColor = DarkBlue
        )
    ) {
        Box(Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = group.name,
                    fontWeight = FontWeight.Bold,
                    modifier = modifier
                        .padding(8.dp),
                    fontSize = 28.sp
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(onClick = {
                        navController.currentBackStackEntry?.savedStateHandle?.set("group", group)
                        navController.navigate(Destination.EDITGROUP.route)
                    }) {
                        Icon(
                            imageVector = NavIcon.EDITGROUP.icon,
                            contentDescription = NavIcon.EDITGROUP.destination.contentDescription
                        )
                    }
                    IconButton(onClick = {
                        showLeavePopup.value = true
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Delete"
                        )
                    }
                }
            }
            val formattedPrice = String.format("%.2f", Math.abs(balance))

            Box(
                modifier = modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .size(height = 30.dp, width = 125.dp)
                    .background(GreyColor, shape = RoundedCornerShape(25.dp)),
                contentAlignment = Alignment.Center
                ){
                Text(
                    "${formattedPrice}",
                    fontWeight = FontWeight.Bold,
                    color = colorChange
                )
            }
        }
    }
    ShowLeavePopup(group.id, memberId, group.name, navController, showLeavePopup)
}

@Composable
fun calculatingStatus(value: Double): BalanceStatus {
    return when {
        value > 0 -> BalanceStatus.OweMoney
        value < 0 -> BalanceStatus.RecieveMoney
        else -> BalanceStatus.Clear
    }
}