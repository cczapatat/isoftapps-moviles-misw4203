package miso4203.mobile.app.vinilos.repositories

import android.app.Application
import miso4203.mobile.app.vinilos.models.ArtistDetail
import miso4203.mobile.app.vinilos.network.NetworkServiceAdapter

class ArtistDetailRepository(private val application: Application) {
    suspend fun refreshData(artistId: Int): ArtistDetail {
        return NetworkServiceAdapter.getInstance(application).getArtistById(artistId = artistId)
    }
}