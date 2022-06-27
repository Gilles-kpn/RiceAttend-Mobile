package fr.gilles.riceattend.services.repositories

import fr.gilles.riceattend.services.entities.models.Activity
import fr.gilles.riceattend.services.entities.models.ActivityPayload
import fr.gilles.riceattend.services.entities.models.Page
import retrofit2.Call
import retrofit2.http.*

interface ActivityRepository {

    @GET("activity")
    @JvmSuppressWildcards
    fun getActivities(@QueryMap params:Map<String, Any>):Call<Page<Activity>>

    @GET("activity/code")
    fun get(@Path("code") code:String):Call<Activity>


    @POST("activity")
    fun create(@Body activity: ActivityPayload):Call<Activity>


}