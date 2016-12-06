package de.dotwee.rgb.canteen.view.dialogs;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;
import android.support.v7.app.AppCompatDialog;
import android.text.Html;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.dotwee.rgb.canteen.R;

/**
 * Created by lukas on 06.12.2016.
 */
public class IngredientsDialog extends AppCompatDialog {
    private static final String TAG = IngredientsDialog.class.getSimpleName();

    @BindView(R.id.textViewIngredients)
    TextView textViewIngredients;

    public IngredientsDialog(@NonNull Context context) {
        super(context, R.style.AppTheme_Dialog);

        setContentView(R.layout.dialog_ingredients);
        ButterKnife.bind(this, getWindow().getDecorView());

        setIngredientsContent();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setIngredientsContent() {
        String ingredients = getRawString(R.raw.ingredients);
        textViewIngredients.setText(Html.fromHtml(ingredients));
    }

    @NonNull
    private String getRawString(@RawRes int rawId) {
        Resources resources = getContext().getResources();

        InputStream inputStream = resources.openRawResource(rawId);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
}
