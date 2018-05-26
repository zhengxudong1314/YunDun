package com.dahua.searchandwarn.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 作用：
 * 作者： 郑旭东
 * 日期：2018/5/21
 */

public class SW_SqliteHelper extends SQLiteOpenHelper {
    public SW_SqliteHelper(Context context) {
        super(context, "db_sw", null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table table_sw (id Integer primary key,alarmId Integer)");
        sqLiteDatabase.execSQL("create table table_address (id Integer primary key,address char,devcode char)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
