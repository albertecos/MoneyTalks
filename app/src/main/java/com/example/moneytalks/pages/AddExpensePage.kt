package com.example.moneytalks.pages

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import com.example.moneytalks.dataclasses.GroupMember
import com.example.moneytalks.utilityclasses.NotificationUtil
import com.example.moneytalks.viewmodel.ExpenseViewModel
import com.example.moneytalks.viewmodel.UserViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpensePage(
    navController: NavController,
    group: Group? = null,
    userVm: UserViewModel,
    expenseVM: ExpenseViewModel = viewModel(),
) {
    if (group == null) {
        navController.navigateUp()
        return
    }
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var action by remember { mutableStateOf("") }

    val groupMembers = remember(group.members) {
        mutableStateListOf<GroupMember>().apply {
            addAll(group.members)
        }
    }

    var errorMessage by remember { mutableStateOf("") }

    var isExpanded by remember { mutableStateOf(false) }
    val chosenMembers: SnapshotStateList<GroupMember> = remember {
        val list = mutableStateListOf<GroupMember>()
        val currentUser = userVm.currentUser.value
        if (currentUser != null) {
            group.members.firstOrNull() { it.username == currentUser.username }
                ?.let { currentMember ->
                    list.add(currentMember)
                }
        }
        list

    }

    val context = LocalContext.current
    val activity = context as? MainActivity

    val gradient = Brush.horizontalGradient(
        listOf(Color(0xFFBADFFF), Color(0XFF3F92DA))
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
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

//        select people
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = !isExpanded }
        ) {
            OutlinedTextField(
                readOnly = true,
                value = chosenMembers.lastOrNull()?.username ?: "",
                onValueChange = {},
                label = { Text("Select members") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = isExpanded
                    )
                },
                modifier = Modifier
                    .menuAnchor(
                        MenuAnchorType.PrimaryNotEditable,
                        true
                    )
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )

            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }
            ) {
                groupMembers
                    .filter { it !in chosenMembers }
                    .forEach { member ->
                        DropdownMenuItem(
                            text = { Text(member.username) },
                            onClick = {
                                chosenMembers.add(member)
                                isExpanded = false
                            }
                        )
                    }
            }
        }

        if (chosenMembers.isNotEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                chosenMembers.forEach { member ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var splitAmount = 0.00

                        if(amount != "") {
                            splitAmount = amount.toDouble() / chosenMembers.size.toDouble()
                        }
                        val formatted = String.format("%.2f", splitAmount)
                        Text(
                            text = "${member.username}                    $formatted",
                            color = Color.Gray,
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize
                        )

                        IconButton(
                            onClick = { chosenMembers.remove(member) },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove member",
                                tint = Color.Gray
                            )
                        }
                    }
                }
            }
        }

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
                if (amount != "" && description != "" && chosenMembers.isNotEmpty()) {
                    errorMessage = ""
                    println("Added expense: $amount to this group: $description")
                    val userId = userVm.currentUser.value!!.id
                    val amount = amount.toDouble()
                    val groupId = group.id
                    val ctx = context

                    val currentUser = userVm.currentUser.value
                    val currentMember =  groupMembers.firstOrNull { it.userId == currentUser?.id }
                    val currentMemberId = currentMember?.id ?: run{"no current member's ID were found"}

                    expenseVM.createExpense(
                        context = ctx,
                        userId = userId,
                        memberId = currentMemberId,
                        groupId = groupId,
                        amount = amount,
                        description = description,
                        action = action,
                        onSuccess = {
                            Toast.makeText(ctx, "Expense added succesfully", Toast.LENGTH_SHORT)
                                .show()

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
                        },
                        payers = chosenMembers
                    )
                } else {
                    Toast.makeText(context, "Please fill out amount and description.", Toast.LENGTH_LONG).show()
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
        if (errorMessage.isNotEmpty()) {
            Text(
                errorMessage,
                color = Color.Red,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

fun isNumeric(toCheck: String): Boolean {
    return toCheck.all { char -> char.isDigit() }
}
