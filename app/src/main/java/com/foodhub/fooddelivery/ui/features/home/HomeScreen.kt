package com.foodhub.fooddelivery.ui.features.home

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.foodhub.fooddelivery.R
import com.foodhub.fooddelivery.data.models.Category
import com.foodhub.fooddelivery.data.models.Restaurant
import com.foodhub.fooddelivery.ui.features.restaurant_details.FoodItemView
import com.foodhub.fooddelivery.ui.features.restaurant_details.RestaurantDetails
import com.foodhub.fooddelivery.ui.features.restaurant_details.RestaurantDetailsViewModel
import com.foodhub.fooddelivery.ui.gridItems
import com.foodhub.fooddelivery.ui.navigation.FoodDetails
import com.foodhub.fooddelivery.ui.navigation.RestaurantDetails
import com.foodhub.fooddelivery.ui.theme.AppColor
import com.foodhub.fooddelivery.ui.theme.AppWhite
import com.foodhub.fooddelivery.ui.theme.Typography
import com.foodhub.fooddelivery.ui.theme.poppinsFontFamily
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.HomeScreen(
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: HomeViewModel = hiltViewModel(),
) {

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collectLatest {
            when (it) {
                is HomeViewModel.HomeScreenNavigationEvent.NavigationToDetail -> {
                    navController.navigate(
                        RestaurantDetails(
                            restaurantID = it.restaurantID,
                            restaurantName = it.name,
                            restaurantImage = it.imageUrl
                        )
                    )
                }
                else -> {}
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        val uiState = viewModel.uiState.collectAsState()

        when (uiState.value) {
            is HomeViewModel.HomeScreenState.Loading -> {
                CircularProgressIndicator(color = AppWhite, modifier = Modifier.size(30.dp))
            }

            is HomeViewModel.HomeScreenState.Empty -> {
                Text("Empty")
            }

            is HomeViewModel.HomeScreenState.Success -> {
                val categories = viewModel.categories
                val restaurants = viewModel.restaurants

                CategoriesList(categories = categories, onCategorySelected = {})
                RestaurantList(
                    restaurants = restaurants, onRestaurantSelected = {
                        viewModel.onRestaurantSelected(it)
                    },
                    animatedVisibilityScope = animatedVisibilityScope
                )

            }


        }
    }
}

@Composable
fun CategoriesList(categories: List<Category>, onCategorySelected: (Category) -> Unit) {
    LazyRow {
        items(categories) {
            CategoryItem(category = it, onCategorySelected = onCategorySelected)
        }
    }
}

@Composable
fun CategoryItem(category: Category, onCategorySelected: (Category) -> Unit) {
    Column(modifier = Modifier
        .padding(8.dp)
        .height(115.dp)
        .clickable {
            onCategorySelected(category)
        }
        .shadow(4.dp, RoundedCornerShape(90.dp), ambientColor = AppColor, spotColor = AppColor)
        .background(AppWhite)
        .clip(RoundedCornerShape(90.dp))
        .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = category.imageUrl, contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Fit
        )
        Spacer(Modifier.size(8.dp))

        Text(
            text = category.name,
            style = TextStyle(fontSize = 10.sp),
            color = Color.DarkGray,
            fontWeight = FontWeight.Medium,
            fontFamily = poppinsFontFamily
        )
    }
}






@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.RestaurantList(
    restaurants: List<Restaurant>,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onRestaurantSelected: (Restaurant) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.popular_restaurants),
                style = Typography.bodyLarge,
                modifier = Modifier.padding(16.dp),
                fontFamily = poppinsFontFamily,
                color = Color.Black
            )
            Spacer(Modifier.size(16.dp))
            TextButton(onClick = {

            }) {
                Text(
                    text = stringResource(R.string.view_all),
                    fontFamily = poppinsFontFamily,
                    color = AppColor,
                    style = Typography.bodyMedium
                )
            }
        }
    }
    LazyRow {
        items(restaurants) {
            RestaurantItem(
                restaurant = it,
                onRestaurantSelected = onRestaurantSelected,
                animatedVisibilityScope = animatedVisibilityScope,
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.RestaurantItem(
    restaurant: Restaurant,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onRestaurantSelected: (Restaurant) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .width(280.dp)
            .height(229.dp)
            .shadow(16.dp, shape = RoundedCornerShape(16.dp))
            .background(Color.White)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onRestaurantSelected(restaurant) }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = restaurant.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .sharedElement(
                        state = rememberSharedContentState(key = "image/${restaurant.id}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    ),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = restaurant.name,
                        style = Typography.titleMedium,
                        textAlign = TextAlign.Center,
                        fontFamily = poppinsFontFamily,
                        modifier = Modifier
                            .sharedElement(
                                state = rememberSharedContentState(key = "title/${restaurant.id}"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                    )
                    Image(
                        painter = painterResource(R.drawable.tick),
                        modifier = Modifier
                            .size(18.dp)
                            .padding(horizontal = 2.dp),
                        contentDescription = null
                    )
                }
                Row {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.delivery_man),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .padding(end = 8.dp)
                                .size(13.dp)
                        )
                        Text(
                            text = stringResource(R.string.free_delivery),
                            style = Typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.clock),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .padding(end = 8.dp)
                                .size(13.dp)
                        )
                        Text(
                            text = stringResource(R.string.minutes),
                            style = Typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart), verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(Color.White)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center

                ) {
                    Text(
                        text = "4.5", style = Typography.titleSmall,

                        modifier = Modifier.padding(4.dp)
                    )
                    Image(
                        painter = painterResource(R.drawable.rating_star),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(
                        text = "(25)", style = Typography.bodySmall, color = Color.Gray
                    )
                }

                Image(
                    painter = painterResource(R.drawable.favorites),
                    contentDescription = null,
                    modifier = Modifier
                        .height(65.dp)
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}