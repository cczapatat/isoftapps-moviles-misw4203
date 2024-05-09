package miso4203.mobile.app.vinilos.repositories

import android.app.Application
import miso4203.mobile.app.vinilos.database.dao.AlbumDao
import miso4203.mobile.app.vinilos.models.Album
import miso4203.mobile.app.vinilos.network.NetworkServiceAdapter

class AlbumRepository(
    private val application: Application,
    private val albumDao: AlbumDao,
) {
    suspend fun refreshData(): List<Album> {
        return if (NetworkServiceAdapter.isInternetAvailable(application.applicationContext)) {
            val albums = NetworkServiceAdapter.getInstance(application).getAlbums()
            this.albumDao.insertManyRaw(albums)

            albums
        } else {
            val cached = this.albumDao.getAll()

            if (cached.isNullOrEmpty()) {
                emptyList()
            } else cached
        }
    }

    suspend fun addAlbum(album: Album): Album {
        return NetworkServiceAdapter.getInstance(application).addAlbum(album)
    }
}