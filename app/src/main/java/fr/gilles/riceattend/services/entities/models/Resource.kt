package fr.gilles.riceattend.services.entities.models

import com.google.gson.annotations.SerializedName

open class Resource(
    @SerializedName("unitPrice") var unitPrice: Long,
    @SerializedName("quantity") var quantity: Long,
    @SerializedName("resourceType") var resourceType: ResourceType,
    @SerializedName("name") var name: String,
) : Audit()

data class ResourcePayload(
    @SerializedName("unitPrice") var unitPrice: Long,
    @SerializedName("quantity") var quantity: Long,
    @SerializedName("resourceType") var resourceType: ResourceType,
    @SerializedName("name") var name: String,
)


enum class ResourceType(private val value: String) {
    WATER("WATER"),
    OTHER("OTHER"),
    MATERIALS("MATERIALS"),
    FERTILIZER("FERTILIZER")
}


class Plant(
    @SerializedName("color") var color: String,
    @SerializedName("description") var description: String,
    @SerializedName("shape") var shape: String,
    @SerializedName("name") var name: String,
    @SerializedName("image") var image: String
) : Audit()


data class PlantPayload(
    @SerializedName("unitPrice") var unitPrice: Long,
    @SerializedName("name") var name: String,
    @SerializedName("color") var color: String,
    @SerializedName("description") var description: String,
    @SerializedName("shape") var shape: String,
)
