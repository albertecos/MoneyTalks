package com.example.moneytalks.bars

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moneytalks.navigation.Destination
import com.example.moneytalks.navigation.NavIcon
import com.example.moneytalks.ui.theme.Black
import com.example.moneytalks.ui.theme.DarkBlue
import com.example.moneytalks.ui.theme.gradient
import com.example.moneytalks.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController, scrollBehavior: TopAppBarScrollBehavior) {

    CenterAlignedTopAppBar(
        modifier = Modifier.background(gradient).statusBarsPadding(),
        colors = TopAppBarDefaults.topAppBarColors(
            //containerColor = Color.Red,
            containerColor = Color.Transparent,
            titleContentColor = Black
        ),
        title = {
            Image(
                painter = painterResource(R.drawable.mtlogo),
                "Logo",
                modifier = Modifier
                    .size(90.dp)
            )
        },
        scrollBehavior = scrollBehavior,
    )
}
