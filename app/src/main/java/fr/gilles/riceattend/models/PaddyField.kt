package fr.gilles.riceattend.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "paddy_fields")
data class PaddyField(
    @ColumnInfo(name = "name")  @SerializedName("name") var name: String,
    @Embedded @SerializedName("surface") var surface: Surface,
    @ColumnInfo(name = "description") @SerializedName("description") var description: String?,
    @ColumnInfo(name = "number_of_plants") @SerializedName("numberOfPlants") var numberOfPlants: Int,
    @Embedded(prefix = "plant") @SerializedName("plant") var plant: Plant
) : Audit()


data class PaddyFieldDetails(
    @SerializedName("name") var name: String,
    @SerializedName("surface") var surface: Surface,
    @SerializedName("description") var description: String?,
    @SerializedName("numberOfPlants") var numberOfPlants: Int,
    @SerializedName("plant") var plant: Plant,
    @SerializedName("activityPaddyFields") var activityPaddyFields: List<ActivityPaddyFieldWithoutPaddyField>,
) : Audit()


data class Surface(
    @SerializedName("unit") var unit: String,
    @SerializedName("value") var value: Long
)


data class PaddyFieldPayLoad(
    @SerializedName("name") var name: String,
    @SerializedName("surface") var surface: Surface,
    @SerializedName("description") var description: String?,
    @SerializedName("numberOfPlants") var numberOfPlants: Int,
    @SerializedName("plantCode") var plantCode: String
)
