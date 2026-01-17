package com.example.resepnusantara.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.resepnusantara.ui.auth.LoginScreen
import com.example.resepnusantara.ui.auth.RegisterScreen

import com.example.resepnusantara.ui.home.HomeScreen
import com.example.resepnusantara.ui.resep.DetailScreen

import com.example.resepnusantara.ui.search.SearchScreen
import com.example.resepnusantara.ui.favorit.FavoritScreen
import com.example.resepnusantara.ui.resepku.ResepkuScreen
import com.example.resepnusantara.ui.resepku.AddEditResepScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(Screen.Register.route) {
            RegisterScreen(navController)
        }
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.Search.route) {
            SearchScreen(navController)
        }
        composable(Screen.Favorit.route) {
            FavoritScreen(navController)
        }
        composable(Screen.Resepku.route) {
            ResepkuScreen(navController)
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("resepId") { type = NavType.IntType })
        ) { backStackEntry ->
            val resepId = backStackEntry.arguments?.getInt("resepId") ?: 0
            DetailScreen(resepId, navController)
        }
        composable(
            route = Screen.AddEditResep.route,
            arguments = listOf(navArgument("resepId") { 
                type = NavType.IntType
                defaultValue = -1 
            })
        ) { backStackEntry ->
            val resepId = backStackEntry.arguments?.getInt("resepId") ?: -1
            AddEditResepScreen(resepId, navController)
        }
    }
}
