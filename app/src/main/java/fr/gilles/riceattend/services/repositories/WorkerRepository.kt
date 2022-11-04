package fr.gilles.riceattend.services.repositories

import fr.gilles.riceattend.services.entities.models.Page
import fr.gilles.riceattend.services.entities.models.Worker
import fr.gilles.riceattend.services.entities.models.WorkerDetails
import fr.gilles.riceattend.services.entities.models.WorkerPayload
import retrofit2.Call
import retrofit2.http.*

interface WorkerRepository {
    @POST("worker")
    fun create(@Body worker: WorkerPayload): Call<Worker>

    @GET("worker")
    @JvmSuppressWildcards
    fun get(@QueryMap params: Map<String, Any>): Call<Page<Worker>>

    @GET("worker/{code}")
    fun get(@Path("code") code: String): Call<WorkerDetails>


    @PUT("worker/{code}")
    fun update(@Path("code") code: String, @Body worker: WorkerPayload): Call<WorkerDetails>
}