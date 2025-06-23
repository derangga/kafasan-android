package com.kafasan.store.ui.pages.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafasan.store.data.Product
import com.kafasan.store.data.ProductEntity
import com.kafasan.store.domain.local.ProductRepository
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
class DetailProductViewModel
    @Inject
    constructor(
        private val storeRepo: StoreRepository,
        private val productRepo: ProductRepository,
    ) : ViewModel() {
        private val _product = MutableStateFlow(State())
        val product: StateFlow<State> = _product.asStateFlow()

        fun getProductById(
            id: Int,
            loading: Boolean = false,
        ) {
            viewModelScope.launch {
                if (loading) {
                    _product.value =
                        _product.value.copy(
                            product = ApiLoad.Loading,
                        )
                }
                when (val result = storeRepo.getProductById(id)) {
                    is Result.Error -> {
                        _product.value =
                            _product.value.copy(
                                product = ApiLoad.Error(result.exception),
                            )
                    }
                    is Result.Success -> {
                        val product = productRepo.getProductById(result.data.id)
                        _product.value =
                            _product.value.copy(
                                isFavorite = product != null,
                                product = ApiLoad.Success(result.data),
                            )
                    }
                }
            }
        }

        fun favoriteProduct(data: Product) {
            val isFavorite = product.value.isFavorite
            viewModelScope.launch {
                if (isFavorite) {
                    val result = productRepo.delete(data.id)
                    _product.value =
                        _product.value.copy(
                            isFavorite = result != 1,
                        )
                } else {
                    val entity =
                        ProductEntity(
                            id = data.id,
                            title = data.title,
                            slug = data.slug,
                            images = data.images,
                            price = data.price,
                            description = data.description,
                            creationAt = data.creationAt,
                            updatedAt = data.updatedAt,
                        )
                    val result = productRepo.insert(entity)
                    _product.value =
                        _product.value.copy(
                            isFavorite = result > 0,
                        )
                }
            }
        }
    }
