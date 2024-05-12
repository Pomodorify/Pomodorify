package com.example.pomodorify;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.provider.ContactsContract;
import android.widget.TextView;

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
