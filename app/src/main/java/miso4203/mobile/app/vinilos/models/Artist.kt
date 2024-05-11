package miso4203.mobile.app.vinilos.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "artists")
class Artist(
    @PrimaryKey
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val totalAlbums: Int,
)
