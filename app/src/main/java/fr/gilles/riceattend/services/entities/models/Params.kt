package fr.gilles.riceattend.services.entities.models

import com.google.gson.annotations.SerializedName

data class Params(
    @SerializedName("pageNumber") var pageNumber: Int = 0,
    @SerializedName("pageSize") var pageSize: Int = 12,
    @SerializedName("sort") var sort: Sort = Sort.DESC,
    @SerializedName("deleted") var deleted: Boolean = false,
    @SerializedName("fields") var fields: List<String> = listOf("createdAt")

) {

    fun toMap(): Map<String, Any> {
        return mapOf(
            "pageNumber" to pageNumber,
            "pageSize" to pageSize,
            "sort" to sort.toString(),
            "deleted" to deleted,
        )
    }


}

data class ActivityParam(
    @SerializedName("pageNumber") var pageNumber: Int = 0,
    @SerializedName("pageSize") var pageSize: Int = 12,
    @SerializedName("sort") var sort: Sort = Sort.DESC,
    @SerializedName("deleted") var deleted: Boolean = false,
    @SerializedName("fields") var fields: List<String> = listOf("code"),
    @SerializedName("status") var status: List<String> = listOf(
        ActivityStatus.INIT.value,
        ActivityStatus.DONE.value,
        ActivityStatus.CANCELLED.value,
        ActivityStatus.UNDONE.value,
        ActivityStatus.IN_PROGRESS.value
    )
){

    fun toMap(): Map<String, Any> {
        return mapOf(
            "pageNumber" to pageNumber,
            "pageSize" to pageSize,
            "sort" to sort.toString(),
            "deleted" to deleted,
        )
    }
}

enum class Sort(private val value: String) {
    ASC("ASC"),
    DESC("DESC");

    override fun toString(): String {
        return value
    }
}