package fr.gilles.riceattend.services.repositories.retrofit

import fr.gilles.riceattend.models.Statistics
import retrofit2.Call
import retrofit2.http.GET

interface StatisticsRepository {
    @GET("statistics")
    fun statistics():Call<Statistics>
}