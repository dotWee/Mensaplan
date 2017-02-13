package de.dotwee.rgb.canteen.view.dialogs;

import android.content.Context;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatDialog;
import android.text.Html;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.dotwee.rgb.canteen.R;
import de.dotwee.rgb.canteen.model.api.specs.Item;
import de.dotwee.rgb.canteen.model.constant.Label;
import de.dotwee.rgb.canteen.model.helper.IngredientsHelper;
import de.dotwee.rgb.canteen.presenter.IngredientsPresenter;
import de.dotwee.rgb.canteen.presenter.IngredientsPresenterImpl;
import timber.log.Timber;

/**
 * Created by lukas on 06.12.2016.
 */
public class IngredientsDialog extends AppCompatDialog implements IngredientsView {
    private static final String TAG = IngredientsDialog.class.getSimpleName();

    @BindView(R.id.textViewName)
    TextView textViewName;

    @BindView(R.id.linearLayoutIngredients)
    LinearLayout linearLayoutIngredients;

    @BindView(R.id.textViewIngredients)
    TextView textViewIngredients;

    @BindView(R.id.linearLayoutAllergens)
    LinearLayout linearLayoutAllergens;

    @BindView(R.id.textViewAllergens)
    TextView textViewAllergens;

    @BindView(R.id.linearLayoutLabels)
    LinearLayout linearLayoutLabels;

    @BindView(R.id.tableLayoutLabels)
    TableLayout tableLayoutLabels;

    @BindView(R.id.linearLayoutNodata)
    LinearLayout linearLayoutNodata;

    private IngredientsPresenter ingredientsPresenter;

    public IngredientsDialog(@NonNull Context context) {
        super(context, R.style.AppTheme_Dialog);
        setContentView(R.layout.dialog_ingredients);

        Window window = getWindow();
        if (window == null) {
            throw new IllegalStateException("Window is null, can't bind view!");
        }

        ButterKnife.bind(this, getWindow().getDecorView());
        ingredientsPresenter = new IngredientsPresenterImpl(this);
    }

    public void setItem(@Nullable Item item) {
        Timber.i("Changing ingredients dialog item to %s", item == null ? null : item.getName());
        ingredientsPresenter.onItemChange(item);
    }

    @NonNull
    private TableRow newTableRow() {
        TableRow tableRow = new TableRow(getContext());
        tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        return tableRow;
    }

    @NonNull
    private TextView getNewTextView(@StringRes int stringId) {
        TextView textView = new TextView(getContext());
        textView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        if (Build.VERSION.SDK_INT < 23) {
            textView.setTextAppearance(getContext(), R.style.AppTheme_Dialog_Ingredients_Section_TextView);
        } else {
            textView.setTextAppearance(R.style.AppTheme_Dialog_Ingredients_Section_TextView);
        }
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setText(stringId);

        return textView;
    }

    @NonNull
    private ImageView getNewImageView(@DrawableRes int drawableId) {
        ImageView imageView = new ImageView(getContext());
        imageView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getContext().getResources().getDisplayMetrics());
        imageView.getLayoutParams().height = dimensionInDp;
        imageView.getLayoutParams().width = dimensionInDp;
        imageView.requestLayout();

        Picasso.with(getContext())
                .load(drawableId)
                .into(imageView);

        return imageView;
    }

    @Override
    public void setName(@Nullable String name) {
        if (name == null) {
            textViewName.setText(R.string.dialog_ingredients_title);
        } else {
            textViewName.setText(name);
        }
    }

    @Override
    public void setLabels(@Nullable Label[] labels) {
        if (labels == null || labels.length == -1 || labels[0] == Label.NONE) {
            linearLayoutLabels.setVisibility(View.GONE);
        } else {
            linearLayoutLabels.setVisibility(View.VISIBLE);

            tableLayoutLabels.removeAllViews();
            for (Label label : labels) {

                if (label != Label.NONE) {
                    TableRow tableRow = newTableRow();
                    tableRow.addView(getNewImageView(label.getDrawableId()));
                    tableRow.addView(getNewTextView(label.getStringId()));

                    tableLayoutLabels.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                }
            }
        }
    }

    @Override
    public boolean areLabelsVisible() {
        return linearLayoutLabels.getVisibility() == View.VISIBLE;
    }

    @Override
    public void setIngredients(@Nullable String itemInfo) {
        if (itemInfo == null || itemInfo.isEmpty()) {
            linearLayoutIngredients.setVisibility(View.GONE);
        } else {
            linearLayoutIngredients.setVisibility(View.VISIBLE);
            String contentString = IngredientsHelper.getIngredientsContent(getContext().getResources(), itemInfo);

            if (contentString.isEmpty()) {
                linearLayoutIngredients.setVisibility(View.GONE);
            } else {
                Spanned spanned = Html.fromHtml(contentString);
                textViewIngredients.setText(spanned);
            }
        }
    }

    @Override
    public boolean areIngredientsVisible() {
        return linearLayoutIngredients.getVisibility() == View.VISIBLE;
    }

    @Override
    public void setAllergens(@Nullable String itemInfo) {
        if (itemInfo == null || itemInfo.isEmpty()) {
            linearLayoutAllergens.setVisibility(View.GONE);
        } else {
            linearLayoutAllergens.setVisibility(View.VISIBLE);
            String contentString = IngredientsHelper.getAllergensContent(getContext().getResources(), itemInfo);

            if (contentString.isEmpty()) {
                linearLayoutAllergens.setVisibility(View.GONE);
            } else {
                Spanned spanned = Html.fromHtml(contentString);
                textViewAllergens.setText(spanned);
            }
        }
    }

    @Override
    public boolean areAllergensVisible() {
        return linearLayoutAllergens.getVisibility() == View.VISIBLE;
    }

    @Override
    public void showNoDataView(boolean isDataAvailable) {
        linearLayoutNodata.setVisibility(isDataAvailable ? View.GONE : View.VISIBLE);
    }
}
