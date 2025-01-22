package com.foodhub.fooddelivery.data.models

data class CartItem(
    val addedAt: String,
    val id: String,
    val menuItemId: FoodItem,
    var quantity: Int,
    val restaurantId: String,
    val userId: String
)