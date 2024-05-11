package miso4203.mobile.app.vinilos

import android.app.Application
import miso4203.mobile.app.vinilos.database.VinylRoomDatabase

class VinilosApplication : Application() {
    val database by lazy { VinylRoomDatabase.getDatabase(this) }
}