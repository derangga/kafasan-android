package com.kafasan.store.domain.network

import com.kafasan.store.data.Product

class StoreRepositoryImpl(private val service: StoreService) : StoreRepository {
    override suspend fun getProducts(
        offset: Int?,
        limit: Int?,
        title: String?,
    ): Result<List<Product>> {
        return safeCallApi { service.getProducts(offset, limit, title) }
    }

    override suspend fun getProductById(path: Int): Result<Product> {
        return safeCallApi { service.getProductById(path) }
    }
}
