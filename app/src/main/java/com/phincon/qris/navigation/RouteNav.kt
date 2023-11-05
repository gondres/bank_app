package com.phincon.qris.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class RouteNav(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Main : RouteNav(
        route = "main/{name}",
        title = "Main Screen",
        icon = Icons.Default.Home
    )
    object Login : RouteNav(
        route = "login",
        title = "Login Screen",
        icon = Icons.Default.Home
    )
    object Home : RouteNav(
        route = "home",
        title = "Home",
        icon = Icons.Outlined.Home
    )

    object Promo : RouteNav(
        route = "promo",
        title = "Promo",
        icon = Icons.Outlined.Person
    )

    object Portfolio : RouteNav(
        route = "portfolio",
        title = "Portfolio",
        icon = Icons.Outlined.Settings
    )
    object Detail : RouteNav(
        route = "detail/{id}",
        title = "Detail Screen",
        icon = Icons.Default.Home
    )

    object Status : RouteNav(
        route = "status/{id}",
        title = "Status Screen",
        icon = Icons.Default.Home
    )

    object DetailChart : RouteNav(
        route = "detailChart/{label}",
        title = "Detail Chart Screen",
        icon = Icons.Default.Home
    )
}