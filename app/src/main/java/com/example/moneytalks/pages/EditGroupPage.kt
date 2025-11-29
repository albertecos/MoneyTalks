package com.example.moneytalks.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moneytalks.dataclasses.GroupMember
import com.example.moneytalks.dataclasses.User
import com.example.moneytalks.viewmodel.GroupsViewModel

@Composable
fun EditGroupPage(group: com.example.moneytalks.dataclasses.Group? = null, navController: NavController, userVM: com.example.moneytalks.viewmodel.UserViewModel) {
    if (group == null) {
        navController.navigateUp()
        return
    }
    var groupName by remember { mutableStateOf(group.name) }
    var addPeople by remember { mutableStateOf("") }
    var searchResults by remember {
        mutableStateOf<List<User>>(
            emptyList()
        )
    }
    var groupVM: GroupsViewModel = viewModel()

    val gradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFFBADFFF), Color(0xFF3F92DA))
    )

    var currentUser = userVM.currentUser.value

    val peopleList = remember(currentUser?.id) {
        mutableStateListOf<GroupMember>()
    }

    val scrollState = rememberScrollState()

    // LaunchedEffect triggered whenever addPeople changes
    LaunchedEffect(addPeople) {
        if (addPeople.isEmpty()) {
            searchResults = emptyList()
            return@LaunchedEffect
        }
        val results = userVM.searchUsers(addPeople) ?: emptyList()
        searchResults = results
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text = "Edit group",
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
            if (group.members.any { it.id == member.id }) {
                continue // Skip if already in the group
            }
            SearchedMemberListElement(member = member, onAddClick = {
                peopleList.add(
                    GroupMember(
                        id = member.id,
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
        for (member in peopleList) {
            MemberListElement(
                member = member,
                additionalActionIcon = Icons.Default.Delete,
                onAdditionalActionClick = {
                    peopleList.removeAll { it.id == member.id }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        for (groupMember in group.members) {
            MemberListElement(member = groupMember)
            Spacer(modifier = Modifier.height(8.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Edit group button
        Box(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(48.dp)
                .border(2.dp, gradient, RoundedCornerShape(20.dp))
                .background(Brush.horizontalGradient(listOf(Color(0XFFBADFFF).copy(alpha=0.5f), Color(0xFF3F92DA).copy(alpha=0.5f))), RoundedCornerShape(20.dp))
        ) {
            Button(
                onClick = { 
                    groupVM.editGroup(userVM.currentUser.value?.id ?: "", group.id, groupName, peopleList.map { it.id })
                    navController.navigateUp()
                },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues()
            ) {
                Text(
                    "Save",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}