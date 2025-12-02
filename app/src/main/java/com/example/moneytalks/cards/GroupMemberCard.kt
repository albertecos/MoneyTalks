package com.example.moneytalks.cards

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.moneytalks.apisetup.RetrofitClient
import com.example.moneytalks.dataclasses.GroupMember
import com.example.moneytalks.ui.theme.DarkBlue
import com.example.moneytalks.ui.theme.GreyColor
import com.example.moneytalks.ui.theme.greenCreditor
import com.example.moneytalks.ui.theme.redInDebt
import com.example.moneytalks.viewmodel.NotificationViewModel

@Composable
fun GroupMemberCard(
    member: GroupMember,
    groupId: String,
    balance: Double,
    notificationViewModel: NotificationViewModel = viewModel()
) {
    val context = LocalContext.current

    if(member.accepted) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp),
            colors = CardDefaults.cardColors(
                containerColor = GreyColor
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color.Gray),
                    contentAlignment = Alignment.Center

                ) {
                    // Profile picture
                    AsyncImage(
                        model = "${RetrofitClient.BASE_URL}image?path=${member.profile_picture}",
                        contentDescription = "Profile picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .matchParentSize()
                            .clip(CircleShape)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Text(
                        text = member.full_name,
                        fontSize = 18.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(7.dp))
                    if(balance >= 0){
                        Text(
                            text = balance.toString(),
                            fontSize = 18.sp,
                            color = greenCreditor,
                            fontWeight = FontWeight.SemiBold
                        )
                    } else
                    Text(
                        text = balance.toString(),
                        fontSize = 18.sp,
                        color = redInDebt,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    IconButton(onClick = {
                        if(balance >= 0){
                            Toast.makeText(context, "Cannot notify that user", Toast.LENGTH_LONG).show()
                        } else {
                            notificationViewModel.sendReminder(member.id, groupId)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Additional action",
                            tint = DarkBlue,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}