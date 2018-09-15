package de.dotwee.rgb.canteen.model.database.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import de.dotwee.rgb.canteen.model.constant.Label;
import de.dotwee.rgb.canteen.model.constant.Type;
import de.dotwee.rgb.canteen.model.database.converters.DateConverters;
import de.dotwee.rgb.canteen.model.database.converters.LabelConverters;
import timber.log.Timber;

@Entity(tableName = "items", foreignKeys = {@ForeignKey(entity = Menu.class, parentColumns = "id", childColumns = "menuId", onDelete = ForeignKey.CASCADE)})
public class Item {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @TypeConverters(DateConverters.class)
    private Date date;

    private String name;
    private String info;
    private String tag;

    private String priceEmployee;
    private String priceGuest;
    private String priceStudent;
    private String priceAll;

    @TypeConverters(LabelConverters.class)
    private Label[] labels;

    @TypeConverters(de.dotwee.rgb.canteen.model.database.converters.TypeConverters.class)
    private Type type;

    @ForeignKey(entity = Menu.class, parentColumns = "id", childColumns = "menuId", onDelete = ForeignKey.CASCADE)
    private long menuId;

    @Ignore
    private List<Item> itemList;

    @Ignore
    public Item() {

    }

    public Item(int id, Date date, String name, String info, String tag, String priceEmployee, String priceGuest, String priceStudent, String priceAll, Label[] labels, Type type, long menuId) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.info = info;
        this.tag = tag;
        this.priceEmployee = priceEmployee;
        this.priceGuest = priceGuest;
        this.priceStudent = priceStudent;
        this.priceAll = priceAll;
        this.labels = labels;
        this.type = type;
        this.menuId = menuId;
    }

    public static Item fromLine(@NonNull String line) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);
        String[] lineSplit = line.split(";");

        Item item = new Item();
        item.setDate(dateFormat.parse(lineSplit[0]));
        item.setName(lineSplit[3]);
        item.setTag(lineSplit[1]);

        String typeValue = lineSplit[2];
        if (typeValue.contains(Type.DESSERT.getIndicator())) {
            item.setType(Type.DESSERT);
        } else if (typeValue.contains(Type.SIDE_DISH.getIndicator())) {
            item.setType(Type.SIDE_DISH);
        } else if (typeValue.contains(Type.SOUP.getIndicator())) {
            item.setType(Type.SOUP);
        } else {
            item.setType(Type.MAIN);
        }

        String[] labelsValue = lineSplit[4].split(",");
        Label[] labels = new Label[labelsValue.length];
        int len = labelsValue.length;
        for (int i = 0; i < len; i++) {
            try {
                labels[i] = Label.valueOf(labelsValue[i]);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                labels[i] = Label.NONE;
                Timber.e(e);
            }
        }
        item.setLabels(labels);

        item.setPriceAll(lineSplit[5]);
        item.setPriceStudent(lineSplit[6]);
        item.setPriceEmployee(lineSplit[7]);
        item.setPriceGuest(lineSplit[8]);

        return item;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getPriceEmployee() {
        return priceEmployee;
    }

    public void setPriceEmployee(String priceEmployee) {
        this.priceEmployee = priceEmployee;
    }

    public String getPriceGuest() {
        return priceGuest;
    }

    public void setPriceGuest(String priceGuest) {
        this.priceGuest = priceGuest;
    }

    public String getPriceStudent() {
        return priceStudent;
    }

    public void setPriceStudent(String priceStudent) {
        this.priceStudent = priceStudent;
    }

    public String getPriceAll() {
        return priceAll;
    }

    public void setPriceAll(String priceAll) {
        this.priceAll = priceAll;
    }

    public Label[] getLabels() {
        return labels;
    }

    public void setLabels(Label[] labels) {
        this.labels = labels;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public long getMenuId() {
        return menuId;
    }

    public void setMenuId(long menuId) {
        this.menuId = menuId;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }
}
