package co.kr.bluebird.newrfid.app.bbrfidbtdemo.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class clsDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=2;
    private static final String DATABASE_NAME="dbBluebirdRFID";
    private static final String TABLE_LOGIN="paramlogin";
    private static final String TABLE_PARAMETROS ="parameterservice";


    public clsDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase toDatabase){
        String psQuery="";
        psQuery="CREATE TABLE "+ TABLE_PARAMETROS +" (codigo int primary key, localendpoint text, extendpoint text, codbodega text, descbodega text, holding text, conexiontype text,  dateini text, dateend text, dispositivoid text, poderlecturarfid )";
        toDatabase.execSQL(psQuery);
        psQuery="";
        psQuery="CREATE TABLE "+ TABLE_LOGIN +" (codigo int primary key, startdate text, user text, estado int  )";
        toDatabase.execSQL(psQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase toDatabase, int oldversion, int newversion){
        toDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_LOGIN);
        toDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_PARAMETROS);
        onCreate(toDatabase);
    }
}
