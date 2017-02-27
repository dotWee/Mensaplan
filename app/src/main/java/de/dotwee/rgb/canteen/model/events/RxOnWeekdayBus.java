package de.dotwee.rgb.canteen.model.events;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import de.dotwee.rgb.canteen.model.constant.Weekday;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

/**
 * Created by lukas on 27.02.17.
 */
public class RxOnWeekdayBus implements Observer<Weekday> {
    private static final String TAG = RxOnWeekdayBus.class.getSimpleName();
    private static RxOnWeekdayBus instance;
    private static OnBusEvent<Weekday> onBusEvent;
    @NonNull
    private final PublishSubject<Weekday> bus = PublishSubject.create();
    private OnBusEvent<Weekday> weekdayOnBusEvent;

    // Private constructor
    private RxOnWeekdayBus() {

    }

    private static synchronized RxOnWeekdayBus getInstance() {
        if (RxOnWeekdayBus.instance == null) {
            RxOnWeekdayBus.instance = new RxOnWeekdayBus();
        }

        return RxOnWeekdayBus.instance;
    }

    public static void subscribeTo(@NonNull OnBusEvent<Weekday> weekdayOnBusEvent) {
        instance.weekdayOnBusEvent = weekdayOnBusEvent;

        instance.toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(instance);
    }

    public static void send(@Nullable final Weekday weekday) {
        instance.onNext(weekday);
    }

    @NonNull
    private Observable<Weekday> toObservable() {
        return bus;
    }

    public boolean hasObservers() {
        boolean hasObservers = bus.hasObservers();

        Timber.i("hasObservers=%s", String.valueOf(hasObservers));
        return hasObservers;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(Weekday weekday) {
        onBusEvent.onEvent(weekday);
    }

    @Override
    public void onError(Throwable e) {
        Timber.e(e);
    }

    @Override
    public void onComplete() {

    }
}
