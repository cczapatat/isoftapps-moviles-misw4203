package miso4203.mobile.app.vinilos.repositories

import android.app.Application
import android.util.Log
import miso4203.mobile.app.vinilos.models.AlbumDetail
import miso4203.mobile.app.vinilos.network.CacheManager
import miso4203.mobile.app.vinilos.network.NetworkServiceAdapter

class AlbumDetailRepository(private val application: Application) {
    suspend fun refreshData(albumId: Int): AlbumDetail {
        val albumDetailByIdFromCache = CacheManager.getInstance(application.applicationContext).getAlbumDetailById(albumId)

        return if(albumDetailByIdFromCache == null ){
            Log.i("Cache decision", "get from network")

            val albumDetailById = NetworkServiceAdapter.getInstance(this.application).getAlbumById(albumId = albumId)
            CacheManager.getInstance(application.applicationContext).addAlbumDetail(albumId, albumDetailById)

            albumDetailById
        } else {
            Log.i("Cache decision", "return ${albumDetailByIdFromCache.id} id")

            albumDetailByIdFromCache
        }
    }
}