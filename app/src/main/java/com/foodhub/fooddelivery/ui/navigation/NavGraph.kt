package com.foodhub.fooddelivery.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.foodhub.fooddelivery.data.FoodHubSession
import com.foodhub.fooddelivery.ui.features.auth.AuthScreen
import com.foodhub.fooddelivery.ui.features.auth.login.LoginScreen
import com.foodhub.fooddelivery.ui.features.auth.signup.SignUpScreen
import com.foodhub.fooddelivery.ui.features.home.HomeScreen

@Composable
fun NavGraph(navController: NavHostController, session: FoodHubSession) {

    NavHost(
        navController = navController, startDestination = if(session.getToken() != null) Home else Auth,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(450)
            ) + fadeIn(animationSpec = tween(450))
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(450)
            ) + fadeOut(animationSpec = tween(450))
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(450)
            ) + fadeIn(animationSpec = tween(450))
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(450)
            ) + fadeOut(animationSpec = tween(450))
        }
    ) {
        composable<SignUp> {
            SignUpScreen(navController)
        }
        composable<Auth> {
            AuthScreen(navController)
        }
        composable<Login> {
            LoginScreen(navController)
        }
        composable<Home> {
            HomeScreen(navController)
        }
    }
}