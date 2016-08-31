package com.example.administrator.filecleandemo;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.administrator.filecleandemo.utils.MyApplication;

/**
 * Created by Administrator on 2016/8/30.
 */

public class MySqliteOpenHelper extends SQLiteOpenHelper{

    public MySqliteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MySqliteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("drop table if exists 'path'");
        String  sql="create table path(_id integer primary key autoincrement,oldpath text,newpath text)";
        db.execSQL(sql);//创建表
    }
    //数据更新调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
