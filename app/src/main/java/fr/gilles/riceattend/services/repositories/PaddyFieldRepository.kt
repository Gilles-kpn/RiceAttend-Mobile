package fr.gilles.riceattend.services.repositories

import fr.gilles.riceattend.services.entities.models.PaddyField
import fr.gilles.riceattend.services.entities.models.PaddyFieldDetails
import fr.gilles.riceattend.services.entities.models.PaddyFieldPayLoad
import fr.gilles.riceattend.services.entities.models.Page
import retrofit2.Call
import retrofit2.http.*

interface PaddyFieldRepository {

    @GET("paddyfield")
    @JvmSuppressWildcards
    fun get(@QueryMap params: Map<String, Any>): Call<Page<PaddyField>>

    @GET("paddyfield/{code}")
    fun get(@Path("code") code: String): Call<PaddyFieldDetails>

    @POST("paddyfield")
    fun create(@Body toPaddyFieldPayload: PaddyFieldPayLoad): Call<PaddyField>


    @PUT("paddyfield/{code}")
    fun update(
        @Path("code") code: String,
        @Body toPaddyFieldPayload: PaddyFieldPayLoad
    ): Call<PaddyFieldDetails>

}