package de.dotwee.rgb.canteen.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;
import java.util.Scanner;

import androidx.annotation.NonNull;
import de.dotwee.rgb.canteen.model.constant.Location;
import de.dotwee.rgb.canteen.model.database.MensaRepository;
import de.dotwee.rgb.canteen.model.database.models.Item;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MensaProxy {

    private static final String URL_FORMAT = "http://www.stwno.de/infomax/daten-extern/csv/%s/%s.csv";

    public static int getCurrentWeeknumber() {
        return Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
    }

    public static URL getMenuUrl(Location location, int weeknumber) throws MalformedURLException {
        return new URL(String.format(Locale.getDefault(), URL_FORMAT, location.getNameTag(), String.valueOf(weeknumber)));
    }

    @NonNull
    public static Observable<Item> getObservable(@NonNull final Location location, final int weekOfYear, final MensaRepository mensaRepository) {
        return Observable.create(new ObservableOnSubscribe<Item>() {

            @Override
            public void subscribe(final ObservableEmitter<Item> e) {
                Timber.i("Starting to fetch items for location=%s week=%s", location.getNameTag(), weekOfYear);
                try {
                    URL url = getMenuUrl(location, weekOfYear);

                    // Connect to server
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.connect();

                    int responseCode = httpURLConnection.getResponseCode();
                    if (responseCode == 200) {
                        InputStream inputStream = httpURLConnection.getInputStream();
                        Scanner scanner = new Scanner(inputStream, "windows-1252");

                        while (scanner.hasNextLine()) {
                            String line = scanner.nextLine();

                            try {
                                final Item item = Item.fromLine(line);
                                final long menuId = mensaRepository.getOrCreateMenuId(location, item.getDate());

                                mensaRepository.getInsertItemObservable(item, menuId)
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(new Consumer<Long>() {
                                            @Override
                                            public void accept(Long itemId) throws Exception {
                                                Timber.i("FETCHED AND INSERTERTED itemId=%s menuId=%s", itemId, menuId);
                                                e.onNext(item);
                                            }
                                        });
                            } catch (IllegalStateException | ParseException ignored) {

                            }
                        }
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                    e.onError(e1);
                } finally {
                    e.onComplete();
                }
            }
        });
    }
}
