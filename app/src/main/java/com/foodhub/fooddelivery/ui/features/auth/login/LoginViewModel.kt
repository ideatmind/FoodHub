package com.foodhub.fooddelivery.ui.features.auth.login

import androidx.activity.ComponentActivity
import androidx.lifecycle.viewModelScope
import com.foodhub.fooddelivery.data.FoodApi
import com.foodhub.fooddelivery.data.FoodHubSession
import com.foodhub.fooddelivery.data.models.LoginRequest
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
class LoginViewModel @Inject constructor(private val foodApi: FoodApi, val session: FoodHubSession) : BaseAuthViewModel() {
    override fun loading() {
        viewModelScope.launch {
            _uiState.value = LoginEvent.Loading
        }
    }

    override fun onGoogleError(msg: String) {
        viewModelScope.launch {
            errorDescription= msg
            error = "Google login failed"
            _uiState.value = LoginEvent.Error
        }
    }

    override fun onFacebookError(msg: String) {
        viewModelScope.launch {
            errorDescription = msg
            error = "Facebook login failed"
            _uiState.value = LoginEvent.Error
        }
    }

    override fun onSocialLoginSuccess(token: String) {
        viewModelScope.launch {
            session.storeToken(token)
            _uiState.value = LoginEvent.Success
            _navigationEvent.emit(LoginNavigationEvent.NavigateToHome)
        }
    }

    private val _uiState = MutableStateFlow<LoginEvent>(LoginEvent.Nothing)
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<LoginNavigationEvent>(replay = 1)
    val navigationEvent = _navigationEvent.asSharedFlow()


    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    fun onEmailChange(email : String) {
        _email.value = email
    }

    fun onPasswordChange(password : String) {
        _password.value =password
    }

    fun onLoginClick() {
        viewModelScope.launch {
            _uiState.value = LoginEvent.Loading
            try {
                val response = foodApi.signIn(
                    LoginRequest(
                        email = email.value,
                        password = password.value
                    )
                )
                if(response.token.isNotEmpty()) {
                    _uiState.value = LoginEvent.Success
                    session.storeToken(response.token)
                    _navigationEvent.tryEmit(LoginNavigationEvent.NavigateToHome)
                }
            } catch (e : Exception) {
                _uiState.value = LoginEvent.Error
            }
        }
    }

    fun onGoogleSignInClick(context: ComponentActivity) {
        try {
            initiateGoogleLogin(foodApi, context)
        } catch (e: CancellationException) {
            _uiState.value = LoginEvent.Cancelled
        } catch (e : Exception) {
            _uiState.value = LoginEvent.Error
        }
    }


    fun onFacebookClicked(context: ComponentActivity) {
        try {
            initiateFacebookLogin(foodApi, context)
        } catch (e: CancellationException) {
            _uiState.value = LoginEvent.Cancelled
        } catch (e : Exception) {
            _uiState.value = LoginEvent.Error
        }
    }

    fun onSignUpClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(LoginNavigationEvent.NavigateToSignUp)
        }
    }

    sealed class LoginNavigationEvent {
        data object NavigateToSignUp : LoginNavigationEvent()
        data object NavigateToHome : LoginNavigationEvent()
    }

    sealed class LoginEvent {
        data object Nothing : LoginEvent()
        data object Success: LoginEvent()
        data object Error : LoginEvent()
        data object Loading : LoginEvent()
        data object Cancelled : LoginEvent()
    }

}