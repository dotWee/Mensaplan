package de.dotwee.rgb.canteen.model.proxy

import de.dotwee.rgb.canteen.model.Item
import okhttp3.Call
import java.io.IOException
import java.util.*

/**
 * Created by lukas on 18.04.18.
 */

interface MensaCallback {

    fun onFailure(call: Call, e: IOException)

    @Throws(IOException::class)
    fun onResponse(call: Call, items: ArrayList<Item>)
}
