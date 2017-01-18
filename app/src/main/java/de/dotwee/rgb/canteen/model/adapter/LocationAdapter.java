package de.dotwee.rgb.canteen.model.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.dotwee.rgb.canteen.R;
import de.dotwee.rgb.canteen.model.api.specs.Meal;
import de.dotwee.rgb.canteen.model.api.specs.WeekMeal;
import de.dotwee.rgb.canteen.model.constant.Location;

/**
 * Created by lukas on 15.01.17.
 */
public class LocationAdapter extends PagerAdapter {
    private static final String TAG = LocationAdapter.class.getSimpleName();

    private Context context;
    private Meal meal;

    public LocationAdapter(@NonNull Context context, @NonNull Meal meal) {
        if (meal.size() == 0) {
            throw new IllegalStateException("Meal is empty!");
        }

        this.context = context;
        this.meal = meal;
    }

    @Override
    public int getCount() {
        return Location.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());

        Location location = Location.values()[position];
        WeekMeal weekMeal = meal.get(location);

        // setup recyclerview
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.content_recyclerview, container, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new WeekMealAdapter(weekMeal));

        container.addView(recyclerView);
        return recyclerView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Location location = Location.values()[position];
        return context.getString(location.getName());
    }
}
