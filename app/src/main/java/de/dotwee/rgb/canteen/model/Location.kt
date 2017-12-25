package de.dotwee.rgb.canteen.model

import android.support.annotation.StringRes

import de.dotwee.rgb.canteen.R

/**
 * Created by lukas on 25.12.17.
 */

enum class Location private constructor(@param:StringRes @field:StringRes
                                        @get:StringRes
                                        val nameVal: Int, val nameTag: String) {
    OTH(R.string.location_oth, "HS-R-tag"),
    OTH_EVENING(R.string.location_oth_evening, "HS-R-abend"),

    PRUEFENING(R.string.location_pruefening, "Cafeteria-Pruefening"),
    UNIVERSITY(R.string.location_university, "UNI-R")
}