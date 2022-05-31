package fr.gilles.riceattend.services.entities.payloader

import com.google.gson.annotations.SerializedName

data class RegisterUser (
    @SerializedName("email") var email:String,
    @SerializedName("password") var password:String,
    @SerializedName("firstName") var firstName:String,
    @SerializedName("lastName") var lastName:String
)