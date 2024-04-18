package com.example.pomodorify;

public class FocusTimer extends Timer{

    private String activityLabel;//zeby wiedziec jaki text wstawic do bazy
    private final int minutes;
    private InsertStatistics insertStatistics;

    FocusTimer(){
        minutes = 0;
    }
}
