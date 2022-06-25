package fr.gilles.riceattend.services.repositories

import fr.gilles.riceattend.services.entities.models.LoginUser
import fr.gilles.riceattend.services.entities.models.RegisterUser
import fr.gilles.riceattend.services.entities.models.UpdatePassword
import fr.gilles.riceattend.services.entities.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthRepository {
    @POST("auth/login")
    fun login(@Body loginUser: LoginUser): Call<String>

    @POST("auth/register")
    fun register(@Body registerUser: RegisterUser): Call<String>

    @GET("auth/activate/{url}")
    fun activate(@Path("url") url: String): Call<String>

    @GET("auth/current")
    fun current(): Call<User>

    @POST("auth/current/password/change")
    fun updatePassword(@Body updatePassword: UpdatePassword): Call<String>
}