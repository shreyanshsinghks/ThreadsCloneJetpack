package com.hello.threads.screens

import android.content.pm.PackageManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.contentValuesOf
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.hello.threads.R
import com.hello.threads.itemView.ThreadItem
import com.hello.threads.model.UserModel
import com.hello.threads.navigation.Routes
import com.hello.threads.utils.SharedPref
import com.hello.threads.viewmodel.AuthViewModel
import com.hello.threads.viewmodel.ProfileViewModel

@Composable
fun Profile(navController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)
    val threads by profileViewModel.threads.observeAsState(null)
    val context = LocalContext.current

    val user =
        UserModel(
            name = SharedPref.getName(context) ?: "",
            userName = SharedPref.getUserName(context) ?: "",
            imageUrl = SharedPref.getImage(context) ?: ""
        )


//    adding below line
    if(firebaseUser != null){
        profileViewModel.fetchThreads(firebaseUser!!.uid)
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



                SharedPref.getName(context)?.let { cont ->
                    Text(
                        text = cont,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 24.sp,
                        modifier = Modifier.constrainAs(text) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        }
                    )
                }

                Image(
                    painter = rememberAsyncImagePainter(model = SharedPref.getImage(context = context)),
                    contentDescription = null,
                    modifier = Modifier
                        .constrainAs(logo) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                        }
                        .size(120.dp)
                        .clip(CircleShape), contentScale = ContentScale.Crop
                )

                SharedPref.getUserName(context)?.let { sharedUsername ->
                    Text(
                        text = sharedUsername,
                        fontSize = 20.sp,
                        modifier = Modifier.constrainAs(userName) {
                            top.linkTo(text.bottom)
                            start.linkTo(parent.start)
                        }
                    )
                }
                SharedPref.getBio(context = context)?.let {
                    Text(
                        text = it,
                        fontSize = 20.sp,
                        modifier = Modifier.constrainAs(bio) {
                            top.linkTo(userName.bottom)
                            start.linkTo(parent.start)
                        }
                    )
                }

                SharedPref.getUserName(context)?.let {
                    Text(
                        text = "3 Followers",
                        fontSize = 20.sp,
                        modifier = Modifier.constrainAs(followers) {
                            top.linkTo(bio.bottom)
                            start.linkTo(parent.start)
                        }
                    )
                }

                SharedPref.getUserName(context)?.let {
                    Text(
                        text = "0 Following",
                        fontSize = 20.sp,
                        modifier = Modifier.constrainAs(following) {
                            top.linkTo(followers.bottom)
                            start.linkTo(parent.start)
                        }
                    )
                }

                ElevatedButton(onClick = { authViewModel.logout() },
                    modifier = Modifier.constrainAs(button) {
                        top.linkTo(following.bottom)
                        start.linkTo(parent.start)
                    }) {
                    Text(text = "Logout")
                }
            }
        }

        items(threads ?: emptyList()) { pair ->
            SharedPref.getUserName(context)?.let {
                ThreadItem(
                    thread = pair,
                    users = user,
                    navController = navController,
                    userId = it
                )
            }
        }
    }


}