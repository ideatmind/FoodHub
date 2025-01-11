package com.foodhub.fooddelivery.ui.features.auth

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.foodhub.fooddelivery.data.FoodApi
import com.foodhub.fooddelivery.data.auth.GoogleAuthUIProvider
import com.foodhub.fooddelivery.data.models.AuthResponse
import com.foodhub.fooddelivery.data.models.OAuthRequest
import com.foodhub.fooddelivery.data.remote.safeApiCall
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

abstract class BaseAuthViewModel : ViewModel(){
    private val googleAuthUIProvider = GoogleAuthUIProvider()
    private lateinit var callbackManager : CallbackManager

    var error: String = ""
    var errorDescription: String = ""

    abstract fun loading()
    abstract fun onGoogleError(msg: String)
    abstract fun onFacebookError(msg: String)
    abstract fun onSocialLoginSuccess(token: String)

    fun initiateGoogleLogin(foodApi: FoodApi, context: ComponentActivity) {
        viewModelScope.launch {
            try {
                loading()
                val response = googleAuthUIProvider.signIn(
                    context,
                    CredentialManager.create(context)
                )
                val request = OAuthRequest(
                    token = response.token,
                    provider = "google"
                )
                val res = foodApi.oAuth(request)
                if (res.token.isNotEmpty()) {
                    onSocialLoginSuccess(res.token)
                } else {
                    onGoogleError("Failed")
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                onGoogleError(e.message ?: "Unknown error")
            }
        }
    }


    fun initiateFacebookLogin(foodApi: FoodApi, context: ComponentActivity) {
        loading()

        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    viewModelScope.launch {
                        val request = OAuthRequest(
                            token = loginResult.accessToken.token,
                            provider = "facebook"
                        )
                        val res = foodApi.oAuth(request)
                        if (res.token.isNotEmpty()) {
                            Log.d("LoginViewModel", "onFacebookClick : ${res.token}")
                            onSocialLoginSuccess(res.token)
                        }else {
                            onFacebookError("Failed")
                        }
                    }
                }

                override fun onCancel() {
                    onFacebookError("Cancelled")
                }

                override fun onError(exception: FacebookException) {
                    Log.d("Facebook", "Exception : $exception")
                    onFacebookError("Failed")
                }
            })

        LoginManager.getInstance().logInWithReadPermissions(
            context,
            callbackManager,
            listOf("public_profile", "email")
        )
    }
}