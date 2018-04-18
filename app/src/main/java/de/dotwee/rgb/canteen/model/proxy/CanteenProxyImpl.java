package de.dotwee.rgb.canteen.model.proxy;

import android.support.annotation.NonNull;

import java.util.Locale;

import de.dotwee.rgb.canteen.model.Location;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by lukas on 18.04.18.
 */

public class CanteenProxyImpl implements CanteenProxy {
    private OkHttpClient okHttpClient;

    public CanteenProxyImpl() {

    }


    @NonNull
    @Override
    public OkHttpClient getHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }

        return okHttpClient;
    }

    @Override
    public void newCall(@NonNull RequestParser requestParser, @NonNull Location location, int weeknumber) {
        Request request = new Request.Builder()
                .url(String.format(Locale.getDefault(), URL_FORMAT, location.getNameTag(), weeknumber))
                .build();

        getHttpClient()
                .newCall(request)
                .enqueue(requestParser);
    }

}