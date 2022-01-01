package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;

public class WorkDataBase {
    private static WorkDataBase instance;
    private SQLiteDatabase dataBase;
    private Cursor cursor;
    private String NAME_DB = "todolist.db";

    WorkDataBase(Context context) {
        dataBase = context.openOrCreateDatabase(NAME_DB, Context.MODE_PRIVATE, null);
    }

    public ArrayList<ArrayList<String>> getTasksForDate(String date) {
        ArrayList<ArrayList<String>> res = new ArrayList<>();
        cursor = dataBase.rawQuery("SELECT * FROM tasks WHERE date = '" + date + "'", null);
        while (cursor.moveToNext()){
            ArrayList<String> row = new ArrayList<>();
            row.add(Integer.toString(cursor.getInt(0)));
            row.add(cursor.getString(1));
            row.add(Integer.toString(cursor.getInt(2)));
            row.add(cursor.getString(3));
            res.add(row);
        }
        cursor.close();
        return res;
    }

    public ArrayList<ArrayList<String>> getTasksForToday() {
        Date curDate = new Date();
        String strDate = curDate.getDate() + "-" + (curDate.getMonth() + 1) + "-" +
                (curDate.getYear() + 1900);

        ArrayList<ArrayList<String>> res = new ArrayList<>();
        cursor = dataBase.rawQuery("SELECT * FROM tasks WHERE date = '" + strDate + "'", null);
        while (cursor.moveToNext()){
            ArrayList<String> row = new ArrayList<>();
            row.add(Integer.toString(cursor.getInt(0)));
            row.add(cursor.getString(1));
            row.add(Integer.toString(cursor.getInt(2)));
            row.add(cursor.getString(3));
            res.add(row);
        }
        cursor.close();
        return res;
    }

    public ArrayList<ArrayList<String>> getTasksCompleted() {
        Date curDate = new Date();
        String strDate = curDate.getDate() + "-" + (curDate.getMonth() + 1) + "-" +
                (curDate.getYear() + 1900);

        ArrayList<ArrayList<String>> res = new ArrayList<>();
        cursor = dataBase.rawQuery("SELECT * FROM tasks " +
                "WHERE done = 1 AND date <> '" + strDate + "'", null);
        while (cursor.moveToNext()){
            ArrayList<String> row = new ArrayList<>();
            row.add(Integer.toString(cursor.getInt(0)));
            row.add(cursor.getString(1));
            row.add(Integer.toString(cursor.getInt(2)));
            row.add(cursor.getString(3));
            res.add(row);
        }
        cursor.close();
        return res;
    }

    public ArrayList<ArrayList<String>> getTasksNotCompleted() {
        Date curDate = new Date();
        String strDate = curDate.getDate() + "-" + (curDate.getMonth() + 1) + "-" +
                (curDate.getYear() + 1900);

        ArrayList<ArrayList<String>> res = new ArrayList<>();
        cursor = dataBase.rawQuery("SELECT * FROM tasks " +
                "WHERE done = 0 AND date <> '" + strDate + "'", null);
        while (cursor.moveToNext()){
            ArrayList<String> row = new ArrayList<>();
            row.add(Integer.toString(cursor.getInt(0)));
            row.add(cursor.getString(1));
            row.add(Integer.toString(cursor.getInt(2)));
            row.add(cursor.getString(3));
            res.add(row);
        }
        cursor.close();
        return res;
    }

    public void addTask(String nameTask, String date) {
        ContentValues cv = new ContentValues();
        cv.put("name", nameTask);
        cv.put("date", date);
        cv.put("done", 0);

        dataBase.insert("tasks", null, cv);
    }

    public void changeDoneTask(String nameTask, String date, int done) {
        ContentValues cv = new ContentValues();
        cv.put("done", done);
        dataBase.update("tasks", cv, "name = ? AND date = ?",
                new String[] {nameTask, date});
    }

    public static WorkDataBase getInstance(Context context) {
        if (instance == null) {
            instance = new WorkDataBase(context);
        }
        return instance;
    }
}
