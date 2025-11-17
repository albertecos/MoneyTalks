package com.example.moneytalks.Pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import com.example.moneytalks.DataClasses.User

@Preview(
    showBackground = true,
)
@Composable
fun EditGroupPage() {
    var groupName by remember { mutableStateOf("") }
    var addPeople by remember { mutableStateOf("") }

    val gradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFFBADFFF), Color(0xFF3F92DA))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text = "Edit group",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        //Group name field
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .border(2.dp, gradient, RoundedCornerShape(20.dp))
        ){
            OutlinedTextField(
                value = groupName,
                onValueChange = { groupName = it },
                label = { Text("Group name") },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF0F0F0), RoundedCornerShape(20.dp)) // match grey background + rounded corners
                    .padding(0.dp), // remove extra padding
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,   // Transparent so Box handles outer border
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    cursorColor = Color.DarkGray,
                    focusedLabelColor = Color.Gray,
                    unfocusedLabelColor = Color.Gray
                ),
                singleLine = true
            )
        }

        //Add people field
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .border(2.dp, gradient, RoundedCornerShape(20.dp))
        ){
            OutlinedTextField(
                value = addPeople,
                onValueChange = { addPeople = it },
                label = { Text("Add people...") },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF0F0F0), RoundedCornerShape(20.dp)) // match grey background + rounded corners
                    .padding(0.dp), // remove extra padding
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,   // Transparent so Box handles outer border
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    cursorColor = Color.DarkGray,
                    focusedLabelColor = Color.Gray,
                    unfocusedLabelColor = Color.Gray
                ),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Members:",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        MemberListElement(User(id = "placeholder", full_name = "John Doe", username = "johndoe", email = "john@example.com", password = "password"))
        Spacer(modifier = Modifier.height(16.dp))

        // Edit group button
        Box(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(48.dp)
                .border(2.dp, gradient, RoundedCornerShape(20.dp))
                .background(Brush.horizontalGradient(listOf(Color(0XFFBADFFF).copy(alpha=0.5f), Color(0xFF3F92DA).copy(alpha=0.5f))), RoundedCornerShape(20.dp))
        ) {
            Button(
                onClick = { /* handle save edits */ },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues()
            ) {
                Text(
                    "Save",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}