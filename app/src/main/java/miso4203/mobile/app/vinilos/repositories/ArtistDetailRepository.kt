package miso4203.mobile.app.vinilos.repositories

import android.app.Application
import android.util.Log
import miso4203.mobile.app.vinilos.models.ArtistDetail
import miso4203.mobile.app.vinilos.network.CacheManager
import miso4203.mobile.app.vinilos.network.NetworkServiceAdapter

class ArtistDetailRepository(private val application: Application) {
    suspend fun refreshData(artistId: Int): ArtistDetail {
        val artistDetailByIdFromCache = CacheManager.getInstance(application.applicationContext).getArtistDetailById(artistId)

        return if(artistDetailByIdFromCache == null ){
            Log.d("Cache decision", "get from network")

            val artistDetailById = NetworkServiceAdapter.getInstance(this.application).getArtistById(artistId = artistId)
            CacheManager.getInstance(application.applicationContext).addArtistDetail(artistId, artistDetailById)

            artistDetailById
        } else {
            Log.d("Cache decision", "return ${artistDetailByIdFromCache.id} id")

            artistDetailByIdFromCache
        }
    }
}