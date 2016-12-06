package de.dotwee.rgb.canteen.model.threads;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

/**
 * Created by lukas on 12.11.2016.
 */
class MainThreadExecutor implements Executor {
    private static final String TAG = MainThreadExecutor.class.getSimpleName();

    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void execute(@NonNull Runnable runnable) {
        handler.post(runnable);
    }
}
