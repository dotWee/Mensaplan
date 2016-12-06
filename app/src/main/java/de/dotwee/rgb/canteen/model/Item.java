package de.dotwee.rgb.canteen.model;

import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.Date;

import de.dotwee.rgb.canteen.model.constant.Label;
import de.dotwee.rgb.canteen.model.constant.Type;
import de.dotwee.rgb.canteen.model.constant.Weekday;

/**
 * Created by lukas on 19.11.2016.
 */
public class Item {
    private static final String TAG = Item.class.getSimpleName();

    private String name;
    private Date date;
    private Weekday weekday;
    private String info;
    private Label[] labels;
    private String priceEmployee;
    private String priceGuest;
    private String priceStudent;
    private String priceAll;
    private String tag;
    private Type type;

    public Item(@NonNull String name) {
        setName(getName(name));
        setInfo(getInfo(name));
    }

    @NonNull
    public String getName() {
        return name;
    }

    private void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public Date getDate() {
        return date;
    }

    public void setDate(@NonNull Date date) {
        this.date = date;
    }

    @NonNull
    public Weekday getWeekday() {
        return weekday;
    }

    public void setWeekday(@NonNull Weekday weekday) {
        this.weekday = weekday;
    }

    @NonNull
    public String getInfo() {
        return info;
    }

    private void setInfo(@NonNull String info) {
        this.info = info;
    }

    @NonNull
    public Label[] getLabels() {
        return labels;
    }

    public void setLabels(@NonNull Label[] labels) {
        this.labels = labels;
    }

    @NonNull
    public String getPriceEmployee() {
        return priceEmployee;
    }

    public void setPriceEmployee(@NonNull String priceEmployee) {
        this.priceEmployee = priceEmployee;
    }

    @NonNull
    public String getPriceGuest() {
        return priceGuest;
    }

    public void setPriceGuest(@NonNull String priceGuest) {
        this.priceGuest = priceGuest;
    }

    @NonNull
    public String getPriceStudent() {
        return priceStudent;
    }

    public void setPriceStudent(@NonNull String priceStudent) {
        this.priceStudent = priceStudent;
    }

    @NonNull
    public String getPriceAll() {
        return priceAll;
    }

    public void setPriceAll(@NonNull String priceAll) {
        this.priceAll = priceAll;
    }

    @NonNull
    public String getTag() {
        return tag;
    }

    public void setTag(@NonNull String tag) {
        this.tag = tag;
    }

    @NonNull
    public Type getType() {
        return type;
    }

    public void setType(@NonNull Type type) {
        this.type = type;
    }

    @NonNull
    private String getName(@NonNull String text) {
        return text.substring(0, getIndexOfStartBracket(text));
    }

    @NonNull
    private String getInfo(@NonNull String text) {
        return text.substring(getIndexOfStartBracket(text));
    }

    private int getIndexOfStartBracket(@NonNull String text) {
        int index = text.indexOf("(");
        return index != -1 ? index : text.length();
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", date=" + date +
                ", info='" + info + '\'' +
                ", labels=" + Arrays.toString(labels) +
                ", priceEmployee='" + priceEmployee + '\'' +
                ", priceGuest='" + priceGuest + '\'' +
                ", priceStudent='" + priceStudent + '\'' +
                ", priceAll='" + priceAll + '\'' +
                ", tag='" + tag + '\'' +
                ", type=" + type +
                '}';
    }
}
