package com.example.pomodorify.pomodoro;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import android.widget.TextView;

import com.example.pomodorify.R;
import com.example.pomodorify.app.MainActivity;
import com.example.pomodorify.database.DBHelper;
import com.example.pomodorify.pomodoro.Pomodoro;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class PomodoroTest {


    private MainActivity activity;
    Pomodoro pomodoro;

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(MainActivity.class).create().start().resume().get();
        pomodoro = (Pomodoro) activity.getSupportFragmentManager().findFragmentById(R.id.frame_layout);
    }

    @After
    public void SetDown(){
        activity.finish();
        DBHelper.closeConnection();
    }

    @Test
    public void setFocusTest(){
        pomodoro.setFocus();
        assertEquals(((TextView)activity.findViewById(R.id.timeLeft)).getText(), "00 : 45");
    }

    @Test
    public void setShortBreakTest(){
        pomodoro.setShortBreak();
        assertEquals(((TextView)activity.findViewById(R.id.timeLeft)).getText(), "00 : 05");
    }

    @Test
    public void setLongBreakTest(){
        pomodoro.setLongBreak();
        assertEquals(((TextView)activity.findViewById(R.id.timeLeft)).getText(), "00 : 15");
    }

}
