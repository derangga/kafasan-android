package com.kafasan.store.ui.pages.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafasan.store.data.Product
import com.kafasan.store.domain.network.ApiLoad
import com.kafasan.store.domain.network.Result
import com.kafasan.store.domain.network.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val storeRepo: StoreRepository
): ViewModel() {
    private val _products = MutableStateFlow<ApiLoad<List<Product>>>(ApiLoad.Loading)
    val products: StateFlow<ApiLoad<List<Product>>> = _products.asStateFlow()

    fun searchProduct(query: String) {
        viewModelScope.launch {
            when(val result = storeRepo.getProducts(title = query)) {
                is Result.Error -> {
                    _products.value = ApiLoad.Error(result.exception)
                }
                is Result.Success -> {
                    _products.value = ApiLoad.Success(result.data)
                }
            }
        }
    }
}