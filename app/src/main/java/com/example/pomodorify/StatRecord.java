package com.example.pomodorify;

public class StatRecord {
    long id;
    long time;//dlugosc trwania
    long date;//w jakim dniu byla robiona sesja
    String activity;//co wpisal user w pole tekstowe

    public StatRecord(long id, long time, long date, String activity) {
        this.id = id;
        this.time = time;
        this.date = date;
        this.activity = activity;
    }

    @Override
    public String toString() {
        return null;
    }
}
