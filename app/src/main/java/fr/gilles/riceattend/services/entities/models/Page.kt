package fr.gilles.riceattend.services.entities.models

import com.google.gson.annotations.SerializedName

data class Page<T> (
    @SerializedName("totalPages"       ) val totalPages       : Int?,
    @SerializedName("totalElements"    ) val totalElements    : Int?,
    @SerializedName("pageable"         ) val pageable         : Pageable?,
    @SerializedName("sort"             ) val sort             : PageableSort?,
    @SerializedName("first"            ) val first            : Boolean?,
    @SerializedName("last"             ) val last             : Boolean?,
    @SerializedName("number"           ) val number           : Int?,
    @SerializedName("numberOfElements" ) var numberOfElements : Int?,
    @SerializedName("size"             ) val size             : Int?,
    @SerializedName("content"          ) var content          : T,
    @SerializedName("empty"            ) val empty            : Boolean?
)

data class Pageable(
    @SerializedName("pageNumber" ) val pageNumber : Int?,
    @SerializedName("pageSize"   ) val pageSize   : Int?,
    @SerializedName("sort"       ) val sort       : PageableSort?,
    @SerializedName("paged"      ) val paged      : Boolean?,
    @SerializedName("unpaged"    ) val unpaged    : Boolean?,
    @SerializedName("offset"     ) val offset     : Int?,
)


data class PageableSort (

    @SerializedName("sorted"   ) val sorted   : Boolean? ,
    @SerializedName("unsorted" ) val unsorted : Boolean? ,
    @SerializedName("empty"    ) val empty    : Boolean? = null

)