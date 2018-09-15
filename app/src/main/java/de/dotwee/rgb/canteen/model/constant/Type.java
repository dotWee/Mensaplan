package de.dotwee.rgb.canteen.model.constant;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import de.dotwee.rgb.canteen.R;

/**
 * Created by lukas on 10.11.2016.
 */
public enum Type {
    SOUP(R.string.type_soup, R.color.md_indigo_600, R.color.md_indigo_800, "Suppe"),
    MAIN(R.string.type_main_dish, R.color.md_blue_600, R.color.md_blue_800, "HG"),
    SIDE_DISH(R.string.type_side_dish, R.color.md_teal_600, R.color.md_teal_800, "B"),
    DESSERT(R.string.type_dessert, R.color.md_green_600, R.color.md_green_800, "N"),
    NONE(R.string.type_none, R.color.md_grey_600, R.color.md_grey_800, "");

    @StringRes
    private final int titleId;

    @ColorRes
    private final int colorId;

    @ColorRes
    private final int darkColorId;

    private final String indicator;

    Type(@StringRes int titleId, @ColorRes int colorId, @ColorRes int darkColorId, @NonNull String indicator) {
        this.titleId = titleId;
        this.colorId = colorId;
        this.darkColorId = darkColorId;
        this.indicator = indicator;
    }

    @StringRes
    public int getTitleId() {
        return this.titleId;
    }

    @ColorRes
    public int getColorId() {
        return colorId;
    }

    @ColorRes
    public int getDarkColorId() {
        return darkColorId;
    }

    @NonNull
    public String getIndicator() {
        return indicator;
    }
}