package fr.gilles.riceattend.services.entities.models

import com.google.gson.annotations.SerializedName
import fr.gilles.riceattend.services.entities.models.Audit

data class User(
    @SerializedName("name"      ) var name      : String,
    @SerializedName("firstName" ) var firstName : String,
    @SerializedName("lastName"  ) var lastName  : String,
    @SerializedName("picture"   ) var picture   : String? = null,
    @SerializedName("email"     ) var email     : String,
    @SerializedName("phone"     ) var phone     : String? = null,
    @SerializedName("authority" ) var authority : String,
    @SerializedName("city"      ) var city      : String? = null,
    @SerializedName("country"   ) var country   : String? = null
    ) : Audit()