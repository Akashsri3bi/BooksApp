package com.example.files;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BooksDatabase extends SQLiteOpenHelper {

    public BooksDatabase(@Nullable Context context) {
        super(context , "Book.db" , null , 1 );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create Table File(Name TEXT primary key , Position INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void Insert(String name , int postition){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name" ,name) ;
        contentValues.put("Position",postition);

        db.insert("File" , null , contentValues);
        db.close();
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from File" ,null);
        return cursor ;
    }

    public void Delete(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("File", "Name=?" , new String[]{name});
        db.close();
    }

}
