package com.foodhub.fooddelivery.ui.features.cart

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodhub.fooddelivery.data.FoodApi
import com.foodhub.fooddelivery.data.models.CartItem
import com.foodhub.fooddelivery.data.models.CartResponse
import com.foodhub.fooddelivery.data.models.UpdateCartItemRequest
import com.foodhub.fooddelivery.data.remote.ApiResponse
import com.foodhub.fooddelivery.data.remote.safeApiCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(val foodApi: FoodApi) : ViewModel() {

    private val _uiState = MutableStateFlow<CartUiState>(CartUiState.Loading)
    val uiState = _uiState.asStateFlow()
    private val _event = MutableSharedFlow<CartEvent>()
    val event = _event.asSharedFlow()
    private var cartResponse: CartResponse? = null
    private val _cartItemCount = MutableStateFlow(0)
    val cartItemCount = _cartItemCount.asStateFlow()

    init {
        getCart()
    }

    fun getCart() {
        viewModelScope.launch {
            _uiState.value = CartUiState.Loading
            val response = safeApiCall {
                foodApi.getCart()
            }
            when (response) {
                is ApiResponse.Success -> {
                    cartResponse = response.data
                    _cartItemCount.value = response.data.items.size
                    _uiState.value = CartUiState.Success(response.data)
                }

                else -> {
                    cartResponse?.let {
                        _uiState.value = CartUiState.Success(cartResponse!!)
                    }
                }
            }
        }
    }

    fun incrementQuantity(cartItem: CartItem) {
        updateItemQuantity(cartItem, cartItem.quantity+1)
    }

    @SuppressLint("SuspiciousIndentation")
    fun decrementQuantity(cartItem: CartItem) {
        if (cartItem.quantity == 1) {
           removeItem(cartItem)
        } else
            updateItemQuantity(cartItem, cartItem.quantity-1)
    }

    fun checkOut() {

    }

    private fun updateItemQuantity(cartItem: CartItem, quantity: Int) {
        viewModelScope.launch {
            _uiState.value = CartUiState.Loading
            val res =
                safeApiCall { foodApi.updateCart(UpdateCartItemRequest(cartItem.id, quantity)) }
            when (res) {
                is ApiResponse.Success -> {
                    getCart()
                }

                else -> {
                    cartResponse?.let {
                        _uiState.value = CartUiState.Success(cartResponse!!)
                    }
                    _event.emit(CartEvent.OnQuantityUpdateError)
                }
            }
        }
    }

    fun removeItem(cartItem: CartItem) {
        viewModelScope.launch {
            _uiState.value = CartUiState.Loading
            val res =
                safeApiCall { foodApi.deleteCartItem(cartItem.id) }
            when (res) {
                is ApiResponse.Success -> {
                    getCart()
                }

                else -> {
                    cartResponse?.let {
                        _uiState.value = CartUiState.Success(cartResponse!!)
                    }
                    _event.emit(CartEvent.OnItemRemoveError)
                }
            }
        }
    }


    sealed class CartUiState {
        data object Nothing : CartUiState()
        data object Loading : CartUiState()
        data class Success(val data: CartResponse) : CartUiState()
        data class Error(val message: String) : CartUiState()
    }

    sealed class CartEvent {
        data object ShowErrorDialog : CartEvent()
        data object OnCheckOut : CartEvent()
        data object OnQuantityUpdateError : CartEvent()
        data object OnItemRemoveError : CartEvent()
    }
}