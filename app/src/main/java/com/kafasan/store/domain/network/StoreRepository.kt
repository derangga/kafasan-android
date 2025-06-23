package com.kafasan.store.domain.network

import com.kafasan.store.data.Product

interface StoreRepository {
    suspend fun getProducts(
        offset: Int? = null,
        limit: Int? = null,
        title: String? = null,
    ): Result<List<Product>>

    suspend fun getProductById(path: Int): Result<Product>
}
