package de.dotwee.rgb.canteen.model.events;

/**
 * Created by lukas on 27.02.17.
 */
public interface OnBusEvent<T> {
    static final String TAG = OnBusEvent.class.getSimpleName();

    void onEvent(T object);
}
