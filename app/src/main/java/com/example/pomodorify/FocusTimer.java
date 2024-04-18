package com.example.pomodorify;

import android.widget.TextView;

public class FocusTimer extends Timer{

    private String activityLabel;//zeby wiedziec jaki text wstawic do bazy
    private final int minutes;
    private InsertStatistics insertStatistics;

    FocusTimer(){
        minutes = 0;
    }

    FocusTimer(long millisInFuture, long countDownInterval, TextView timeLeft, InsertStatistics insertStatistics, String activityLabel){
        minutes = 0;
    }
}
