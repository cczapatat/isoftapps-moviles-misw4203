package miso4203.mobile.app.vinilos.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "albums")
open class Album(
    @PrimaryKey
    open val id: Int = 0,
    open val name: String,
    open val cover: String,
    open val releaseDate: String = "",
    open val description: String = "",
    open val genre: String,
    val recordLabel: String = ""
)