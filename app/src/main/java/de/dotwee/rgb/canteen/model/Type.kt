package de.dotwee.rgb.canteen.model

/**
 * Created by lukas on 25.12.17.
 */

import android.support.annotation.ColorRes
import android.support.annotation.StringRes

import de.dotwee.rgb.canteen.R

/**
 * Created by lukas on 10.11.2016.
 */
enum class Type private constructor(@param:StringRes @field:StringRes
                                    @get:StringRes
                                    val titleId: Int, @param:ColorRes @field:ColorRes
                                    @get:ColorRes
                                    val colorId: Int, @param:ColorRes @field:ColorRes
                                    @get:ColorRes
                                    val darkColorId: Int,
                                    @param:StringRes @get:StringRes
                                    val resId: Int) {

    SOUP(R.string.type_soup, R.color.md_indigo_600, R.color.md_indigo_800, R.string.type_indicator_soup),
    MAIN(R.string.type_main_dish, R.color.md_blue_600, R.color.md_blue_800, R.string.type_indicator_main),
    SIDE_DISH(R.string.type_side_dish, R.color.md_teal_600, R.color.md_teal_800, R.string.type_indicator_side_dish),
    DESSERT(R.string.type_dessert, R.color.md_green_600, R.color.md_green_800, R.string.type_indicator_dessert),
    NONE(R.string.type_none, R.color.md_grey_600, R.color.md_grey_800, R.string.type_indicator_none)

}