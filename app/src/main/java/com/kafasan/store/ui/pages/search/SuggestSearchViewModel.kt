package com.kafasan.store.ui.pages.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafasan.store.data.Product
import com.kafasan.store.domain.network.Result
import com.kafasan.store.domain.network.StoreRepository
import com.kafasan.store.domain.network.TimerUtility
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SuggestSearchViewModel @Inject constructor(
    private val timerUtility: TimerUtility,
    private val storeRepo: StoreRepository
): ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products = _products.asStateFlow()

    private val _query = MutableStateFlow("")

    init {
        viewModelScope.launch {
            _query
                .debounce(timerUtility.debounceTime())
                .distinctUntilChanged()
                .collectLatest { query ->
                    if (query.isNotBlank()) {
                        when(val products = storeRepo.getProducts(title = query)) {
                            is Result.Error -> Unit // do nothing
                            is Result.Success -> {
                                _products.value = products.data
                            }
                        }
                    } else {
                        _products.value = emptyList()
                    }
                }
        }
    }

    fun getSuggestion(query: String) {
        _query.value = query
    }
}