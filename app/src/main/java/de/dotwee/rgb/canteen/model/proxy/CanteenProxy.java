package de.dotwee.rgb.canteen.model.proxy;

import android.support.annotation.NonNull;

import de.dotwee.rgb.canteen.model.Location;
import okhttp3.OkHttpClient;

/**
 * Created by lukas on 18.04.18.
 */

public interface CanteenProxy {
    static final String URL_FORMAT = "http://www.stwno.de/infomax/daten-extern/csv/%s/%s.csv";

    @NonNull
    OkHttpClient getHttpClient();

    void newCall(@NonNull RequestParser requestParser, @NonNull Location location, int weeknumber);

}
