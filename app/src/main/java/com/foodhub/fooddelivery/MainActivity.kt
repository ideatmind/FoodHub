package com.foodhub.fooddelivery

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.foodhub.fooddelivery.data.FoodApi
import com.foodhub.fooddelivery.data.FoodHubSession
import com.foodhub.fooddelivery.ui.features.cart.CartViewModel
import com.foodhub.fooddelivery.ui.navigation.Cart
import com.foodhub.fooddelivery.ui.navigation.Home
import com.foodhub.fooddelivery.ui.navigation.NavGraph
import com.foodhub.fooddelivery.ui.navigation.NavRoute
import com.foodhub.fooddelivery.ui.navigation.Notification
import com.foodhub.fooddelivery.ui.theme.AppColor
import com.foodhub.fooddelivery.ui.theme.FoodDeliveryTheme
import com.foodhub.fooddelivery.ui.theme.Mustard
import com.foodhub.fooddelivery.ui.theme.poppinsFontFamily
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    var showSplashScreen = false

    @Inject
    lateinit var foodApi: FoodApi

    @Inject
    lateinit var session: FoodHubSession

    sealed class BottomNavItems(val route: NavRoute, val icon: Int) {
        data object Home :
            BottomNavItems(com.foodhub.fooddelivery.ui.navigation.Home, R.drawable.home_icon)

        data object Cart :
            BottomNavItems(com.foodhub.fooddelivery.ui.navigation.Cart, R.drawable.cart_icon)

        data object Notification : BottomNavItems(
            com.foodhub.fooddelivery.ui.navigation.Notification,
            R.drawable.favorites_icon
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                showSplashScreen
            }
            setOnExitAnimationListener { screen ->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_X,
                    0.5f,
                    0f
                )
                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    0.5f,
                    0f
                )
                zoomX.duration = 500
                zoomY.duration = 500
                zoomX.interpolator = OvershootInterpolator()
                zoomY.interpolator = OvershootInterpolator()
                zoomX.doOnEnd {
                    screen.remove()
                }
                zoomY.doOnEnd {
                    screen.remove()
                }
                zoomY.start()
                zoomX.start()
            }
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.Transparent.toArgb(),
                Color.DarkGray.toArgb()
            )
        )
        setContent {
            FoodDeliveryTheme {
                val shouldShowBottomNav = remember { mutableStateOf(false) }

                val navItems = listOf(
                    BottomNavItems.Home,
                    BottomNavItems.Cart,
                    BottomNavItems.Notification
                )
                val navController = rememberNavController()
                val cartViewModel: CartViewModel = hiltViewModel()
                val cartItemSize = cartViewModel.cartItemCount.collectAsStateWithLifecycle()
                Scaffold(modifier = Modifier.fillMaxSize(),

                    bottomBar = {
                        val backStackEntry = navController.currentBackStackEntryAsState()
                        AnimatedVisibility(visible = shouldShowBottomNav.value) {

                            BottomAppBar(
                                modifier = Modifier
                                    .height(90.dp),
                                containerColor = Color.White
                            ) {
                                navItems.forEach { item ->
                                    val selected = backStackEntry.value?.destination?.hierarchy?.any {
                                        it.route == item.route::class.qualifiedName
                                    } == true

                                    NavigationBarItem(colors = NavigationBarItemColors(
                                        selectedIndicatorColor = Color.Transparent,
                                        selectedIconColor = AppColor,
                                        selectedTextColor = AppColor,
                                        unselectedIconColor = Color.LightGray,
                                        unselectedTextColor = Color.LightGray,
                                        disabledIconColor = Color.LightGray,
                                        disabledTextColor = Color.LightGray
                                    ),
                                        selected = selected,
                                        onClick = { navController.navigate(item.route) },
                                        icon = {
                                            Box(modifier = Modifier.size(48.dp)) {
                                                Icon(
                                                    painter = painterResource(item.icon),
                                                    contentDescription = null,
                                                    modifier = Modifier.align(Center)
                                                )
                                                if(item.route == Cart && cartItemSize.value!=0) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(16.dp)
                                                            .clip(CircleShape)
                                                            .background(Mustard)
                                                            .align(TopEnd)
                                                    ) {
                                                        Text(
                                                            "${cartItemSize.value}",
                                                            modifier = Modifier.align(Alignment.Center),
                                                            color = Color.White,
                                                            fontFamily = poppinsFontFamily,
                                                            fontSize = 12.sp
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    )
                                }
                            }



                        }

                    }) { padding ->
                    NavGraph(
                        navController = navController,
                        session = session,
                        shouldShowBottomNav = shouldShowBottomNav
                    )
                }
            }
        }
    }
}
