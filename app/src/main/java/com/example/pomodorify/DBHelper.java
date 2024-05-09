package com.example.pomodorify;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper implements GetStatistics, InsertStatistics, GetTimes, ChangeTimes {

    //fields used for statistics
    public static final String STAT_TABLE_NAME = "Statistics";
    public static final String STAT_ID = "id";
    public static final String STAT_TIME = "time";
    public static final String STAT_DATE = "date";//W jakim dniu bylo robione pomodoro
    public static final String STAT_ACT = "activity";

    //fields used for timers length
    public static final String PARAM_TABLE_NAME = "TimeParameters";
    public static final String PARAM_ID = "id";
    public static final String PARAM_LEN = "minutes";

    public static final String focusKey = "focus";
    public static final String shortBreakKey = "shortBreak";
    public static final String longBreakKey = "longBreak";



    public DBHelper(Context context){
        super(context, "Pomodorify.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DBHelper.STAT_TABLE_NAME + " (" + DBHelper.STAT_ID + " INTEGER PRIMARY KEY, "
                + DBHelper.STAT_TIME + " INTEGER, " + DBHelper.STAT_DATE + " INTEGER, " + DBHelper.STAT_ACT + " TEXT)");
        db.execSQL("CREATE TABLE " + PARAM_TABLE_NAME + " (" + PARAM_ID + " TEXT PRIMARY KEY, " + PARAM_LEN + " INTEGER)");//rodzaj i dlugosc trwania timera

        //insert default timer values
        ContentValues values = new ContentValues();
        String[][] data = {
                {focusKey, "45"},
                {shortBreakKey, "5"},
                {longBreakKey, "15"}
        };

        for (String[] row : data) {
            values.clear();
            values.put(PARAM_ID, row[0]);
            values.put(PARAM_LEN, row[1]);
            db.insert(PARAM_TABLE_NAME, null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + STAT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PARAM_TABLE_NAME);
    }

    public boolean insertStatistics(int time, String activity){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBHelper.STAT_TIME, time);
        values.put(DBHelper.STAT_DATE, System.currentTimeMillis());
        values.put(DBHelper.STAT_ACT, activity);

        long result = db.insert(DBHelper.STAT_TABLE_NAME, null, values);

        if(result == -1)
            return false;
        else
            return true;
    }

    public List<StatRecord> getStatisticsData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(DBHelper.STAT_TABLE_NAME, null, null, null, null, null, null);

        List<StatRecord> list = new LinkedList<>();

        while(cursor.moveToNext()){
            StatRecord record = new StatRecord(cursor.getLong(0), cursor.getLong(1), cursor.getLong(2), cursor.getString(3));
            list.add(record);
        }
        cursor.close();
        return list;
    }

    public int getFocusTime(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + PARAM_LEN + " FROM " + PARAM_TABLE_NAME + " WHERE id=?", new String[]{focusKey});

        int returnVal = -1;

        if(cursor.moveToFirst())
            returnVal = cursor.getInt(0);

        cursor.close();
        return returnVal;
    }

    public int getShortBreakTime(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + PARAM_LEN + " FROM " + PARAM_TABLE_NAME + " WHERE id=?", new String[]{shortBreakKey});

        int returnVal = -1;

        if(cursor.moveToFirst())
            returnVal = cursor.getInt(0);

        cursor.close();
        return returnVal;
    }

    public int getLongBreakTime(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + PARAM_LEN + " FROM " + PARAM_TABLE_NAME + " WHERE id=?", new String[]{longBreakKey});

        int returnVal = -1;

        if(cursor.moveToFirst())
            returnVal = cursor.getInt(0);

        cursor.close();
        return returnVal;
    }

    public void ChangeFocus(int minutes) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PARAM_LEN, minutes);

        String selection = PARAM_ID + " LIKE ?";
        String[] selectionArgs = { focusKey };

        db.update(PARAM_TABLE_NAME, values, selection, selectionArgs);
    }

    public void ChangeShortBreak(int minutes) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PARAM_LEN, minutes);

        String selection = PARAM_ID + " LIKE ?";
        String[] selectionArgs = { shortBreakKey };

        db.update(PARAM_TABLE_NAME, values, selection, selectionArgs);
    }
    public void ChangeLongBreak(int minutes) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PARAM_LEN, minutes);

        String selection = PARAM_ID + " LIKE ?";
        String[] selectionArgs = { longBreakKey };

        db.update(PARAM_TABLE_NAME, values, selection, selectionArgs);
    }


}

/*
    TODO:
    - kursory napewno zamykamy (cursor.close()), baze danych nie koniecznie (db.close())
    - z dokumentacji :   Since getWritableDatabase() and getReadableDatabase() are expensive to call when the
     database is closed, you should leave your database connection open for as long as you possibly need to access it.
 */
