package com.foodhub.fooddelivery.ui.navigation

import com.foodhub.fooddelivery.data.models.FoodItem
import kotlinx.serialization.Serializable

interface NavRoute

@Serializable
object Login: NavRoute

@Serializable
object Auth: NavRoute

@Serializable
object SignUp: NavRoute

@Serializable
object Home: NavRoute

@Serializable
data class RestaurantDetails(
    val restaurantID: String,
    val restaurantName: String,
    val restaurantImage: String
): NavRoute

@Serializable
data class FoodDetails(
    val foodItem: FoodItem
): NavRoute

@Serializable
object Cart: NavRoute

@Serializable
object Notification: NavRoute