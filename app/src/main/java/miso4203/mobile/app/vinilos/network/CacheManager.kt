package miso4203.mobile.app.vinilos.network

import android.content.Context
import miso4203.mobile.app.vinilos.models.AlbumDetail
import miso4203.mobile.app.vinilos.models.ArtistDetail

class CacheManager(context: Context) {
    companion object{
        var instance: CacheManager? = null
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: CacheManager(context).also {
                    instance = it
                }
            }
    }

    private var albumDetails: HashMap<Int, AlbumDetail> = hashMapOf()
    private var artistDetails: HashMap<Int, ArtistDetail> = hashMapOf()
    fun addAlbumDetail(albumId: Int, albumDetail: AlbumDetail){
        if (!albumDetails.containsKey(albumId)){
            albumDetails[albumId] = albumDetail
        }
    }

    fun addArtistDetail(artistId: Int, artistDetail: ArtistDetail){
        if (!artistDetails.containsKey(artistId)){
            artistDetails[artistId] = artistDetail
        }
    }

    fun getAlbumDetailById(albumId: Int) : AlbumDetail? {
        return if (albumDetails.containsKey(albumId)) albumDetails[albumId]!! else null
    }

    fun getArtistDetailById(artistId: Int) : ArtistDetail? {
        return if (artistDetails.containsKey(artistId)) artistDetails[artistId]!! else null
    }
}