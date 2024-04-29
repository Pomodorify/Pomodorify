package com.example.pomodorify;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class DBHelperTest {

    private MainActivity activity;
    private DBHelper dbHelper;

    @Before
    public void setUp() {
        // Initialize Robolectric
        activity = Robolectric.buildActivity(MainActivity.class).create().start().resume().get();
        dbHelper = new DBHelper(activity);
    }

    @After
    public void SetDown(){
        activity.finish();
    }

    @Test
    public void testInsertStatistics() {
        // Insert sample statistics data
        boolean insertResult = dbHelper.insertStatistics(100, "Sample Activity");
        assertTrue(insertResult);
    }

    @Test
    public void testInsertGetStatisticsData() {
        dbHelper.insertStatistics(100, "Sample Activity");

        List<StatRecord> statisticsData = dbHelper.getStatisticsData();

        assertEquals(1, statisticsData.size());

        StatRecord record = statisticsData.get(0);
        assertEquals("Sample Activity", record.activity);
        assertEquals(100, record.time);
    }

    @Test
    public void testGetStatisticsMoreData() {
        dbHelper.insertStatistics(100, "Sample Activity1");
        dbHelper.insertStatistics(200, "Sample Activity2");
        dbHelper.insertStatistics(300, "Sample Activity3");

        List<StatRecord> statisticsData = dbHelper.getStatisticsData();

        assertEquals(3, statisticsData.size());

        StatRecord record = statisticsData.get(1);
        assertEquals("Sample Activity2", record.activity);
        assertEquals(200, record.time);

        record = statisticsData.get(2);
        assertEquals("Sample Activity3", record.activity);
        assertEquals(300, record.time);
    }

    @Test
    public void getDefaultFocusTimeTest() {
        assertEquals(dbHelper.getFocusTime(), 10);
    }

    @Test
    public void getDefaultLongBreakTimeTest() {
        assertEquals(dbHelper.getLongBreakTime(), 15);
    }

    @Test
    public void getDefaultShortBreakTimeTest() {
        assertEquals(dbHelper.getShortBreakTime(), 5);
    }

    @Test
    public void setFocusTest(){
        dbHelper.ChangeFocus(101);
        assertEquals(dbHelper.getFocusTime(), 101);
    }

    @Test
    public void setShortBreakTest(){
        dbHelper.ChangeShortBreak(15);
        assertEquals(dbHelper.getShortBreakTime(), 15);
    }

    @Test
    public void setLongBreakTest(){
        dbHelper.ChangeFocus(27);
        assertEquals(dbHelper.getLongBreakTime(), 27);
    }
}
