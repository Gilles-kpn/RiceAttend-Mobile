package fr.gilles.riceattend.services.entities.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class Activity(
    @SerializedName("name") var name: String,
    @SerializedName("startDate") var startDate: Date,
    @SerializedName("endDate") var endDate: Date,
    @SerializedName("status") var status: ActivityStatus,
    @SerializedName("estimatedPrice") var estimatedPrice: Double,
    @SerializedName("realPrice") var realPrice: Double,
) : Audit()


data class ActivityPaddyField(
    @SerializedName("activity") var activity: Activity,
    @SerializedName("paddyField") var paddyField: PaddyField,
    @SerializedName("status") var status: ActivityStatus,
) : Audit()


data class ActivityWorker(
    @SerializedName("activity") var activity: Activity,
    @SerializedName("worker") var worker: Worker,
    @SerializedName("price") var price: Double,
) : Audit()


data class ActivityResource(
    @SerializedName("activity") var activity: Activity,
    @SerializedName("resource") var resource: Resource,
    @SerializedName("quantity") var quantity: Float,
    @SerializedName("value") var value: Double,
) : Audit()

enum class ActivityStatus(val value: String) {
    INIT("INIT"),
    IN_PROGRESS("IN_PROGRESS"),
    CANCELLED("CANCELLED"),
    UNDONE("UNDONE"),
    DONE("DONE")
}