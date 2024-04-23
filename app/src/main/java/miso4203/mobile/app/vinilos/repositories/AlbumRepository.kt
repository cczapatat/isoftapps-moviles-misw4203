package miso4203.mobile.app.vinilos.repositories

import android.app.Application
import com.android.volley.VolleyError
import miso4203.mobile.app.vinilos.models.Album
import miso4203.mobile.app.vinilos.network.NetworkServiceAdapter

class AlbumRepository(private val application: Application) {
    fun refreshData(callback: (List<Album>) -> Unit, onError: (VolleyError) -> Unit) {
        NetworkServiceAdapter.getInstance(application).getAlbums({
            callback(it)
        }, {
            onError
        })
    }
}