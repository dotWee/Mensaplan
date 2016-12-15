package de.dotwee.rgb.canteen.model.constant;

import android.support.annotation.DrawableRes;

/**
 * Created by lukas on 10.11.2016.
 */
public enum Label {
    V(de.dotwee.rgb.canteen.R.drawable.ic_meal_vegetarian),
    VG(de.dotwee.rgb.canteen.R.drawable.ic_meal_vegan),
    S(de.dotwee.rgb.canteen.R.drawable.ic_meal_pork),
    R(de.dotwee.rgb.canteen.R.drawable.ic_meal_cattle),
    A(de.dotwee.rgb.canteen.R.drawable.ic_meal_alcohol),
    G(de.dotwee.rgb.canteen.R.drawable.ic_meal_poultry),
    B(de.dotwee.rgb.canteen.R.drawable.ic_meal_oeko),
    F(de.dotwee.rgb.canteen.R.drawable.ic_meal_fish),
    MV(de.dotwee.rgb.canteen.R.drawable.ic_meal_vital),
    L(de.dotwee.rgb.canteen.R.drawable.ic_meal_lamb),
    W(de.dotwee.rgb.canteen.R.drawable.ic_meal_wild),
    NONE(0);

    final int drawableId;

    Label(@DrawableRes int drawableId) {
        this.drawableId = drawableId;
    }

    @DrawableRes
    public int getDrawableId() {
        return drawableId;
    }
}