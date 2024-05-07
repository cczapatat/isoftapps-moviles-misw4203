package miso4203.mobile.app.vinilos.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import miso4203.mobile.app.vinilos.models.Album
import miso4203.mobile.app.vinilos.models.AlbumDetail
import miso4203.mobile.app.vinilos.models.Artist
import miso4203.mobile.app.vinilos.models.ArtistDetail
import miso4203.mobile.app.vinilos.models.Performer
import miso4203.mobile.app.vinilos.models.Track
import org.json.JSONArray
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class NetworkServiceAdapter constructor(context: Context) {
    companion object {
        const val BASE_URL = "http://cryzat.xyz/"
        const val UNKNOWN = "unknown"
        const val COVER_UNKNOWN =
            "https://www.alleganyco.gov/wp-content/uploads/unknown-person-icon-Image-from.png"
        private var instance: NetworkServiceAdapter? = null
        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: NetworkServiceAdapter(context).also {
                instance = it
            }
        }
    }

    private val requestQueue: RequestQueue by lazy {
        // applicationContext keeps you from leaking the Activity or BroadcastReceiver if someone passes one in.
        Volley.newRequestQueue(context.applicationContext)
    }

    suspend fun getAlbums() = suspendCoroutine<List<Album>> { cont ->
        requestQueue.add(
            getRequest("albums", { response ->
                val resp = JSONArray(response)
                val list = mutableListOf<Album>()
                for (i in 0 until resp.length()) {
                    val item = resp.getJSONObject(i)
                    val albumName = item.optString("name", UNKNOWN)
                    list.add(
                        i, Album(
                            id = item.optInt("id", -1),
                            name = if (albumName.length > 16) "${
                                albumName.substring(
                                    0, 16
                                )
                            }..." else albumName,
                            cover = item.optString("cover", COVER_UNKNOWN),
                            recordLabel = item.optString("recordLabel", UNKNOWN),
                            releaseDate = item.optString("releaseDate", UNKNOWN),
                            genre = item.optString("genre", UNKNOWN),
                            description = item.optString("description", UNKNOWN)
                        )
                    )
                }
                cont.resume(list)
            }, {
                cont.resumeWithException(it)
            })
        )
    }

    suspend fun getAlbumById(albumId: Int) = suspendCoroutine { cont ->
        requestQueue.add(
            getRequest("albums/${albumId}", { response ->
                val resp = JSONObject(response)
                val albumDetail = AlbumDetail(
                    id = resp.optInt("id", -1),
                    name = resp.optString("name", UNKNOWN),
                    cover = resp.optString("cover", COVER_UNKNOWN),
                    recordLabel = resp.optString("recordLabel", UNKNOWN),
                    releaseDate = resp.optString("releaseDate", UNKNOWN),
                    genre = resp.optString("genre", UNKNOWN),
                    description = resp.optString("description", UNKNOWN),
                    tracks = arrayListOf(),
                    performers = arrayListOf(),
                )
                for (i in 0 until resp.getJSONArray("tracks").length()) {
                    val item = resp.getJSONArray("tracks").getJSONObject(i)
                    albumDetail.tracks.add(
                        Track(
                            id = item.optInt("id", -1),
                            name = item.optString("name", UNKNOWN),
                            duration = item.optString("duration", UNKNOWN)
                        )
                    )
                }
                for (i in 0 until resp.getJSONArray("performers").length()) {
                    val item = resp.getJSONArray("performers").getJSONObject(i)
                    albumDetail.performers.add(
                        Performer(
                            id = item.optInt("id", -1),
                            name = item.optString("name", UNKNOWN),
                            image = item.optString("image"),
                            description = item.optString("description", UNKNOWN),
                            birthDate = item.optString("birthDate", UNKNOWN),
                        )
                    )
                }
                cont.resume(albumDetail)
            }, {
                cont.resumeWithException(it)
            })
        )
    }

    suspend fun getArtists() = suspendCoroutine<List<Artist>> { cont ->
        requestQueue.add(
            getRequest("musicians", { response ->
                val resp = JSONArray(response)
                val list = mutableListOf<Artist>()
                for (i in 0 until resp.length()) {
                    val item = resp.getJSONObject(i)
                    val artistName = item.optString("name", UNKNOWN)
                    list.add(
                        i, Artist(
                            id = item.optInt("id", -1),
                            name = if (artistName.length > 16) "${
                                artistName.substring(
                                    0, 16
                                )
                            }..." else artistName,
                            image = item.optString("image", COVER_UNKNOWN),
                            description = item.optString("description", UNKNOWN),
                            birthDate = item.optString("birthDate", UNKNOWN),
                            totalAlbums = item.optJSONArray("albums")?.length() ?: 0
                        )
                    )
                }
                cont.resume(list)
            }, {
                cont.resumeWithException(it)
            })
        )
    }

    suspend fun getArtistById(artistId: Int) = suspendCoroutine { cont ->
        requestQueue.add(
            getRequest("musicians/${artistId}", { response ->
                val resp = JSONObject(response)
                val artistDetail = ArtistDetail(
                    id = resp.optInt("id", -1),
                    name = resp.optString("name", UNKNOWN),
                    image = resp.optString("image", COVER_UNKNOWN),
                    description = resp.optString("description", UNKNOWN),
                    birthDate = resp.optString("birthDate", UNKNOWN),
                    albums = arrayListOf(),
                )
                for (i in 0 until resp.getJSONArray("albums").length()) {
                    val item = resp.getJSONArray("albums").getJSONObject(i)
                    artistDetail.albums.add(
                        Album(
                            id = item.optInt("id", -1),
                            name = item.optString("name", UNKNOWN),
                            cover = item.optString("cover", COVER_UNKNOWN),
                            recordLabel = item.optString("recordLabel", UNKNOWN),
                            releaseDate = item.optString("releaseDate", UNKNOWN),
                            genre = item.optString("genre", UNKNOWN),
                            description = item.optString("description", UNKNOWN)
                        )
                    )
                }
                cont.resume(artistDetail)
            }, {
                cont.resumeWithException(it)
            })
        )
    }


    private fun getRequest(
        path: String,
        responseListener: Response.Listener<String>,
        errorListener: Response.ErrorListener
    ): StringRequest {
        return StringRequest(Request.Method.GET, BASE_URL + path, responseListener, errorListener)
    }
}