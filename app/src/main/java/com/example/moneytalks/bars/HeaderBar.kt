package com.example.moneytalks.bars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import com.example.moneytalks.ui.theme.LilyScriptOne
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.moneytalks.navigation.Destination
import com.example.moneytalks.navigation.NavIcon
import com.example.moneytalks.ui.theme.Black
import com.example.moneytalks.ui.theme.DarkBlue
import com.example.moneytalks.ui.theme.gradient


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier
    ) {

    CenterAlignedTopAppBar(
        modifier = modifier
            .statusBarsPadding()
            .background(gradient)
            .zIndex(1f),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent,
            titleContentColor = Black
        ),
        title = {
            Text(
                text = "Money Talk",
                fontFamily = LilyScriptOne,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        scrollBehavior = scrollBehavior,
    )
}
