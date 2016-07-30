package com.javaclass.anima.android21datastorage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by anima on 2016/7/30.
 */
public class MyDBHelper extends SQLiteOpenHelper{

    private static final String createCustTable =
            "CREATE TABLE cust (_id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                    "name TEXT, tel TEXT, birthday DATE)";

    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(createCustTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
