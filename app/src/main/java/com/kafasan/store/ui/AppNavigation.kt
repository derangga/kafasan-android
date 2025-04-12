package com.kafasan.store.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kafasan.store.ui.pages.detail.DetailProductScreen
import com.kafasan.store.ui.pages.favorite.FavoriteScreen
import com.kafasan.store.ui.pages.home.HomeScreen
import com.kafasan.store.ui.pages.onboarding.OnBoardingScreen
import com.kafasan.store.ui.pages.search.SearchScreen
import com.kafasan.store.ui.pages.search.SuggestSearchScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "home") {
        composable("onboarding") { OnBoardingScreen(navController) }
        composable("home") { HomeScreen(navController, hiltViewModel()) }
        composable("product/{id}") { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getString("id")
            id?.let{ DetailProductScreen(navController, it.toInt(), hiltViewModel()) }
        }
        composable("favorite") { FavoriteScreen(navController, hiltViewModel()) }
        composable("suggest-search") { SuggestSearchScreen(navController, hiltViewModel()) }
        composable("search/{query}") { navBackStackEntry ->
            val query = navBackStackEntry.arguments?.getString("query").orEmpty()
            SearchScreen(navController, query, hiltViewModel())
        }
    }
}