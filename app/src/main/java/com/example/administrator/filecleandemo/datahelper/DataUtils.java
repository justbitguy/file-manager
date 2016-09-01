package com.example.administrator.filecleandemo.datahelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.filecleandemo.utils.MyApplication;

/**
 * Created by Administrator on 2016/9/1.
 */

public class DataUtils {
    public static final String sql="select _id,oldpath,newpath from path";
    public static final String sql1="delete from path where newpath=?";
    public static final String sql2="delete from path where newpath=?";
    Context context= MyApplication.getInstance().getApplicationContext();

    private SQLiteDatabase getDB(){
        MySqliteOpenHelper helper=new MySqliteOpenHelper(context,"imgpath.db",null,1);
        SQLiteDatabase db=helper.getReadableDatabase();
        return db;
    }

    public Cursor query(){
        SQLiteDatabase db=getDB();
        Cursor cursor=db.rawQuery(sql,null);
        return cursor;
    }

    public void exexSQL(String newpath){
        SQLiteDatabase db=getDB();
        db.execSQL(sql1,new Object[]{newpath});
        db.close();
    }

    public void delete(String newpath){
        SQLiteDatabase db=getDB();
        db.execSQL(sql2,new Object[]{newpath});
        db.close();
    }

    public void insert(String oldpath,String newpath){
        SQLiteDatabase db=getDB();
        ContentValues values=new ContentValues();
        values.put("oldpath",oldpath);
        values.put("newpath",newpath);
        db.insert("path",null,values);
        db.close();

    }
}
