package com.foodhub.fooddelivery.data

import com.foodhub.fooddelivery.data.models.AddToCartRequest
import com.foodhub.fooddelivery.data.models.AddToCartResponse
import com.foodhub.fooddelivery.data.models.AuthResponse
import com.foodhub.fooddelivery.data.models.CartResponse
import com.foodhub.fooddelivery.data.models.CategoriesResponse
import com.foodhub.fooddelivery.data.models.FoodItemResponse
import com.foodhub.fooddelivery.data.models.GenericMessageResponse
import com.foodhub.fooddelivery.data.models.LoginRequest
import com.foodhub.fooddelivery.data.models.OAuthRequest
import com.foodhub.fooddelivery.data.models.RestaurantsResponse
import com.foodhub.fooddelivery.data.models.SignUpRequest
import com.foodhub.fooddelivery.data.models.UpdateCartItemRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FoodApi {

    @POST("/auth/signup")
    suspend fun signUp(@Body request: SignUpRequest) : AuthResponse

    @POST("/auth/login")
    suspend fun signIn(@Body request: LoginRequest) : AuthResponse

    @POST("/auth/oauth")
    suspend fun oAuth(@Body request: OAuthRequest) : AuthResponse

    @GET("/categories")
    suspend fun getCategories() : Response<CategoriesResponse>

    @GET("/restaurants")
    suspend fun getRestaurants(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Response<RestaurantsResponse>

    @GET("/restaurants/{restaurantID}/menu")
    suspend fun getFoodItemsForRestaurant(@Path("restaurantID") restaurantID : String): Response<FoodItemResponse>

    @POST("/cart")
    suspend fun addToCart(@Body request : AddToCartRequest): Response<AddToCartResponse>

    @GET("/cart")
    suspend fun getCart() : Response<CartResponse>

    @PATCH("/cart")
    suspend fun updateCart(@Body request: UpdateCartItemRequest) : Response<GenericMessageResponse>

    @DELETE("/cart/{cartItemId}")
    suspend fun deleteCartItem(@Path("cartItemId") cartItemId: String) : Response<GenericMessageResponse>
}
