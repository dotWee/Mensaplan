package de.dotwee.rgb.canteen.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import de.dotwee.rgb.canteen.R;

import java.util.Calendar;
import java.util.Locale;

public class DayTabLayout extends FrameLayout implements Button.OnClickListener {
    static int[] WEEKDAYS = new int[]
            {
                    Calendar.MONDAY,
                    Calendar.TUESDAY,
                    Calendar.WEDNESDAY,
                    Calendar.THURSDAY,
                    Calendar.FRIDAY,
                    Calendar.SATURDAY,
                    Calendar.SUNDAY,
            };

    Callback callback;

    public DayTabLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        Calendar calendar = Calendar.getInstance();

        inflate(context, R.layout.frame_daytabs, this);
        LinearLayout linearLayout = findViewById(R.id.linearLayout);

        final int buttonCount = linearLayout.getChildCount();
        for (int i = 0; i < buttonCount; i++) {
            int weekday = WEEKDAYS[i];
            Button button = (Button) linearLayout.getChildAt(i);
            button.setTag(weekday);

            calendar.set(Calendar.DAY_OF_WEEK, weekday);
            String dayVal = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
            button.setText(dayVal);
        }
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onClick(View v) {
        if (callback != null) {
            callback.onDaySelected((int) v.getTag());
        }
    }

    public interface Callback {

        void onDaySelected(int day);
    }
}
