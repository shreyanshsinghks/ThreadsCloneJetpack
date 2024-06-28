package com.hello.threads.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.hello.threads.itemView.ThreadItem
import com.hello.threads.model.ThreadModel
import com.hello.threads.model.UserModel
import com.hello.threads.utils.SharedPref
import com.hello.threads.viewmodel.HomeViewModel

@Composable
fun Home(navController: NavHostController) {
    val context = LocalContext.current
    val homeViewModel: HomeViewModel = viewModel()
    val threadAndUsers by homeViewModel.threadsAndUsers.observeAsState(null)

    LazyColumn {
        items(threadAndUsers ?: emptyList()) { pairs ->
            ThreadItem(
                thread = pairs.first,
                users = pairs.second,
                navController = navController,
                userId = FirebaseAuth.getInstance().currentUser!!.uid
            )
        }
    }


}

@Preview(showBackground = true)
@Composable
private fun PreviewHome() {
}