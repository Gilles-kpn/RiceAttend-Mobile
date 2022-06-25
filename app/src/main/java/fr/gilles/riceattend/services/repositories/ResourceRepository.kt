package fr.gilles.riceattend.services.repositories

import fr.gilles.riceattend.services.entities.models.Page
import fr.gilles.riceattend.services.entities.models.Plant
import fr.gilles.riceattend.services.entities.models.Resource
import fr.gilles.riceattend.services.entities.models.ResourcePayload
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface ResourceRepository {
    @POST("resource")
    fun create(@Body resourcePayloader: ResourcePayload): Call<Resource>

    @GET("resource/plant")
    @JvmSuppressWildcards
    fun getPlant(@QueryMap params: Map<String, Any>): Call<Page<Plant>>

    @GET("resource")
    @JvmSuppressWildcards
    fun get(@QueryMap params: Map<String, Any>): Call<Page<Resource>>

}