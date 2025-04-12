package com.kafasan.store.domain.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kafasan.store.data.ProductEntity

@Dao
interface ProductDao {
    @Insert
    suspend fun insert(data: ProductEntity): Long

    @Query("delete from product where id=:id")
    suspend fun delete(id: Long): Int

    @Query("select * from product")
    suspend fun getProducts(): List<ProductEntity>

    @Query("select * from product where id=:id")
    suspend fun getProductById(id: Long): ProductEntity?
}