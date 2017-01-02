package de.dotwee.rgb.canteen.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import de.dotwee.rgb.canteen.R;
import de.dotwee.rgb.canteen.model.api.data.CacheHelper;
import de.dotwee.rgb.canteen.model.constant.Location;
import de.dotwee.rgb.canteen.model.helper.DateHelper;
import de.dotwee.rgb.canteen.view.dialogs.ClosedDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();
    public static int INTENT_FORCE_REFRESH = 0;
    private final Consumer<Throwable> throwableConsumer = new Consumer<Throwable>() {
        @Override
        public void accept(Throwable throwable) throws Exception {
            throwable.printStackTrace();
            Timber.e(throwable);
        }
    };
    private ClosedDialog closedDialog;
    private final Consumer<Void> finishConsumer = new Consumer<Void>() {
        @Override
        public void accept(Void aVoid) throws Exception {
            SplashActivity.this.finish();
        }
    };

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
                    .subscribe(finishConsumer, throwableConsumer);
        } else {
            this.finish();
        }
    }

    @Override
    public void finish() {
        Timber.i("onFinished");

        Location[] locations = CacheHelper.getCached(getCacheDir(), DateHelper.getCurrentWeeknumber());
        Timber.i("LocationArrayLength=%d", locations.length);

        // Check if there are any cached locations for this week
        if (locations.length != -1) {
            startActivity(new Intent(this, MainActivity.class));
            super.finish();
        } else {
            Timber.w("No data available!");
            closedDialog.show();
        }
    }
}