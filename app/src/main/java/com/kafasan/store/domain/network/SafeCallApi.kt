package com.kafasan.store.domain.network

import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

suspend fun <T: Any> safeCallApi(call: suspend () -> Response<T>) : Result<T> {
    return try {
        val response = call.invoke()
        if (response.isSuccessful && response.body() != null) {
            Result.Success(response.body()!!)
        } else {
            val message = response.errorBody()?.getErrorMessage()
            Result.Error(response.code(), Exception(message))
        }
    } catch (e: Exception) {
        Result.Error(0, e)
    }
}

fun ResponseBody.getErrorMessage(): String {
    return try {
        val jsonParser = JSONObject(this.string())
        jsonParser.getString("errors")
    } catch (e: Exception){
        e.message.orEmpty()
    }
}