package miso4203.mobile.app.vinilos.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import miso4203.mobile.app.vinilos.models.Album
import miso4203.mobile.app.vinilos.models.Collector

@Dao
interface CollectorDao {
    @Query("SELECT * FROM collectors ORDER BY id ASC")
    suspend fun getAll(): List<Collector>?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertManyRaw(collectors: List<Collector>): List<Long>

    @Query("SELECT * FROM collectors WHERE id = :collectorId LIMIT 1")
    suspend fun getById(collectorId: Int): Collector
}