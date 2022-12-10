package com.example.sample6;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {

    public Database(Context context) {
        super(context, "student_portal.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table STUDENT(USN TEXT primary key,Name TEXT,Mobile_no TEXT,Password TEXT)");
        db.execSQL("create Table PROFESSOR(Email TEXT primary key,Password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if  exists STUDENT");
        db.execSQL("drop table if exists PROFESSOR");
    }

    public boolean register_student(String usn,String name,String mob,String pass)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("USN",usn);
        contentValues.put("Name",name);
        contentValues.put("Mobile_no",mob);
        contentValues.put("Password",pass);
        long result = db.insert("STUDENT",null,contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean register_professor(String email,String pass)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Email",email);
        contentValues.put("Password",pass);
        long result = db.insert("PROFESSOR",null,contentValues);
        if(result==-1)
            return false;
        else
            return true;
    }

    public boolean check_student_login(String usn,String pass) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from STUDENT where USN=? and Password=?", new String[]{usn, pass});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public boolean check_professor_login(String email,String pass)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from PROFESSOR where Email=? and Password=?",new String[] {email,pass});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }

}
