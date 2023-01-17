package fr.gilles.riceattend.models

import androidx.room.*
import com.google.gson.annotations.SerializedName
import fr.gilles.riceattend.services.repositories.room.converters.ResourceTypeConverter

@Entity(tableName = "resources")
@TypeConverters(ResourceTypeConverter::class)
open class Resource(
    @ColumnInfo(name = "unit_price") @SerializedName("unitPrice") var unitPrice: Long,
    @ColumnInfo(name = "quantity") @SerializedName("quantity") var quantity: Long,
    @ColumnInfo(name = "resource_type") @SerializedName("resourceType") var resourceType: ResourceType,
    @ColumnInfo(name = "name") @SerializedName("name") var name: String,
) : Audit()


enum class ResourceType( val value: String,  val label: String) {
    WATER("WATER", "Eau"),
    OTHER("OTHER", "Autre"),
    MATERIALS("MATERIALS", "Materiel"),
    FERTILIZER("FERTILIZER", "Fertilisant");
}


data class ResourcePayload(
    @SerializedName("unitPrice") var unitPrice: Long,
    @SerializedName("quantity") var quantity: Long,
    @SerializedName("resourceType") var resourceType: ResourceType,
    @SerializedName("name") var name: String,
)

data class  AddQuantity(
    @SerializedName("quantity") var quantity: Long,
)

data class ResourceDetails(
    @SerializedName("unitPrice") var unitPrice: Long,
    @SerializedName("quantity") var quantity: Long,
    @SerializedName("resourceType") var resourceType: ResourceType,
    @SerializedName("name") var name: String,
    @SerializedName("activityResources") var activityResources: List<ActivityResourceWithoutResource>,
) : Audit()




@Entity(tableName = "plants")
data class Plant(
    @ColumnInfo(name = "color") @SerializedName("color") var color: String,
    @ColumnInfo(name = "description") @SerializedName("description") var description: String,
    @ColumnInfo(name = "shape") @SerializedName("shape") var shape: String,
    @ColumnInfo(name = "name") @SerializedName("name") var name: String,
    @ColumnInfo(name = "cultivation_time") @SerializedName("cultivationTime") var cultivationTime:String?,
    @ColumnInfo(name = "variety") @SerializedName("variety") var variety:String?,
    @ColumnInfo(name = "image") @SerializedName("image") var image: String,
) : Audit()


data class PlantPayload(
    @SerializedName("unitPrice") var unitPrice: Long,
    @SerializedName("name") var name: String,
    @SerializedName("color") var color: String,
    @SerializedName("description") var description: String,
    @SerializedName("shape") var shape: String,
)
