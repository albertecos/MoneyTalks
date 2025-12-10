package com.example.moneytalks.bars

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.moneytalks.ui.theme.Black
import com.example.moneytalks.ui.theme.gradient
import com.example.moneytalks.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    title: String? = null
) {

    CenterAlignedTopAppBar(
        modifier = Modifier
            .background(gradient)
            .statusBarsPadding()
            .zIndex(1f),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent,
            titleContentColor = Black
        ),
        title = {
            if (title != null) {
                Text(
                    title,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.mtlogo),
                    "Logo",
                    modifier = Modifier
                        .size(90.dp)
                )
            }
        },
        scrollBehavior = scrollBehavior,
    )
}
