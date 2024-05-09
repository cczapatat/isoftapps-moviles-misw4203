package miso4203.mobile.app.vinilos.repositories

import android.app.Application
import android.util.Log
import miso4203.mobile.app.vinilos.database.dao.ArtistDao
import miso4203.mobile.app.vinilos.models.Artist
import miso4203.mobile.app.vinilos.network.CacheManager
import miso4203.mobile.app.vinilos.network.NetworkServiceAdapter

class ArtistDetailRepository(
    private val application: Application,
    private val artistDao: ArtistDao,
) {
    suspend fun refreshData(artistId: Int): Artist? {
        val artistDetailByIdFromCache =
            CacheManager.getInstance(application.applicationContext).getArtistDetailById(artistId)

        return if (artistDetailByIdFromCache == null) {
            val artistDetailById =
                if (NetworkServiceAdapter.isInternetAvailable(application.applicationContext)) {
                    NetworkServiceAdapter.getInstance(this.application).getArtistById(artistId)
                } else {
                    this.artistDao.getById(artistId)
                }
            if (artistDetailById != null) {
                CacheManager.getInstance(application.applicationContext).addArtistDetail(
                    artistId,
                    artistDetailById,
                )
            }
            artistDetailById
        } else {
            artistDetailByIdFromCache
        }
    }
}