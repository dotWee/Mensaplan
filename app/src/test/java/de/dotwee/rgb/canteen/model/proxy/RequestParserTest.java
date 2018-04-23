package de.dotwee.rgb.canteen.model.proxy;

import android.support.annotation.NonNull;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.dotwee.rgb.canteen.model.Item;
import de.dotwee.rgb.canteen.model.Label;
import de.dotwee.rgb.canteen.model.Price;
import de.dotwee.rgb.canteen.model.Type;
import okhttp3.Call;

import static org.junit.Assert.assertEquals;

/**
 * Created by lukas on 18.04.18.
 */
public class RequestParserTest {
    private static String line = "16.04.2018;Mo;Suppe;Feine Kräutersuppe (3,A,G,I,AA);V;0,70 / 0,90 / 1,40;0,70;0,90;1,40";
    private MensaCallback mensaCallback;
    private RequestParser requestParser;

    @Before
    public void setUp() throws Exception {
        mensaCallback = new MensaCallback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull ArrayList<Item> items) throws IOException {

            }
        };
        requestParser = new RequestParser(mensaCallback);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetType() throws Exception {
        String[] values = requestParser.getLineValues(line);
        Type expected = Type.SOUP;
        Type actual = requestParser.getType(values);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetLineValues() throws Exception {
        String[] values = requestParser.getLineValues(line);
        Assert.assertEquals(9, values.length);
    }

    @Test
    public void testGetName() throws Exception {
        String expected = "Feine Kräutersuppe (3,A,G,I,AA)";

        String[] values = requestParser.getLineValues(line);
        String actual = requestParser.getName(values);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetPrice() throws Exception {
        String[] values = requestParser.getLineValues(line);

        String priceAllActual = requestParser.getPrice(values, Price.ALL);
        String priceAllExpected = "0,70 / 0,90 / 1,40";
        Assert.assertEquals(priceAllExpected, priceAllActual);


        String priceGuestActual = requestParser.getPrice(values, Price.GUEST);
        String priceGuestExpected = "1,40";
        assertEquals(priceGuestExpected, priceGuestActual);

        String priceStudentActual = requestParser.getPrice(values, Price.STUDENT);
        String priceStudentExpected = "0,70";
        assertEquals(priceStudentExpected, priceStudentActual);

        String priceEmployeeActual = requestParser.getPrice(values, Price.EMPLOYEE);
        String priceEmployeeExpected = "0,90";
        assertEquals(priceEmployeeExpected, priceEmployeeActual);
    }

    @Test
    public void testGetDate() throws Exception {
        Date expected = new Date(Long.parseLong("1523829600000"));

        String[] values = requestParser.getLineValues(line);
        Date actual = requestParser.getDate(values);
        Assert.assertEquals(expected, actual);


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(actual);

        int weekdayExpected = Calendar.MONDAY;
        int weekdayActual = calendar.get(Calendar.DAY_OF_WEEK);

        Assert.assertEquals(weekdayExpected, weekdayActual);
    }

    @Test
    public void testGetLabel() throws Exception {
        String[] lineValues = requestParser.getLineValues(line);
        Label[] labels = requestParser.getLabel(lineValues);

        Assert.assertEquals(labels[0], Label.V);
    }
}