package com.kafasan.store.domain.network

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kafasan.store.data.Product

class StorePagingSource(
    private val repos: StoreRepository,
): PagingSource<Int, Product>() {

    private val offsetInitial = 0
    private val limit = 20
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val offset = params.key ?: offsetInitial
        return when (val result = repos.getProducts(offset, limit)) {
            is Result.Error ->  LoadResult.Error(result.exception)
            is Result.Success -> {
                LoadResult.Page(
                    data = result.data,
                    prevKey = if (offset == offsetInitial) null else offset - limit,
                    nextKey = if (result.data.isEmpty()) null else offset + limit
                )
            }
        }
    }
}