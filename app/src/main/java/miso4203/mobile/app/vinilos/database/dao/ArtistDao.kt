package miso4203.mobile.app.vinilos.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import miso4203.mobile.app.vinilos.models.Album
import miso4203.mobile.app.vinilos.models.Artist

@Dao
interface ArtistDao {
    @Query("SELECT * FROM artists ORDER BY id ASC")
    suspend fun getAll(): List<Artist>?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertManyRaw(albums: List<Artist>): List<Long>

    @Query("SELECT * FROM artists WHERE id = :artistId LIMIT 1")
    suspend fun getById(artistId: Int): Artist?
}