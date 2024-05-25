package com.example.pomodorify.app;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.view.MenuItem;

import androidx.fragment.app.Fragment;

import com.example.pomodorify.R;
import com.example.pomodorify.app.BottomNavHandler;
import com.example.pomodorify.app.FragmentHandler;
import com.example.pomodorify.app.MainActivity;
import com.example.pomodorify.database.DBHelper;
import com.example.pomodorify.pomodoro.Pomodoro;
import com.example.pomodorify.settings.Settings;
import com.example.pomodorify.statistics.Statistics;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class BottomNavHandlerTest {

    private MainActivity activity;
    private BottomNavHandler bottomNavHandler;
    private FragmentHandler fragmentHandler;
    private Fragment statistics;
    private Fragment pomodoro;
    private Fragment settings;

    @Before
    public void setUp() {
        // Initialize Robolectric
        activity = Robolectric.buildActivity(MainActivity.class).create().start().resume().get();
        statistics = new Statistics();
        pomodoro = new Pomodoro();
        settings = new Settings();
        fragmentHandler = new FragmentHandler(statistics, pomodoro, settings, activity.getSupportFragmentManager());
        bottomNavHandler = new BottomNavHandler(fragmentHandler, activity.binding);
    }

    @After
    public void SetDown(){
        activity.finish();
        DBHelper.closeConnection();
    }

    @Test
    public void testDefaultFragment(){
        bottomNavHandler.setDefaultSettings();
        activity.getSupportFragmentManager().executePendingTransactions();

        //znajdz aktualnie wybrany fragment
        Fragment fragment = activity.getSupportFragmentManager().findFragmentById(R.id.frame_layout);

        //fragment pomodoro powinine byc wybrany
        assertEquals(fragment, pomodoro);
        assertNotEquals(fragment, settings);
        assertNotEquals(fragment, statistics);
    }

    @Test
    public void testDefaultMenuSelected(){
        bottomNavHandler.setDefaultSettings();
        //sprawdz czy srodkowy przycisk jest wybrany
        assertTrue(((BottomNavigationView) activity.findViewById(R.id.bottomNavigationView)).getMenu().findItem(R.id.pomodoro).isChecked());
    }

    @Test
    public void testNavigationItemSelected() {
        //Symulacja klinkiecia na przycisk settings
        MenuItem item = ((BottomNavigationView) activity.findViewById(R.id.bottomNavigationView)).getMenu().findItem(R.id.settings);

        bottomNavHandler.onNavigationItemSelected(item);

        activity.getSupportFragmentManager().executePendingTransactions();

        Fragment fragment = activity.getSupportFragmentManager().findFragmentById(R.id.frame_layout);

        assertEquals(fragment, settings);
        assertNotEquals(fragment, pomodoro);
        assertNotEquals(fragment, statistics);
    }
}
