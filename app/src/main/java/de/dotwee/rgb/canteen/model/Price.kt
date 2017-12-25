package de.dotwee.rgb.canteen.model

/**
 * Created by lukas on 25.12.17.
 */


import android.support.annotation.StringRes

import de.dotwee.rgb.canteen.R

/**
 * Created by lukas on 10.11.2016.
 */
enum class Price private constructor(@param:StringRes @get:StringRes
                                     val resId: Int) {
    STUDENT(R.string.price_student),
    EMPLOYEE(R.string.price_employee),
    GUEST(R.string.price_guest),
    ALL(R.string.price_all)
}