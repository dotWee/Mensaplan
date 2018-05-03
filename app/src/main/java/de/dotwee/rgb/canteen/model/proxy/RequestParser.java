package de.dotwee.rgb.canteen.model.proxy;

import android.support.annotation.NonNull;
import android.util.Log;
import de.dotwee.rgb.canteen.model.Item;
import de.dotwee.rgb.canteen.model.Label;
import de.dotwee.rgb.canteen.model.Price;
import de.dotwee.rgb.canteen.model.Type;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

/**
 * Created by lukas on 18.04.18.
 */

public class RequestParser implements Callback {

    //private static final String INDICATOR_MAIN = "HG";
    private static final String INDICATOR_DESSERT = "N";
    private static final String INDICATOR_SIDE_DISH = "B";
    private static final String INDICATOR_SOUP = "Suppe";

    private ArrayList<Item> items;
    private MensaCallback mensaCallback;

    public RequestParser(MensaCallback mensaCallback) {
        this.mensaCallback = mensaCallback;
        items = new ArrayList<>();
    }

    @NonNull
    public ArrayList<Item> getItems() {
        return items;
    }

    @NonNull
    private Item getItem(@NonNull String line) {
        String[] lineValues = getLineValues(line);

        Item item = new Item(getName(lineValues));
        try {
            item.setDate(getDate(lineValues));
            item.setTag(getTag(lineValues));

            item.setType(getType(lineValues));
            item.setLabel(getLabel(lineValues));

            item.setPrice(Price.ALL, getPrice(lineValues, Price.ALL));
            item.setPrice(Price.STUDENT, getPrice(lineValues, Price.STUDENT));
            item.setPrice(Price.EMPLOYEE, getPrice(lineValues, Price.EMPLOYEE));
            item.setPrice(Price.GUEST, getPrice(lineValues, Price.GUEST));
        } catch (ParseException e) {
            Log.i(getClass().getSimpleName(), "Error with line: " + line);
            e.printStackTrace();
        }
        return item;
    }

    @NonNull
    String getTag(@NonNull String[] lineValues) {
        return lineValues[1];
    }

    @NonNull
    Type getType(@NonNull String[] lineValues) {
        String value = lineValues[2];

        if (value.contains(INDICATOR_DESSERT)) {
            return Type.DESSERT;
        } else if (value.contains(INDICATOR_SIDE_DISH)) {
            return Type.SIDE_DISH;
        } else if (value.contains(INDICATOR_SOUP)) {
            return Type.SOUP;
        } else {
            return Type.MAIN;
        }
    }

    @NonNull
    String getPrice(@NonNull String[] lineValues, @NonNull Price price) {

        switch (price) {
            case STUDENT:
                return lineValues[6];
            case EMPLOYEE:
                return lineValues[7];
            case GUEST:
                return lineValues[8];
            default:
                return lineValues[5];
        }
    }

    @NonNull
    String getName(@NonNull String[] lineValues) {
        return lineValues[3];
    }

    @NonNull
    Date getDate(@NonNull String[] lineValues) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);
        return dateFormat.parse(lineValues[0]);
    }

    @NonNull
    String[] getLineValues(@NonNull String line) {
        return line.split(";");
    }

    @NonNull
    Label[] getLabel(@NonNull String[] lineValues) {
        String line = lineValues[4];

        String[] labels = line.split(",");
        Label[] types = new Label[labels.length];
        int len = labels.length;

        for (int i = 0; i < len; i++) {
            try {
                types[i] = Label.valueOf(labels[i]);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                types[i] = Label.NONE;
            }
        }
        return types;
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
        mensaCallback.onFailure(call, e);
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            String responseContent = responseBody.string();
            Scanner scanner = new Scanner(responseContent);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Item item = getItem(line);
                items.add(item);
            }
            scanner.close();
            mensaCallback.onResponse(call, items);
        }
    }
}
