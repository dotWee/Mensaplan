package de.dotwee.rgb.canteen.model.database.daos;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import de.dotwee.rgb.canteen.model.database.models.Menu;

@Dao
public interface MenuDao {

    @Insert
    long insert(Menu menu);

    @Insert
    void insert(Menu... menus);

    @Update
    void update(Menu... menus);

    @Delete
    void delete(Menu... menus);

    @Query("DELETE FROM menus")
    void deleteAll();

    @Query("SELECT * FROM menus")
    LiveData<List<Menu>> getAll();

    @Query("SELECT * FROM menus WHERE id = :id")
    Menu getById(int id);

    @Query("SELECT * FROM menus WHERE location = :location AND date = :date")
    Menu getSpecificMenu(String location, long date);
}
