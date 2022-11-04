package fr.gilles.riceattend.services.repositories

import fr.gilles.riceattend.services.entities.models.*
import retrofit2.Call
import retrofit2.http.*

interface ActivityRepository {

    @GET("activity")
    @JvmSuppressWildcards
    fun getActivities(@QueryMap(encoded = true) params: Map<String, Any> = Params().toMap(), @Query("status") status:List<String> = listOf()): Call<Page<Activity>>

    @GET("activity/{code}")
    fun get(@Path("code") code: String): Call<ActivityWithDetails>


    @POST("activity")
    fun create(@Body activity: ActivityPayload): Call<Activity>


    @DELETE("activity/{code}")
    fun delete(@Path("code") code:String): Call<Void>





    @POST("activity/{code}/paddyFields")
    fun addPaddyFieldsToActivity(@Path("code") code:String, @Body paddyFieldsCode:List<String>):Call<List<ActivityPaddyFieldWithoutActivity>>

    @POST("activity/{code}/workers")
    fun addWorkersToActivity(@Path("code") code:String, @Body workersCode:List<String>):Call<List<ActivityWorkerWithoutActivity>>


    @PUT("activity/{code}/undone")
    fun undoneActivity(@Path("code") code: String): Call<Void>


    @PUT("activity/{code}/started")
    fun startedActivity(@Path("code") code: String): Call<Void>

    @PUT("activity/{code}/done")
    fun doneActivity(@Path("code") code: String): Call<Void>

    @PUT("activity/{code}/cancel")
    fun cancelActivity(@Path("code") code: String): Call<Void>

    @PUT("activity/{code}/paddyFields/{paddyFieldCode}/undone")
    fun undonePaddyField(@Path("code") code: String, @Path("paddyFieldCode") paddyFieldCode: String): Call<Any>

    @PUT("activity/{code}/paddyFields/{paddyFieldCode}/started")
    fun startedPaddyField(@Path("code") code: String, @Path("paddyFieldCode") paddyFieldCode: String): Call<Any>

    @PUT("activity/{code}/paddyFields/{paddyFieldCode}/done")
    fun donePaddyField(@Path("code") code: String, @Path("paddyFieldCode") paddyFieldCode: String): Call<Any>

    @PUT("activity/{code}/paddyFields/{paddyFieldCode}/cancelled")
    fun cancelPaddyField(@Path("code") code: String, @Path("paddyFieldCode") paddyFieldCode: String): Call<Any>


    @DELETE("activity/{code}/paddyFields/{paddyFieldCode}")
    fun deletePaddyFieldFromActivity(@Path("code") code:String, @Path("paddyFieldCode") paddyFieldCode:String):Call<Any>


    @DELETE("activity/{code}/workers/{workerCode}")
    fun deleteWorkerFromActivity(@Path("code") code:String, @Path("workerCode") workerCode:String):Call<Any>

    @DELETE("activity/{code}/resources/{resourceCode}")
    fun deleteResourceFromActivity(@Path("code") code:String, @Path("resourceCode") resourceCode:String):Call<Any>





}