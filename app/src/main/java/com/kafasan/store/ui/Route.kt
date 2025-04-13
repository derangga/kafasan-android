package com.kafasan.store.ui

object Route {
    const val HOME = "home"
    const val FAVORITE = "favorite"
    const val PRODUCT_DETAIL = "product/{id}"
    const val SUGGEST_SEARCH = "suggest-search"
    const val SEARCH = "search/{query}"

    fun productDetail(id: Long): String {
        return "product/$id"
    }

    fun search(query: String): String {
        return "search/$query"
    }
}