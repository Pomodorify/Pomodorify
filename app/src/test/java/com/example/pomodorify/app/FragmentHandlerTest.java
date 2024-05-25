package com.example.pomodorify.app;

import static org.junit.Assert.*;
import static org.robolectric.Shadows.shadowOf;

//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.pomodorify.R;
import com.example.pomodorify.app.FragmentHandler;
import com.example.pomodorify.app.MainActivity;
import com.example.pomodorify.database.DBHelper;
import com.example.pomodorify.pomodoro.Pomodoro;
import com.example.pomodorify.settings.Settings;
import com.example.pomodorify.statistics.Statistics;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class FragmentHandlerTest {

    private MainActivity activity;
    private FragmentManager fragmentManager;

    private FragmentHandler fragmentHandler;

    private Fragment statistics;
    private Fragment pomodoro;
    private Fragment settings;

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(MainActivity.class).create().start().resume().get();
        fragmentManager = activity.getSupportFragmentManager();
        statistics = new Statistics();
        pomodoro = new Pomodoro();
        settings = new Settings();
        fragmentHandler = new FragmentHandler(statistics, pomodoro, settings, fragmentManager);
    }

    @After
    public void SetDown(){
        activity.finish();
        DBHelper.closeConnection();
    }

    @Test
    public void testGetStatistics(){
        assertEquals(statistics, fragmentHandler.getStatisticsFragment());
    }

    @Test
    public void testGetPomodoro(){
        assertEquals(pomodoro, fragmentHandler.getPomodoroFragment());
    }

    @Test
    public void testGetSettings(){
        assertEquals(settings, fragmentHandler.getSettingsFragment());
    }

    @Test
    public void testReplaceFragment() {

        fragmentHandler.replaceFragment(settings); // to testujemy

        fragmentManager.executePendingTransactions();

        Fragment fragment = fragmentManager.findFragmentById(R.id.frame_layout);

        assertEquals(fragment, settings);
        assertNotEquals(fragment, pomodoro);
        assertNotEquals(fragment, statistics);
    }

}
