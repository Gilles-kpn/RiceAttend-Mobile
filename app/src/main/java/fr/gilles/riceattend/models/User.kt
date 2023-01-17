package fr.gilles.riceattend.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "users")
data class User(
    @ColumnInfo(name = "name") @SerializedName("name") var name: String,
    @ColumnInfo(name = "email") @SerializedName("email") var email: String,
    @Embedded @SerializedName("address") var address: Address?,
    @ColumnInfo(name = "enabled") @SerializedName("enabled") var enabled: Boolean
) : Audit()

data class Address(
    @ColumnInfo(name = "country") @SerializedName("country") var country: String?,
    @ColumnInfo(name = "city") @SerializedName("city") var city: String?,
    @ColumnInfo(name = "street") @SerializedName("street") var street: String?
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


data class GoogleUser(
    @SerializedName("user") var user: User,
    @SerializedName("authorization") var authorization: String,
)


data class UpdatePassword(
    @SerializedName("oldPassword") val password: String,
    @SerializedName("newPassword") val newPassword: String
)