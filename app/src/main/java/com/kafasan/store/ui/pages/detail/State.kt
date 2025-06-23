package com.kafasan.store.ui.pages.detail

import com.kafasan.store.data.Product
import com.kafasan.store.domain.network.ApiLoad

data class State(
    val isFavorite: Boolean = false,
    val product: ApiLoad<Product> = ApiLoad.Loading,
)
