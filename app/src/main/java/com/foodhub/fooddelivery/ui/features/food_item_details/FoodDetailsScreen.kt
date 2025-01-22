package com.foodhub.fooddelivery.ui.features.food_item_details

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.foodhub.fooddelivery.R
import com.foodhub.fooddelivery.data.models.FoodItem
import com.foodhub.fooddelivery.ui.features.restaurant_details.RestaurantDetails
import com.foodhub.fooddelivery.ui.features.restaurant_details.RestaurantDetailsHeader
import com.foodhub.fooddelivery.ui.navigation.Cart
import com.foodhub.fooddelivery.ui.theme.AppColor
import com.foodhub.fooddelivery.ui.theme.AppWhite
import com.foodhub.fooddelivery.ui.theme.poppinsFontFamily
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SharedTransitionScope.FoodDetailsScreen(
    navController: NavController,
    foodItem: FoodItem,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onItemAddedToCart: () -> Unit,
    viewModel: FoodDetailsViewModel = hiltViewModel()
) {
    val showSuccessDialog = remember {
        mutableStateOf(false)
    }
    val showErrorDialog = remember {
        mutableStateOf(false)
    }
    val count = viewModel.quantity.collectAsStateWithLifecycle()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val isLoading = remember { mutableStateOf(false) }

    when (uiState.value) {
        FoodDetailsViewModel.FoodDetailsUiState.Loading -> {
            isLoading.value = true
        }

        FoodDetailsViewModel.FoodDetailsUiState.Success -> {
            isLoading.value = false
        }

        else -> {
            isLoading.value = false
        }
    }

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest {
            when (it) {
                is FoodDetailsViewModel.FoodDetailsEvent.OnAddToCart -> {
                    showSuccessDialog.value = true
                    onItemAddedToCart()
                }

                is FoodDetailsViewModel.FoodDetailsEvent.ShowErrorDialog -> {
                    showErrorDialog.value = true
                }

                is FoodDetailsViewModel.FoodDetailsEvent.GoToCart -> {
                    navController.navigate(Cart)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp), contentAlignment = Alignment.Center
        ) {
            RestaurantDetailsHeader(
                imageUrl = foodItem.imageUrl,
                restaurantID = foodItem.restaurantId,
                animatedVisibilityScope = animatedVisibilityScope,
                onBackButton = { navController.popBackStack() },
                onFavoriteButton = {

                }
            )
        }

        RestaurantDetails(
            title = foodItem.name,
            description = foodItem.description,
            restaurantID = foodItem.restaurantId,
            animatedVisibilityScope = animatedVisibilityScope
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "₹${foodItem.price}", color = AppColor,
                style = MaterialTheme.typography.headlineLarge,
                fontFamily = poppinsFontFamily
            )
            Spacer(Modifier.weight(1f))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.remove_button),
                    modifier = Modifier
                        .size(35.dp)
                        .clip(CircleShape)
                        .clickable {
                            viewModel.decrementQuantity()
                        },
                    contentDescription = null
                )
                Spacer(Modifier.size(12.dp))
                Text(
                    text = "${count.value}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = poppinsFontFamily,
                    color = Color.DarkGray
                )
                Spacer(Modifier.size(12.dp))
                Image(
                    painter = painterResource(id = R.drawable.add_button),
                    modifier = Modifier
                        .size(35.dp)
                        .clip(CircleShape)
                        .clickable {
                            viewModel.incrementQuantity()
                        },
                    contentDescription = null
                )
            }

        }
        Spacer(Modifier.weight(1f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .systemBarsPadding(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    viewModel.addToCart(
                        restaurantID = foodItem.restaurantId,
                        foodItemId = foodItem.id
                    )
                },
                enabled = !isLoading.value,
                modifier = Modifier.padding(8.dp), colors = ButtonDefaults.buttonColors(AppColor)
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(32.dp)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AnimatedVisibility(visible = !isLoading.value) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.cart_bag),
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(
                                text = "Add to Cart".uppercase(),
                                style = MaterialTheme.typography.bodyMedium,
                                fontFamily = poppinsFontFamily,
                                color = Color.White
                            )
                        }
                    }
                    AnimatedVisibility(visible = isLoading.value) {
                        CircularProgressIndicator(modifier = Modifier.size(40.dp))
                    }
                }
            }
        }
    }

    if (showSuccessDialog.value) {
        ModalBottomSheet(
            onDismissRequest = { showSuccessDialog.value = false },
            containerColor = AppWhite
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "${count.value} ${foodItem.name} added to Cart",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.size(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            showSuccessDialog.value = false
                            viewModel.goToCart()
                        },
                        colors = ButtonDefaults.buttonColors(AppWhite),
                        border = BorderStroke(1.dp, AppColor)
                    ) {
                        Text("Go To Cart", fontFamily = poppinsFontFamily, color = AppColor)
                    }
                    Button(
                        onClick = { showSuccessDialog.value = false },
                        colors = ButtonDefaults.buttonColors(AppColor)
                    ) {
                        Text("OK", fontFamily = poppinsFontFamily, color = Color.White)
                    }
                }
            }
        }
    }

    if (showErrorDialog.value) {
        ModalBottomSheet(
            onDismissRequest = { showErrorDialog.value = false },
            containerColor = AppWhite
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = (uiState.value as? FoodDetailsViewModel.FoodDetailsUiState.Error)?.message
                        ?: "failed",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.size(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { showErrorDialog.value = false },
                        colors = ButtonDefaults.buttonColors(AppColor)
                    ) {
                        Text("OK", fontFamily = poppinsFontFamily, color = Color.White)
                    }
                }
            }
        }
    }
}