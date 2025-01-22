package com.foodhub.fooddelivery.ui.features.restaurant_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodhub.fooddelivery.data.FoodApi
import com.foodhub.fooddelivery.data.models.FoodItem
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
class RestaurantDetailsViewModel @Inject constructor(val foodApi: FoodApi): ViewModel() {
    var errorMsg = ""
    var errorDescription = ""

    private val _uiState = MutableStateFlow<RestaurantEvent>(RestaurantEvent.Error)
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<RestaurantNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun getFoodItem(id: String) {
        viewModelScope.launch {
            _uiState.value = RestaurantEvent.Loading
            try {
                val response = safeApiCall {
                    foodApi.getFoodItemsForRestaurant(id)
                }
                when (response) {
                    is ApiResponse.Success -> {
                        _uiState.value = RestaurantEvent.Success(response.data.foodItems)
                    }
                    is ApiResponse.Error -> {
                        _uiState.value = RestaurantEvent.Error
                        _navigationEvent.emit(RestaurantNavigationEvent.ShowErrorDialog)
                    }
                    else -> {
                        _uiState.value = RestaurantEvent.Error
                        _navigationEvent.emit(RestaurantNavigationEvent.ShowErrorDialog)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = RestaurantEvent.Error
                _navigationEvent.emit(RestaurantNavigationEvent.ShowErrorDialog)
            }
        }
    }


    sealed class RestaurantNavigationEvent {
        data object GoBack : RestaurantNavigationEvent()
        data object ShowErrorDialog : RestaurantNavigationEvent()
        data class NavigateToProductDetails(val productID: String) : RestaurantNavigationEvent()
    }

    sealed class RestaurantEvent {
        data object Nothing : RestaurantEvent()
        data class Success(val foodItems: List<FoodItem>) : RestaurantEvent()
        data object Error : RestaurantEvent()
        data object Loading : RestaurantEvent()
    }
}