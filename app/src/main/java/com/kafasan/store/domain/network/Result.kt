package com.kafasan.store.domain.network

sealed class Result<out T: Any> {
    data class Success<out T: Any>(val data: T): Result<T>()
    data class Error(val status: Int, val exception: Exception): Result<Nothing>()
}