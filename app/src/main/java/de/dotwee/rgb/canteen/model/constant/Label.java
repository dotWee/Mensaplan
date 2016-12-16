package de.dotwee.rgb.canteen.model.constant;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

/**
 * Created by lukas on 10.11.2016.
 */
public enum Label {
    V(de.dotwee.rgb.canteen.R.drawable.ic_meal_vegetarian, de.dotwee.rgb.canteen.R.string.ingredient_vegetarian),
    VG(de.dotwee.rgb.canteen.R.drawable.ic_meal_vegan, de.dotwee.rgb.canteen.R.string.ingredient_vegan),
    S(de.dotwee.rgb.canteen.R.drawable.ic_meal_pork, de.dotwee.rgb.canteen.R.string.ingredient_pork),
    R(de.dotwee.rgb.canteen.R.drawable.ic_meal_cattle, de.dotwee.rgb.canteen.R.string.ingredient_cattle),
    A(de.dotwee.rgb.canteen.R.drawable.ic_meal_alcohol, de.dotwee.rgb.canteen.R.string.ingredient_alcohol),
    G(de.dotwee.rgb.canteen.R.drawable.ic_meal_poultry, de.dotwee.rgb.canteen.R.string.ingredient_poultry),
    B(de.dotwee.rgb.canteen.R.drawable.ic_meal_oeko, de.dotwee.rgb.canteen.R.string.ingredient_oeko),
    F(de.dotwee.rgb.canteen.R.drawable.ic_meal_fish, de.dotwee.rgb.canteen.R.string.ingredient_fish),
    MV(de.dotwee.rgb.canteen.R.drawable.ic_meal_vital, de.dotwee.rgb.canteen.R.string.ingredient_vital),
    L(de.dotwee.rgb.canteen.R.drawable.ic_meal_lamb, de.dotwee.rgb.canteen.R.string.ingredient_lamb),
    W(de.dotwee.rgb.canteen.R.drawable.ic_meal_wild, de.dotwee.rgb.canteen.R.string.ingredient_wild),
    NONE(0, 0);

    @DrawableRes
    final int drawableId;

    @StringRes
    final int stringId;

    Label(@DrawableRes int drawableId, @StringRes int stringId) {
        this.drawableId = drawableId;
        this.stringId = stringId;
    }

    @DrawableRes
    public int getDrawableId() {
        return drawableId;
    }

    public int getStringId() {
        return stringId;
    }
}