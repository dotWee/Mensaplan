package de.dotwee.rgb.canteen.model.database.daos;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import de.dotwee.rgb.canteen.model.database.models.Item;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ItemDao {

    @Insert
    public long insert(Item item);

    @Insert(onConflict = REPLACE)
    public void insert(Item... items);

    @Update
    public void update(Item... items);

    @Delete
    public void delete(Item... items);

    @Query("DELETE FROM items")
    void deleteAll();

    @Query("SELECT * FROM items")
    LiveData<List<Item>> getAll();

    @Query("SELECT * FROM items WHERE id = :id")
    Item getById(long id);

    @Query("SELECT * FROM items WHERE menuId=:menuId")
    List<Item> getByMenuId(final long menuId);
}
