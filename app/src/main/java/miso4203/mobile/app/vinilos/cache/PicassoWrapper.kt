package miso4203.mobile.app.vinilos.cache

import android.content.Context
import com.squareup.picasso.LruCache
import com.squareup.picasso.Picasso

class PicassoWrapper {

    companion object {
        private var instance: Picasso? =  null
        private const val memoryCacheSizeBytes = 25 * 1024 * 1024 // 25 MB in bytes

        fun getInstance(context: Context): Picasso = instance ?: synchronized(this) {
            instance ?: Picasso.Builder(context)
                .memoryCache(LruCache(memoryCacheSizeBytes))
                .build()
                .also {
                    instance = it
                }
        }
    }
}