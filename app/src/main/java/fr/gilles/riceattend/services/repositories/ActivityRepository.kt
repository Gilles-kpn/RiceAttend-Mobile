package fr.gilles.riceattend.services.repositories

import fr.gilles.riceattend.services.entities.models.Activity
import fr.gilles.riceattend.services.entities.models.Page
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface ActivityRepository {

    @GET("activity")
    @JvmSuppressWildcards
    fun getActivities(@QueryMap params:Map<String, Any>):Call<Page<Activity>>

    @GET("activity/code")
    fun get(@Path("code") code:String):Call<Activity>


}