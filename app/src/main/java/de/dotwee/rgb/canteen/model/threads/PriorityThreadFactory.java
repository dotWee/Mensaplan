package de.dotwee.rgb.canteen.model.threads;

import android.os.Process;
import android.support.annotation.NonNull;

import java.util.concurrent.ThreadFactory;

import timber.log.Timber;

/**
 * Created by lukas on 12.11.2016.
 */
class PriorityThreadFactory implements ThreadFactory {
    private static final String TAG = PriorityThreadFactory.class.getSimpleName();

    private final int mThreadPriority;

    public PriorityThreadFactory() {
        mThreadPriority = Process.THREAD_PRIORITY_BACKGROUND;
    }

    @Override
    public Thread newThread(@NonNull final Runnable runnable) {
        Runnable wrapperRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Process.setThreadPriority(mThreadPriority);
                } catch (Throwable t) {
                    Timber.e(t);
                }

                runnable.run();
            }
        };
        return new Thread(wrapperRunnable);
    }
}
