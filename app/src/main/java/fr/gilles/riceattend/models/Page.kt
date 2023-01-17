package fr.gilles.riceattend.models

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
) : Iterable<T> {
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

//    fun copyWith(
//        totalPages: Int? = this.totalPages,
//        totalElements: Int? = this.totalElements,
//        pageable: Pageable? = this.pageable,
//        sort: PageableSort? = this.sort,
//        first: Boolean? = this.first,
//        last: Boolean? = this.last,
//        number: Int? = this.number,
//        numberOfElements: Int? = this.numberOfElements,
//        size: Int?  = this.size,
//        content: List<T> = this.content,
//        empty: Boolean? = this.empty
//    ): Page<T> {
//        return Page(
//            totalPages, totalElements, pageable, sort, first, last, number, numberOfElements, size, content, empty
//        )
//    }



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

    override fun iterator(): Iterator<T> {
        return this.content.iterator()
    }



}

fun <T> pageFromList(list: List<T>): Page<T> {
    return Page(
        totalPages = 1,
        totalElements = list.size,
        pageable = null,
        sort = null,
        first = true,
        last = true,
        number = 0,
        numberOfElements = list.size,
        size = list.size,
        content = list,
        empty = list.isEmpty()
    )
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