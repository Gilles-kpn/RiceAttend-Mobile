package fr.gilles.riceattend.models

import com.google.gson.annotations.SerializedName

data class Statistics(
    @SerializedName("activitiesNumber") val activitiesNumber:Int,
    @SerializedName("paddyFieldsNumber") val paddyFieldsNumber:Int,
    @SerializedName("workersNumber") val workersNumber:Int,
    @SerializedName("resourcesNumber") val resourcesNumber:Int,
    @SerializedName("activitiesData") val activitiesData: ActivitiesData,
    @SerializedName("resourcesData") val resourcesData: ResourcesData
)

data class ActivitiesData(
    @SerializedName("activitiesByStatus") val activitiesByStatus:Map<ActivityStatus, Int>,
    @SerializedName("activitiesByCreatedDate") val activitiesByCreatedDate:Map<String, Int>,
    @SerializedName("finishedActivitiesByLastUpdate") val finishedActivitiesByLastUpdate:Map<String, Int>
)

data class ResourcesData(
    @SerializedName("resourcesByCreatedDate") val resourcesByCreatedDate: Map<String,Int >,
    @SerializedName("finishedResources") val finishedResources: Int
)
