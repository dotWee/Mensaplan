package de.dotwee.rgb.canteen.view.interfaces;

import android.support.annotation.Nullable;

import de.dotwee.rgb.canteen.model.constant.Label;

/**
 * Created by lukas on 09.02.17.
 */
public interface IngredientsView {
    String TAG = IngredientsView.class.getSimpleName();

    void setName(@Nullable String name);

    void setLabels(@Nullable Label[] labels);

    boolean areLabelsVisible();

    void setIngredients(@Nullable String itemInfo);

    boolean areIngredientsVisible();

    void setAllergens(@Nullable String itemInfo);

    boolean areAllergensVisible();

    void showNoDataView(boolean isDataAvailable);
}
