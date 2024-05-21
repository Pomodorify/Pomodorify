package com.example.pomodorify.app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;

import com.example.pomodorify.app.Utility;

@RunWith(RobolectricTestRunner.class)
public class UtilityTest {

    @Test
    public void convertTimestampToDate() {
        long timestamp = 1618560000000L; // April 16, 2021
        String expected = "2021-04-16";
        String actual = Utility.convertTimestampToDate(timestamp);
        assertEquals(expected, actual);
    }

    @Test
    public void formatMillis() {
        long millis = 1500 * 1000; // 1500 seconds
        String expected = "25 : 00";
        String actual = Utility.formatMillis(millis);
        assertEquals(expected, actual);
    }
}
