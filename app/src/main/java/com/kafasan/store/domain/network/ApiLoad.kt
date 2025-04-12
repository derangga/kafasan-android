package com.kafasan.store.domain.network

sealed class ApiLoad<out T: Any> {
    data object Loading: ApiLoad<Nothing>()
    data class Success<out T: Any>(val data: T): ApiLoad<T>()
    data class Error(val exception: Exception): ApiLoad<Nothing>()
}
