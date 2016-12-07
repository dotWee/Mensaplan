package de.dotwee.rgb.canteen.view.dialogs;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialog;
import android.text.Html;
import android.text.Spanned;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.dotwee.rgb.canteen.R;
import de.dotwee.rgb.canteen.model.helper.IngredientsHelper;

/**
 * Created by lukas on 06.12.2016.
 */
public class IngredientsDialog extends AppCompatDialog {
    private static final String TAG = IngredientsDialog.class.getSimpleName();

    @BindView(R.id.textViewIngredients)
    TextView textViewIngredients;

    private String itemInfo = null;

    public IngredientsDialog(@NonNull Context context) {
        super(context, R.style.AppTheme_Dialog);

        setContentView(R.layout.dialog_ingredients);
        ButterKnife.bind(this, getWindow().getDecorView());
        setItemInfo(null);

        setIngredientsContent();
    }

    public IngredientsDialog(@NonNull Context context, @NonNull String itemInfo) {
        super(context, R.style.AppTheme_Dialog);

        setContentView(R.layout.dialog_ingredients);
        ButterKnife.bind(this, getWindow().getDecorView());
        setItemInfo(itemInfo);

        setIngredientsContent();
    }

    public void setItemInfo(@Nullable String itemInfo) {
        this.itemInfo = itemInfo;
        setIngredientsContent();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setIngredientsContent() {
        Resources resources = getContext().getResources();
        String contentString = IngredientsHelper.getIngredientsContent(resources, itemInfo);

        Spanned spanned = Html.fromHtml(contentString);
        textViewIngredients.setText(spanned);
    }

}
