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

@Preview(
    showBackground = true,
)


@Composable
fun LoginScreen() {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
            text = "Login",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        //username field
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .border(2.dp, gradient, RoundedCornerShape(20.dp))
        ){
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )
        }

        // Password field
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .border(2.dp, gradient, RoundedCornerShape(20.dp))
        ) {
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                shape = RoundedCornerShape(20.dp),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
            )
        }

        /*
        //Forgot password - skal slettes?
        Text(
            text = "Forgot password?",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 8.dp, bottom = 24.dp)
        )
         */

        Spacer(modifier = Modifier.height(16.dp))


        // Login button
        Box(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(48.dp)
                .border(2.dp, gradient, RoundedCornerShape(20.dp))
                .background(Brush.horizontalGradient(listOf(Color(0XFFBADFFF).copy(alpha=0.5f), Color(0xFF3F92DA).copy(alpha=0.5f))), RoundedCornerShape(20.dp))
        ) {
            Button(
                onClick = { /* handle login */ },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues()
            ) {
                Text(
                    "Login",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Create account button
        Box(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(48.dp)
                .border(2.dp, gradient, RoundedCornerShape(20.dp))
                .background(Brush.horizontalGradient(listOf(Color(0XFFBADFFF).copy(alpha=0.5f), Color(0xFF3F92DA).copy(alpha=0.5f))), RoundedCornerShape(20.dp))
        ) {
            Button(
                onClick = { /* handle login */ },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues()
            ) {
                Text(
                    "Create Account",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }

    }
}