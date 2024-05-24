package miso4203.mobile.app.vinilos.repositories

import android.app.Application
import miso4203.mobile.app.vinilos.database.dao.AlbumDao
import miso4203.mobile.app.vinilos.models.Album
import miso4203.mobile.app.vinilos.network.CacheManager
import miso4203.mobile.app.vinilos.network.NetworkServiceAdapter

class AlbumDetailRepository(
    private val application: Application,
    private val albumDao: AlbumDao,
) {
    suspend fun refreshData(albumId: Int): Album {
        val albumDetailByIdFromCache =
            CacheManager.getInstance(application.applicationContext).getAlbumDetailById(albumId)

        return if (albumDetailByIdFromCache == null) {
            val albumDetailById =
                if (NetworkServiceAdapter.isInternetAvailable(application.applicationContext)) {
                    NetworkServiceAdapter.getInstance(this.application)
                        .getAlbumById(albumId = albumId)
                } else {
                    this.albumDao.getById(albumId)
                }
            CacheManager.getInstance(application.applicationContext).addAlbumDetail(
                albumId,
                albumDetailById,
            )
            albumDetailById
        } else {
            albumDetailByIdFromCache
        }
    }

    suspend fun refreshDataForced(albumId: Int): Album {

        val albumDetailById =
            if (NetworkServiceAdapter.isInternetAvailable(application.applicationContext)) {
                NetworkServiceAdapter.getInstance(this.application)
                    .getAlbumById(albumId = albumId)
            } else {
                this.albumDao.getById(albumId)
            }
        CacheManager.getInstance(application.applicationContext).replaceAlbumDetail(
            albumId,
            albumDetailById,
        )
        return albumDetailById

    }
}