package de.dotwee.rgb.canteen.model.database.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import de.dotwee.rgb.canteen.model.constant.Location;
import de.dotwee.rgb.canteen.model.database.converters.DateConverters;
import de.dotwee.rgb.canteen.model.database.converters.LocationConverters;

@Entity(tableName = "menus")
public class Menu {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @TypeConverters(DateConverters.class)
    private Date date;

    @TypeConverters(LocationConverters.class)
    private Location location;
    @Ignore
    private List<Item> itemList;

    @Ignore
    public Menu(Date date, Location location) {
        this.date = date;
        this.location = location;
    }

    public Menu(int id, Date date, Location location) {
        this.id = id;
        this.date = date;
        this.location = location;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public void addItem(Item item) {
        if (itemList == null) {
            itemList = new ArrayList<>();
        }
        itemList.add(item);
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

}
