package miso4203.mobile.app.vinilos.repositories

import android.app.Application
import miso4203.mobile.app.vinilos.database.dao.ArtistDao
import miso4203.mobile.app.vinilos.models.Artist
import miso4203.mobile.app.vinilos.network.NetworkServiceAdapter

class ArtistRepository(
    private val application: Application,
    private val artistDao: ArtistDao,
) {
    suspend fun refreshData(): List<Artist> {
        return if (NetworkServiceAdapter.isInternetAvailable(application.applicationContext)) {
            val artists = NetworkServiceAdapter.getInstance(application).getArtists()
            this.artistDao.insertManyRaw(artists)

            artists
        } else {
            val cached = this.artistDao.getAll()

            if (cached.isNullOrEmpty()) {
                emptyList()
            } else cached
        }
    }
}