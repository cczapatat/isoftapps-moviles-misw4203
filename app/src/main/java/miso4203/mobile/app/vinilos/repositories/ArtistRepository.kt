package miso4203.mobile.app.vinilos.repositories

import android.app.Application
import miso4203.mobile.app.vinilos.models.Artist
import miso4203.mobile.app.vinilos.network.NetworkServiceAdapter

class ArtistRepository(private val application: Application) {
    suspend fun refreshData(): List<Artist> {
        return NetworkServiceAdapter.getInstance(application).getArtists()
    }
}