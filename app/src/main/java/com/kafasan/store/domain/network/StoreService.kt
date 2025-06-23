package com.kafasan.store.domain.network

import com.kafasan.store.data.Product
import com.kafasan.store.domain.network.ApiUrl.PRODUCTS
import com.kafasan.store.domain.network.ApiUrl.PRODUCT_BY_ID
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StoreService {
    @GET(PRODUCTS)
    suspend fun getProducts(
        @Query("offset") offset: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("title") title: String? = null,
    ): Response<List<Product>>

    @GET(PRODUCT_BY_ID)
    suspend fun getProductById(
        @Path("id") id: Int,
    ): Response<Product>
}
