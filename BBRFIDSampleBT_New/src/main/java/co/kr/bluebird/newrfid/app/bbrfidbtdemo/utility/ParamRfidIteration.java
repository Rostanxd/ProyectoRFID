package co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.versionedparcelable.VersionedParcel;

import java.text.ParseException;
import java.util.Date;

import co.kr.bluebird.newrfid.app.bbrfidbtdemo.Database.AdminSQLiteOpenHelper;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ParamLectorRfid;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ParamLogin;

import java.text.SimpleDateFormat;

public class ParamRfidIteration {

    private Context mContext;

    public ParamRfidIteration(Context context){
        mContext = context;
    }

    public ParamLogin ConsultarParametrosLogin(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(mContext,"dbBluebirdRFID", null, 1);
        SQLiteDatabase base = admin.getWritableDatabase();
        int codigo = 1;
        ParamLogin paramLogin = null;

        Cursor fila = base.rawQuery("select startdate, user, estado from paramlogin where codigo=" +codigo, null);

        if(fila.moveToFirst()){
            //String estado = fila.getString(3);
            paramLogin = new ParamLogin();
            paramLogin.setStartdate(fila.getString(0));
            paramLogin.setUsuario(fila.getString(1));
            paramLogin.setEstado(Integer.parseInt(fila.getString(2) ));
            paramLogin.setValidseccion((DiferenciaFechas(fila.getString(0)) <121 ));
        }

        return paramLogin;
    }

    private long DiferenciaFechas(String fechaUltimoInicioSeccion){

        long minutos = 0;

        Date fechaActual = new Date();
        Date DfechaActualFormat = null;
        Date DfechaUltimoInicioSeccion = null;
        String strDateFormat = "yyyy-MM-dd HH:mm";

        SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat);

        try{
            DfechaUltimoInicioSeccion = objSDF.parse(objSDF.format(parseDate(fechaUltimoInicioSeccion)));
            DfechaActualFormat = objSDF.parse(objSDF.format(fechaActual));


            long diff = DfechaUltimoInicioSeccion.getTime() - DfechaActualFormat.getTime();

            long segundos = diff / 1000;
            minutos = segundos / 60;
            /*long horas = minutos / 60;
            long dias = horas / 24;*/
        }
        catch (ParseException pe){
            minutos = 121;
        }



        return minutos;

    }

    public ParamLectorRfid ConsultarParametros(){
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



    public boolean RegistrarModificarParamLogin( ParamLogin paramLogin, boolean isChangeStatus)
    {

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(mContext,"dbBluebirdRFID", null, 1);
        SQLiteDatabase base = admin.getWritableDatabase();

        int codigo = 1;
        boolean isIngModExitosa = false;


        Cursor fila1 = base.rawQuery("SELECT datetime('NOW', 'LOCALTIME')", null);
        String startdate = null;
        if(fila1.moveToFirst()){
            startdate = fila1.getString(0);
        }


        ContentValues registro = new ContentValues();

        registro.put("codigo", 1);
        registro.put("estado", paramLogin.getEstado());
        if(!isChangeStatus){
            registro.put("startdate", startdate);
            registro.put("user", paramLogin.getUsuario());
        }




        Cursor fila = base.rawQuery("select codigo from paramlogin where codigo ="+codigo, null);

        if(!fila.moveToFirst()){
            // insert

            final long parametro = base.insert("paramlogin", null, registro);
            if(parametro > 0){
                isIngModExitosa = true;
            }
            base.close();
        }
        else
        {
            //update
            int cant = base.update("paramlogin",registro, "codigo="+codigo, null);
            if(cant  > 0 ){
                isIngModExitosa = true;
            }
            base.close();

        }
        return isIngModExitosa;
    }


}
