package com.foodhub.fooddelivery.data.models

data class AddToCartRequest(
    val restaurantId: String,
    val menuItemId: String,
    val quantity: Int
)
