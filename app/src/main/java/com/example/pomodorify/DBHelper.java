package com.example.pomodorify;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper implements GetStatistics, InsertStatistics, GetTimes {

    public static final String TABLE_NAME = "Statistics";
    public static final String STAT_ID = "id";
    public static final String STAT_TIME = "time";
    public static final String STAT_DATE = "date";//W jakim dniu bylo robione pomodoro

    public static final String STAT_ACT = "activity";



    public DBHelper(Context context){
        super(context, "Pomodorify.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DBHelper.TABLE_NAME + " (" + DBHelper.STAT_ID + " INTEGER PRIMARY KEY, "
                + DBHelper.STAT_TIME + " INTEGER, " + DBHelper.STAT_DATE + " INTEGER, " + DBHelper.STAT_ACT + " TEXT)");
        db.execSQL("CREATE TABLE TimeParameters (id TEXT PRIMARY KEY, minutes INTEGER)");//rodzaj i dlugosc trwania timera

        //insert default timer values
        ContentValues values = new ContentValues();
        String[][] data = {
                {"focus", "45"},
                {"shortBreak", "5"},
                {"longBreak", "15"}
        };

        for (String[] row : data) {
            values.clear();
            values.put("id", row[0]);
            values.put("minutes", row[1]);
            db.insert("TimeParameters", null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBHelper.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS TimeParameters");
    }

    public boolean insertStatistics(int time, String activity){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBHelper.STAT_TIME, time);
        values.put(DBHelper.STAT_DATE, System.currentTimeMillis());
        values.put(DBHelper.STAT_ACT, activity);

        long result = db.insert(DBHelper.TABLE_NAME, null, values);

        if(result == -1)
            return false;
        else
            return true;
    }

    public List<StatRecord> getStatisticsData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);

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
        Cursor cursor = db.rawQuery("SELECT minutes FROM TimeParameters WHERE id=?", new String[]{"focus"});

        int returnVal = -1;

        if(cursor.moveToFirst())
            returnVal = cursor.getInt(0);

        cursor.close();
        return returnVal;
    }

    public int getShortBreakTime(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT minutes FROM TimeParameters WHERE id=?", new String[]{"shortBreak"});

        int returnVal = -1;

        if(cursor.moveToFirst())
            returnVal = cursor.getInt(0);

        cursor.close();
        return returnVal;
    }

    public int getLongBreakTime(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT minutes FROM TimeParameters WHERE id=?", new String[]{"longBreak"});

        int returnVal = -1;

        if(cursor.moveToFirst())
            returnVal = cursor.getInt(0);

        cursor.close();
        return returnVal;
    }
}

/*
    TODO:
    - kursory napewno zamykamy (cursor.close()), baze danych nie koniecznie (db.close())
    - z dokumentacji :   Since getWritableDatabase() and getReadableDatabase() are expensive to call when the
     database is closed, you should leave your database connection open for as long as you possibly need to access it.
 */
