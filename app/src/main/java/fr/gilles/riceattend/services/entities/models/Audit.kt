package fr.gilles.riceattend.services.entities.models

import com.google.gson.annotations.SerializedName
import java.util.*

sealed class Audit {
    @SerializedName("code")
    var code: String = ""
    @SerializedName("createdAt")
    var createdAt: Date = Date()
    @SerializedName("updatedAt")
    val updatedAt: Date = Date()
}