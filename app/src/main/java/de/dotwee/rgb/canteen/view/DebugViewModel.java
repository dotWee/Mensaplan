package de.dotwee.rgb.canteen.view;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import de.dotwee.rgb.canteen.model.database.MensaRepository;
import de.dotwee.rgb.canteen.model.database.models.Item;
import de.dotwee.rgb.canteen.model.database.models.Menu;

public class DebugViewModel extends AndroidViewModel {
    private MensaRepository mensaRepository;
    private LiveData<List<Menu>> allMenusLiveData;
    private LiveData<List<Item>> allItemsLiveData;

    public DebugViewModel(@NonNull Application application) {
        super(application);

        mensaRepository = new MensaRepository(application);
        allMenusLiveData = mensaRepository.getMenusLiveData();
        allItemsLiveData = mensaRepository.getItemsLiveData();
    }

    public LiveData<List<Menu>> getAllMenusLiveData() {
        return allMenusLiveData;
    }

    public LiveData<List<Item>> getAllItemsLiveData() {
        return allItemsLiveData;
    }

    // TODO remove
    public MensaRepository getMensaRepository() {
        return mensaRepository;
    }
}
