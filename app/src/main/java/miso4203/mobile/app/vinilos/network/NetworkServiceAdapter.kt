package miso4203.mobile.app.vinilos.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.android.volley.ClientError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import miso4203.mobile.app.vinilos.models.Album
import miso4203.mobile.app.vinilos.models.Artist
import miso4203.mobile.app.vinilos.models.Collector
import miso4203.mobile.app.vinilos.models.CollectorAlbum
import miso4203.mobile.app.vinilos.models.FavoritePerformer
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

        fun isInternetAvailable(context: Context): Boolean {
            var result = false
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }

            return result
        }
    }

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    suspend fun getAlbums() = suspendCoroutine<List<Album>> { cont ->
        requestQueue.add(
            getRequest("albums", { response ->
                val resp = JSONArray(response)
                val list = mutableListOf<Album>()
                for (i in 0 until resp.length()) {
                    list.add(i, this.jsonToAlbum(resp.getString(i), true))
                }
                cont.resume(list)
            }, {
                cont.resumeWithException(it)
            })
        )
    }

    suspend fun getAlbumById(albumId: Int) = suspendCoroutine { cont ->
        requestQueue.add(
            getRequest("albums/${albumId}", {
                cont.resume(this.jsonToAlbum(it, false))
            }, {
                cont.resumeWithException(it)
            })
        )
    }

    private fun jsonToAlbum(response: String, subStr: Boolean): Album {
        val resp = JSONObject(response)
        val albumName = resp.optString("name", UNKNOWN)
        val albumDetail = Album(
            id = resp.optInt("id", -1),
            name = if (subStr && albumName.length > 16) "${
                albumName.substring(
                    0, 16
                )
            }..." else albumName,
            cover = resp.optString("cover", COVER_UNKNOWN),
            releaseDate = resp.optString("releaseDate", UNKNOWN),
            genre = resp.optString("genre", UNKNOWN),
            description = resp.optString("description", UNKNOWN),
            tracks = arrayListOf(),
            performers = arrayListOf(),
        )
        var itemtrack: JSONObject?

        val tracks = resp.optJSONArray("tracks")
        val performers = resp.optJSONArray("performers")

        if (tracks != null && tracks.length() > 0) {
            for (i in 0 until resp.getJSONArray("tracks").length()) {
                itemtrack = resp.getJSONArray("tracks").getJSONObject(i)
                albumDetail.tracks.add(
                    Track(
                        id = itemtrack.optInt("id", -1),
                        name = itemtrack.optString("name", UNKNOWN),
                        duration = itemtrack.optString("duration", UNKNOWN)
                    )
                )
            }
        }

        if (performers != null && performers.length() > 0) {
            var itemPerformer: JSONObject?
            for (i in 0 until resp.getJSONArray("performers").length()) {
                itemPerformer = resp.getJSONArray("performers").getJSONObject(i)
                albumDetail.performers.add(
                    Performer(
                        id = itemPerformer.optInt("id", -1),
                        name = itemPerformer.optString("name", UNKNOWN),
                    )
                )
            }
        }

        return albumDetail
    }

    suspend fun getArtists() = suspendCoroutine<List<Artist>> { cont ->
        requestQueue.add(
            getRequest("musicians", { response ->
                val resp = JSONArray(response)
                val list = mutableListOf<Artist>()
                var item: JSONObject?
                for (i in 0 until resp.length()) {
                    item = resp.getJSONObject(i)
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

    suspend fun getArtistById(artistId: Int): Artist {
        try {
            return this.getPerformer(artistId, "musicians")
        } catch (err: ClientError) {
            if (err.networkResponse?.statusCode == 404) {
                return this.getBandById(artistId)
            }

            throw err
        } catch (e: Exception) {
            throw e
        }
    }

    private suspend fun getBandById(bandId: Int) = this.getPerformer(bandId, "bands")

    private suspend fun getPerformer(performerId: Int, path: String) = suspendCoroutine { cont ->
        requestQueue.add(
            getRequest("${path}/${performerId}", { response ->
                val resp = JSONObject(response)
                val artistDetail = Artist(
                    id = resp.optInt("id", -1),
                    name = resp.optString("name", UNKNOWN),
                    image = resp.optString("image", COVER_UNKNOWN),
                    description = resp.optString("description", UNKNOWN),
                    totalAlbums = resp.optJSONArray("albums")?.length() ?: 0
                )
                cont.resume(artistDetail)
            }, {
                cont.resumeWithException(it)
            })
        )
    }

    suspend fun addAlbum(album: Album) = suspendCoroutine { cont ->
        val jsonPayload = JSONObject()
        jsonPayload.put("name", album.name).put("cover", album.cover)
            .put("releaseDate", album.releaseDate).put("description", album.description)
            .put("genre", album.genre).put("recordLabel", album.recordLabel)

        requestQueue.add(
            postRequest(
                "albums",
                jsonPayload,
                { response ->
                    val albumCreated = Album(
                        id = response.optInt("id"),
                        name = response.optString("name"),
                        cover = response.optString("cover"),
                        releaseDate = response.optString("releaseDate"),
                        description = response.optString("description"),
                        genre = response.optString("genre"),
                        tracks = ArrayList(),
                        performers = ArrayList()
                    )
                    cont.resume(albumCreated)
                },
                {
                    cont.resumeWithException(it)
                })
        )
    }

    suspend fun getCollectors() = suspendCoroutine<List<Collector>> { cont ->
        requestQueue.add(
            getRequest("collectors", { response ->
                val resp = JSONArray(response)
                val list = mutableListOf<Collector>()
                for (i in 0 until resp.length()) {
                    list.add(i, this.jsonToCollector(resp.getString(i), true))
                }
                cont.resume(list)
            }, {
                cont.resumeWithException(it)
            })
        )
    }

    suspend fun getCollectorAlbumsByCollectorId(collectorId: Int) = suspendCoroutine { cont ->
        requestQueue.add(
            getRequest("collectors/${collectorId}/albums", { response ->
                val resp = JSONArray(response)
                val list = mutableListOf<CollectorAlbum>()
                for (i in 0 until resp.length()) {
                    list.add(i, this.jsonToCollectorAlbum(resp.getString(i), false))
                }
                cont.resume(list)
            }, {
                cont.resumeWithException(it)
            })
        )
    }

    private fun jsonToCollector(response: String, subStr: Boolean): Collector {
        val resp = JSONObject(response)
        val collectorName = resp.optString("name", UNKNOWN)
        val collectorDetail = Collector(
            id = resp.optInt("id", -1),
            name = if (subStr && collectorName.length > 16) "${
                collectorName.substring(
                    0, 16
                )
            }..." else collectorName,
            telephone = resp.optString("telephone", UNKNOWN),
            email = resp.optString("email", UNKNOWN),
            favoritePerformers = arrayListOf(),
            collectorAlbums = arrayListOf(),
        )

        val collectorAlbums = resp.optJSONArray("collectorAlbums")
        val favoritePerformers = resp.optJSONArray("favoritePerformers")

        if (collectorAlbums != null && collectorAlbums.length() > 0) {
            var itemAlbum: JSONObject?
            for (i in 0 until collectorAlbums.length()) {
                itemAlbum = resp.getJSONArray("collectorAlbums").getJSONObject(i)
                collectorDetail.collectorAlbums.add(
                    CollectorAlbum(
                        id = itemAlbum.optInt("id", -1),
                        price = itemAlbum.optInt("price", -1),
                        status = itemAlbum.optString("status", UNKNOWN)
                    )
                )
            }
        }

        if (favoritePerformers != null && favoritePerformers.length() > 0) {
            var itemPerformer: JSONObject?
            for (i in 0 until resp.getJSONArray("favoritePerformers").length()) {
                itemPerformer = resp.getJSONArray("favoritePerformers").getJSONObject(i)
                collectorDetail.favoritePerformers.add(
                    FavoritePerformer(
                        id = itemPerformer.optInt("id", -1),
                        name = itemPerformer.optString("name", UNKNOWN),
                        image = itemPerformer.optString("image", COVER_UNKNOWN),
                    )
                )
            }
        }

        return collectorDetail
    }

    private fun jsonToCollectorAlbum(response: String, subStr: Boolean): CollectorAlbum {
        val resp = JSONObject(response)

        return CollectorAlbum(
            id = resp.optInt("id", -1),
            price = resp.optInt("price", -1),
            status = resp.optString("status", UNKNOWN),
            album = this.jsonToAlbum(resp.getString("album"), subStr),
            collector = this.jsonToCollector(resp.getString("collector"), subStr)
        )
    }

    private fun getRequest(
        path: String,
        responseListener: Response.Listener<String>,
        errorListener: Response.ErrorListener
    ): StringRequest {
        return StringRequest(Request.Method.GET, BASE_URL + path, responseListener, errorListener)
    }

    private fun postRequest(
        path: String,
        body: JSONObject,
        responseListener: Response.Listener<JSONObject>,
        errorListener: Response.ErrorListener
    ): JsonObjectRequest {
        return JsonObjectRequest(
            Request.Method.POST,
            BASE_URL + path,
            body,
            responseListener,
            errorListener
        )
    }
}