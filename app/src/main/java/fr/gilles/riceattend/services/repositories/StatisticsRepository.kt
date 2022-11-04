package fr.gilles.riceattend.services.repositories

import fr.gilles.riceattend.services.entities.models.Statistics
import retrofit2.Call
import retrofit2.http.GET

interface StatisticsRepository {
    @GET("statistics")
    fun statistics():Call<Statistics>
}