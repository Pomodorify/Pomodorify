package com.example.pomodorify;

import androidx.annotation.NonNull;

public class StatRecord {
    private long id;
    private long time;//dlugosc trwania
    private long date;//w jakim dniu byla robiona sesja
    private String activity;//co wpisal user w pole tekstowe

    public StatRecord(long id, long time, long date, String activity) {
        this.id = id;
        this.time = time;
        this.date = date;
        this.activity = activity;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Duration: " + time + "\n");
        sb.append("Date: " + Utility.convertTimestampToDate(date) + "\n");

        if(activity.equals(""))
            sb.append("Activity: unspecified" + "\n");
        else
            sb.append("Activity: " + activity + "\n");

        return sb.toString();
    }

    public long getId() {
        return id;
    }
    public long getTime() {
        return time;
    }
    public long getDate() {
        return date;
    }

    public String getFormattedDate(){
        return Utility.convertTimestampToDate(date);
    }

    public String getActivity() {
        return activity;
    }


}
