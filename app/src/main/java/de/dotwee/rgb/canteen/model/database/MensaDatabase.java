package de.dotwee.rgb.canteen.model.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import de.dotwee.rgb.canteen.model.database.daos.ItemDao;
import de.dotwee.rgb.canteen.model.database.daos.MenuDao;
import de.dotwee.rgb.canteen.model.database.models.Item;
import de.dotwee.rgb.canteen.model.database.models.Menu;

@Database(entities = {Item.class, Menu.class}, version = 1)
public abstract class MensaDatabase extends RoomDatabase {

    private static volatile MensaDatabase INSTANCE;

    static MensaDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MensaDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MensaDatabase.class, "mensa_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract MenuDao getMenuDao();

    public abstract ItemDao getItemDao();
}
