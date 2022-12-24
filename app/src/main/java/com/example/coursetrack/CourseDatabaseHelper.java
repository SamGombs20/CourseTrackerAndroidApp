package com.example.coursetrack;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CourseDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "CourseTrack";
    private static final int DB_VERSION = 2;
    public CourseDatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        updateMyDatabase(sqLiteDatabase, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        updateMyDatabase(sqLiteDatabase, i, i1);
    }
    public void addCourse(SQLiteDatabase db,String name, String category, int rating, String description){
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", name);
        contentValues.put("Category", category);
        contentValues.put("Rating", rating);
        contentValues.put("Description", description);
        db.insert("Courses", null, contentValues);
    }
    public void addCourse(SQLiteDatabase db,String name, String category, int rating, String description, int imageId){
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", name);
        contentValues.put("Category", category);
        contentValues.put("Rating", rating);
        contentValues.put("Description", description);
        contentValues.put("imageId", imageId);
        db.insert("Courses", null, contentValues);
    }
    public void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion){
        String command;
        if(oldVersion<1){
            command = "CREATE TABLE Courses(_id INTEGER PRIMARY KEY AUTOINCREMENT,Name TEXT,Category TEXT,Rating INTEGER,Description TEXT,imageId INTEGER);";
            db.execSQL(command);
        }
        if(oldVersion<2){
            command = "ALTER TABLE Courses ADD COLUMN Favorite NUMERIC";
            db.execSQL(command);
        }
    }
}
