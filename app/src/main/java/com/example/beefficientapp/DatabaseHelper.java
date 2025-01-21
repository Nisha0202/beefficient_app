package com.example.beefficientapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;



public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ToDoApp.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "user_data";
    private static final String COL_USERNAME = "username";
    private static final String COL_DATE = "date";
    private static final String COL_TIME = "time";
    private static final String COL_TASK_NAME = "taskname";
    private static final String COL_ALARM_STATUS = "alarm_status";
    private static final String COL_TAGS = "tags";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_USERNAME + " TEXT, " +
                COL_DATE + " TEXT, " +
                COL_TIME + " TEXT, " +
                COL_TASK_NAME + " TEXT, " +
                COL_ALARM_STATUS + " INTEGER, " +
                COL_TAGS + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String username, String date, String time, String taskName, boolean alarmStatus, String tags) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_DATE, date);
        values.put(COL_TIME, time);
        values.put(COL_TASK_NAME, taskName);
        values.put(COL_ALARM_STATUS, alarmStatus ? 1 : 0);
        values.put(COL_TAGS, tags);

        try {
            long result = db.insert(TABLE_NAME, null, values);
            return result != -1;
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error inserting data", e);
            return false;
        } finally {
            db.close();
        }
    }
    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String taskName = cursor.getString(cursor.getColumnIndex(COL_TASK_NAME));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(COL_DATE));
                @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(COL_TIME));
                @SuppressLint("Range") String tags = cursor.getString(cursor.getColumnIndex(COL_TAGS));

                // Create Task object
                Task task = new Task(taskName, date, time, tags);
                taskList.add(task);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return taskList;
    }




}

