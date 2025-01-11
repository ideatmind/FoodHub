package com.foodhub.fooddelivery.ui.features.auth.login

import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.foodhub.fooddelivery.R
import com.foodhub.fooddelivery.ui.FoodHubTextField
import com.foodhub.fooddelivery.ui.GroupSocialButtons
import com.foodhub.fooddelivery.ui.navigation.Auth
import com.foodhub.fooddelivery.ui.navigation.Home
import com.foodhub.fooddelivery.ui.navigation.Login
import com.foodhub.fooddelivery.ui.navigation.SignUp
import com.foodhub.fooddelivery.ui.theme.AppColor
import com.foodhub.fooddelivery.ui.theme.AppWhite
import com.foodhub.fooddelivery.ui.theme.poppinsFontFamily
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(navController: NavController,viewModel: LoginViewModel = hiltViewModel()) {
    var passwordVisible by remember { mutableStateOf(false) }
    val email = viewModel.email.collectAsStateWithLifecycle()
    val password = viewModel.password.collectAsStateWithLifecycle()
    val errorMessage = remember { mutableStateOf<String?>(null) }
    val loading = remember { mutableStateOf(false) }

    val uiState = viewModel.uiState.collectAsState()
    when(uiState.value) {
        is LoginViewModel.LoginEvent.Error -> {
            loading.value = false
            errorMessage.value = "Something went wrong"
        }
        is LoginViewModel.LoginEvent.Cancelled -> {
            loading.value = false
            navController.navigate(Login)
        }
        is LoginViewModel.LoginEvent.Loading -> {
            loading.value = true
            errorMessage.value = null
        }
        else -> {
            loading.value = false
            errorMessage.value = null
        }
    }
    LaunchedEffect(true) {
        viewModel.navigationEvent.collectLatest { event ->
            when (event) {
                is LoginViewModel.LoginNavigationEvent.NavigateToHome -> {
                    navController.navigate(Home) {
                        popUpTo(Auth) {
                            inclusive = true
                        }
                    }
                }
                is LoginViewModel.LoginNavigationEvent.NavigateToSignUp -> {
                    navController.navigate(SignUp) {
                        popUpTo(Login) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(R.drawable.sign_up), contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.FillBounds)


        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally)
        {
            Text(
                stringResource(R.string.login_title),
                fontSize = 38.sp,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(Modifier.height(22.dp))

            FoodHubTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                shape = RoundedCornerShape(25.dp),
                value = email.value,
                onValueChange = {viewModel.onEmailChange(it)},
                label = {
                    Text(text = stringResource(R.string.email), color = Color.DarkGray)
                }
            )
            Spacer(Modifier.height(8.dp))
            FoodHubTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                shape = RoundedCornerShape(25.dp),
                value = password.value,
                onValueChange = {viewModel.onPasswordChange(it)},
                label = {
                    Text(text = stringResource(R.string.password), color = Color.DarkGray)
                },
                colors = OutlinedTextFieldDefaults.colors().copy(
                    focusedTrailingIconColor = Color.DarkGray,
                    focusedIndicatorColor = AppColor,
                    unfocusedIndicatorColor = Color.LightGray.copy(alpha = 0.4f),
                    focusedTextColor = Color.DarkGray,
                    cursorColor = AppColor,
                    unfocusedTextColor = Color.Gray
                ),
                trailingIcon = {
                    val icon = if (passwordVisible) {
                        Icons.Filled.Visibility
                    } else {
                        Icons.Filled.VisibilityOff
                    }

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = icon, contentDescription = null)
                    }
                },
                visualTransformation = if (passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                }
            )

            Spacer(Modifier.height(8.dp))
            TextButton(
                onClick = {

                }
            ) {
                Text(
                    text = stringResource(id = R.string.forgot_password),
                    color = AppColor,
                    fontFamily = poppinsFontFamily
                )
            }
            Spacer(Modifier.height(8.dp))

            Text(errorMessage.value ?: "", color = Color.Red)
            // sign up button
            Button(onClick = viewModel::onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(20.dp)
                    .padding(horizontal = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppColor)
            ) {
                Box {
                    AnimatedContent(
                        targetState = loading.value,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(300)) + scaleIn(initialScale = 0.8f) togetherWith
                                    fadeOut(animationSpec = tween(300)) + scaleOut(targetScale = 0.8f)
                        }
                        ) {target ->
                        if (target) {
                            CircularProgressIndicator(color = AppWhite, modifier = Modifier.size(30.dp))
                        } else {
                            Text(text = stringResource(id = R.string.login_ac), fontSize = 18.sp, fontFamily = poppinsFontFamily, color = AppWhite, letterSpacing = 1.sp)
                        }
                    }
                }
            }

            // don't have an account
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.dont_have_an_account),
                    color = Color.Black
                )
                TextButton(onClick = {
                    viewModel.onSignUpClicked()
                }) {
                    Text(
                        text = stringResource(R.string.sign_up_title),
                        fontFamily = poppinsFontFamily,
                        fontSize = 17.sp,
                        color = AppColor
                    )
                }
            }
            Spacer(Modifier.height(40.dp))
            val context = LocalContext.current

            GroupSocialButtons(
                color = Color.Black,
                onFaceBookClick = {
                    viewModel.onFacebookClicked(context as ComponentActivity)
                },
                onGoogleClick = {
                    viewModel.onGoogleSignInClick(context as ComponentActivity)
                }
            )
        }
    }

}