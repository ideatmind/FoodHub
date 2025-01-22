package com.foodhub.fooddelivery.ui.features.restaurant_details

import android.util.Log
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.foodhub.fooddelivery.R
import com.foodhub.fooddelivery.data.models.FoodItem
import com.foodhub.fooddelivery.ui.gridItems
import com.foodhub.fooddelivery.ui.navigation.FoodDetails
import com.foodhub.fooddelivery.ui.theme.AppColor
import com.foodhub.fooddelivery.ui.theme.AppWhite
import com.foodhub.fooddelivery.ui.theme.poppinsFontFamily

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.RestaurantDetailsScreen(
    navController: NavController,
    name: String,
    imageUrl: String,
    restaurantID: String,
    viewModel: RestaurantDetailsViewModel = hiltViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val uiState = viewModel.uiState.collectAsState()

    LaunchedEffect(restaurantID) {
        viewModel.getFoodItem(restaurantID)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()

    ) {
        RestaurantDetailsHeader(
            imageUrl = imageUrl,
            onBackButton = { navController.popBackStack() },
            onFavoriteButton = {

            },
            animatedVisibilityScope = animatedVisibilityScope,
            restaurantID = restaurantID
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 12.dp)
        ) {
            item {
                RestaurantDetails(
                    title = name,
                    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                    animatedVisibilityScope = animatedVisibilityScope,
                    restaurantID = restaurantID
                )
            }

            when (uiState.value) {
                is RestaurantDetailsViewModel.RestaurantEvent.Success -> {
                    val foodItems =
                        (uiState.value as RestaurantDetailsViewModel.RestaurantEvent.Success).foodItems
                    gridItems(foodItems, 2) { foodItem ->
                        FoodItemView(foodItem = foodItem, animatedVisibilityScope = animatedVisibilityScope) {
                            navController.navigate(FoodDetails(foodItem))
                        }
                    }
                }

                RestaurantDetailsViewModel.RestaurantEvent.Loading -> {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(color = AppWhite)
                            Text("Loading...", fontFamily = poppinsFontFamily)
                        }
                    }
                }

                RestaurantDetailsViewModel.RestaurantEvent.Error -> {
                    item {
                        Text("Something went wrong", fontFamily = poppinsFontFamily)
                    }
                }

                RestaurantDetailsViewModel.RestaurantEvent.Nothing -> {}
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.RestaurantDetails(title: String, description: String, animatedVisibilityScope: AnimatedVisibilityScope, restaurantID: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontFamily = poppinsFontFamily,
                modifier = Modifier.sharedElement(state = rememberSharedContentState(key = "title/$restaurantID"), animatedVisibilityScope = animatedVisibilityScope)
            )
            Spacer(Modifier.size(4.dp))
            Image(
                painter = painterResource(R.drawable.tick),
                modifier = Modifier.size(16.dp),
                contentDescription = null
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.rating_star),
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "4.5",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterVertically),
                fontFamily = poppinsFontFamily
            )
            Spacer(modifier = Modifier.size(8.dp))

            Text(
                text = "(30+)",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterVertically),
                fontFamily = poppinsFontFamily
            )
            Spacer(modifier = Modifier.size(8.dp))
            TextButton(onClick = {}) {
                Text(
                    text = "View All Reviews",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppColor,
                    fontFamily = poppinsFontFamily
                )
            }
        }
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            fontFamily = poppinsFontFamily,
            color = Color.Gray
        )
    }
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.RestaurantDetailsHeader(
    imageUrl: String,
    onBackButton: () -> Unit,
    onFavoriteButton: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    restaurantID: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .clip(
                RoundedCornerShape(16.dp)
            )
    ) {
        AsyncImage(
            model = imageUrl, contentDescription = null, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .clip(
                    RoundedCornerShape(16.dp)
                )
                .sharedElement(
                    state = rememberSharedContentState(key = "image/$restaurantID"),
                    animatedVisibilityScope = animatedVisibilityScope
                ),
            contentScale = ContentScale.Crop
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
                Image(
                    painter = painterResource(R.drawable.back_button), contentDescription = null,
                    modifier = Modifier
                        .padding(6.dp)
                        .size(78.dp)
                        .clickable {
                            onBackButton()
                        }
                )
                Image(
                    painter = painterResource(R.drawable.favorites), contentDescription = null,
                    modifier = Modifier
                        .padding(6.dp)
                        .size(78.dp)
                        .clip(CircleShape)
                        .clickable {
                            onFavoriteButton()
                        }
                )
            }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.FoodItemView(foodItem: FoodItem, animatedVisibilityScope: AnimatedVisibilityScope, onClick:(FoodItem) -> Unit) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .width(162.dp)
            .height(216.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp), ambientColor = AppColor, spotColor = AppColor)
            .background(Color.White)
            .clickable {
                onClick.invoke(foodItem)
            }
            .clip(RoundedCornerShape(16.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(147.dp)
        ) {
            AsyncImage(
                model = foodItem.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
                    .sharedElement(rememberSharedContentState(key = "image/${foodItem.id}"), animatedVisibilityScope = animatedVisibilityScope),
                contentScale = ContentScale.Crop
            )
            Text(
                text = "₹${foodItem.price}",
                style = MaterialTheme.typography.bodySmall,
                fontFamily = poppinsFontFamily,
                modifier = Modifier
                    .padding(6.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(horizontal = 16.dp)
                    .align(Alignment.TopStart)
            )
            Image(
                painter = painterResource(R.drawable.favorites),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(55.dp)
                    .align(Alignment.TopEnd)
                    .clickable {

                    }
            )
            Row(
                modifier = Modifier
                    .padding(0.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(horizontal = 2.dp)
                    .align(Alignment.BottomEnd), verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "4.5",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    fontFamily = poppinsFontFamily
                )
                Spacer(modifier = Modifier.size(1.dp))
                Image(
                    painter = painterResource(R.drawable.rating_star),
                    contentDescription = null,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.size(2.dp))
                Text(
                    text = "(25)",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    fontFamily = poppinsFontFamily
                )
            }
        }
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = foodItem.name,
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = poppinsFontFamily,
                color = Color.DarkGray,
                maxLines = 1
            )
            Text(
                text = foodItem.description,
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = poppinsFontFamily,
                color = Color.Gray,
                maxLines = 1
            )
        }
    }
}