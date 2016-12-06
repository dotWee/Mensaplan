package de.dotwee.rgb.canteen.model.threads;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by lukas on 12.11.2016.
 */
public class DefaultExecutorSupplier {
    private static final String TAG = DefaultExecutorSupplier.class.getSimpleName();

    /*
    * Number of cores to decide the number of threads
    */
    private static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    /*
    * an instance of DefaultExecutorSupplier
    */
    private static DefaultExecutorSupplier sInstance;
    /*
    * thread pool executor for background tasks
    */
    private final ThreadPoolExecutor mForBackgroundTasks;
    /*
    * thread pool executor for light weight background tasks
    */
    private final ThreadPoolExecutor mForLightWeightBackgroundTasks;
    /*
    * thread pool executor for main thread tasks
    */
    private final Executor mMainThreadExecutor;

    /*
    * constructor for  DefaultExecutorSupplier
    */
    private DefaultExecutorSupplier() {

        // setting the thread factory
        ThreadFactory backgroundPriorityThreadFactory = new
                PriorityThreadFactory();

        // setting the thread pool executor for mForBackgroundTasks;
        mForBackgroundTasks = new ThreadPoolExecutor(
                NUMBER_OF_CORES * 2,
                NUMBER_OF_CORES * 2,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                backgroundPriorityThreadFactory
        );

        // setting the thread pool executor for mForLightWeightBackgroundTasks;
        mForLightWeightBackgroundTasks = new ThreadPoolExecutor(
                NUMBER_OF_CORES * 2,
                NUMBER_OF_CORES * 2,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                backgroundPriorityThreadFactory
        );

        // setting the thread pool executor for mMainThreadExecutor;
        mMainThreadExecutor = new MainThreadExecutor();
    }

    /*
    * returns the instance of DefaultExecutorSupplier
    */
    public static DefaultExecutorSupplier getInstance() {
        if (sInstance == null) {
            synchronized (DefaultExecutorSupplier.class) {
                sInstance = new DefaultExecutorSupplier();
            }
        }

        return sInstance;
    }

    /*
    * returns the thread pool executor for background task
    */
    private ThreadPoolExecutor forBackgroundTasks() {
        return mForBackgroundTasks;
    }

    public void executeBackgroundTask(@NonNull Runnable command) {
        forBackgroundTasks().execute(command);
    }

    /*
    * returns the thread pool executor for light weight background task
    */
    private ThreadPoolExecutor forLightWeightBackgroundTasks() {
        return mForLightWeightBackgroundTasks;
    }

    public void executeLightWeightBackgroundTask(@NonNull Runnable command) {
        forLightWeightBackgroundTasks().execute(command);
    }

    /*
    * returns the thread pool executor for main thread task
    */
    public Executor forMainThreadTasks() {
        return mMainThreadExecutor;
    }


}
