package de.dotwee.rgb.canteen.model.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.dotwee.rgb.canteen.R;
import de.dotwee.rgb.canteen.model.api.specs.DayMeal;
import de.dotwee.rgb.canteen.model.api.specs.WeekMeal;
import de.dotwee.rgb.canteen.model.constant.Weekday;

/**
 * Created by lukas on 15.01.17.
 */
public class WeekMealAdapter extends RecyclerView.Adapter<WeekMealAdapter.ViewHolder> {
    private static final String TAG = WeekMealAdapter.class.getSimpleName();
    private WeekMeal weekMeal;

    public WeekMealAdapter(@NonNull WeekMeal weekMeal) {
        this.weekMeal = weekMeal;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_weekmenu_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DayMeal dayMeal = weekMeal.get(Weekday.values()[position]);


        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, dayMeal.getWeekday().getDayOfWeek());
        String weekdayValue = new SimpleDateFormat("EEEE", Locale.getDefault()).format(calendar.getTime());
        holder.textViewWeekday.setText(weekdayValue);
    }

    @Override
    public int getItemCount() {
        return weekMeal == null ? 0 : weekMeal.size();
    }

    static final class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textViewWeekday)
        TextView textViewWeekday;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
