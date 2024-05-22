package com.example.pomodorify.database;

public interface ChangeTimes {
    void ChangeFocus(int minutes);
    void ChangeShortBreak(int minutes);
    void ChangeLongBreak(int minutes);
}
