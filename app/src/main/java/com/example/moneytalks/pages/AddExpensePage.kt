package com.example.moneytalks.pages

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.moneytalks.MainActivity
import com.example.moneytalks.dataclasses.Group
import com.example.moneytalks.utilityclasses.NotificationUtil
import com.example.moneytalks.viewmodel.ExpenseViewModel
import com.example.moneytalks.viewmodel.NotificationViewModel
import com.example.moneytalks.viewmodel.UserViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpensePage(
    navController: NavController,
    group: Group? = null,
    userVm: UserViewModel,
    expenseVM: ExpenseViewModel = viewModel(),
    notificationVM: NotificationViewModel = viewModel()
) {
    if (group == null) {
        navController.navigateUp()
        return
    }
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var action by remember { mutableStateOf("") }

    val context = LocalContext.current
    val activity = context as? MainActivity

    val gradient = Brush.horizontalGradient(
        listOf(Color(0xFFBADFFF), Color(0XFF3F92DA))
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Amount input field
        OutlinedTextField(
            value = amount,
            onValueChange = { newValue ->
                if (isNumeric(newValue)) {
                    amount = newValue
                }
            },
            label = { Text("Amount") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        //Button
        Button(
            onClick = {
                if (amount != "" && description != "") {
                    println("Added expense: $amount to this group: $description")
                    val userId = userVm.currentUser.value!!.id
                    val amount = amount.toDouble()
                    val groupId = group.id
                    val ctx = context

                    expenseVM.createExpense(
                        context = ctx,
                        userId = userId,
                        groupId = groupId,
                        amount = amount,
                        description = description,
                        action = action,
                        onSuccess = {
                            Toast.makeText(ctx, "Expense added succesfully", Toast.LENGTH_SHORT)
                                .show()

                            val currentUser = userVm.currentUser.value
                            if (currentUser != null) {
                                val expenseTitle = "Expense added to your group!"
                                val expenseMessage =
                                    "${currentUser.username} added the expense: $description: $amount,-"
                                NotificationUtil.sendNotification(
                                    context,
                                    expenseTitle,
                                    expenseMessage
                                )
                            }
                            navController.navigateUp()
                        },
                        onNetworkRetryScheduled = {
                            Toast.makeText(
                                ctx,
                                "Network issue: we will try in the background",
                                Toast.LENGTH_LONG
                            ).show()

                            navController.navigateUp()
                        },
                        onError = { errorMsg ->
                            Toast.makeText(ctx, errorMsg, Toast.LENGTH_LONG).show()
                        }

                    )
                } else {
                    Toast.makeText(context, "Please fill out amount and description.", Toast.LENGTH_LONG)
                }
            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(gradient, shape = RoundedCornerShape(12.dp))
        ) {
            Text(
                text = "Add expense",
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}

fun isNumeric(toCheck: String): Boolean {
    return toCheck.all { char -> char.isDigit() }
}
