package miso4203.mobile.app.vinilos.repositories

import android.app.Application
import miso4203.mobile.app.vinilos.models.AlbumDetail
import miso4203.mobile.app.vinilos.network.NetworkServiceAdapter

class AlbumDetailRepository(private val application: Application) {
    suspend fun refreshData(albumId: Int): AlbumDetail {
        return NetworkServiceAdapter.getInstance(this.application).getAlbumById(albumId = albumId)
    }
}