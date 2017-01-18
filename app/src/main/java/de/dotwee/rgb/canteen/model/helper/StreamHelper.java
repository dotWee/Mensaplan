package de.dotwee.rgb.canteen.model.helper;

import android.support.annotation.Nullable;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Created by lukas on 15.01.17.
 */
public class StreamHelper {
    private static final String TAG = StreamHelper.class.getSimpleName();

    @Nullable
    public static String toString(@Nullable InputStream inputStream, @Nullable String charset) {
        if (inputStream == null) {
            return null;
        }

        Scanner scanner = new Scanner(inputStream, charset == null ? "UTF-8" : charset).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : null;
    }


}
