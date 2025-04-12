package com.kafasan.store.ui.pages.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kafasan.store.data.Product
import com.kafasan.store.domain.network.StorePagingSource
import com.kafasan.store.domain.network.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: StoreRepository
) :ViewModel() {

    fun getProducts(): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(pageSize = 1, enablePlaceholders = false),
            pagingSourceFactory = { StorePagingSource(repository) }
        ).flow.cachedIn(viewModelScope)
    }
}