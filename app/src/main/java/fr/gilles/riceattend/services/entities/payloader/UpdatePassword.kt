package fr.gilles.riceattend.services.entities.payloader

import com.google.gson.annotations.SerializedName

data class UpdatePassword(
    @SerializedName("oldPassword") val password: String,
    @SerializedName("newPassword") val newPassword: String
)
