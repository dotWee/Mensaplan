package de.dotwee.rgb.canteen.presenter;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;

import de.dotwee.rgb.canteen.R;
import de.dotwee.rgb.canteen.model.api.specs.Item;
import de.dotwee.rgb.canteen.model.constant.Label;
import de.dotwee.rgb.canteen.model.helper.IngredientsHelper;
import de.dotwee.rgb.canteen.view.dialogs.IngredientsDialog;

import static de.dotwee.rgb.canteen.model.helper.IngredientsHelper.KEYS_ALL;

/**
 * Created by lukas on 20.12.2016.
 */
public class IngredientsPresenterImpl implements IngredientsPresenter {
    private static final String TAG = IngredientsPresenterImpl.class.getSimpleName();
    private IngredientsDialog ingredientsDialog;
    private Resources resources;

    public IngredientsPresenterImpl(@NonNull IngredientsDialog ingredientsDialog) {
        this.ingredientsDialog = ingredientsDialog;
        this.resources = ingredientsDialog.getContext().getResources();

        onItemChange(null);
    }

    @Override
    public void onItemChange(@Nullable Item item) {
        onNameChange(item == null ? null : item.getName());

        onLabelsChange(item == null ? Label.values() : item.getLabels());
        boolean areLabelsVisible = (ingredientsDialog.linearLayoutLabels.getVisibility() == View.VISIBLE);

        String itemInfo = item == null ? KEYS_ALL : item.getInfo();
        onIngredientsChange(itemInfo);
        boolean areIngredientsVisible = (ingredientsDialog.linearLayoutIngredients.getVisibility() == View.VISIBLE);

        onAllergensChange(itemInfo);
        boolean areAllergensVisible = (ingredientsDialog.linearLayoutAllergens.getVisibility() == View.VISIBLE);

        boolean isDataAvailable = areLabelsVisible || areIngredientsVisible || areAllergensVisible;
        ingredientsDialog.linearLayoutNodata.setVisibility(isDataAvailable ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onNameChange(@Nullable String name) {
        if (name == null) {
            ingredientsDialog.textViewName.setText(R.string.dialog_ingredients_title);
        } else {
            ingredientsDialog.textViewName.setText(name);
        }
    }

    @Override
    public void onLabelsChange(@Nullable Label[] labels) {
        if (labels == null || labels.length == -1 || labels[0] == Label.NONE) {
            ingredientsDialog.linearLayoutLabels.setVisibility(View.GONE);
        } else {
            ingredientsDialog.linearLayoutLabels.setVisibility(View.VISIBLE);

            ingredientsDialog.tableLayoutLabels.removeAllViews();
            for (Label label : labels) {

                if (label != Label.NONE) {
                    TableRow tableRow = ingredientsDialog.newTableRow();
                    tableRow.addView(ingredientsDialog.getNewImageView(label.getDrawableId()));
                    tableRow.addView(ingredientsDialog.getNewTextView(label.getStringId()));

                    ingredientsDialog.tableLayoutLabels.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                }
            }
        }
    }

    @Override
    public void onIngredientsChange(@Nullable String itemInfo) {
        if (itemInfo == null || itemInfo.isEmpty()) {
            ingredientsDialog.linearLayoutIngredients.setVisibility(View.GONE);
        } else {
            ingredientsDialog.linearLayoutIngredients.setVisibility(View.VISIBLE);
            String contentString = IngredientsHelper.getIngredientsContent(resources, itemInfo);

            if (contentString.isEmpty()) {
                ingredientsDialog.linearLayoutIngredients.setVisibility(View.GONE);
            } else {
                Spanned spanned = Html.fromHtml(contentString);
                ingredientsDialog.textViewIngredients.setText(spanned);
            }
        }
    }

    @Override
    public void onAllergensChange(@Nullable String itemInfo) {
        if (itemInfo == null || itemInfo.isEmpty()) {
            ingredientsDialog.linearLayoutAllergens.setVisibility(View.GONE);
        } else {
            ingredientsDialog.linearLayoutAllergens.setVisibility(View.VISIBLE);
            String contentString = IngredientsHelper.getAllergensContent(resources, itemInfo);

            if (contentString.isEmpty()) {
                ingredientsDialog.linearLayoutAllergens.setVisibility(View.GONE);
            } else {
                Spanned spanned = Html.fromHtml(contentString);
                ingredientsDialog.textViewAllergens.setText(spanned);
            }
        }
    }
}
