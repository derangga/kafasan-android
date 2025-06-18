package com.kafasan.store.ui

sealed class Route(val route: String) {
    data object Login : Route("login")
    data object Home : Route("home")
    data object Favorite : Route("favorite")
    data object ProductDetail : Route("product/{id}") {
        fun createRoute(id: Long) = "product/$id"
        fun deeplinkURI() = "kfapp://kafasan.com/product/{id}"
    }
    data object SuggestSearch : Route("suggest-search")
    data object Search : Route("search/{query}") {
        fun createRoute(query: String) = "search/$query"
    }
}