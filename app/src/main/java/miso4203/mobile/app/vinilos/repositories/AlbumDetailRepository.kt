package miso4203.mobile.app.vinilos.repositories

import android.app.Application
import com.android.volley.VolleyError
import miso4203.mobile.app.vinilos.models.AlbumDetail
import miso4203.mobile.app.vinilos.network.NetworkServiceAdapter

class AlbumDetailRepository(private val application: Application) {
    fun refreshData(albumId: Int, callback: (AlbumDetail) -> Unit, onError: (VolleyError) -> Unit) {
        NetworkServiceAdapter.getInstance(this.application).getAlbumById(albumId = albumId, {
            callback(it)
        }, { onError })
    }
}