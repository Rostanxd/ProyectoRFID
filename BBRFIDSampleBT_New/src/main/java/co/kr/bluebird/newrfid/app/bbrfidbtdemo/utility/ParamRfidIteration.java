package co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.util.Date;

import co.kr.bluebird.newrfid.app.bbrfidbtdemo.Database.AdminSQLiteOpenHelper;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ParamLectorRfid;
import java.text.SimpleDateFormat;

public class ParamRfidIteration {

    public ParamLectorRfid ConsultarParametros(Context mContext){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(mContext,"dbBluebirdRFID", null, 1);
        SQLiteDatabase base = admin.getWritableDatabase();

        int codigo = 1;
        ParamLectorRfid paramLectorRfid = null;



        Cursor fila = base.rawQuery("select localhost,localport, exthost, extport, codbodega, descbodega, holding, conexiontype, dateini, dateend, dispositivoid from parameterservice where codigo ="+codigo, null);
        if(fila.moveToFirst()){
            paramLectorRfid = new ParamLectorRfid();
            int indexLE;

            Date fechaActual = new Date();
            Date fechaActualFormat = null;
            Date fechaFinParamFormat = null;
            String strDateFormat = "yyyy-MM-dd HH:mm";

            SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat);
            //String fechaActualFormat = objSDF.format(fechaActual);

            if(fila.getString(9) != null && !fila.getString(9).isEmpty()){
                try {
                    fechaActualFormat = objSDF.parse(objSDF.format(fechaActual));
                    /*Date myDate = new Date(2014, 02, 11,20,00);*/
                    fechaFinParamFormat = objSDF.parse(objSDF.format(parseDate(fila.getString(9))));
                }
                catch (ParseException ex){

                }
            }


            String port = "";
            String host = "";
            if(fila.getString(7).equals("Local"))
            {
                host = fila.getString(0);
                port = fila.getString(1);
            }
            else if(fechaActualFormat != null && fechaFinParamFormat != null && CompararFechas(fechaActualFormat,fechaFinParamFormat)){
                host = fila.getString(2);
                port = fila.getString(3);
            }
            else {
                host = fila.getString(0);
                port = fila.getString(1);
            }

            if(port == null && port.isEmpty()){
                port = "80";
            }
            paramLectorRfid.setHost(host);
            paramLectorRfid.setPort(port);
            paramLectorRfid.setHostPort(host+":"+port);



            paramLectorRfid.setCodbodega(fila.getString(4));
            paramLectorRfid.setDescBodega(fila.getString(5));
            paramLectorRfid.setHolding(fila.getString(6));


            if(fila.getString(7).equals("Local"))
            {
                indexLE = 0;
            }
            else {
                indexLE = 1;
            }
            paramLectorRfid.setConexiontype(String.valueOf(indexLE) );
            paramLectorRfid.setDateini(fila.getString(8));
            paramLectorRfid.setDateend(fila.getString(9));
            paramLectorRfid.setDispositivoid(fila.getString(10));

        }

        base.close();

        return paramLectorRfid;
    }

    private  Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    private boolean CompararFechas(Date fechaActualFormat, Date fechaFinParamFormat){

        boolean igual = fechaActualFormat.equals(fechaFinParamFormat);
        boolean before = fechaActualFormat.before(fechaFinParamFormat);
        //boolean after = fechaFinParamFormat.after(fechaActualFormat);
        if(fechaActualFormat.equals(fechaFinParamFormat) || fechaActualFormat.before(fechaFinParamFormat)){
            return  true;
        }
        else {
            return false;
        }
    }
}
