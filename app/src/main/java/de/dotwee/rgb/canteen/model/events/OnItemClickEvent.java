package de.dotwee.rgb.canteen.model.events;

import android.support.annotation.NonNull;

import de.dotwee.rgb.canteen.model.api.specs.Item;

/**
 * Created by lukas on 07.12.2016.
 */
public class OnItemClickEvent {
    private static final String TAG = OnItemClickEvent.class.getSimpleName();
    private Item item = null;

    public OnItemClickEvent(@NonNull Item item) {
        this.item = item;
    }

    @NonNull
    public Item getItem() {
        return item;
    }
}
