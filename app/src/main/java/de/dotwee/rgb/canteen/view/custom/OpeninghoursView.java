package de.dotwee.rgb.canteen.view.custom;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.dotwee.rgb.canteen.R;
import de.dotwee.rgb.canteen.model.constant.Location;
import de.dotwee.rgb.canteen.model.constant.Weekday;
import de.dotwee.rgb.canteen.model.helper.DateHelper;

/**
 * Created by lukas on 19.01.17.
 */
public class OpeninghoursView extends FrameLayout {
    private static final String TAG = OpeninghoursView.class.getSimpleName();
    static TableLayout.LayoutParams defaultTableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
    static TableRow.LayoutParams defaultRowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
    final Location location;

    @BindView(R.id.tableLayoutDuring)
    TableLayout tableLayoutDuring;

    @BindView(R.id.tableLayoutOutside)
    TableLayout tableLayoutOutside;

    public OpeninghoursView(@NonNull Context context) {
        this(context, null);
    }

    public OpeninghoursView(@NonNull Context context, @NonNull AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OpeninghoursView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, Location.OTH);
    }

    public OpeninghoursView(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr, @NonNull Location location) {
        super(context);
        this.location = location;


        inflate(context, R.layout.view_openinghours, this);
        ButterKnife.bind(this);

        for (Weekday weekday : Weekday.values()) {
            setDuringPeriod(weekday);
            setOutsidePeriod(weekday);
        }
    }

    private void setDuringPeriod(@NonNull Weekday weekday) {
        TableRow tableRow = newTableRow();

        String weekdayValue = DateHelper.format(weekday, new SimpleDateFormat("EEEE", Locale.getDefault()));
        TextView textViewWeekday = getNewTextView(weekdayValue);
        tableRow.addView(textViewWeekday);

        String openingValue = location.getOpeninghours().getDuringLectures(weekday);
        if (openingValue == null)
            openingValue = getResources().getString(R.string.openingperiod_closed);
        TextView textViewOpening = getNewTextView(openingValue);
        tableRow.addView(textViewOpening);

        tableLayoutDuring.addView(tableRow, defaultTableParams);
    }

    private void setOutsidePeriod(@NonNull Weekday weekday) {
        TableRow tableRow = newTableRow();

        String weekdayValue = DateHelper.format(weekday, new SimpleDateFormat("EEEE", Locale.getDefault()));
        TextView textViewWeekday = getNewTextView(weekdayValue);
        tableRow.addView(textViewWeekday);

        String openingValue = location.getOpeninghours().getOutsideLecture(weekday);
        if (openingValue == null)
            openingValue = getResources().getString(R.string.openingperiod_closed);
        TextView textViewOpening = getNewTextView(openingValue);
        tableRow.addView(textViewOpening);

        tableLayoutOutside.addView(tableRow, defaultTableParams);
    }

    @NonNull
    private TableRow newTableRow() {
        TableRow tableRow = new TableRow(getContext());
        tableRow.setLayoutParams(defaultRowParams);

        return tableRow;
    }

    @NonNull
    public TextView getNewTextView(@NonNull String value) {
        TextView textView = new TextView(getContext());
        textView.setLayoutParams(defaultRowParams);
        if (Build.VERSION.SDK_INT < 23) {
            textView.setTextAppearance(getContext(), R.style.AppTheme_View_Openinghours_Section_TextView);
        } else {
            textView.setTextAppearance(R.style.AppTheme_View_Openinghours_Section_TextView);
        }
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setText(value);

        return textView;
    }

}
