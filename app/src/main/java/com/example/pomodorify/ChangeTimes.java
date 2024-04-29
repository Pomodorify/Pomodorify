package com.example.pomodorify;

public interface ChangeTimes {//z tego bedziemy korzystac w zakladce settings, a implementowac to bedzie DBHelper
    void ChangeFocus(int minutes);
    void ChangeShortBreak(int minutes);
    void ChangeLongBreak(int minutes);
}
