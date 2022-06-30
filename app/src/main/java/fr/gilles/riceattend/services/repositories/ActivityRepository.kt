package fr.gilles.riceattend.services.repositories

import fr.gilles.riceattend.services.entities.models.*
import retrofit2.Call
import retrofit2.http.*

interface ActivityRepository {

    @GET("activity")
    @JvmSuppressWildcards
    fun getActivities(@QueryMap params: Map<String, Any>): Call<Page<Activity>>

    @GET("activity/{code}")
    fun get(@Path("code") code: String): Call<Activity>


    @POST("activity")
    fun create(@Body activity: ActivityPayload): Call<Activity>


    @GET("activity/{code}/workers")
    fun getActivityWorkers(@Path("code") code: String): Call<List<ActivityWorker>>


    @GET("activity/{code}/paddyFields")
    fun getActivityPaddyFields(@Path("code") code: String): Call<List<ActivityPaddyField>>

    @GET("activity/{code}/resources")
    fun getActivityResources(@Path("code") code: String): Call<List<ActivityResource>>


}