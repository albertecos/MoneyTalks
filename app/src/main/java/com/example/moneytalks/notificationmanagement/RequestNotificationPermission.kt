package com.example.moneytalks.notificationmanagement

import android.Manifest
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun requestNotificationPermission() {
    val permissionState =
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
    val context = LocalContext.current


//    val permissionLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.RequestPermission()
//    ) { grantedAccess ->
//        if (grantedAccess) {
//            //set notification permission to true
//        }
//    }
}
private var requestPermissionLauncher: ActivityResultLauncher<String> =
    registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (!isGranted) {
            Log.d("POST_NOTIFICATION_PERMISSION", "USER DENIED PERMISSION")
        } else {
            Log.d("POST_NOTIFICATION_PERMISSION", "USER GRANTED PERMISSION")
        }
    }
