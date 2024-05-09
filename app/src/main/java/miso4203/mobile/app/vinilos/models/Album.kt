package miso4203.mobile.app.vinilos.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Entity(tableName = "albums")
@TypeConverters(TrackConvert::class, PerformerConverter::class)
class Album(
    @PrimaryKey
    val id: Int = 0,
    val name: String,
    val cover: String,
    val releaseDate: String = "",
    val description: String = "",
    val genre: String,
    val recordLabel: String = "",
    val tracks: ArrayList<Track> = arrayListOf(),
    val performers: ArrayList<Performer> = arrayListOf(),
)

object TrackConvert {
    @TypeConverter
    fun toString(tracks: ArrayList<Track>): String {
        val stringList = mutableListOf<String>()
        for (i in 0 until tracks.size) {
            stringList.add(tracks[i].id.toString())
            stringList.add(tracks[i].name)
            stringList.add(tracks[i].duration)
        }

        return stringList.joinToString(",")
    }

    @TypeConverter
    fun toTrackList(str: String?): ArrayList<Track> {
        if (str.isNullOrEmpty()) {
            return arrayListOf()
        }
        val tracks = arrayListOf<Track>()
        val strList = str.split(",")
        for (i in strList.indices step 3) {
            tracks.add(Track(strList[i].toInt(), strList[i + 1], strList[i + 2]))
        }

        return tracks
    }
}

object PerformerConverter {
    @TypeConverter
    fun toString(tracks: ArrayList<Performer>): String {
        val stringList = mutableListOf<String>()
        for (i in 0 until tracks.size) {
            stringList.add(tracks[i].id.toString())
            stringList.add(tracks[i].name)
        }

        return stringList.joinToString(",")
    }

    @TypeConverter
    fun toPerformerList(str: String?): ArrayList<Performer> {
        if (str.isNullOrEmpty()) {
            return arrayListOf()
        }
        val performers = arrayListOf<Performer>()
        val strList = str.split(",")
        for (i in strList.indices step 2) {
            performers.add(Performer(strList[i].toInt(), strList[i + 1]))
        }

        return performers
    }
}