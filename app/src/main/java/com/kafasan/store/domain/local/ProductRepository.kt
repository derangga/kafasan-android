package com.kafasan.store.domain.local

import com.kafasan.store.data.ProductEntity

interface ProductRepository {
    suspend fun insert(product: ProductEntity): Long

    suspend fun delete(id: Long): Int

    suspend fun getProducts(): List<ProductEntity>

    suspend fun getProductById(id: Long): ProductEntity?
}
