package de.dotwee.rgb.canteen.model.helper;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import timber.log.Timber;

/**
 * Created by lukas on 12.11.2016.
 */
public class CacheHelper {
    private static final String TAG = CacheHelper.class.getSimpleName();

    public static void persist(@NonNull File cacheDir, @NonNull InputStream inputStream, @NonNull String filename) {
        OutputStream fileOutputStream = null;
        File file = new File(cacheDir, filename);
        try {
            fileOutputStream = new FileOutputStream(file);

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, read);
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            Timber.e(e);
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Timber.e(e);
                }
            }
        }
    }

    @NonNull
    public static InputStream read(@NonNull File cacheDir, @NonNull String filename) throws FileNotFoundException {
        return new FileInputStream(new File(cacheDir, filename));
    }

    public static boolean exists(@NonNull File cacheDir, @NonNull String filename) {
        return new File(cacheDir, filename).exists();
    }
}
