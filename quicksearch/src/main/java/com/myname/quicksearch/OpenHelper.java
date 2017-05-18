package com.myname.quicksearch;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/1/28.
 */

public class OpenHelper extends SQLiteOpenHelper {
    public OpenHelper(Context context) {
        super(context, "LOL.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table  lol(_id integer primary key autoincrement,name text)");
        for (int x=0;x<20;x++){
            sqLiteDatabase.execSQL("insert into lol(name) values(?)",new String[]{"LOL"+x});
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
