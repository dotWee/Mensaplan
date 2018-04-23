package de.dotwee.rgb.canteen.model.proxy;

import android.support.annotation.NonNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import de.dotwee.rgb.canteen.model.Item;
import de.dotwee.rgb.canteen.model.Location;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static org.junit.Assert.assertEquals;

/**
 * Created by lukas on 18.04.18.
 */
public class MensaProxyImplTest {
    private final String responsePath = String.format(MensaProxy.Companion.getURL_PATH(), Location.UNIVERSITY, 16);
    private final String responseBody = "datum;tag;warengruppe;name;kennz;preis;stud;bed;gast\n" +
            "16.04.2018;Mo;Suppe;Feine Kräutersuppe (3,A,G,I,AA);V;0,70 / 0,90 / 1,40;0,70;0,90;1,40\n" +
            "16.04.2018;Mo;HG1;Fusilli mit Zucchini (3,A,C,G,I,AA);V;2,00 / 2,80 / 3,60;2,00;2,80;3,60\n" +
            "16.04.2018;Mo;HG2;Schweinegulasch mit Bio-Fusilli (3,A,AA);S;2,80 / 3,40 / 4,40;2,80;3,40;4,40\n" +
            "16.04.2018;Mo;HG3;Hähnchen-Filet-Spieß mit Reis (3,G,I);G;3,60 / 4,60 / 5,10;3,60;4,60;5,10\n" +
            "16.04.2018;Mo;B1;Kartoffelpüree (3,G);V;0,70 / 0,90 / 1,40;0,70;0,90;1,40\n" +
            "16.04.2018;Mo;B2;Paprikagemüse ungarisch (3,I);V;0,80 / 1,00 / 1,50;0,80;1,00;1,50\n" +
            "16.04.2018;Mo;B3;Bio-Basmatireis;B,VG;0,80 / 1,00 / 1,50;0,80;1,00;1,50";
    private MockWebServer server;

    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
        server.enqueue(new MockResponse().setBody(responseBody));
        server.start();
    }

    @Test
    public void newCall() throws Exception {
        HttpUrl httpUrl = server.url(responsePath);
        MensaProxyImpl canteenProxy = new MensaProxyImpl(null);

        MensaCallback mensaCallback = new MensaCallback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull ArrayList<Item> items) throws IOException {
                System.out.print("Received items size=" + items.size());
                assertEquals(7, items.size());
            }
        };

        RequestParser requestParser = new RequestParser(mensaCallback);
        canteenProxy.newCall(requestParser, httpUrl);

        RecordedRequest request = server.takeRequest();
        assertEquals(responsePath, request.getPath());
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }
}