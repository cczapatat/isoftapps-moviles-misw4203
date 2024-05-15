package miso4203.mobile.app.vinilos.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Entity(tableName = "collectors")
@TypeConverters(FavoritePerformerConvert::class, AlbumConverter::class)
class Collector(
    @PrimaryKey
    val id: Int = 0,
    val name: String,
    val telephone: String,
    val email: String,
    val favoritePerformers: ArrayList<FavoritePerformer> = arrayListOf(),
    val collectorAlbums: ArrayList<CollectorAlbum> = arrayListOf(),
)

object FavoritePerformerConvert {
    @TypeConverter
    fun toString(performers: ArrayList<FavoritePerformer>): String {
        val stringList = mutableListOf<String>()
        for (i in 0 until performers.size) {
            stringList.add(performers[i].id.toString())
            stringList.add(performers[i].name)
            stringList.add(performers[i].image)
        }

        return stringList.joinToString(",")
    }

    @TypeConverter
    fun toFavoritePerformerList(str: String?): ArrayList<FavoritePerformer> {
        if (str.isNullOrEmpty()) {
            return arrayListOf()
        }
        val performers = arrayListOf<FavoritePerformer>()
        val strList = str.split(",")
        for (i in strList.indices step 3) {
            performers.add(FavoritePerformer(strList[i].toInt(), strList[i + 1], strList[i + 2]))
        }

        return performers
    }

}

object AlbumConverter {
    @TypeConverter
    fun toString(albums: ArrayList<CollectorAlbum>): String {
        val stringList = mutableListOf<String>()
        for (i in 0 until albums.size) {
            stringList.add(albums[i].id.toString())
            stringList.add(albums[i].price.toString())
            stringList.add(albums[i].status)
        }

        return stringList.joinToString(",")
    }

    @TypeConverter
    fun toAlbumList(str: String?): ArrayList<CollectorAlbum> {
        if (str.isNullOrEmpty()) {
            return arrayListOf()
        }
        val albums = arrayListOf<CollectorAlbum>()
        val strList = str.split(",")
        for (i in strList.indices step 3) {
            albums.add(CollectorAlbum(strList[i].toInt(), strList[i + 1].toInt(), strList[i + 2]))
        }

        return albums
    }
}