package com.kafasan.store.domain.network

import retrofit2.Response

suspend fun <T: Any> safeCallApi(call: suspend () -> Response<T>) : Result<T> {
    return try {
        val response = call.invoke()
        if (response.isSuccessful && response.body() != null) {
            Result.Success(response.body()!!)
        } else {
            val message = response.errorBody().toString()
            Result.Error(response.code(), Exception(message))
        }
    } catch (e: Exception) {
        Result.Error(0, e)
    }
}