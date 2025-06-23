package com.kafasan.store.domain.local

import com.kafasan.store.data.ProductEntity

class ProductRepositoryImpl(private val productDao: ProductDao) : ProductRepository {
    override suspend fun insert(product: ProductEntity): Long {
        return productDao.insert(product)
    }

    override suspend fun delete(id: Long): Int {
        return productDao.delete(id)
    }

    override suspend fun getProducts(): List<ProductEntity> {
        return productDao.getProducts()
    }

    override suspend fun getProductById(id: Long): ProductEntity? {
        return productDao.getProductById(id)
    }
}
