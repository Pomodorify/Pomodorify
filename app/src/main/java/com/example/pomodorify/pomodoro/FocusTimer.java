package com.example.pomodorify.pomodoro;

import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pomodorify.database.InsertStatistics;

public class FocusTimer extends Timer {

    private final String activityLabel;
    private final int minutes;
    private final InsertStatistics insertStatistics;

    FocusTimer(long millisInFuture, long countDownInterval, TextView timeLeft, InsertStatistics insertStatistics, String activityLabel, ProgressBar progressBar){
        super(millisInFuture, countDownInterval, timeLeft, progressBar);
        this.minutes = (int)millisInFuture / 1000;
        this.insertStatistics = insertStatistics;
        this.activityLabel = activityLabel;
    }

    @Override
    public void onFinish() {
        insertStatistics.insertStatistics(minutes, activityLabel);

        super.onFinish();
    }
}
