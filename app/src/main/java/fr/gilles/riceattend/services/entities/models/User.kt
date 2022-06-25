package fr.gilles.riceattend.services.entities.models

import com.google.gson.annotations.SerializedName


data class User(
    @SerializedName("name") var name: String,
    @SerializedName("email") var email: String,
    @SerializedName("address") var address: Address?,
    @SerializedName("enabled") var enabled: Boolean
) : Audit()


data class Address(
    @SerializedName("country") var country: String?,
    @SerializedName("city") var city: String?,
    @SerializedName("street") var street: String?
)


data class LoginUser(
    @SerializedName("email") var email: String,
    @SerializedName("password") var password: String
)

data class RegisterUser(
    @SerializedName("email") var email: String,
    @SerializedName("password") var password: String,
    @SerializedName("name") var name: String,
    @SerializedName("firstName") var firstName: String,
    @SerializedName("lastName") var lastName: String
)


data class UpdatePassword(
    @SerializedName("oldPassword") val password: String,
    @SerializedName("newPassword") val newPassword: String
)