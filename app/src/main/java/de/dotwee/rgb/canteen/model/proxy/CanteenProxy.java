package de.dotwee.rgb.canteen.model.proxy;

import android.support.annotation.NonNull;

import de.dotwee.rgb.canteen.model.Location;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * Created by lukas on 18.04.18.
 */

public interface CanteenProxy {
    static final String URL_BASE = "http://www.stwno.de";
    static final String URL_PATH = "/infomax/daten-extern/csv/%s/%s.csv";
    static final String URL_FORMAT = URL_BASE + URL_PATH;

    @NonNull
    OkHttpClient getHttpClient();

    @NonNull
    HttpUrl getHttpUrl(@NonNull Location location, int weeknumber);

    void newCall(@NonNull RequestParser requestParser, @NonNull HttpUrl httpUrl);

}
