package fr.gilles.riceattend.services.repositories.room

import androidx.lifecycle.LiveData
import androidx.room.*
import fr.gilles.riceattend.models.Worker

@Dao
abstract class RoomWorkerRepository {
    @Query("SELECT * FROM workers")
    @Transaction
    abstract fun get(): LiveData<List<Worker>>

    @Query("SELECT * FROM workers WHERE code = :code")
    @Transaction
    abstract fun get(code: String): LiveData<Worker?>

    @Query("SELECT * FROM workers WHERE id = :id")
    @Transaction
    abstract fun get(id: Int): LiveData<Worker?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Transaction
    abstract fun insert(worker: Worker)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    @Transaction
    abstract fun update(worker: Worker)


    fun insertAndGet(worker: Worker): LiveData<Worker?> {
        insert(worker)
        return get(worker.code)
    }

    fun updateAndGet(worker: Worker): Worker {
       update(worker)
        return get(worker.code).value!!
    }
}