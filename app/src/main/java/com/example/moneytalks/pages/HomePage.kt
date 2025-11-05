package com.example.moneytalks.pages

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.moneytalks.cards.GroupCard


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