package fr.gilles.riceattend.services.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiService {
    private val baseUrl:String = "https://riceattend.herokuapp.com/"

    private val gson: Gson by lazy{
        GsonBuilder().setLenient().create()
    }

    private val httpClient : OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(logger)
            .build()
    }

    private val headerInterceptor = Interceptor { chain ->
        val request = chain.request()
//    val newRequest = Session.getBearerToken()?.let {
//        request.newBuilder()
//            .addHeader("Content-Type", "application/json")
//            .addHeader("Accept", "application/json")
//            .addHeader("Authorization", it)
//            .build()
//    } ?: request
        chain.proceed(request = request)
    }


    private val logger: HttpLoggingInterceptor = run {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val retrofit : Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun<T> buildRepository(service:Class<T>):T{
        return retrofit.create(service)
    }
}