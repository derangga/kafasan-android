package com.kafasan.store.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.kafasan.store.ui.pages.detail.DetailProductScreen
import com.kafasan.store.ui.pages.detail.DetailProductViewModel
import com.kafasan.store.ui.pages.favorite.FavoriteScreen
import com.kafasan.store.ui.pages.favorite.FavoriteViewModel
import com.kafasan.store.ui.pages.home.HomeScreen
import com.kafasan.store.ui.pages.home.HomeViewModel
import com.kafasan.store.ui.pages.search.SearchScreen
import com.kafasan.store.ui.pages.search.SearchViewModel
import com.kafasan.store.ui.pages.search.SuggestSearchScreen
import com.kafasan.store.ui.pages.search.SuggestSearchViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Route.HOME) {
        composable(Route.HOME) {
            HomeScreen(navController, hiltViewModel<HomeViewModel>())
        }
        composable(Route.PRODUCT_DETAIL) { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getString("id")

            id?.let {
                DetailProductScreen(
                    navController = navController,
                    id = it.toInt(),
                    viewModel = hiltViewModel<DetailProductViewModel>()
                )
            }
        }
        composable(Route.FAVORITE) {
            FavoriteScreen(navController, hiltViewModel<FavoriteViewModel>())
        }
        composable(Route.SUGGEST_SEARCH) {
            SuggestSearchScreen(navController, hiltViewModel<SuggestSearchViewModel>())
        }
        composable(Route.SEARCH) { navBackStackEntry ->
            val query = navBackStackEntry.arguments?.getString("query").orEmpty()
            SearchScreen(navController, query, hiltViewModel<SearchViewModel>())
        }
    }
}