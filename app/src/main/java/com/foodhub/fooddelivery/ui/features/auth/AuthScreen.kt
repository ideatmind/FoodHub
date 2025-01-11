package com.foodhub.fooddelivery.ui.features.auth

import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.foodhub.fooddelivery.R
import com.foodhub.fooddelivery.ui.GroupSocialButtons
import com.foodhub.fooddelivery.ui.features.auth.login.LoginViewModel
import com.foodhub.fooddelivery.ui.navigation.Auth
import com.foodhub.fooddelivery.ui.navigation.Home
import com.foodhub.fooddelivery.ui.navigation.Login
import com.foodhub.fooddelivery.ui.navigation.SignUp
import com.foodhub.fooddelivery.ui.theme.AppColor
import com.foodhub.fooddelivery.ui.theme.AppWhite
import com.foodhub.fooddelivery.ui.theme.poppinsFontFamily
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AuthScreen(navController: NavController, viewModel: LoginViewModel = hiltViewModel()) {
    val imageSize = remember { mutableStateOf(IntSize.Zero) }
    val brush = Brush.verticalGradient(
        colors = listOf(
            Color.Transparent,
            Color.Black
        ),
        startY = imageSize.value.height.toFloat()/3
    )
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

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)) {

        Image(painter = painterResource(R.drawable.login_background), contentScale = ContentScale.FillBounds,contentDescription = null,
            modifier = Modifier
                .onGloballyPositioned {
                    imageSize.value = it.size
                }
                .alpha(0.8f))

        Box(modifier = Modifier
            .matchParentSize()
            .background(brush = brush)
        )

        Button(onClick = {

        },
            colors = ButtonDefaults.buttonColors(containerColor = AppWhite),
            modifier = Modifier
                .width(107.dp)
                .height(80.dp)
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(horizontal = 15.dp)
                .padding(bottom = 8.dp)
            ) {
            Text(text = stringResource(id = R.string.skip), color = AppColor, fontFamily = poppinsFontFamily)
        }

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .padding(top = 130.dp))
        {
            Text(
                text = stringResource(id = R.string.welcome),
                color = Color.Black,
                modifier = Modifier,
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(id = R.string.foodhub),
                color = AppColor,
                modifier = Modifier,
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold
                )
            Spacer(Modifier.height(10.dp))
            Text(
                text = stringResource(id = R.string.foodhub_description),
                color = Color.DarkGray,
                modifier = Modifier,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = stringResource(id = R.string.foodhub_description_end),
                color = Color.DarkGray,
                modifier = Modifier,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
        }

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            val context = LocalContext.current
            if(loading.value) {
                CircularProgressIndicator(color = AppWhite, modifier = Modifier.size(30.dp))
            } else {
                Text(text = errorMessage.value ?: "", color = Color.Red,)
            }
            // login buttons
            GroupSocialButtons(
                color = Color.White,
                onFaceBookClick = {
                    viewModel.onFacebookClicked(context as ComponentActivity)
                },
                onGoogleClick = {
                    viewModel.onGoogleSignInClick(context as ComponentActivity)
                },
            )

            // login with email button
            Button(onClick = {
                navController.navigate(SignUp)
            },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(85.dp)
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray.copy(alpha = 0.7f)),
                shape = RoundedCornerShape(32.dp),
                border = BorderStroke(0.7.dp, AppWhite)
            ) {
                Text(text = stringResource(id = R.string.sign_with_email), fontSize = 18.sp, fontFamily = poppinsFontFamily, color = AppWhite)
            }

            // already have an account
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.already_account),
                    color = AppWhite
                )
                TextButton(onClick = {
                    navController.navigate(Login)
                }) {
                    Text(
                        text = stringResource(R.string.sign_in),
                        fontFamily = poppinsFontFamily,
                        fontSize = 17.sp,
                        color = AppWhite,
                        style = TextStyle(
                            textDecoration = TextDecoration.Underline
                        )
                    )
                }
            }
        }
    }
}