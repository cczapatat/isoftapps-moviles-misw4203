package miso4203.mobile.app.vinilos.repositories

import android.app.Application
import miso4203.mobile.app.vinilos.models.Album
import miso4203.mobile.app.vinilos.network.NetworkServiceAdapter

class AlbumRepository(private val application: Application) {
    suspend fun refreshData(): List<Album> {
        return NetworkServiceAdapter.getInstance(application).getAlbums()
    }

    suspend fun addAlbum(album: Album): Album {
        return NetworkServiceAdapter.getInstance(application).addAlbum(album)
    }
}