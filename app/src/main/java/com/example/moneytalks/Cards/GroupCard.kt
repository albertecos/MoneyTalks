package com.example.moneytalks.Cards

import android.service.autofill.OnClickAction
import android.widget.PopupWindow
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import com.example.moneytalks.Navigation.Destination
import com.example.moneytalks.Navigation.NavIcon
import com.example.moneytalks.ui.theme.DarkBlue
import com.example.moneytalks.ui.theme.LightBlue
import com.example.moneytalks.ui.theme.LilyScriptOne


@Composable
fun GroupCard(
    groupName: String,
    navController: NavController,
    payment: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { navController.navigate(Destination.GROUPVIEW.route) },
) {
    var showLeavePopup = remember { mutableStateOf(false) }

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
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {

                IconButton(onClick = { navController.navigate(Destination.EDITGROUP.route) }) {
                    Icon(
                        imageVector = NavIcon.EDITGROUP.icon,
                        contentDescription = NavIcon.EDITGROUP.destination.contentDescription
                    )
                }
                IconButton(onClick = { showLeavePopup.value = true }) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete"
                    )
                }
            }
            Text(
                text = groupName,
                modifier = modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                fontFamily = LilyScriptOne,
                fontSize = 28.sp
            )
        }
    }
    ShowLeavePopup(groupName, payment, navController, showLeavePopup)
}


@Preview(showBackground = true)
@Composable
private fun GroupCardPreview() {

}