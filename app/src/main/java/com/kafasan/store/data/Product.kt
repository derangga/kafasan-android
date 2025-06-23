package com.kafasan.store.data

data class Product(
    val id: Long,
    val title: String,
    val slug: String,
    val price: Long,
    val description: String,
    val category: Category?,
    val images: List<String>,
    val creationAt: String,
    val updatedAt: String,
)

data class Category(
    val id: Long,
    val name: String,
    val slug: String,
    val image: String,
    val creationAt: String,
    val updatedAt: String,
)
