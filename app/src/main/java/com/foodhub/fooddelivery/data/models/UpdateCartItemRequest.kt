package com.foodhub.fooddelivery.data.models

data class UpdateCartItemRequest(
    val cartItemId: String,
    val quantity: Int
)
