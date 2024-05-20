package miso4203.mobile.app.vinilos.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import miso4203.mobile.app.vinilos.models.CollectorAlbum

@Dao
interface CollectorAlbumDao {
    @Query("SELECT * FROM collector_album ORDER BY id ASC")
    suspend fun getAll(): List<CollectorAlbum>?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertManyRaw(collectorAlbums: List<CollectorAlbum>): List<Long>
}