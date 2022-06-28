package fr.gilles.riceattend.services.entities.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class Activity(
    @SerializedName("name") var name: String,
    @SerializedName("description") var description: String,
    @SerializedName("startInstant") var startDate: Date,
    @SerializedName("endInstant") var endDate: Date,
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


data class ActivityPayload(
    @SerializedName("name") var name: String,
    @SerializedName("startInstant") var startDate: String,
    @SerializedName("endInstant") var endDate: String,
    @SerializedName("description") var description: String,
    @SerializedName("paddyFields") var paddyFields: List<String>,
    @SerializedName("workers") var workers: List<String>,
    @SerializedName("resources") var resources: List<ActivityResourcePayload>,

    )

enum class ActivityStatus(val value: String) {
    INIT("INIT"),
    IN_PROGRESS("IN_PROGRESS"),
    CANCELLED("CANCELLED"),
    UNDONE("UNDONE"),
    DONE("DONE")
}

data class ActivityResourcePayload(
    var resourceCode: String,
    var quantity: Float
)