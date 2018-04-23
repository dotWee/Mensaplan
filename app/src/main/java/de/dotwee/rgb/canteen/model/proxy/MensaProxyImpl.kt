package de.dotwee.rgb.canteen.model.proxy

import de.dotwee.rgb.canteen.model.Location
import okhttp3.Cache
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File

/**
 * Created by lukas on 18.04.18.
 */

class MensaProxyImpl(private val cacheDirectory: File?) : MensaProxy {

    private var okHttpClient: OkHttpClient? = null
    private var cache: Cache? = null

    override val httpClient: OkHttpClient
        get() {
            if (okHttpClient == null) {
                if (cacheDirectory != null) {
                    cache = Cache(cacheDirectory, CACHE_SIZE.toLong())
                }

                if (cache != null) {
                    okHttpClient = OkHttpClient.Builder()
                            .cache(cache)
                            .build()
                } else {
                    okHttpClient = OkHttpClient()
                }
            }

            return okHttpClient!!
        }

    override fun getHttpUrl(location: Location, weeknumber: Int): HttpUrl {
        return HttpUrl.parse(String.format(MensaProxy.Companion.URL_FORMAT, location.nameTag, weeknumber))!!
    }

    override fun newCall(requestParser: RequestParser, httpUrl: HttpUrl) {
        val request = Request.Builder()
                .url(httpUrl)
                .build()

        httpClient
                .newCall(request)
                .enqueue(requestParser)
    }

    companion object {
        private val CACHE_SIZE = 50 * 1024 * 1024 // 50 MiB
    }
}
