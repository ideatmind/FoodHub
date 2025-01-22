package com.foodhub.fooddelivery.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.foodhub.fooddelivery.R
import com.foodhub.fooddelivery.data.FoodHubSession
import com.foodhub.fooddelivery.data.models.FoodItem
import com.foodhub.fooddelivery.ui.features.auth.AuthScreen
import com.foodhub.fooddelivery.ui.features.auth.login.LoginScreen
import com.foodhub.fooddelivery.ui.features.auth.signup.SignUpScreen
import com.foodhub.fooddelivery.ui.features.cart.CartScreen
import com.foodhub.fooddelivery.ui.features.cart.CartViewModel
import com.foodhub.fooddelivery.ui.features.food_item_details.FoodDetailsScreen
import com.foodhub.fooddelivery.ui.features.home.HomeScreen
import com.foodhub.fooddelivery.ui.features.restaurant_details.RestaurantDetailsScreen
import kotlin.reflect.typeOf

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NavGraph(navController: NavHostController, session: FoodHubSession, shouldShowBottomNav: MutableState<Boolean>) {
    val cartViewModel: CartViewModel = hiltViewModel()
    SharedTransitionLayout {
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
                shouldShowBottomNav.value = false
                SignUpScreen(navController)
            }
            composable<Auth> {
                shouldShowBottomNav.value = false
                AuthScreen(navController)
            }
            composable<Login> {
                shouldShowBottomNav.value = false
                LoginScreen(navController)
            }
            composable<Home> {
                shouldShowBottomNav.value = true
                HomeScreen(
                    navController = navController,
                    animatedVisibilityScope = this
                )
            }
            composable<Cart> {
                shouldShowBottomNav.value = true
                CartScreen(navController, cartViewModel)
            }
            composable<Notification> {
                shouldShowBottomNav.value = true
                Box() {

                }
            }
            composable<RestaurantDetails> {
                shouldShowBottomNav.value = false
                val route = it.toRoute<RestaurantDetails>()
                RestaurantDetailsScreen(
                    navController = navController,
                    name = route.restaurantName,
                    imageUrl = route.restaurantImage,
                    restaurantID = route.restaurantID,
                    animatedVisibilityScope = this
                )
            }
            composable<FoodDetails>(
                typeMap = mapOf(typeOf<FoodItem>() to foodItemNavType)
            ) {
                shouldShowBottomNav.value = false
                val route = it.toRoute<FoodDetails>()
                FoodDetailsScreen(
                    navController = navController,
                    foodItem = route.foodItem,
                    animatedVisibilityScope = this,
                    onItemAddedToCart = { cartViewModel.getCart() }
                )
            }
        }
    }
}
