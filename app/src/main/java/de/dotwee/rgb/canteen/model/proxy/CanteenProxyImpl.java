package de.dotwee.rgb.canteen.model.proxy;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.util.Locale;

import de.dotwee.rgb.canteen.model.Location;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by lukas on 18.04.18.
 */

public class CanteenProxyImpl implements CanteenProxy {
    private static int CACHE_SIZE = 50 * 1024 * 1024; // 50 MiB

    private OkHttpClient okHttpClient;
    private File cacheDirectory;
    private Cache cache;

    public CanteenProxyImpl(@Nullable File cacheDirectory) {
        this.cacheDirectory = cacheDirectory;
    }


    @NonNull
    @Override
    public OkHttpClient getHttpClient() {
        if (okHttpClient == null) {
            if (cacheDirectory != null) {
                cache = new Cache(cacheDirectory, CACHE_SIZE);
            }

            if (cache != null) {
                okHttpClient = new OkHttpClient.Builder()
                        .cache(cache)
                        .build();
            } else {
                okHttpClient = new OkHttpClient();
            }
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
