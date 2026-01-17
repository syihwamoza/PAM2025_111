package com.example.resepnusantara.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.resepnusantara.ui.navigation.NavGraph
import com.example.resepnusantara.ui.navigation.Screen
import com.example.resepnusantara.ui.theme.ResepNusantaraTheme
import com.example.resepnusantara.utils.SessionManager

class MainActivity : ComponentActivity() {
    
    private lateinit var sessionManager: SessionManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)
        
        setContent {
            ResepNusantaraTheme {
                MainApp(sessionManager)
            }
        }
    }
}

@Composable
fun MainApp(sessionManager: SessionManager) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    val showBottomBar = currentDestination?.route in listOf(
        Screen.Home.route,
        Screen.Search.route,
        Screen.Favorit.route,
        Screen.Resepku.route
    )
    
    val startDestination = remember { 
        if (sessionManager.isLoggedIn()) Screen.Home.route else Screen.Login.route 
    }
    
    val items = listOf(
        BottomNavItem("Home", Screen.Home.route, Icons.Default.Home),
        BottomNavItem("Search", Screen.Search.route, Icons.Default.Search),
        BottomNavItem("Favorit", Screen.Favorit.route, Icons.Default.Favorite),
        BottomNavItem("Resepku", Screen.Resepku.route, Icons.Default.Person)
    )
    
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                com.example.resepnusantara.ui.components.CustomBottomNavigation(
                    items = items,
                    currentRoute = currentDestination?.route,
                    onItemClick = { item ->
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavGraph(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

data class BottomNavItem(val label: String, val route: String, val icon: ImageVector)
