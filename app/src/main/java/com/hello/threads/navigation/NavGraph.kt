package com.hello.threads.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hello.threads.screens.AddThreads
import com.hello.threads.screens.BottomNav
import com.hello.threads.screens.Home
import com.hello.threads.screens.Login
import com.hello.threads.screens.Notification
import com.hello.threads.screens.OtherUsers
import com.hello.threads.screens.Profile
import com.hello.threads.screens.Register
import com.hello.threads.screens.Search
import com.hello.threads.screens.SplashScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.Splash.routes) {
        composable(Routes.Splash.routes) {
            SplashScreen(navController)
        }

        composable(Routes.Home.routes) {
            Home(navController)
        }

        composable(Routes.Notification.routes) {
            Notification()
        }
        composable(Routes.Search.routes) {
            Search(navController)
        }
        composable(Routes.AddThread.routes) {
            AddThreads(navController)
        }
        composable(Routes.Profile.routes) {
            Profile(navController)
        }
        composable(Routes.BottomNav.routes) {
            BottomNav(navController = navController)
        }
        composable(Routes.Login.routes) {
            Login(navController)
        }
        composable(Routes.Register.routes) {
            Register(navController)
        }
        composable(Routes.OtherUsers.routes) {
            val data = it.arguments!!.getString("data")
            OtherUsers(navController = navController, uid =  data!!)
        }
    }
}