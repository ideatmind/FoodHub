package com.foodhub.fooddelivery.data.models

data class CartResponse(
    val checkoutDetails: CheckoutDetails,
    val items: List<CartItem>
)