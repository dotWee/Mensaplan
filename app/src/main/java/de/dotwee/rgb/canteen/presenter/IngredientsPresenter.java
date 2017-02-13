package de.dotwee.rgb.canteen.presenter;

import android.support.annotation.Nullable;

import de.dotwee.rgb.canteen.model.api.specs.Item;

/**
 * Created by lukas on 09.02.17.
 */
public interface IngredientsPresenter {
    String TAG = IngredientsPresenter.class.getSimpleName();

    void onItemChange(@Nullable Item item);
}
