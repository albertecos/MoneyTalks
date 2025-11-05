package com.example.moneytalks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import com.example.moneytalks.ui.theme.LilyScriptOne
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.TopAppBarScrollBehavior


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(scrollBehavior: TopAppBarScrollBehavior) {
    val gradient = Brush.horizontalGradient(0f to Color(0xFFBADFFF), 1.0f to Color(0xFF3F92DA))

    CenterAlignedTopAppBar(
        modifier = Modifier.background(gradient).statusBarsPadding(),
        colors = TopAppBarDefaults.topAppBarColors(
            //containerColor = Color.Red,
            containerColor = Color.Transparent,
            titleContentColor = Color(0xFF000000)
        ),
        title = {
            Text(
                text = "Money Talk",
                fontFamily = LilyScriptOne,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = { TODO() }) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications"
                )
            }
        },
        scrollBehavior = scrollBehavior,
    )
}
