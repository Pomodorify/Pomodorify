package com.example.pomodorify.pomodoro;

import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pomodorify.database.InsertStatistics;

public class FocusTimer extends Timer {

    private String activityLabel;//zeby wiedziec jaki text wstawic do bazy
    private final int minutes;
    private InsertStatistics insertStatistics;

    FocusTimer(long millisInFuture, long countDownInterval, TextView timeLeft, InsertStatistics insertStatistics, String activityLabel, ProgressBar progressBar){
        super(millisInFuture, countDownInterval, timeLeft, progressBar);
        this.minutes = (int)millisInFuture / 1000;
        this.insertStatistics = insertStatistics;
        this.activityLabel = activityLabel;
    }

    @Override
    public void onFinish() {
        //timer focus wiec musi wstawic statystyki
        insertStatistics.insertStatistics(minutes, activityLabel);

        super.onFinish();//wywolaj metode z timer
    }
}
