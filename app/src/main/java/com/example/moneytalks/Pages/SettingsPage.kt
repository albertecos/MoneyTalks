package com.example.moneytalks.Pages
// TODO: ADD FUNCTIONALITY TO PAGE  (CHANGE EMAIL,
//  CHANGE PASSWORD, TURN OFF NOTIFICATIONS)

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.moneytalks.R

@Composable
fun SettingsPage(modifier: Modifier = Modifier) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }
    var notificationsEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Settings Section
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(
                text = "Profile settings",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Profile Picture with Add Icon
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                // Profile Image
                Image(
                    painter = if (imageUri != null) {
                        rememberAsyncImagePainter(imageUri)
                    } else {
                        painterResource(id = R.drawable.profileplaceholder)
                    },
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color(0xFF4A90E2), CircleShape)
                        .background(Color(0xFFE0E0E0))
                )

                // Add Icon Overlay
                IconButton(
                    onClick = { launcher.launch("image/*") },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Photo",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButtonUI(text = "Change email")
            OutlinedButtonUI(text = "Change password")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Notification Settings Section
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(
                text = "Notification settings",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButtonUI(
                text = if (notificationsEnabled) "Turn off notifications" else "Turn on notifications",
                onClick = { notificationsEnabled = !notificationsEnabled }
            )
            OutlinedButtonUI(text = "...")
            OutlinedButtonUI(text = "...")
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun OutlinedButtonUI(text: String, onClick: () -> Unit = {}) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .height(48.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color.Black,
            containerColor = Color(0xFFF8F9FA)
        ),
        shape = RoundedCornerShape(12.dp),
    ) {
        Text(text = text, textAlign = TextAlign.Center, fontSize = 16.sp)
    }
}
