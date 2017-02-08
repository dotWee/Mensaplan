package de.dotwee.rgb.canteen.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.dotwee.rgb.canteen.R;
import de.dotwee.rgb.canteen.model.api.data.CacheHelper;
import de.dotwee.rgb.canteen.model.constant.Location;
import de.dotwee.rgb.canteen.model.helper.DateHelper;
import de.dotwee.rgb.canteen.view.dialogs.MessageDialog;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class SplashActivity extends AppCompatActivity implements Observer<Location> {
    private static final String TAG = SplashActivity.class.getSimpleName();
    public static int INTENT_FORCE_REFRESH = 0;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.textView)
    TextView textView;

    private MessageDialog messageDialog;
    private Observable<Location> locationObservable;
    private Location currentLocation;
    private int locationPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.tag(TAG);

        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);
        messageDialog = new MessageDialog(this);


        onCacheNext();
    }

    private void onCacheNext() {
        currentLocation = Location.values()[locationPosition];

        if (locationObservable != null) {
            locationObservable.unsubscribeOn(Schedulers.single());
        }

        locationObservable = CacheHelper.getObservable(currentLocation, DateHelper.getCurrentWeeknumber(), getCacheDir());
        locationObservable
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);
    }

    private void setCaption(@NonNull String message) {
        Timber.i("setCaption: %s", message);
        textView.setText(message);
    }

    private String getCurrentLocationName() {
        return getString(currentLocation.getName());
    }

    @Override
    public void onSubscribe(Disposable d) {

        // Started caching for current location
        String message = getString(R.string.activity_splash_caption_downloading, getCurrentLocationName());
        setCaption(message);
    }

    @Override
    public void onNext(Location location) {

        // Current location successfully cached
        String message = getString(R.string.activity_splash_caption_success, getCurrentLocationName());
        setCaption(message);
    }

    @Override
    public void onError(Throwable e) {
        // Issue with current location...
        e.printStackTrace();
        Timber.e(e);

        if (e instanceof ConnectException || e instanceof UnknownHostException) {
            // Connection issues
            messageDialog.setDialog(MessageDialog.DialogMessage.ISSUE_CONNECTION);
            messageDialog.show();
        } else if (e instanceof IOException) {
            // Mensa seems closed
            messageDialog.setDialog(MessageDialog.DialogMessage.ISSUE_CLOSED);
            messageDialog.show();
        }
    }

    @Override
    public void onComplete() {
        // Completed current location > continuing
        locationPosition++;

        // Finish activity if last location has been cached
        if (locationPosition == Location.values().length) {

            if (!messageDialog.isShowing()) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        } else onCacheNext();
    }
}