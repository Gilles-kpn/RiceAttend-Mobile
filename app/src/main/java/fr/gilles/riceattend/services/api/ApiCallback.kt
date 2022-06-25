package fr.gilles.riceattend.services.api

import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class ApiCallback<T> : Callback<T> {
    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.isSuccessful) {
            onSuccess(response.body()!!)
        } else {
            val apiResponseError: ApiResponseError? = Gson().fromJson(
                (response.errorBody())?.string(),
                ApiResponseError::class.java
            )
            apiResponseError?.let {
                onError(it)
            } ?: run {
                onError(
                    ApiResponseError(
                        "",
                        "",
                        "",
                        0,
                        ""
                    )
                )
            }

        }
    }

    abstract fun onSuccess(response: T)
    abstract fun onError(error: ApiResponseError)


    override fun onFailure(call: Call<T>, t: Throwable) {
        t.printStackTrace()
        Log.e("ApiCallback Error", t.message.toString())
    }
}

data class ApiResponseError(
    @SerializedName("error") val error: String,
    @SerializedName("message") val message: String,
    @SerializedName("path") val path: String,
    @SerializedName("status") val status: Int,
    @SerializedName("timestamp") val timestamp: String
)
