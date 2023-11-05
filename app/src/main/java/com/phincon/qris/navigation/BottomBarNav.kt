package com.phincon.qris.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelStore
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.phincon.qris.screen.home.HomeScreen
import com.phincon.qris.screen.portfolio.PortfolioScreen
import com.phincon.qris.screen.promo.PromoScreen
import com.phincon.qris.screen.home.vm.HomeViewModel

@Composable
fun BottomBarNav(navController: NavHostController,argsHome: String, homeViewModel: HomeViewModel) {
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
            PromoScreen()
        }

        composable(route = RouteNav.Portfolio.route) {
          PortfolioScreen()
        }

    }
}