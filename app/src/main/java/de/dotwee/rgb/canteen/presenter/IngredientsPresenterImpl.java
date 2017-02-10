package de.dotwee.rgb.canteen.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import de.dotwee.rgb.canteen.model.api.specs.Item;
import de.dotwee.rgb.canteen.model.constant.Label;
import de.dotwee.rgb.canteen.view.interfaces.IngredientsView;

import static de.dotwee.rgb.canteen.model.helper.IngredientsHelper.KEYS_ALL;

/**
 * Created by lukas on 10.02.17.
 */
public class IngredientsPresenterImpl implements IngredientsPresenter {
    private static final String TAG = IngredientsPresenterImpl.class.getSimpleName();
    private final IngredientsView ingredientsView;

    public IngredientsPresenterImpl(@NonNull IngredientsView ingredientsView) {
        this.ingredientsView = ingredientsView;

        // Init empty view
        onItemChange(null);
    }

    @Override
    public void onItemChange(@Nullable Item item) {
        String itemInfo = item == null ? KEYS_ALL : item.getInfo();

        // Update name
        ingredientsView.setName(item == null ? null : item.getName());

        // Update labels
        ingredientsView.setLabels(item == null ? Label.values() : item.getLabels());
        boolean areLabelsVisible = ingredientsView.areLabelsVisible();

        // Update ingredients
        ingredientsView.setIngredients(itemInfo);
        boolean areIngredientsVisible = ingredientsView.areIngredientsVisible();

        // Update allergens
        ingredientsView.setAllergens(itemInfo);
        boolean areAllergensVisible = ingredientsView.areAllergensVisible();

        // Set nodata view if necessary
        boolean isDataAvailable = areLabelsVisible || areIngredientsVisible || areAllergensVisible;
        ingredientsView.showNoDataView(isDataAvailable);
    }
}
