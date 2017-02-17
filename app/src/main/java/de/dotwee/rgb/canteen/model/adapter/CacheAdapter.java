package de.dotwee.rgb.canteen.model.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.dotwee.rgb.canteen.R;
import de.dotwee.rgb.canteen.model.constant.Location;
import de.dotwee.rgb.canteen.view.custom.LocationCacheView;

/**
 * Created by lukas on 17.02.17.
 */
public class CacheAdapter extends RecyclerView.Adapter<CacheAdapter.ViewHolder> {
    private static final String TAG = CacheAdapter.class.getSimpleName();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_locationcache, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Location location = Location.values()[position];
        holder.locationCacheView.setLocation(location);
    }

    @Override
    public int getItemCount() {
        return Location.values().length + 1;
    }

    static final class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.locationCacheView)
        LocationCacheView locationCacheView;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
