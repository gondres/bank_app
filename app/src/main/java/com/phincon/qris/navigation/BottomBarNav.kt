package com.phincon.qris.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStore
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.phincon.qris.screen.home.HomeScreen
import com.phincon.qris.screen.portfolio.PortfolioScreen
import com.phincon.qris.screen.promo.PromoScreen
import com.phincon.qris.screen.home.vm.HomeViewModel
import com.phincon.qris.screen.promo.vm.PromoViewModel

@Composable
fun BottomBarNav( homeViewModel: HomeViewModel,navController : NavHostController,argsHome: String, argsPromo: String) {
    val viewModelStore = remember { ViewModelStore() }
    navController.setViewModelStore(viewModelStore)

    NavHost(
        navController = navController,
        startDestination = RouteNav.Home.route
    ) {

        composable(route = RouteNav.Home.route) { backStackEntry ->
            HomeScreen(argsHome, homeViewModel)
        }
        composable(route = RouteNav.Promo.route) {
            val viewModel = hiltViewModel<PromoViewModel>()
            PromoScreen(navController,viewModel)
        }

        composable(route = RouteNav.Portfolio.route) {
          PortfolioScreen()
        }

    }
}