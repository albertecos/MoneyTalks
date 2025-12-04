package com.example.moneytalks.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.moneytalks.dataclasses.GroupMember
import com.example.moneytalks.dataclasses.User
import com.example.moneytalks.viewmodel.UserViewModel
import com.example.moneytalks.apisetup.RetrofitClient


@Composable
fun CreateGroup(navController: NavController, userVM: UserViewModel) {
    var groupName by remember { mutableStateOf("") }
    var addPeople by remember { mutableStateOf("") }
    var searchResults by remember {
        mutableStateOf<List<User>>(
            emptyList()
        )
    }
    var groupVM: com.example.moneytalks.viewmodel.GroupsViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel()

    // add current user to the group by default
    val currentUser = userVM.currentUser.value


    val peopleList = remember(currentUser?.id) {
        mutableStateListOf<GroupMember>().apply {
            if (currentUser != null) {
                add(
                    GroupMember(
                        id = currentUser.id,
                        userId = currentUser.id, //for now
                        full_name = currentUser.full_name,
                        profile_picture = currentUser.profile_picture,
                        username = currentUser.username,
                        email = currentUser.email,
                        password = currentUser.password,
                        accepted = true
                    )
                )
            }
        }
    }
    
    // LaunchedEffect triggered whenever addPeople changes
    LaunchedEffect(addPeople) {
        if (addPeople.isEmpty()) {
            searchResults = emptyList()
            return@LaunchedEffect
        }
        val results = userVM.searchUsers(addPeople) ?: emptyList()
        searchResults = results
    }

    val gradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFFBADFFF), Color(0xFF3F92DA))
    )

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text = "Create group",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        //Group name field
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .border(2.dp, gradient, RoundedCornerShape(20.dp))
        ){
            OutlinedTextField(
                value = groupName,
                onValueChange = { groupName = it },
                label = { Text("Group name") },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF0F0F0), RoundedCornerShape(20.dp)) // match grey background + rounded corners
                    .padding(0.dp), // remove extra padding
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,   // Transparent so Box handles outer border
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    cursorColor = Color.DarkGray,
                    focusedLabelColor = Color.Gray,
                    unfocusedLabelColor = Color.Gray
                ),
                singleLine = true
            )
        }

        //Add people field
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .border(2.dp, gradient, RoundedCornerShape(20.dp))
        ){
            OutlinedTextField(
                value = addPeople,
                onValueChange = { 
                    addPeople = it
                },
                label = { Text("Add people...") },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF0F0F0), RoundedCornerShape(20.dp)) // match grey background + rounded corners
                    .padding(0.dp), // remove extra padding
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,   // Transparent so Box handles outer border
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    cursorColor = Color.DarkGray,
                    focusedLabelColor = Color.Gray,
                    unfocusedLabelColor = Color.Gray
                ),
                singleLine = true
            )
        }

        for (member in searchResults) {
            if (peopleList.any { it.id == member.id }) {
                continue // Skip if already in the group
            }
            SearchedMemberListElement(member = member, onAddClick = {
                peopleList.add(
                    GroupMember(
                        id = member.id,
                        userId = member.id,
                        full_name = member.full_name,
                        profile_picture = member.profile_picture,
                        username = member.username,
                        email = member.email,
                        password = member.password,
                        accepted = false
                    )
                )
                addPeople = ""
            })
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Members:",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        for (groupMember in peopleList) {
            if (groupMember.id == currentUser?.id) {
                MemberListElement(member = groupMember);
            } else {
                MemberListElement(
                    member = groupMember,
                    additionalActionIcon = Icons.Default.Delete,
                    onAdditionalActionClick = {
                        peopleList.removeAll { it.id == groupMember.id }
                    }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Create group button
        Box(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(48.dp)
                .border(2.dp, gradient, RoundedCornerShape(20.dp))
                .background(Brush.horizontalGradient(listOf(Color(0XFFBADFFF).copy(alpha=0.5f), Color(0xFF3F92DA).copy(alpha=0.5f))), RoundedCornerShape(20.dp))
        ) {
            Button(
                onClick = { 
                    val userId = userVM.currentUser.value?.id
                    if (userId != null) {
                        groupVM.createGroup(userId, groupName, peopleList.map { it.id })
                        navController.navigateUp()
                    } else {
                        navController.navigate(com.example.moneytalks.navigation.Destination.LOGIN.route)
                    }
                },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues()
            ) {
                Text(
                    "Create group",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}


@Composable
fun SearchedMemberListElement(member: User, onAddClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Profile picture
        AsyncImage(
            model = "${RetrofitClient.BASE_URL}image?path=${member.profile_picture}",
            contentDescription = "Profile picture",
            modifier = Modifier
                .size(40.dp)
                .background(Color.Gray, shape = RoundedCornerShape(20.dp))
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = member.full_name,
            fontSize = 18.sp,
            color = Color.Black
        )

        Text(
            text = " (@${member.username})",
            fontSize = 14.sp,
            color = Color.DarkGray,
            modifier = Modifier.padding(start = 8.dp)
        )

        Box(
            modifier = Modifier
                .weight(1f),
            contentAlignment = Alignment.CenterEnd
        ) {
            Button(
                onClick = onAddClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add member",
                    tint = Color.Blue,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun MemberListElement(member: GroupMember, additionalActionIcon: ImageVector? = null, onAdditionalActionClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Profile picture
        AsyncImage(
            model = "${RetrofitClient.BASE_URL}image?path=${member.profile_picture}",
            contentDescription = "Profile picture",
            modifier = Modifier
                .size(40.dp)
                .background(Color.Gray, shape = RoundedCornerShape(20.dp))
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = member.full_name,
            fontSize = 18.sp,
            color = Color.Black
        )

        Text(
            text = " (@${member.username})",
            fontSize = 14.sp,
            color = Color.DarkGray,
            modifier = Modifier.padding(start = 8.dp)
        )

        Text(
            text = if (member.accepted) "Accepted" else "Pending",
            fontSize = 14.sp,
            color = if (member.accepted) Color.Green else Color.Gray,
            modifier = Modifier.padding(start = 8.dp)
        )

        if (additionalActionIcon == null) {
            return
        }
        Box(
            modifier = Modifier
                .weight(1f),
            contentAlignment = Alignment.CenterEnd
        ) {
            Button(
                onClick = onAdditionalActionClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Icon(
                    imageVector = additionalActionIcon,
                    contentDescription = "Additional action",
                    tint = Color.Blue,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}