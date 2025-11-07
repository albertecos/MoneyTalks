package com.example.moneytalks.Pages

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.moneytalks.Cards.GroupCard


@Composable
fun HomePage(modifier: Modifier = Modifier){
    GroupCard(
        groupName = "Our name"
    )

}

@Preview
@Composable
fun HomePagePreview(){
    HomePage()
}