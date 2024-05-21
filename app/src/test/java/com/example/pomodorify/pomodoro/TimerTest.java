package com.example.pomodorify.pomodoro;

import android.widget.ProgressBar;
import android.widget.TextView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.example.pomodorify.pomodoro.NotifyPomodoro;
import com.example.pomodorify.pomodoro.Timer;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class TimerTest {

    private Timer timer;
    private TextView mockTextView;
    private NotifyPomodoro mockListener;

    @Before
    public void setUp() {
        mockTextView = mock(TextView.class);
        mockListener = mock(NotifyPomodoro.class);
        ProgressBar progressBarMock = mock(ProgressBar.class);
        timer = new Timer(10000, 1000, mockTextView, progressBarMock);
        timer.setCustomObjectListener(mockListener);
    }

    @Test
    public void onTick_updatesTextView() {
        timer.onTick(5000);
        verify(mockTextView).setText("00 : 05");
    }

    @Test
    public void onFinish_notifiesListener() {
        timer.onFinish();
        verify(mockListener).onFinish();
    }
}
