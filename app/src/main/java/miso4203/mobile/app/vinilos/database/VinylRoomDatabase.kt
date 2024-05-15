package miso4203.mobile.app.vinilos.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import miso4203.mobile.app.vinilos.database.dao.AlbumDao
import miso4203.mobile.app.vinilos.database.dao.ArtistDao
import miso4203.mobile.app.vinilos.database.dao.CollectorDao
import miso4203.mobile.app.vinilos.models.Album
import miso4203.mobile.app.vinilos.models.Artist
import miso4203.mobile.app.vinilos.models.Collector

@Database(entities = [Album::class, Artist::class, Collector::class], version = 1, exportSchema = false)
abstract class VinylRoomDatabase : RoomDatabase() {

    abstract fun albumsDao(): AlbumDao

    abstract fun artistsDao(): ArtistDao

    abstract fun collectorsDao(): CollectorDao

    companion object {
        @Volatile
        private var INSTANCE: VinylRoomDatabase? = null

        fun getDatabase(context: Context): VinylRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VinylRoomDatabase::class.java,
                    "vinyls_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}