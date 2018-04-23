package de.dotwee.rgb.canteen.model.proxy

import de.dotwee.rgb.canteen.model.Location
import okhttp3.HttpUrl
import okhttp3.OkHttpClient

/**
 * Created by lukas on 18.04.18.
 */

interface MensaProxy {

    val httpClient: OkHttpClient

    fun getHttpUrl(location: Location, weeknumber: Int): HttpUrl

    fun newCall(requestParser: RequestParser, httpUrl: HttpUrl)

    companion object {
        val URL_BASE = "http://www.stwno.de"
        val URL_PATH = "/infomax/daten-extern/csv/%s/%s.csv"
        val URL_FORMAT = URL_BASE + URL_PATH
    }
}
