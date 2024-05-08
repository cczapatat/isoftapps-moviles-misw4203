package miso4203.mobile.app.vinilos.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import miso4203.mobile.app.vinilos.models.Album

@Dao
interface AlbumDao {
    @Query("SELECT * FROM albums")
    fun getAll(): List<Album>?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(album: Album)

    @Query("SELECT * FROM albums WHERE id = :albumId LIMIT 1")
    fun getById(albumId: Int): Album
}