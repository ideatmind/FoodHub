package com.foodhub.fooddelivery.ui.features.auth.signup

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.viewModelScope
import com.foodhub.fooddelivery.data.FoodApi
import com.foodhub.fooddelivery.data.FoodHubSession
import com.foodhub.fooddelivery.data.models.SignUpRequest
import com.foodhub.fooddelivery.ui.features.auth.BaseAuthViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class SignUpViewModel @Inject constructor(val foodApi: FoodApi, val session: FoodHubSession) : BaseAuthViewModel() {

    private val _uiState = MutableStateFlow<SignUpEvent>(SignUpEvent.Nothing)
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<SignUpNavigationEvent>(replay = 1)
    val navigationEvent = _navigationEvent.asSharedFlow()


    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    fun onEmailChange(email : String) {
        _email.value = email
    }

    fun onPasswordChange(password : String) {
        _password.value =password
    }

    fun onNameChange(name : String) {
        _name.value = name
    }

    fun onSignUpClick() {
        viewModelScope.launch {
            _uiState.value = SignUpEvent.Loading
            try {
                val response = foodApi.signUp(
                    SignUpRequest(
                        name = name.value,
                        email = email.value,
                        password = password.value
                    )
                )
                if(response.token.isNotEmpty()) {
                    _uiState.value = SignUpEvent.Success
                    session.storeToken(response.token)
                    _navigationEvent.tryEmit(SignUpNavigationEvent.NavigateToHome)
                }
            } catch (e : Exception) {
                Log.d("signup", e.toString())
                _uiState.value = SignUpEvent.Error
            }
        }
    }

    fun onGoogleSignInClick(context: ComponentActivity) {
        try {
            initiateGoogleLogin(foodApi, context)
        } catch (e: CancellationException) {
            _uiState.value = SignUpEvent.Cancelled
        } catch (e : Exception) {
            _uiState.value = SignUpEvent.Error
        }
    }


    fun onFacebookClicked(context: ComponentActivity) {
        try {
            initiateFacebookLogin(foodApi, context)
        } catch (e: CancellationException) {
            _uiState.value = SignUpEvent.Cancelled
        } catch (e : Exception) {
            _uiState.value = SignUpEvent.Error
        }
    }

    fun onLoginClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(SignUpNavigationEvent.NavigateToLogin)
        }
    }

    sealed class SignUpNavigationEvent {
        object NavigateToLogin : SignUpNavigationEvent()
        object NavigateToHome : SignUpNavigationEvent()
    }

    sealed class SignUpEvent {
        object Nothing : SignUpEvent()
        object Success: SignUpEvent()
        object Error : SignUpEvent()
        object Loading : SignUpEvent()
        object Cancelled : SignUpEvent()
    }

    override fun loading() {
        viewModelScope.launch {
            _uiState.value = SignUpEvent.Loading
        }
    }

    override fun onGoogleError(msg: String) {
        viewModelScope.launch {
            _uiState.value = SignUpEvent.Error
        }
    }

    override fun onFacebookError(msg: String) {
        viewModelScope.launch {
            _uiState.value = SignUpEvent.Error
        }
    }

    override fun onSocialLoginSuccess(token: String) {
        viewModelScope.launch {
            session.storeToken(token)
            _uiState.value = SignUpEvent.Success
            _navigationEvent.emit(SignUpNavigationEvent.NavigateToHome)
        }
    }

}