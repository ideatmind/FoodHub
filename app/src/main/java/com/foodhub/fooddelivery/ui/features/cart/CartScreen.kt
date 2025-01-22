package com.foodhub.fooddelivery.ui.features.cart

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.foodhub.fooddelivery.R
import com.foodhub.fooddelivery.data.models.Address
import com.foodhub.fooddelivery.data.models.CartItem
import com.foodhub.fooddelivery.data.models.CheckoutDetails
import com.foodhub.fooddelivery.ui.navigation.Cart
import com.foodhub.fooddelivery.ui.navigation.Home
import com.foodhub.fooddelivery.ui.theme.AppColor
import com.foodhub.fooddelivery.ui.theme.AppWhite
import com.foodhub.fooddelivery.ui.theme.poppinsFontFamily
import kotlinx.coroutines.flow.collectLatest
import java.util.Currency

@Composable
fun CartScreen(navController: NavController, viewModel: CartViewModel) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        viewModel.event.collectLatest {
            when (it) {
                CartViewModel.CartEvent.OnItemRemoveError -> {

                }

                CartViewModel.CartEvent.OnQuantityUpdateError -> {

                }

                CartViewModel.CartEvent.ShowErrorDialog -> {

                }

                else -> {

                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(bottom = 90.dp)
            .statusBarsPadding()
    ) {
        CartHeaderView(onBack = { navController.popBackStack() })

        Spacer(Modifier.size(16.dp))

        when (uiState.value) {
            is CartViewModel.CartUiState.Success -> {
                val data = (uiState.value as CartViewModel.CartUiState.Success).data
                if (data.items.isNotEmpty()) {
                    LazyColumn {
                        items(data.items) { it ->
                            CartItemView(
                                cartItem = it,
                                onIncrement = { cartItem, _ ->
                                    viewModel.incrementQuantity(cartItem)
                                },
                                onDecrement = { cartItem, _ ->
                                    viewModel.decrementQuantity(cartItem)
                                },
                                onRemove = {
                                    viewModel.removeItem(
                                        it
                                    )
                                }
                            )
                        }
                        item {
                            CheckoutDetailsView(
                                data.checkoutDetails
                            )

                            Spacer(Modifier.height(40.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(2.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = {

                                    },
                                    colors = ButtonDefaults.buttonColors(AppColor),
                                ) {
                                    Text(
                                        "Checkout".uppercase(),
                                        color = Color.White,
                                        fontFamily = poppinsFontFamily
                                    )
                                }
                            }
                        }

                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.cart_icon),
                            contentDescription = null,
                            tint = Color.Gray
                        )
                        Text(
                            "Cart is Empty",
                            fontFamily = poppinsFontFamily,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Normal,
                            color = Color.Gray
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(25.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {
                                    navController.navigate(Home) {
                                        popUpTo(Home) {
                                            inclusive = true
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(AppColor),
                            ) {
                                Text(
                                    "Browse Products",
                                    color = Color.White,
                                    fontFamily = poppinsFontFamily
                                )
                            }
                        }

                    }
                }
            }

            is CartViewModel.CartUiState.Loading -> {
                Spacer(Modifier.size(16.dp))
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Spacer(Modifier.size(16.dp))
                    CircularProgressIndicator()
                }
            }

            is CartViewModel.CartUiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    val message = (uiState.value as CartViewModel.CartUiState.Error).message
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = poppinsFontFamily
                    )
                    IconButton(onClick = {

                    }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Retry",
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            }

            else -> {}
        }
    }

}

@Composable
fun AddressCard(address: Address?, onAddressSelected: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp)
            .background(Color.White)
            .padding(horizontal = 16.dp)
            .clip(
                RoundedCornerShape(16.dp)
            )
            .clickable {
                onAddressSelected()
            }
    ) {
        if (address != null) {
            Column {
                Text(
                    text = address.addressLine1,
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = poppinsFontFamily
                )
                Spacer(Modifier.size(4.dp))
                Text(
                    text = "$address.city, $address.state, ${address.zipCode}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = poppinsFontFamily,
                    color = Color.Gray
                )

            }
        } else {
            Text(
                "Select Address",
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = poppinsFontFamily
            )
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun CheckoutDetailsView(checkoutDetails: CheckoutDetails) {
    Spacer(Modifier.height(20.dp))
    Column {
        CheckoutRowItem(
            title = "SubTotal",
            value = String.format("%.2f", checkoutDetails.subTotal),
            currency = "USD"
        )
        CheckoutRowItem(
            title = "Taxes",
            value = String.format("%.2f", checkoutDetails.tax),
            currency = "USD"
        )
        CheckoutRowItem(
            title = "Delivery Fee",
            value = String.format("%.2f", checkoutDetails.deliveryFee),
            currency = "USD"
        )
        CheckoutRowItem(
            title = "Total",
            value = String.format("%.2f", checkoutDetails.totalAmount),
            currency = "USD"
        )
    }
}


@Composable
fun CheckoutRowItem(title: String, value: String, currency: String) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 2.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = poppinsFontFamily
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = poppinsFontFamily
            )
            Text(
                text = currency,
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = poppinsFontFamily,
                color = Color.LightGray
            )
        }
        VerticalDivider()
    }
}

@Composable
fun CartItemView(
    cartItem: CartItem,
    onIncrement: (CartItem, Int) -> Unit,
    onDecrement: (CartItem, Int) -> Unit,
    onRemove: (CartItem) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = cartItem.menuItemId.imageUrl, contentDescription = null,
            modifier = Modifier
                .size(82.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.size(12.dp))
        Column(
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = cartItem.menuItemId.name,
                    maxLines = 1,
                    color = Color.DarkGray,
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = poppinsFontFamily
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick = {
                    onRemove(cartItem)
                }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = null,
                        tint = AppColor
                    )
                }
            }
            Text(
                text = cartItem.menuItemId.description,
                maxLines = 1,
                color = Color.Gray,
                fontFamily = poppinsFontFamily,
                style = MaterialTheme.typography.bodyMedium
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                Text(
                    text = "$${cartItem.menuItemId.price}",
                    maxLines = 1,
                    color = AppColor,
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = poppinsFontFamily
                )
                Spacer(Modifier.weight(1f))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.remove_button),
                        modifier = Modifier
                            .size(25.dp)
                            .clip(CircleShape)
                            .clickable {
                                onDecrement(cartItem, cartItem.quantity)
                            },
                        contentDescription = null
                    )
                    Spacer(Modifier.size(12.dp))
                    Text(
                        text = "${cartItem.quantity}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontFamily = poppinsFontFamily,
                        color = Color.DarkGray
                    )
                    Spacer(Modifier.size(12.dp))
                    Image(
                        painter = painterResource(id = R.drawable.add_button),
                        modifier = Modifier
                            .size(25.dp)
                            .clip(CircleShape)
                            .clickable {
                                onIncrement(cartItem, cartItem.quantity)
                            },
                        contentDescription = null
                    )
                }
            }
        }
    }
    HorizontalDivider(
        Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        color = Color.LightGray
    )
}


@Composable
fun CartHeaderView(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = null)
        }
        Text(
            stringResource(R.string.cart),
            fontFamily = poppinsFontFamily,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold
        )

    }
}