package de.dotwee.rgb.canteen.model.events;

import android.support.annotation.NonNull;

import de.dotwee.rgb.canteen.model.api.specs.Item;

/**
 * Created by lukas on 07.12.2016.
 */
public class OnItemClickEvent {
    private static final String TAG = OnItemClickEvent.class.getSimpleName();
    private int adapterPosition;
    private String itemInfo = null;
    private Item item;

    public OnItemClickEvent(int adapterPosition) {
        this.adapterPosition = adapterPosition;
    }

    public OnItemClickEvent(String itemInfo) {
        this.itemInfo = itemInfo;
    }

    public OnItemClickEvent(@NonNull Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public String getItemInfo() {
        return itemInfo;
    }

    public int getAdapterPosition() {
        return adapterPosition;
    }
}
