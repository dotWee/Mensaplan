package de.dotwee.rgb.canteen.model.proxy;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.ArrayList;

import de.dotwee.rgb.canteen.model.Item;
import okhttp3.Call;

/**
 * Created by lukas on 18.04.18.
 */

public interface MensaCallback {

    void onFailure(@NonNull Call call, @NonNull IOException e);

    void onResponse(@NonNull Call call, @NonNull ArrayList<Item> items) throws IOException;
}
