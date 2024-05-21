package com.example.pomodorify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class StatisticsRecordTest {

    @Test
    public void statRecord_toString() {
        long id = 1;
        long time = 1500;
        long date = 1618560000000L;
        String activity = "Work";

        StatisticsRecord statisticsRecord = new StatisticsRecord(id, time, date, activity);

        String expected = "Duration: 1500\nDate: 2021-04-16\nActivity: Work\n";
        String actual = statisticsRecord.toString();

        assertEquals(expected, actual);
    }
}
