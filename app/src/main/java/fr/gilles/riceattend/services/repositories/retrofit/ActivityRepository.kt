package fr.gilles.riceattend.services.repositories.retrofit

import fr.gilles.riceattend.models.*
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
    fun delete(@Path("code") code:String): Call<Unit>





    @POST("activity/{code}/paddyFields")
    fun addPaddyFieldsToActivity(@Path("code") code:String, @Body paddyFieldsCode:List<String>):Call<List<ActivityPaddyFieldWithoutActivity>>

    @POST("activity/{code}/workers")
    fun addWorkersToActivity(@Path("code") code:String, @Body workersCode:List<String>):Call<List<ActivityWorkerWithoutActivity>>


    @PUT("activity/{code}/undone")
    fun undoneActivity(@Path("code") code: String): Call<Unit>


    @PUT("activity/{code}/started")
    fun startedActivity(@Path("code") code: String): Call<Unit>

    @PUT("activity/{code}/done")
    fun doneActivity(@Path("code") code: String): Call<Unit>

    @PUT("activity/{code}/cancel")
    fun cancelActivity(@Path("code") code: String): Call<Unit>


    @PUT("activity/{code}/paddyField/{paddyFieldCode}/started")
    fun startedPaddyField(@Path("code") code: String, @Path("paddyFieldCode") paddyFieldCode: String): Call<Unit>

    @PUT("activity/{code}/paddyField/{paddyFieldCode}/done")
    fun donePaddyField(@Path("code") code: String, @Path("paddyFieldCode") paddyFieldCode: String): Call<Unit>

    @PUT("activity/{code}/paddyField/{paddyFieldCode}/cancelled")
    fun cancelPaddyField(@Path("code") code: String, @Path("paddyFieldCode") paddyFieldCode: String): Call<Unit>


    @DELETE("activity/{code}/paddyField/{paddyFieldCode}")
    fun deletePaddyFieldFromActivity(@Path("code") code:String, @Path("paddyFieldCode") paddyFieldCode:String):Call<Unit>


    @DELETE("activity/{code}/worker/{workerCode}")
    fun deleteWorkerFromActivity(@Path("code") code:String, @Path("workerCode") workerCode:String):Call<Unit>

    @DELETE("activity/{code}/resource/{resourceCode}")
    fun deleteResourceFromActivity(@Path("code") code:String, @Path("resourceCode") resourceCode:String):Call<Unit>





}