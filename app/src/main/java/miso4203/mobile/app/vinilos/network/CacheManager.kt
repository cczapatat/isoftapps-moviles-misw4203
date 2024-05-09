package miso4203.mobile.app.vinilos.network

import android.content.Context
import androidx.collection.ArrayMap
import androidx.collection.arrayMapOf
import miso4203.mobile.app.vinilos.models.Album
import miso4203.mobile.app.vinilos.models.Artist

class CacheManager(context: Context) {
    companion object {
        var instance: CacheManager? = null
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: CacheManager(context).also {
                    instance = it
                }
            }
    }

    private var albumDetails: ArrayMap<Int, Album> = arrayMapOf()
    private var artistDetails: ArrayMap<Int, Artist> = arrayMapOf()
    fun addAlbumDetail(albumId: Int, albumDetail: Album) {
        if (!albumDetails.containsKey(albumId)) {
            albumDetails[albumId] = albumDetail
        }
    }

    fun addArtistDetail(artistId: Int, artistDetail: Artist) {
        if (!artistDetails.containsKey(artistId)) {
            artistDetails[artistId] = artistDetail
        }
    }

    fun getAlbumDetailById(albumId: Int): Album? {
        return if (albumDetails.containsKey(albumId)) albumDetails[albumId]!! else null
    }

    fun getArtistDetailById(artistId: Int): Artist? {
        return if (artistDetails.containsKey(artistId)) artistDetails[artistId]!! else null
    }
}