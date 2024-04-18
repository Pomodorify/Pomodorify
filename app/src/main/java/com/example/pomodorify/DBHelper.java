package com.example.pomodorify;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper implements GetStatistics, InsertStatistics, GetTimes {
    public DBHelper(Context context){
        super(context, "Pomodorify.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertStatistics(int time, String activity){
        return false;
    }

    public List<StatRecord> getStatisticsData(){
        return null;
    }

    public int getFocusTime(){
        return 0;
    }

    public int getShortBreakTime(){
        return 0;
    }

    public int getLongBreakTime(){
        return 0;
    }


}
