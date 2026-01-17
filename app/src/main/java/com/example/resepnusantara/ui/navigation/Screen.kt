package com.example.resepnusantara.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Search : Screen("search")
    object Favorit : Screen("favorit")
    object Resepku : Screen("resepku")
    object Detail : Screen("detail/{resepId}") {
        fun createRoute(resepId: Int) = "detail/$resepId"
    }
    object AddEditResep : Screen("add_edit_resep?resepId={resepId}") {
        fun createRoute(resepId: Int? = null) = if (resepId != null) "add_edit_resep?resepId=$resepId" else "add_edit_resep"
    }
}
