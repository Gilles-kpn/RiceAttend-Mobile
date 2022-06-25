package fr.gilles.riceattend.services.entities.models

import com.google.gson.annotations.SerializedName

data class PaddyField(
    @SerializedName("name") var name: String,
    @SerializedName("surface") var surface: Surface,
    @SerializedName("description") var description: String?,
    @SerializedName("numberOfPlants") var numberOfPlants: Int,
    @SerializedName("plant") var plant: Plant
) : Audit()


data class Surface(
    @SerializedName("unit") var unit: String,
    @SerializedName("value") var value: Long
)


data class PaddyFielPayLoad(
    @SerializedName("name") var name: String,
    @SerializedName("surface") var surface: Surface,
    @SerializedName("description") var description: String?,
    @SerializedName("numberOfPlants") var numberOfPlants: Int,
    @SerializedName("plantCode") var plantCode: String
)
