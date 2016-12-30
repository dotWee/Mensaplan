package de.dotwee.rgb.canteen.view.dialogs;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatDialog;
import android.util.TypedValue;
import android.view.Gravity;
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
import de.dotwee.rgb.canteen.presenter.IngredientsPresenter;
import de.dotwee.rgb.canteen.presenter.IngredientsPresenterImpl;

/**
 * Created by lukas on 06.12.2016.
 */
public class IngredientsDialog extends AppCompatDialog {
    private static final String TAG = IngredientsDialog.class.getSimpleName();

    @BindView(R.id.textViewName)
    public TextView textViewName;

    @BindView(R.id.linearLayoutIngredients)
    public LinearLayout linearLayoutIngredients;

    @BindView(R.id.textViewIngredients)
    public TextView textViewIngredients;

    @BindView(R.id.linearLayoutAllergens)
    public LinearLayout linearLayoutAllergens;

    @BindView(R.id.textViewAllergens)
    public TextView textViewAllergens;

    @BindView(R.id.linearLayoutLabels)
    public LinearLayout linearLayoutLabels;

    @BindView(R.id.tableLayoutLabels)
    public TableLayout tableLayoutLabels;

    @BindView(R.id.linearLayoutNodata)
    public LinearLayout linearLayoutNodata;

    private IngredientsPresenter ingredientsPresenter;

    public IngredientsDialog(@NonNull Context context) {
        super(context, R.style.AppTheme_Dialog);

        setContentView(R.layout.dialog_ingredients);
        ButterKnife.bind(this, getWindow().getDecorView());
        ingredientsPresenter = new IngredientsPresenterImpl(this);
    }

    public void setItem(@Nullable Item item) {
        ingredientsPresenter.onItemChange(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    public TableRow newTableRow() {
        TableRow tableRow = new TableRow(getContext());
        tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        return tableRow;
    }

    @NonNull
    public TextView getNewTextView(@StringRes int stringId) {
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
    public ImageView getNewImageView(@DrawableRes int drawableId) {
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
}
