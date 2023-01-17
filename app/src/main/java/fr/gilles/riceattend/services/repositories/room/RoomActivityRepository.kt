package fr.gilles.riceattend.services.repositories.room

import androidx.room.Dao
import androidx.room.Query
import fr.gilles.riceattend.models.Activity

@Dao
interface RoomActivityRepository {

    @Query("SELECT * FROM activity")
    fun getAll(): List<Activity>
}