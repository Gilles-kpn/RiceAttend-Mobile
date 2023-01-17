package fr.gilles.riceattend.services.repositories.room

import androidx.room.Database
import androidx.room.RoomDatabase
import fr.gilles.riceattend.models.Activity
import fr.gilles.riceattend.models.PaddyField
import fr.gilles.riceattend.models.Resource
import fr.gilles.riceattend.models.Worker

@Database(entities = [Activity::class, Worker::class, PaddyField::class, Resource::class], version = 1,  exportSchema = false)
abstract  class RiceAttendDatabase: RoomDatabase() {
 abstract fun activityRepository(): RoomActivityRepository
 abstract fun workerRepository(): RoomWorkerRepository
 abstract fun resourceRepository(): RoomResourceRepository
 abstract fun paddyFieldRepository(): RoomPaddyFieldRepository



}