package com.hello.threads.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.hello.threads.itemView.ThreadItem
import com.hello.threads.navigation.Routes
import com.hello.threads.utils.SharedPref
import com.hello.threads.viewmodel.AuthViewModel
import com.hello.threads.viewmodel.ProfileViewModel

@Composable
fun OtherUsers(navController: NavHostController, uid: String) {
    val authViewModel: AuthViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)
    val threads by profileViewModel.threads.observeAsState(null)
    val users by profileViewModel.users.observeAsState(null)

    val followersList by profileViewModel.followerList.observeAsState(null)
    val followingList by profileViewModel.followingList.observeAsState(null)


    profileViewModel.fetchThreads(uid)
    profileViewModel.fetchUser(uid)
    profileViewModel.getFollowers(uid)
    profileViewModel.getFollowing(uid)

    var currentUserId = ""
    if (FirebaseAuth.getInstance().currentUser != null) {
        currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
    }


    LaunchedEffect(firebaseUser) {
        if (firebaseUser == null) {
            navController.navigate(Routes.Login.routes) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }

    LazyColumn {
        item {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                val (text, logo, userName, bio, followers, following, button) = createRefs()



                Text(
                    text = users!!.name,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    modifier = Modifier.constrainAs(text) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                )

                Image(
                    painter = rememberAsyncImagePainter(model = users!!.imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .constrainAs(logo) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                        }
                        .size(120.dp)
                        .clip(CircleShape), contentScale = ContentScale.Crop
                )

                Text(
                    text = users!!.userName,
                    fontSize = 20.sp,
                    modifier = Modifier.constrainAs(userName) {
                        top.linkTo(text.bottom)
                        start.linkTo(parent.start)
                    }
                )

                Text(
                    text = users!!.bio,
                    fontSize = 20.sp,
                    modifier = Modifier.constrainAs(bio) {
                        top.linkTo(userName.bottom)
                        start.linkTo(parent.start)
                    }
                )


                Text(
                    text = "${followersList?.size} Followers",
                    fontSize = 20.sp,
                    modifier = Modifier.constrainAs(followers) {
                        top.linkTo(bio.bottom)
                        start.linkTo(parent.start)
                    }
                )


                Text(
                    text = "${followingList?.size} Following",
                    fontSize = 20.sp,
                    modifier = Modifier.constrainAs(following) {
                        top.linkTo(followers.bottom)
                        start.linkTo(parent.start)
                    }
                )

                ElevatedButton(onClick = {
                    if (currentUserId != "") {
                        profileViewModel.followUser(
                            uid,
                            currentUserId
                        )
                    }
                },
                    modifier = Modifier.constrainAs(button) {
                        top.linkTo(following.bottom)
                        start.linkTo(parent.start)
                    }) {
                    Text(
                        text = if (followersList != null && followersList!!.isNotEmpty() && followersList!!.contains(
                                currentUserId
                            )
                        ) "Following" else "Follow"
                    )
                }
            }
        }
        if (threads != null && users != null) {

            items(threads ?: emptyList()) { pair ->
                ThreadItem(
                    thread = pair,
                    users = users!!,
                    navController = navController,
                    userId = users!!.userName
                )
            }
        }
    }
}