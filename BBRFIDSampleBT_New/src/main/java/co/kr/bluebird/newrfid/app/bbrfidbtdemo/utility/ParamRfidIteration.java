package co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.versionedparcelable.VersionedParcel;

import com.google.gson.Gson;

import java.text.ParseException;
import java.util.Date;

import co.kr.bluebird.newrfid.app.bbrfidbtdemo.Database.AdminSQLiteOpenHelper;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.Database.clsDatabase;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ParamLectorRfid;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ParamLogin;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.PowerStateRfid;

import java.text.SimpleDateFormat;

public class ParamRfidIteration {

    private Context mContext;
    private clsDatabase loDatabase;
    private SQLiteDatabase loExecute;

    public ParamRfidIteration(Context context){
        mContext = context;
        loDatabase = new clsDatabase(mContext);
    }

    public ParamLogin ConsultarParametrosLogin(){
        //AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(mContext,"dbBluebirdRFID", null, 1);
        loExecute = loDatabase.getWritableDatabase();
        int codigo = 1;
        ParamLogin paramLogin = null;

        Cursor fila = loExecute.rawQuery("select startdate, user, estado from paramlogin where codigo=" +codigo, null);

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


            long diff = DfechaActualFormat.getTime() - DfechaUltimoInicioSeccion.getTime() ;

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
        //AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(mContext,"dbBluebirdRFID", null, 1);
        //SQLiteDatabase base = admin.getWritableDatabase();
        loExecute = loDatabase.getReadableDatabase();
        int codigo = 1;
        ParamLectorRfid paramLectorRfid = null;



        Cursor fila = loExecute.rawQuery("select localendpoint, extendpoint, codbodega, descbodega, holding, conexiontype, dateini, dateend, dispositivoid, poderlecturarfid from parameterservice where codigo ="+codigo, null);
        if(fila.moveToFirst()){
            paramLectorRfid = new ParamLectorRfid();
            int indexLE;

            Date fechaActual = new Date();
            Date fechaActualFormat = null;
            Date fechaFinParamFormat = null;
            String strDateFormat = "yyyy-MM-dd HH:mm";

            SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat);
            //String fechaActualFormat = objSDF.format(fechaActual);

            if(fila.getString(7) != null && !fila.getString(7).isEmpty()){
                try {
                    fechaActualFormat = objSDF.parse(objSDF.format(fechaActual));
                    /*Date myDate = new Date(2014, 02, 11,20,00);*/
                    fechaFinParamFormat = objSDF.parse(objSDF.format(parseDate(fila.getString(7))));
                }
                catch (ParseException ex){

                }
            }



            String endpoint = "";
            paramLectorRfid.setEndpointSelect("Local");
            if(fila.getString(5).equals("Local"))
            {
                endpoint = fila.getString(0);

            }
            else if(fechaActualFormat != null && fechaFinParamFormat != null && CompararFechas(fechaActualFormat,fechaFinParamFormat)){
                endpoint = fila.getString(1);
                paramLectorRfid.setEndpointSelect("Externo");

            }
            else {
                endpoint = fila.getString(0);

            }


            paramLectorRfid.setEndpoint(endpoint);
            paramLectorRfid.setEndpointExt(fila.getString(1));
            paramLectorRfid.setCodbodega(fila.getString(2));
            paramLectorRfid.setDescBodega(fila.getString(3));
            paramLectorRfid.setHolding(fila.getString(4));


            if(fila.getString(5).equals("Local"))
            {
                indexLE = 0;
            }
            else {
                indexLE = 1;
            }
            paramLectorRfid.setConexiontype(String.valueOf(indexLE) );
            paramLectorRfid.setDateini(fila.getString(6));
            paramLectorRfid.setDateend(fila.getString(7));
            paramLectorRfid.setDispositivoid(fila.getString(8));



            paramLectorRfid.setPowerStateRfid(DeserializeJsonPowerStateRfid(fila.getString(9)));

        }

        loExecute.close();

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

        //AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(mContext,"dbBluebirdRFID", null, 1);
        //SQLiteDatabase base = admin.getWritableDatabase();
        loExecute = loDatabase.getReadableDatabase();

        int codigo = 1;
        boolean isIngModExitosa = false;


        Cursor fila1 = loExecute.rawQuery("SELECT datetime('NOW', 'LOCALTIME')", null);
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




        Cursor fila = loExecute.rawQuery("select codigo from paramlogin where codigo ="+codigo, null);
        loExecute = loDatabase.getWritableDatabase();
        if(!fila.moveToFirst()){
            // insert

            final long parametro = loExecute.insert("paramlogin", null, registro);
            if(parametro > 0){
                isIngModExitosa = true;
            }
            loExecute.close();
        }
        else
        {
            //update
            int cant = loExecute.update("paramlogin",registro, "codigo="+codigo, null);
            if(cant  > 0 ){
                isIngModExitosa = true;
            }
            loExecute.close();

        }
        return isIngModExitosa;
    }

    private PowerStateRfid DeserializeJsonPowerStateRfid(String Json){
        Gson gson = new Gson();
        PowerStateRfid powerStateRfid = gson.fromJson(Json, PowerStateRfid.class);
        return powerStateRfid;
    }

}
