package fr.gilles.riceattend.models

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import fr.gilles.riceattend.services.repositories.room.converters.DateConverter
import java.util.*

@TypeConverters(DateConverter::class)
sealed class Audit {
    @SerializedName("code")
    var code: String = ""

    @PrimaryKey(autoGenerate = true)
    var  id:Int  = 0

    @SerializedName("createdAt")
    @ColumnInfo(name = "created_at")
    @TypeConverters(DateConverter::class)
    var createdAt: Date = Date()

    @SerializedName("updatedAt")
    @ColumnInfo(name = "updated_at")
    @TypeConverters(DateConverter::class)
    var updatedAt: Date = Date()
}