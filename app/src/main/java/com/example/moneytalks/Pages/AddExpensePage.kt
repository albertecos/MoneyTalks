package com.example.moneytalks.Pages

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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.moneytalks.Navigation.Destination
import com.example.moneytalks.ViewModel.AddExpenseViewModel
import com.example.moneytalks.ViewModel.GroupsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpensePage(
    navController: NavController,
    groupId: String,
    memberId: String,
    vm: AddExpenseViewModel = viewModel()
) {
//    val groupsVm: GroupsViewModel = viewModel()
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val isLoading = vm.isLoading
    val errorMessage = vm.errorMessage

    LaunchedEffect(memberId) {
        vm.fetchMembers(groupId)
    }


//    val groups = groupsVm.groups
//    val currentGroup = groups.firstOrNull{it.id == groupId}

//    val peopleInGroup = currentGroup?.members ?: emptyList()

//    var groupMembers = remember { mutableStateListOf<String>() }

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
                val amount = amount.toDoubleOrNull()
                if (amount == null) {
                    println("Invalid amount")
                    return@Button
                }
                vm.createExpenseForGroup(
                    groupId = groupId,
                    memberId = memberId,
                    amount = amount,
                    description = description,
                    onSuccess = {
                        println("$memberId added expense: $amount to this group: $description")
                        navController.navigate("${Destination.GROUPVIEW.route}/$groupId")
                    },
                    onError = {error -> println("Error: $error")}
                )
            },
            enabled = !isLoading,

            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(gradient, shape = RoundedCornerShape(12.dp))
        ) {
            Text(
                text = if(isLoading) "Saving..." else "Add expense",
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun AddExpensePreview() {
//    AddExpensePage()
//}

fun isNumeric(toCheck: String): Boolean {
    return toCheck.all { char -> char.isDigit() }
}