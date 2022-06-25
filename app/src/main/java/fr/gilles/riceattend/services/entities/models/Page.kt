package fr.gilles.riceattend.services.entities.models

import com.google.gson.annotations.SerializedName

data class Page<T>(
    @SerializedName("totalPages") val totalPages: Int?,
    @SerializedName("totalElements") val totalElements: Int?,
    @SerializedName("pageable") val pageable: Pageable?,
    @SerializedName("sort") val sort: PageableSort?,
    @SerializedName("first") val first: Boolean?,
    @SerializedName("last") val last: Boolean?,
    @SerializedName("number") val number: Int?,
    @SerializedName("numberOfElements") var numberOfElements: Int?,
    @SerializedName("size") val size: Int?,
    @SerializedName("content") var content: List<T>,
    @SerializedName("empty") val empty: Boolean?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Page<*>

        if (totalPages != other.totalPages) return false
        if (totalElements != other.totalElements) return false
        if (pageable != other.pageable) return false
        if (sort != other.sort) return false
        if (first != other.first) return false
        if (last != other.last) return false
        if (number != other.number) return false
        if (numberOfElements != other.numberOfElements) return false
        if (size != other.size) return false
        if (!content.containsAll(other.content)) return false
        if (empty != other.empty) return false

        return true
    }

    override fun hashCode(): Int {
        var result = totalPages ?: 0
        result = 31 * result + (totalElements ?: 0)
        result = 31 * result + (pageable?.hashCode() ?: 0)
        result = 31 * result + (sort?.hashCode() ?: 0)
        result = 31 * result + (first?.hashCode() ?: 0)
        result = 31 * result + (last?.hashCode() ?: 0)
        result = 31 * result + (number ?: 0)
        result = 31 * result + (numberOfElements ?: 0)
        result = 31 * result + (size ?: 0)
        result = 31 * result + content.hashCode()
        result = 31 * result + (empty?.hashCode() ?: 0)
        return result
    }
}

data class Pageable(
    @SerializedName("pageNumber") val pageNumber: Int?,
    @SerializedName("pageSize") val pageSize: Int?,
    @SerializedName("sort") val sort: PageableSort?,
    @SerializedName("paged") val paged: Boolean?,
    @SerializedName("unpaged") val unpaged: Boolean?,
    @SerializedName("offset") val offset: Int?,
)


data class PageableSort(

    @SerializedName("sorted") val sorted: Boolean?,
    @SerializedName("unsorted") val unsorted: Boolean?,
    @SerializedName("empty") val empty: Boolean? = null

)