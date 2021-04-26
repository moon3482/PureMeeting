package com.example.mana.chating;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE if not exists chat ("
                +"id integer primary key autoincrement,"
                +"room varchar(300),"
                +"type varchar(500),"
                +"chat varchar(300),"
                +"chat_id varchar(500),"
                +"img varchar(3000),"
                +"txt text,"
                +"date datetime);";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE if exists chat";

        db.execSQL(sql);
        onCreate(db);
    }
}
