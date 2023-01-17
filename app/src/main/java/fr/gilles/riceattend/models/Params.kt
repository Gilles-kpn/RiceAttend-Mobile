package fr.gilles.riceattend.models

import com.google.gson.annotations.SerializedName

data class Params(
    @SerializedName("pageNumber") var pageNumber: Int = 0,
    @SerializedName("pageSize") var pageSize: Int = 12,
    @SerializedName("sort") var sort: Sort = Sort.DESC,
    @SerializedName("name") var name: String = "",
    @SerializedName("deleted") var deleted: Boolean = false,
    @SerializedName("fields") var fields: List<String> = listOf("createdAt")

) {

    fun toMap(): Map<String, Any> {
        return mapOf(
            "pageNumber" to pageNumber,
            "pageSize" to pageSize,
            "sort" to sort.toString(),
            "name" to name,
            "deleted" to deleted,
        )
    }


}

data class ActivityParam(
    @SerializedName("pageNumber") var pageNumber: Int = 0,
    @SerializedName("pageSize") var pageSize: Int = 12,
    @SerializedName("sort") var sort: Sort = Sort.DESC,
    @SerializedName("deleted") var deleted: Boolean = false,
    @SerializedName("search") var name: String = "",
    @SerializedName("fields") var fields: List<String> = listOf("name"),
    @SerializedName("status") var status: List<ActivityStatus> = listOf(
        ActivityStatus.INIT,
        ActivityStatus.DONE,
        ActivityStatus.CANCELLED,
        ActivityStatus.UNDONE,
        ActivityStatus.IN_PROGRESS
    )
){

    fun toMap(): Map<String, Any> {
        return mapOf(
            "pageNumber" to pageNumber,
            "pageSize" to pageSize,
            "sort" to sort.toString(),
            "search" to name,
            "deleted" to deleted,
        )
    }
}

var ActivityStatusList = listOf(
    ActivityStatus.INIT,
    ActivityStatus.DONE,
    ActivityStatus.CANCELLED,
    ActivityStatus.IN_PROGRESS
)

enum class Sort(private val value: String) {
    ASC("ASC"),
    DESC("DESC");

    override fun toString(): String {
        return value
    }
}