package com.foodhub.fooddelivery.data

import com.foodhub.fooddelivery.data.models.AuthResponse
import com.foodhub.fooddelivery.data.models.CategoriesResponse
import com.foodhub.fooddelivery.data.models.LoginRequest
import com.foodhub.fooddelivery.data.models.OAuthRequest
import com.foodhub.fooddelivery.data.models.RestaurantsResponse
import com.foodhub.fooddelivery.data.models.SignUpRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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
}
