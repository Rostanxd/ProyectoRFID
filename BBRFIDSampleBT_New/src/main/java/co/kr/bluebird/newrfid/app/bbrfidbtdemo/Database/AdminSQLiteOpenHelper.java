package co.kr.bluebird.newrfid.app.bbrfidbtdemo.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AdminSQLiteOpenHelper
{
    /*
    public AdminSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table parameterservice(codigo int primary key, localendpoint text, extendpoint text, codbodega text, descbodega text, holding text, conexiontype text,  dateini text, dateend text, dispositivoid text )");
        sqLiteDatabase.execSQL("create table paramlogin(codigo int primary key, startdate text, user text, estado int  )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists parameterservice");
        sqLiteDatabase.execSQL("drop table if exists paramlogin");
        onCreate(sqLiteDatabase);
    }
    */
}