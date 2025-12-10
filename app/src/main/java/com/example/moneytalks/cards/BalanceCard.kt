package com.example.moneytalks.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneytalks.ui.theme.GreyColor
import com.example.moneytalks.ui.theme.blueDebtFree
import com.example.moneytalks.ui.theme.blueDebtFreeV2
import com.example.moneytalks.ui.theme.greenCreditor
import com.example.moneytalks.ui.theme.greenCreditorV2
import com.example.moneytalks.ui.theme.redInDebt
import com.example.moneytalks.ui.theme.redInDebtV2

@Composable
fun BalanceBox(value: Double, modifier: Modifier) {
    val balanceStatus = calculateStatus(value)

    val borderColor = when (balanceStatus) {
        BalanceStatus.OweMoney -> (Brush.horizontalGradient(0f to (redInDebtV2), 1.0f to redInDebt))
        BalanceStatus.RecieveMoney -> (Brush.horizontalGradient(0f to (greenCreditorV2), 1.0f to greenCreditor))
        BalanceStatus.Clear -> (Brush.horizontalGradient(0f to (blueDebtFreeV2), 1.0f to blueDebtFree))
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(height = 60.dp, width = 300.dp)
            .background(GreyColor, shape = RoundedCornerShape(25.dp))
            .border(
                width = 3.dp,
                brush = borderColor,
                shape = RoundedCornerShape(25.dp),
            )

    ) {
        BalanceStatusText(value, balanceStatus)
    }
}

@Composable
fun BalanceStatusText(value: Double, balanceStatus: BalanceStatus) {
    val formattedPrice = String.format("%.2f", Math.abs(value))

    val message = when (balanceStatus) {
        BalanceStatus.OweMoney -> "You owe -" + formattedPrice + ".-"
        BalanceStatus.RecieveMoney -> "You'll recieve " + formattedPrice + ".-"
        BalanceStatus.Clear -> "You are all clear!"
    }

    Text(
        message,
        fontSize = 18.sp,
        color = Color.Black,
        fontWeight = FontWeight.Bold
    )
}

enum class BalanceStatus {
    OweMoney, RecieveMoney, Clear
}

@Composable
fun calculateStatus(value: Double): BalanceStatus {
    return when {
        value > 0 -> BalanceStatus.OweMoney
        value < 0 -> BalanceStatus.RecieveMoney
        else -> BalanceStatus.Clear
    }
}

