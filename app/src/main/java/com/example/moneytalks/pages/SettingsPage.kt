package com.example.moneytalks.pages

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
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.moneytalks.R
import com.example.moneytalks.navigation.Destination
import com.example.moneytalks.viewmodel.UserViewModel

// Dummy data class for user information
data class DummyUserData(val email: String, val password: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(
    navController: NavController,
    userVM: UserViewModel
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    //image launcher
    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    //permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { grantedAccess ->
        if (grantedAccess) {
            imageLauncher.launch("image/*")
        }
    }

    var notificationsEnabled by remember { mutableStateOf(true) }
    var userData by remember { mutableStateOf(DummyUserData("user@example.com", "password123")) }
    var showChangeEmailDialog by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }

    if (showChangeEmailDialog) {
        ChangeInfoDialog(
            title = "Change Email",
            currentValue = userData.email,
            onDismiss = { showChangeEmailDialog = false },
            onSave = { newEmail ->
                userData = userData.copy(email = newEmail)
                showChangeEmailDialog = false
            }
        )
    }

    if (showChangePasswordDialog) {
        ChangeInfoDialog(
            title = "Change Password",
            currentValue = "", // Don't show current password
            onDismiss = { showChangePasswordDialog = false },
            onSave = { newPassword ->
                userData = userData.copy(password = newPassword)
                showChangePasswordDialog = false
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
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
                        onClick = { permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES) },
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

                OutlinedButtonUI(text = "Change email", onClick = { showChangeEmailDialog = true })
                OutlinedButtonUI(
                    text = "Change password",
                    onClick = { showChangePasswordDialog = true })
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

        IconButton(
            onClick = {
                userVM.logout()
                navController.navigate(Destination.LOGIN.route)
            },
            modifier = Modifier
                .padding(5.dp)
                .size(48.dp)
                .align(Alignment.TopEnd)

        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ExitToApp,
                contentDescription = "Logout",
                tint = Color.Black,
                modifier = Modifier.size(35.dp)
            )
        }
    }
}

@Composable
fun OutlinedButtonUI(text: String, onClick: () -> Unit = {}) {

    val gradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFFBADFFF), Color(0xFF3F92DA))
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(vertical = 6.dp)
            .border(2.dp, gradient, RoundedCornerShape(20.dp))
            .background(Color(0xFFF8F9FA), RoundedCornerShape(20.dp))
    ) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues()
        ) {
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeInfoDialog(
    title: String,
    currentValue: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var text by remember { mutableStateOf(currentValue) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("New $title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { onSave(text) }) {
                        Text("Save")
                    }
                }
            }
        }
    }
}
