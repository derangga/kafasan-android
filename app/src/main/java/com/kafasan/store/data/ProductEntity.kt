package com.kafasan.store.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class ProductEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val slug: String,
    val price: Long,
    val description: String,
    val images: List<String>,
    val creationAt: String,
    val updatedAt: String,
)
