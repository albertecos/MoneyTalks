package com.example.moneytalks.Cards

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneytalks.ui.theme.LilyScriptOne

/*
GroupCard(
                        groupName = "Our Name",
                        modifier = Modifier.padding(innerPadding),
                        onDelete = { TODO() },
                        onEdit = { TODO() }
                    )
 */

@Composable
fun GroupCard(
    groupName: String,
    modifier: Modifier = Modifier,
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {}
){
    Card (
        modifier = modifier.width(290.dp).height(161.dp).padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFBADFFF),
            contentColor = Color(0xFF3C4780)
        )
    ) {
        Box(Modifier.fillMaxSize()){
            Row(
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ){

                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Edit"
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete"
                    )
                }
            }
            Text(
                text = groupName,
                modifier = modifier.align(Alignment.BottomStart).padding(16.dp),
                fontFamily = LilyScriptOne,
                fontSize = 28.sp
            )
        }
    }
}
@Composable
fun DeleteButton(){
    IconButton(
        onClick = {TODO()}
    ) {
        Icon(
            imageVector = Icons.Outlined.Delete,
            contentDescription = "Delete",
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GroupCardPreview(){
    GroupCard("The Name")
}