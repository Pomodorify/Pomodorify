package com.example.pomodorify.app;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public final class Utility {

    private Utility(){}

    public static String convertTimestampToDate(long timestamp) {
        // Create a SimpleDateFormat object with your desired date format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // Convert timestamp to Date object
        Date date = new Date(timestamp); // Multiply by 1000 if your timestamp is in seconds

        // Format the Date object into a string
        return sdf.format(date);
    }

    public static String formatMillis(long millis){
        long MM = TimeUnit.MILLISECONDS.toMinutes(millis);
        long SS = TimeUnit.MILLISECONDS.toSeconds(millis) % 60;

        return String.format("%02d : %02d",MM, SS);
    }
}