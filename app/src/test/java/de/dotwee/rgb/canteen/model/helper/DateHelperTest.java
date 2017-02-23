package de.dotwee.rgb.canteen.model.helper;

import org.junit.Test;

import java.util.Date;
import java.util.Locale;

import de.dotwee.rgb.canteen.model.constant.Weekday;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by lukas on 21.02.17.
 */
public class DateHelperTest {
    private static final String TAG = DateHelperTest.class.getSimpleName();
    private static Date testDate = new Date(Long.valueOf("1487608773289"));

    @Test
    public void testFormat() throws Exception {

        String dateValue = DateHelper.format(testDate, Locale.GERMAN);
        String dateValueExpected = "Montag, 20.02.2017";

        boolean equal = dateValue.equalsIgnoreCase(dateValueExpected);
        assertTrue(equal);
    }

    @Test
    public void getWeekday() throws Exception {
        Weekday weekdayExpected = Weekday.MONDAY;
        Weekday weekday = DateHelper.getWeekday(testDate);

        assertEquals(weekday, weekdayExpected);
    }
}