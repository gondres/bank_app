package com.phincon.qris.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.phincon.qris.MainActivity
import com.phincon.qris.R
import com.phincon.qris.navigation.BottomBarNav
import com.phincon.qris.navigation.RouteNav
import com.phincon.qris.screen.home.vm.HomeViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun MainScreen(args: String, homeViewModel: HomeViewModel, mainNavHostController: NavHostController) {
    val navController = rememberNavController()
    val activity = LocalView.current.context as? MainActivity
    val context = LocalContext.current
    Log.d("Main Screen",args)
    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text(text = args) },
                    navigationIcon = {
                        IconButton(onClick = { /* do something */ }) {
                            Icon(
                                imageVector = Icons.Filled.AccountCircle,
                                contentDescription = "Localized description"
                            )
                        }
                    },
                )
                Divider()
            }

        },
        bottomBar = { BottomBar(navController = navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { activity?.checkCameraPermission(context) },
                backgroundColor = Color.White
            ) {
                Image(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = R.drawable.ic_scan),
                    contentDescription = "fab"
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            BottomBarNav(navController, args, homeViewModel)
        }

    }
}


@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        RouteNav.Home,
        RouteNav.Promo,
        RouteNav.Portfolio,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation(backgroundColor = Color.White) {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController,
                )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: RouteNav,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    BottomNavigationItem(
        label = {
            Text(text = screen.title, fontSize = 12.sp, color = if(currentDestination?.route == screen.route) MaterialTheme.colorScheme.primary else Color.Gray)
        },
        icon = {
            Icon(
                imageVector = if(screen.title == "Voucher") ImageVector.vectorResource(id = R.drawable.ic_voucher) else if(screen.title == "Chart")ImageVector.vectorResource(id = R.drawable.ic_chart)else screen.icon,
                contentDescription = "Navigation Icon",
                tint = if(currentDestination?.route == screen.route) MaterialTheme.colorScheme.primary else Color.Gray
            )

        },

        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        selectedContentColor = MaterialTheme.colorScheme.primary,
        unselectedContentColor = Color.Gray,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    )
}