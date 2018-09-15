package de.dotwee.rgb.canteen.model.database;

import android.app.Application;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import de.dotwee.rgb.canteen.model.constant.Location;
import de.dotwee.rgb.canteen.model.database.converters.DateConverters;
import de.dotwee.rgb.canteen.model.database.converters.LocationConverters;
import de.dotwee.rgb.canteen.model.database.daos.ItemDao;
import de.dotwee.rgb.canteen.model.database.daos.MenuDao;
import de.dotwee.rgb.canteen.model.database.models.Item;
import de.dotwee.rgb.canteen.model.database.models.Menu;
import de.dotwee.rgb.canteen.network.MensaProxy;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MensaRepository {
    private ItemDao itemDao;
    private MenuDao menuDao;

    private LiveData<List<Menu>> menusLiveData;
    private LiveData<List<Item>> itemsLiveData;

    public MensaRepository(Application application) {
        MensaDatabase mensaDatabase = MensaDatabase.getDatabase(application.getApplicationContext());
        itemDao = mensaDatabase.getItemDao();
        menuDao = mensaDatabase.getMenuDao();

        menusLiveData = menuDao.getAll();
        itemsLiveData = itemDao.getAll();
    }

    public LiveData<List<Menu>> getMenusLiveData() {
        return menusLiveData;
    }

    public LiveData<List<Item>> getItemsLiveData() {
        return itemsLiveData;
    }

    public Single<Long> getInsertItemObservable(final Item item, final long menuId) {
        return Single.create(new SingleOnSubscribe<Long>() {
            @Override
            public void subscribe(SingleEmitter<Long> e) throws Exception {
                item.setMenuId(menuId);
                long itemId = itemDao.insert(item);

                e.onSuccess(itemId);
            }
        });
    }

    public void performDeleteAll() {
        performDeleteItems();
        performDeleteMenus();
    }

    public void performDeleteItems() {
        getDeleteItemsObservable().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    private Single<Void> getDeleteItemsObservable() {
        return Single.create(new SingleOnSubscribe<Void>() {
            @Override
            public void subscribe(SingleEmitter<Void> e) throws Exception {
                itemDao.deleteAll();
            }
        });
    }

    public void performDeleteMenus() {
        getDeleteMenusObservable().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    private Single<Void> getDeleteMenusObservable() {
        return Single.create(new SingleOnSubscribe<Void>() {
            @Override
            public void subscribe(SingleEmitter<Void> e) throws Exception {
                menuDao.deleteAll();
            }
        });
    }

    public long getOrCreateMenuId(Location location, Date date) {
        Menu menu = menuDao.getSpecificMenu(LocationConverters.locationToString(location), DateConverters.dateToLong(date));
        long menuId;
        if (menu == null) {
            menu = new Menu(date, location);
            menuId = menuDao.insert(menu);
            Timber.i("getOrCreateMenu() NEW menuId=%s", menuId);
        } else {
            menuId = menu.getId();
            Timber.i("getOrCreateMenu() EXISTING menuId=%s", menuId);
        }

        return menuId;
    }

    public void performDatabaseRefresh() {
        MensaProxy.getObservable(Location.OTH, MensaProxy.getCurrentWeeknumber(), this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
