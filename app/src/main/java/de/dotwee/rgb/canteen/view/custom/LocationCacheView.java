package de.dotwee.rgb.canteen.view.custom;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.dotwee.rgb.canteen.R;
import de.dotwee.rgb.canteen.model.api.cache.CacheWrapper;
import de.dotwee.rgb.canteen.model.constant.Location;
import de.dotwee.rgb.canteen.model.helper.DateHelper;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by lukas on 17.02.17.
 */
public class LocationCacheView extends FrameLayout {
    private static final String TAG = LocationCacheView.class.getSimpleName();
    private static final int WEEKNUMBER_OFFSET = 5;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private Location location;
    private Observable<LinkedHashMap<Integer, File>> observable;
    private ChildAdapter childAdapter;

    public LocationCacheView(Context context) {
        this(context, null);
    }

    public LocationCacheView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LocationCacheView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, Location.OTH);
    }

    public LocationCacheView(Context context, AttributeSet attrs, int defStyleAttr, Location location) {
        super(context, attrs, defStyleAttr);

        this.location = location;
        init();
    }

    private void init() {

        // Inflate view
        View view = inflate(getContext(), R.layout.view_locationcache, this);

        // Bind view fields
        ButterKnife.bind(this, view);

        // Set location
        setLocation(location);
    }

    public void setLocation(Location location) {
        this.location = location;

        // Set TextView
        setTextView();

        // Set RecyclerView
        setRecyclerView();

        // Load data
        loadData();
    }

    private void setTextView() {
        int nameResId = location.getName();
        textView.setText(nameResId);
    }

    private void setRecyclerView() {
        observable = CacheWrapper.getObservable(getContext().getCacheDir(), location, WEEKNUMBER_OFFSET);
        childAdapter = new ChildAdapter();

        recyclerView.setAdapter(childAdapter);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void loadData() {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(childAdapter);
    }

    /**
     * Created by lukas on 17.02.17.
     */
    public static class ChildAdapter extends RecyclerView.Adapter<ChildViewHolder> implements Consumer<LinkedHashMap<Integer, File>> {
        private static final String TAG = ChildAdapter.class.getSimpleName();
        private Map<Integer, File> fileMap;

        @Override
        public ChildViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_locationcache_item, parent, false);
            return new ChildViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ChildViewHolder holder, int position) {
            int weeknumber = (int) (fileMap.keySet().toArray()[position]);
            holder.cardView.setTag(weeknumber);

            File cacheFile = fileMap.get(weeknumber);
            boolean exists = cacheFile.exists();

            holder.imageButtonDelete.setEnabled(exists);
            holder.imageButtonDownload.setEnabled(!exists);

            holder.textViewWeekNumber.setText(String.valueOf(weeknumber));
            holder.textViewWeekCaptionRange.setText(DateHelper.getWeekRange(weeknumber));
        }

        @Override
        public int getItemCount() {
            return fileMap != null ? fileMap.size() : 0;
        }

        @Override
        public void accept(LinkedHashMap<Integer, File> integerFileMap) throws Exception {
            Timber.i("accept map size=%d", integerFileMap.size());
            this.fileMap = integerFileMap;
        }
    }

    static final class ChildViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cardView)
        CardView cardView;

        @BindView(R.id.textViewWeekNumber)
        TextView textViewWeekNumber;

        @BindView(R.id.textViewWeekCaptionRange)
        TextView textViewWeekCaptionRange;

        @BindView(R.id.imageButtonDownload)
        ImageButton imageButtonDownload;

        @BindView(R.id.imageButtonDelete)
        ImageButton imageButtonDelete;

        ChildViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.imageButtonDownload)
        void onClickDownload(View v) {
            Timber.i("onClickDownload");
            // todo download
        }

        @OnClick(R.id.imageButtonDelete)
        void onClickDelete(View v) {
            Timber.i("onClickDelete");
            // todo delete
        }
    }
}
