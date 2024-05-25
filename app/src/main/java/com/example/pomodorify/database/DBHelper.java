package com.example.pomodorify.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pomodorify.statistics.StatisticsRecord;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper implements GetStatistics, InsertStatistics, GetTimes, ChangeTimes, GetEndNotficationPreferences, SetEndNotificationPreferences, GetDarkThemePreferences, SetDarkThemePreferences, RemoveSelectedStatistic {

    //fields used for Singleton Design Pattern
    private static DBHelper dbHelper;

    //fields used for statistics
    public static final String STAT_TABLE_NAME = "Statistics";
    public static final String STAT_ID = "id";
    public static final String STAT_TIME = "time";
    public static final String STAT_DATE = "date";
    public static final String STAT_ACT = "activity";

    //fields used for user preferences
    public static final String PREF_TABLE_NAME = "UserPreferences";
    public static final String PREF_ID = "id";
    public static final String PREF_FOCUS_LENGTH = "focusLength";
    public static final String PREF_SHORT_LENGTH = "shortLength";
    public static final String PREF_LONG_LENGTH = "longLength";
    public static final String PREF_DARK_THEME = "darkTheme";
    public static final String PREF_END_NOTIFICATION = "endNotification";
    public static final String PREF_END_SOUND = "endSound";


    private DBHelper(Context context){
        super(context, "Pomodorify.db", null, 1);
    }

    public static DBHelper getInstance(Context context){
        if(dbHelper == null){
            dbHelper = new DBHelper(context);
        }
        return dbHelper;
    }

    public static void closeConnection(){
        if(dbHelper != null)
            dbHelper.close();
    }

    /*
        Called when database is created for the first time
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + STAT_TABLE_NAME + " (" + STAT_ID + " INTEGER PRIMARY KEY, "
                + STAT_TIME + " INTEGER, " + STAT_DATE + " INTEGER, " + STAT_ACT + " TEXT)"
        );
        db.execSQL(
                "CREATE TABLE " + PREF_TABLE_NAME + " (" + PREF_ID + " TEXT PRIMARY KEY, "
                        + PREF_FOCUS_LENGTH + " INTEGER, " + PREF_SHORT_LENGTH + " INTEGER, " + PREF_LONG_LENGTH + " INTEGER, "
                        + PREF_DARK_THEME + " BOOLEAN NOT NULL CHECK (" + PREF_DARK_THEME + " IN (0, 1)), "
                        + PREF_END_NOTIFICATION + " BOOLEAN NOT NULL CHECK (" + PREF_END_NOTIFICATION + " IN (0, 1)), "
                        + PREF_END_SOUND + " BOOLEAN NOT NULL CHECK (" + PREF_END_SOUND + " IN (0, 1)))"
        );

        //insert default user preferences
        ContentValues values = new ContentValues();

        values.put(PREF_ID, 1);
        values.put(PREF_FOCUS_LENGTH, 45);
        values.put(PREF_SHORT_LENGTH, 5);
        values.put(PREF_LONG_LENGTH, 15);
        values.put(PREF_DARK_THEME, 0);
        values.put(PREF_END_NOTIFICATION, 0);
        values.put(PREF_END_SOUND, 0);

        db.insert(PREF_TABLE_NAME, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + STAT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PREF_TABLE_NAME);
    }

    public boolean insertStatistics(int time, String activity){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBHelper.STAT_TIME, time);
        values.put(DBHelper.STAT_DATE, System.currentTimeMillis());
        values.put(DBHelper.STAT_ACT, activity);

        long result = db.insert(DBHelper.STAT_TABLE_NAME, null, values);

        return result != -1;
    }

    public List<StatisticsRecord> getStatisticsData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(STAT_TABLE_NAME, null, null, null, null, null, STAT_ID + " DESC");

        List<StatisticsRecord> list = new ArrayList<>();

        while(cursor.moveToNext()){
            StatisticsRecord record = new StatisticsRecord(cursor.getLong(0), cursor.getLong(1), cursor.getLong(2), cursor.getString(3));
            list.add(record);
        }

        cursor.close();
        return list;
    }

    public int getTimes(String timerType){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + timerType + " FROM " + PREF_TABLE_NAME, null);

        int returnVal = -1;

        if(cursor.moveToFirst())
            returnVal = cursor.getInt(0);

        cursor.close();
        return returnVal;
    }

    public int getFocusTime(){
        return getTimes(PREF_FOCUS_LENGTH);
    }

    public int getShortBreakTime(){
        return getTimes(PREF_SHORT_LENGTH);
    }

    public int getLongBreakTime(){
        return getTimes(PREF_LONG_LENGTH);
    }

    public void ChangeTimerLength(int duration, String timerType) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(timerType, duration);

        db.update(PREF_TABLE_NAME, values, null, null);
    }

    public boolean getDarkThemePreferences(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + PREF_DARK_THEME + " FROM " + PREF_TABLE_NAME, null);

        int returnVal = 0;
        if(cursor.moveToFirst())
            returnVal = cursor.getInt(0);

        cursor.close();
        return returnVal > 0;
    }

    public boolean getEndNotificationPreferences(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + PREF_END_NOTIFICATION + " FROM " + PREF_TABLE_NAME, null);

        int returnVal = 0;
        if(cursor.moveToFirst())
            returnVal = cursor.getInt(0);

        cursor.close();
        return returnVal > 0;
    }

    public boolean getEndSoundPreferences(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + PREF_END_SOUND + " FROM " + PREF_TABLE_NAME, null);

        int returnVal = 0;
        if(cursor.moveToFirst())
            returnVal = cursor.getInt(0);

        cursor.close();
        return returnVal > 0;
    }

    public void setDarkThemePreferences(boolean b){
        SQLiteDatabase db = this.getWritableDatabase();

        int x = b ? 1 : 0;

        ContentValues values = new ContentValues();
        values.put(PREF_DARK_THEME, x);

        db.update(PREF_TABLE_NAME, values, null, null);
    }

    public void setEndNotificationPreferences(boolean b){
        SQLiteDatabase db = this.getWritableDatabase();

        int x = b ? 1 : 0;

        ContentValues values = new ContentValues();
        values.put(PREF_END_NOTIFICATION, x);

        db.update(PREF_TABLE_NAME, values, null, null);
    }

    public void setEndSoundPreferences(boolean b){
        SQLiteDatabase db = this.getWritableDatabase();

        int x = b ? 1 : 0;

        ContentValues values = new ContentValues();
        values.put(PREF_END_SOUND, x);

        db.update(PREF_TABLE_NAME, values, null, null);
    }

    public void removeSelectedStatistic(long index){
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = STAT_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(index) };
        db.delete(STAT_TABLE_NAME, selection, selectionArgs);
    }
}