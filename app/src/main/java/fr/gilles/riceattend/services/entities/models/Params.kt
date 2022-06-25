package fr.gilles.riceattend.services.entities.models

import com.google.gson.annotations.SerializedName

data class Params(
    @SerializedName("pageNumber") var pageNumber: Int = 0,
    @SerializedName("pageSize") var pageSize: Int = 12,
    @SerializedName("sort") var sort: Sort = Sort.DESC,
    @SerializedName("deleted") var deleted: Boolean = false,
    @SerializedName("fields") var fields: Array<String> = arrayOf("code")

) {

    fun toMap(): Map<String, Any> {
        return mapOf(
            "pageNumber" to pageNumber,
            "pageSize" to pageSize,
            "sort" to sort.toString(),
            "deleted" to deleted,
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Params

        if (pageNumber != other.pageNumber) return false
        if (pageSize != other.pageSize) return false
        if (sort != other.sort) return false
        if (deleted != other.deleted) return false
        if (!fields.contentEquals(other.fields)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = pageNumber
        result = 31 * result + pageSize
        result = 31 * result + sort.hashCode()
        result = 31 * result + deleted.hashCode()
        result = 31 * result + fields.contentHashCode()
        return result
    }

}

enum class Sort(private val value: String) {
    ASC("ASC"),
    DESC("DESC");

    override fun toString(): String {
        return value
    }
}