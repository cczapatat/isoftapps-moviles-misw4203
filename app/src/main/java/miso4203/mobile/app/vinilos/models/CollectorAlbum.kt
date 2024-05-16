package miso4203.mobile.app.vinilos.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Entity(tableName = "collector_album")
@TypeConverters(CollectorConverter::class, AlbumConverter::class)
class CollectorAlbum(
    @PrimaryKey
    val id: Int,
    val price: Int,
    val status: String,
    var album: Album? = null,
    var collector: Collector? = null,
)

object CollectorConverter {
    @TypeConverter
    fun toString(collector: Collector?): String {
        return collector.toString()
    }

    @TypeConverter
    fun toCollector(str: String?): Collector? {
        if (str.isNullOrEmpty()) {
            return null
        }

        val collectorFields = str.split(",")

        return Collector(
            collectorFields[0].toInt(),
            collectorFields[1],
            collectorFields[2],
            collectorFields[3]
        )
    }
}

object AlbumConverter {
    @TypeConverter
    fun toString(album: Album?): String {
        return album.toString()
    }

    @TypeConverter
    fun toAlbum(str: String?): Album? {
        if (str.isNullOrEmpty()) {
            return null
        }

        val albumFields = str.split(",")

        return Album(
            id = albumFields[0].toInt(),
            name = albumFields[1],
            cover = albumFields[2],
            genre = albumFields[3]
        )
    }
}
