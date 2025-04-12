package com.kafasan.store.ui.pages.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafasan.store.data.Product
import com.kafasan.store.domain.local.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val productRepo: ProductRepository
): ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    fun getFavoriteProducts() {
        viewModelScope.launch {
            val result = productRepo.getProducts()
            _products.value = result.map {
                Product(
                    id = it.id,
                    title = it.title,
                    slug = it.slug,
                    images = it.images,
                    price = it.price,
                    description = it.description,
                    creationAt = it.creationAt,
                    category = null,
                    updatedAt = it.updatedAt
                )
            }
        }
    }
}