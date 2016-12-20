package de.dotwee.rgb.canteen.presenter;

import android.support.annotation.Nullable;

import de.dotwee.rgb.canteen.model.api.specs.Item;
import de.dotwee.rgb.canteen.model.constant.Label;

/**
 * Created by lukas on 20.12.2016.
 */
public interface IngredientsPresenter {

    void onItemChange(@Nullable Item item);

    void onLabelsChange(@Nullable Label[] labels);

    void onIngredientsChange(@Nullable String itemInfo);

    void onAllergensChange(@Nullable String itemInfo);
}
