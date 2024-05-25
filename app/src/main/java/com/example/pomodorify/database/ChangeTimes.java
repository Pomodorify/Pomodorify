package com.example.pomodorify.database;

@FunctionalInterface
public interface ChangeTimes {
    public void ChangeTimerLength(int duration, String timerType);
}
