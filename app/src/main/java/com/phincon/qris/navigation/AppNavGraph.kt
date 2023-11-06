package com.phincon.qris.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStore
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.phincon.qris.model.PromoParcelable
import com.phincon.qris.screen.MainScreen
import com.phincon.qris.screen.home.HomeScreen
import com.phincon.qris.screen.home.vm.HomeViewModel
import com.phincon.qris.screen.login.LoginScreen
import com.phincon.qris.screen.login.vm.LoginViewModel
import com.phincon.qris.screen.payment.DetailScreen
import com.phincon.qris.screen.payment.StatusPaymentScreen
import com.phincon.qris.screen.payment.vm.PaymentViewModel
import com.phincon.qris.screen.portfolio.PortfolioDetailScreen
import com.phincon.qris.screen.portfolio.PortfolioScreen
import com.phincon.qris.screen.promo.DetailPromoScreen
import com.phincon.qris.screen.promo.PromoScreen
import com.phincon.qris.screen.promo.vm.PromoViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BottomNavGraph(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    homeViewModel: HomeViewModel,
    paymentViewModel: PaymentViewModel
) {
    val viewModelStore = remember { ViewModelStore() }
    navController.setViewModelStore(viewModelStore)
    NavHost(
        navController = navController,
        startDestination = RouteNav.Login.route
    ) {
        composable(route = RouteNav.Main.route, arguments = listOf(navArgument("name") {
            type = NavType.StringType
        })) { backStackEntry ->
            val args = backStackEntry.arguments?.getString("name")
            if (args != null) {
                MainScreen(args, args,navController, homeViewModel)
            }
        }
        composable(route = RouteNav.Login.route) {
            LoginScreen(navController, loginViewModel)
        }
        composable(route = RouteNav.Home.route, arguments = listOf(navArgument("name") {
            type = NavType.StringType
        })) { backStackEntry ->
            val args = backStackEntry.arguments?.getString("name")
            HomeScreen(args, homeViewModel)
        }
        composable(route = RouteNav.Promo.route, arguments = listOf(navArgument("name") {
            type = NavType.StringType
        })){ backStackEntry ->
            val args = backStackEntry.arguments?.getString("name")
            val viewModel = hiltViewModel<PromoViewModel>()
            if (args != null) {
                PromoScreen(navController,viewModel)
            }

        }

        composable(route = RouteNav.Portfolio.route) {
            PortfolioScreen()
        }
        composable(
            route = RouteNav.Detail.route,
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val qrResult = backStackEntry.arguments?.getString("id")
            if (qrResult != null) {
                DetailScreen(qrResult, navController, paymentViewModel, homeViewModel)
            }

        }
        composable(
            route = RouteNav.Status.route,
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val qrResult = backStackEntry.arguments?.getString("id")
            if (qrResult != null) {
                StatusPaymentScreen(qrResult, navController, homeViewModel, paymentViewModel)
            }

        }
        composable(route = RouteNav.DetailChart.route, arguments = listOf(navArgument("label") {
            type = NavType.StringType
        })) { backStackEntry ->
            val label = backStackEntry.arguments?.getString("label")
            if (label != null) {
                PortfolioDetailScreen(navController, label)
            }
        }

        composable(route = RouteNav.DetailPromo.route){
            val result = navController.previousBackStackEntry?.savedStateHandle?.get<PromoParcelable>("promo")
            result?.title?.let { it1 -> Log.d("Promo", it1) }
            if (result != null) {
                DetailPromoScreen(navController,result)
            }
        }

    }
}