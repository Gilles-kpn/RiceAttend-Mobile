package fr.gilles.riceattend.models

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.*
import com.google.gson.annotations.SerializedName
import fr.gilles.riceattend.services.repositories.room.converters.DateConverter
import java.time.Duration
import java.util.*

@Entity(tableName = "activity")
@TypeConverters(DateConverter::class)
data class Activity(
    @SerializedName("name")
    @ColumnInfo(name = "name")
    var name: String,
    @SerializedName("description")
    @ColumnInfo(name = "description")
    var description: String,
    @SerializedName("startInstant")
    @ColumnInfo(name = "start_date")
    var startDate: Date,
    @SerializedName("endInstant")
    @ColumnInfo(name = "end_date")
    var endDate: Date,
    @SerializedName("status")
    @ColumnInfo(name = "status")
    var status: ActivityStatus,
    @SerializedName("estimatedPrice")
    @ColumnInfo(name = "estimated_price")
    var estimatedPrice: Double,
    @SerializedName("realPrice")
    @ColumnInfo(name = "real_price")
    var realPrice: Double,
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

@Entity(tableName = "activity")
data class ActivityWithDetails(
    @SerializedName("name")
    @ColumnInfo(name = "name")
    var name: String,
    @SerializedName("description")
    @ColumnInfo(name = "description")
    var description: String,
    @SerializedName("startInstant")
    @ColumnInfo(name = "start_date")
    var startDate: Date,
    @SerializedName("endInstant")
    @ColumnInfo(name = "end_date")
    var endDate: Date,
    @SerializedName("status")
    @ColumnInfo(name = "status")
    var status: ActivityStatus,
    @SerializedName("estimatedPrice")
    @ColumnInfo(name = "estimated_price")
    var estimatedPrice: Double,
    @SerializedName("type")
    @ColumnInfo(name = "type")
    var type: ActivityType?,
    @SerializedName("realPrice")
    @ColumnInfo(name = "real_price")
    var realPrice: Double,
    @SerializedName("activityPaddyFields")
    @ColumnInfo(name = "activity_paddy_fields")
    var activityPaddyFields: List<ActivityPaddyFieldWithoutActivity>,
    @SerializedName("activityWorkers")
    var activityWorkers: List<ActivityWorkerWithoutActivity>,
    @SerializedName("activityResources")
    var activityResources: List<ActivityResourceWithoutActivity>,

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

    fun cost(): Double {
        var workerCost = activityWorkers.map { e->e.price }.sum()
        return activityWorkers.sumOf { e -> e.price } + activityResources.sumOf { e -> e.value }
    }
}


data class ActivityPaddyField(
    @SerializedName("activity") var activity: Activity,
    @SerializedName("paddyField") var paddyField: PaddyField,
    @SerializedName("activityStatus") var status: ActivityStatus,
) : Audit()


data class ActivityPaddyFieldWithoutActivity(
    @SerializedName("paddyField")
    var paddyField: PaddyField,
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

@Entity(tableName = "activity_resource",
    primaryKeys = ["activity_code", "resource_code"],
    foreignKeys = [
        ForeignKey(entity = Activity::class, parentColumns = ["code"], childColumns = ["activity_code"]),
        ForeignKey(entity = Resource::class, parentColumns = ["code"], childColumns = ["resource_code"])
    ]
)
data class ActivityResource(
    @SerializedName("activity") @Embedded var activity: Activity,
    @SerializedName("resource") @Embedded var resource: Resource,
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
    @SerializedName("type") var type: ActivityType,
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


enum class ActivityType(val value: String, val label: String) {
    HARVEST("HARVEST", "Récolte"),
    PLANTING("PLANTING", "Plantation"),
    MAINTENANCE("MAINTENANCE", "Entretien"),
    OTHER("OTHER", "Autre"),
}
data class ActivityResourcePayload(
    var resourceCode: String,
    var quantity: Float
)