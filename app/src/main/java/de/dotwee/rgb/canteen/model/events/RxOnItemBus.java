package de.dotwee.rgb.canteen.model.events;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import de.dotwee.rgb.canteen.model.api.specs.Item;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

/**
 * Created by lukas on 23.02.17.
 */
public final class RxOnItemBus {
    private static final String TAG = RxOnItemBus.class.getSimpleName();
    private static RxOnItemBus instance;
    @NonNull
    private final PublishSubject<Item> bus = PublishSubject.create();

    // Private constructor
    private RxOnItemBus() {

    }

    public static synchronized RxOnItemBus getInstance() {
        if (RxOnItemBus.instance == null) {
            Timber.i("Creating new %s instance", TAG);
            RxOnItemBus.instance = new RxOnItemBus();
        }

        return RxOnItemBus.instance;
    }

    public static void subscribeTo(@NonNull Observer<Item> observer) {
        RxOnItemBus.getInstance()
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void send(@Nullable final Item item) {
        bus.onNext(item);
    }

    @NonNull
    private Observable<Item> toObservable() {
        Timber.i("toObservable");
        return bus;
    }

    public boolean hasObservers() {
        boolean hasObservers = bus.hasObservers();

        Timber.i("hasObservers=%s", String.valueOf(hasObservers));
        return hasObservers;
    }
}
