package fr.gilles.riceattend.utils

import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


fun <T> callFrom( value: T): Call<T>{
    return object : Call<T> {
        override fun enqueue(callback: Callback<T>) {
            callback.onResponse(this, Response.success(value))
        }

        override fun isExecuted(): Boolean {
            return true
        }

        override fun clone(): Call<T> {
            return this
        }

        override fun isCanceled(): Boolean {
            return false
        }

        override fun cancel() {
        }

        override fun execute(): Response<T> {
            return Response.success(value)
        }

        override fun request(): Request {
            return Request.Builder().url("http://localhost").build()
        }

        override fun timeout(): Timeout {
            return Timeout()
        }
    }
}