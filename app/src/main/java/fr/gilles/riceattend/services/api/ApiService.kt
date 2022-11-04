package fr.gilles.riceattend.services.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import fr.gilles.riceattend.services.app.SessionManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

class ApiService {
    companion object {
         private fun baseUrl(): String = "https://riceattend.herokuapp.com/"

        val gson: Gson by lazy {
            val builder = GsonBuilder()
            builder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer<Any?> { json, _, _ -> Date(json.asJsonPrimitive.asLong * 1000) })
            builder.create()
        }

        val httpClient: OkHttpClient by lazy {
            OkHttpClient.Builder()
                .addInterceptor(headerInterceptor)
                .addInterceptor(logger)
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build()
        }

        private val headerInterceptor = Interceptor { chain ->
            chain.proceed(
                request = chain.request().newBuilder().addHeader(
                    "Authorization",
                    SessionManager.session.authorization
                ).build()
            )
        }


        private val logger: HttpLoggingInterceptor = run {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.apply {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            }
        }

         val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(baseUrl())
                .client(httpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }

        fun <T> buildRepository(service: Class<T>): T {
            return retrofit.create(service)
        }
    }
}