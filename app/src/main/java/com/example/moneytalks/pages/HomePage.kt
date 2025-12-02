package com.example.moneytalks.pages

import androidx.compose.foundation.layout.Arrangement
import com.example.moneytalks.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.example.moneytalks.cards.GroupCard
import com.example.moneytalks.navigation.Destination
import com.example.moneytalks.viewmodel.GroupsViewModel


@Composable
fun HomePage(
    memberId: String,
    navController: NavController,
    modifier: Modifier = Modifier
){
    val vm: GroupsViewModel = viewModel()
    val context = LocalContext.current


    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (android.os.Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()




    LaunchedEffect(memberId) {
        vm.fetchGroups(memberId)
    }

    val groups = vm.groups
    Box(modifier = modifier.fillMaxSize()) { // <-- top-level container

        // Main content
        if (groups.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(R.drawable.cactusstatus)
                        .crossfade(true)
                        .build(),
                    contentDescription = "No groups animation",
                    imageLoader = imageLoader,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)

                )

            }
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                groups.forEach { group ->
                    GroupCard(
                        group = group,
                        memberId = memberId,
                        navController = navController
                    )
                }
            }
        }

    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        FloatingActionButton(
            onClick = {
                navController.navigate(Destination.CREATEGROUP.route)
            },
            modifier = Modifier
                .align(androidx.compose.ui.Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create Group"
            )
        }
    }
}}