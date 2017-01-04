package de.dotwee.rgb.canteen.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import de.dotwee.rgb.canteen.R;
import de.dotwee.rgb.canteen.model.api.data.CacheHelper;
import de.dotwee.rgb.canteen.model.constant.Location;
import de.dotwee.rgb.canteen.model.helper.DateHelper;
import de.dotwee.rgb.canteen.view.dialogs.ClosedDialog;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class SplashActivity extends AppCompatActivity implements Observer<Location> {
    private static final String TAG = SplashActivity.class.getSimpleName();
    public static int INTENT_FORCE_REFRESH = 0;
    private ClosedDialog closedDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        closedDialog = new ClosedDialog(this);
        performCheck();
    }

    public void performCheck() {
        if (getIntent().getIntExtra(TAG, -1) == INTENT_FORCE_REFRESH) {
            CacheHelper.getObservable(DateHelper.getCurrentWeeknumber(), getCacheDir())
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this);
        } else {
            this.onComplete();
        }
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(Location location) {
        Timber.i("Cached meal for %s", location.toString());
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        Timber.e(e);
    }

    @Override
    public void onComplete() {
        Timber.i("onComplete");

        Location[] locations = CacheHelper.getCached(getCacheDir(), DateHelper.getCurrentWeeknumber());
        Timber.i("LocationArrayLength=%d", locations.length);

        // Check if there are any cached locations for this week
        if (locations.length != -1) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            Timber.w("No data available!");
            closedDialog.show();
        }
    }
}