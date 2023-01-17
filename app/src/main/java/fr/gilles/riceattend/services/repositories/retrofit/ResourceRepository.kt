package fr.gilles.riceattend.services.repositories.retrofit

import fr.gilles.riceattend.models.*
import retrofit2.Call
import retrofit2.http.*

interface ResourceRepository {
    @POST("resource")
    fun create(@Body resourcePayloader: ResourcePayload): Call<Resource>

    @POST("resource/{code}")
    fun addQuantity(@Path("code")  code:String, @Body addQuantity: AddQuantity): Call<Unit>

    @GET("resource/plant")
    @JvmSuppressWildcards
    fun getPlant(@QueryMap params: Map<String, Any>): Call<Page<Plant>>

    @GET("resource")
    @JvmSuppressWildcards
    fun get(@QueryMap params: Map<String, Any>): Call<Page<Resource>>


    @GET("resource/{code}")
    fun get(@Path("code")  code:String): Call<ResourceDetails>

}