package com.foodhub.fooddelivery.ui.features.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodhub.fooddelivery.data.FoodApi
import com.foodhub.fooddelivery.data.models.Category
import com.foodhub.fooddelivery.data.models.Restaurant
import com.foodhub.fooddelivery.data.remote.ApiResponse
import com.foodhub.fooddelivery.data.remote.safeApiCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val foodApi: FoodApi): ViewModel() {
    private val _uiState = MutableStateFlow<HomeScreenState>(HomeScreenState.Loading)
    val uiState: StateFlow<HomeScreenState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<HomeScreenNavigationEvent?>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    var categories = emptyList<Category>()
        private set

    var restaurants = emptyList<Restaurant>()
        private set

    init {
        viewModelScope.launch {
           categories =  getCategories()
           restaurants =  getPopularRestaurants()

            if (categories.isNotEmpty() && restaurants.isNotEmpty()) {
                _uiState.value = HomeScreenState.Success
            } else {
                _uiState.value = HomeScreenState.Empty
            }
        }
    }

    private suspend fun getCategories(): List<Category> {
        var list = emptyList<Category>()
        val response = safeApiCall {
            foodApi.getCategories()
        }
        when (response) {
            is ApiResponse.Success -> {
                list = response.data.data
            }
            else -> {
            }
        }
        return list
    }

    private suspend fun getPopularRestaurants(): List<Restaurant> {
        var list = emptyList<Restaurant>()
        val response = safeApiCall {
            foodApi.getRestaurants(40.7128, -74.0060)
        }
        when (response) {
            is ApiResponse.Success -> {
                list = response.data.data
            }
            else -> {
            }
        }
        return list
    }



    sealed class HomeScreenState {
        data object Loading : HomeScreenState()
        data object Empty : HomeScreenState()
        data object Success : HomeScreenState()
    }

    sealed class HomeScreenNavigationEvent {
        data object NavigationToDetail : HomeScreenNavigationEvent()
    }
}