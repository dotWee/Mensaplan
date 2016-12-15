package de.dotwee.rgb.canteen.model.api.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import de.dotwee.rgb.canteen.model.constant.Location;
import timber.log.Timber;

/**
 * Created by lukas on 12.11.2016.
 */
public class CacheHelper {
    public static final String FILENAME_FORMAT = "%s-%d.csv";
    private static final String TAG = CacheHelper.class.getSimpleName();

    public static void clear(@NonNull File cacheDir) {
        for (File file : cacheDir.listFiles()) {
            boolean deletion = file.delete();

            if (!deletion) {
                Timber.e("File with path=%s couldn't be deleted", file.getAbsolutePath());
            }
        }
    }

    static void persist(@NonNull File cacheDir, @NonNull InputStream inputStream, @NonNull String filename) {
        Timber.i("persisting file=%s", filename);

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

    @Nullable
    public static InputStream read(@NonNull File cacheDir, @NonNull String filename) throws FileNotFoundException {
        Timber.i("reading file=%s", filename);

        return new FileInputStream(new File(cacheDir, filename));
    }

    public static boolean exists(@NonNull File cacheDir, @NonNull String filename) {
        return new File(cacheDir, filename).exists();
    }

    public static boolean exists(@NonNull File cacheDir, int weeknumber) {
        boolean state = true;

        for (Location location : Location.values()) {
            File file = new File(String.format(Locale.getDefault(), FILENAME_FORMAT, location.getNameTag(), weeknumber));
            state = state == file.exists();
        }

        return state;
    }

}
