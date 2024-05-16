package miso4203.mobile.app.vinilos.repositories

import android.app.Application
import miso4203.mobile.app.vinilos.database.dao.CollectorAlbumDao
import miso4203.mobile.app.vinilos.models.CollectorAlbum
import miso4203.mobile.app.vinilos.network.NetworkServiceAdapter

class CollectorDetailRepository(
    private val application: Application,
    private val collectorAlbumDao: CollectorAlbumDao,
) {
    suspend fun refreshData(collectorId: Int): List<CollectorAlbum> {
        return if (NetworkServiceAdapter.isInternetAvailable(application.applicationContext)) {
            val collectorAlbums = NetworkServiceAdapter.getInstance(application).getCollectorAlbumsByCollectorId(collectorId)
            this.collectorAlbumDao.insertManyRaw(collectorAlbums)

            collectorAlbums
        } else {
            val cached = this.collectorAlbumDao.getAll()

            if (cached.isNullOrEmpty()) {
                emptyList()
            } else cached
        }
    }
}