package de.dotwee.rgb.canteen.view.dialogs;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatDialog;
import android.text.Html;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
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

/**
 * Created by lukas on 06.12.2016.
 */
public class IngredientsDialog extends AppCompatDialog {
    private static final String TAG = IngredientsDialog.class.getSimpleName();

    @BindView(R.id.tableLayout)
    TableLayout tableLayout;

    @BindView(R.id.textViewIngredients)
    TextView textViewIngredients;

    @BindView(R.id.textViewExplanations)
    TextView textViewExplanations;

    public IngredientsDialog(@NonNull Context context) {
        super(context, R.style.AppTheme_Dialog);

        setContentView(R.layout.dialog_ingredients);
        ButterKnife.bind(this, getWindow().getDecorView());
        setItem(null);
    }

    public void setItem(@Nullable Item item) {
        if (item != null) {
            setItemInfo(item.getInfo());
            setItemLabels(item.getLabels());
            return;
        }

        setItemInfo(null);
        setItemLabels(Label.values());
    }

    private void setItemLabels(Label[] labels) {
        labels = (labels == null) ? Label.values() : labels;

        tableLayout.removeAllViews();
        for (Label label : labels) {
            addLabel(label);
        }
    }

    private void setItemInfo(@Nullable String itemInfo) {
        Resources resources = getContext().getResources();
        String contentString = IngredientsHelper.getIngredientsContent(resources, itemInfo);

        Spanned spanned = Html.fromHtml(contentString);
        textViewIngredients.setText(spanned);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void addLabel(Label label) {
        if (label == Label.NONE) {
            return;
        }

        TableRow tableRow = newTableRow();
        tableRow.addView(getNewImageView(label.getDrawableId()));
        tableRow.addView(getNewTextView(label.getStringId()));

        tableLayout.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
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
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
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
}
