package fr.gilles.riceattend.services.entities.models

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.annotations.SerializedName
import java.time.Duration
import java.util.*

data class Activity(
    @SerializedName("name") var name: String,
    @SerializedName("description") var description: String,
    @SerializedName("startInstant") var startDate: Date,
    @SerializedName("endInstant") var endDate: Date,
    @SerializedName("status") var status: ActivityStatus,
    @SerializedName("estimatedPrice") var estimatedPrice: Double,
    @SerializedName("realPrice") var realPrice: Double,
) : Audit() {
    @RequiresApi(Build.VERSION_CODES.O)
    fun duration(): String {
        val duration = (
                Duration.between(
                    startDate.toInstant(),
                    endDate.toInstant()
                ).seconds / (3600 * 24))
        return if (duration.toInt() != 0) {
            "${duration.toInt()}  Jours"
        } else {
            "${(duration * 24).toInt()} heures"
        }

    }
}


data class ActivityWithDetails(
    @SerializedName("name") var name: String,
    @SerializedName("description") var description: String,
    @SerializedName("startInstant") var startDate: Date,
    @SerializedName("endInstant") var endDate: Date,
    @SerializedName("status") var status: ActivityStatus,
    @SerializedName("estimatedPrice") var estimatedPrice: Double,
    @SerializedName("realPrice") var realPrice: Double,
    @SerializedName("activityPaddyFields") var activityPaddyFields: List<ActivityPaddyFieldWithoutActivity>,
    @SerializedName("activityWorkers") var activityWorkers: List<ActivityWorkerWithoutActivity>,
    @SerializedName("activityResources") var activityResources: List<ActivityResourceWithoutActivity>,

    ) : Audit() {


    @RequiresApi(Build.VERSION_CODES.O)
    fun duration(): String {
        val duration = (
                Duration.between(
                    startDate.toInstant(),
                    endDate.toInstant()
                ).seconds / (3600 * 24))
        return if (duration.toInt() != 0) {
            "${duration.toInt()}  Jours"
        } else {
            "${(duration * 24).toInt()} heures"
        }

    }
}


data class ActivityPaddyField(
    @SerializedName("activity") var activity: Activity,
    @SerializedName("paddyField") var paddyField: PaddyField,
    @SerializedName("activityStatus") var status: ActivityStatus,
) : Audit()


data class ActivityPaddyFieldWithoutActivity(
    @SerializedName("paddyField") var paddyField: PaddyField,
    @SerializedName("activityStatus") var status: ActivityStatus,
) : Audit()

data class ActivityPaddyFieldWithoutPaddyField(
    @SerializedName("activity") var activity: Activity,
    @SerializedName("activityStatus") var status: ActivityStatus,
) : Audit()

data class ActivityWorker(
    @SerializedName("activity") var activity: Activity,
    @SerializedName("worker") var worker: Worker,
    @SerializedName("price") var price: Double,
) : Audit()


data class ActivityWorkerWithoutActivity(
    @SerializedName("worker") var worker: Worker,
    @SerializedName("price") var price: Double,
) : Audit()

data class ActivityWorkerWithoutWorker(
    @SerializedName("activity") var activity: Activity,
    @SerializedName("price") var price: Double,
) : Audit()

data class ActivityResource(
    @SerializedName("activity") var activity: Activity,
    @SerializedName("resource") var resource: Resource,
    @SerializedName("usedQuantity") var quantity: Float,
    @SerializedName("value") var value: Double,
) : Audit()

data class ActivityResourceWithoutActivity(
    @SerializedName("resource") var resource: Resource,
    @SerializedName("usedQuantity") var quantity: Float,
    @SerializedName("value") var value: Double,
) : Audit()

data class ActivityResourceWithoutResource(
    @SerializedName("activity") var activity: Activity,
    @SerializedName("usedQuantity") var quantity: Float,
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

enum class ActivityStatus(val value: String, val label: String) {
    INIT("INIT", "En attente"),
    IN_PROGRESS("IN_PROGRESS", "En cours"),
    CANCELLED("CANCELLED", "Annulé"),
    UNDONE("UNDONE", "Non effectué"),
    DONE("DONE", "Effectué"),
}

data class ActivityResourcePayload(
    var resourceCode: String,
    var quantity: Float
)