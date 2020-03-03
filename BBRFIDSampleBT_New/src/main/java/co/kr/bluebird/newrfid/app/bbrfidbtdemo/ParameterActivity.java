package co.kr.bluebird.newrfid.app.bbrfidbtdemo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import co.kr.bluebird.newrfid.app.bbrfidbtdemo.Database.AdminSQLiteOpenHelper;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.Database.clsDatabase;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.PowerStateRfid;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.clsMensaje;

public class ParameterActivity extends Activity {

    private Context mContext;
    private EditText medEndPointLocal,medEndPointExt,medCodBodega,medDesBodega,medHolding, medTimeDuration,medDateIni,medDispositivoId,medDateFin;
    private Spinner mspinTipoConexion;
    private SeekBar mSeekBarPowerGEN, mSeekBarPowerGDE, mSeekBarPowerEM, mSeekBarPowerRM, mSeekBarPowerREP, mSeekBarPowerIP;
    private TextView mtvPowerGEN, mtvPowerGDE, mtvPowerEM, mtvPowerRM, mtvPowerREP, mtvPowerIP;
    private Button mimgBtnSave;
    private clsDatabase loDatabase;
    private SQLiteDatabase loExecute;
    private clsMensaje loDialogo;
    private ViewGroup loVistaDialogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameter);

        mContext = this;
        loDatabase = new clsDatabase(mContext);
        //##################### CLASE MENSAJE (DIALOGO)######################
        loDialogo = new clsMensaje(mContext);
        loVistaDialogo = findViewById(android.R.id.content);
        //###################################################################
        InicializateControls();
    }



    // funciones de transaccion sqlite
    private void LLenarCampos(){
        //AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(mContext,"dbBluebirdRFID", null, 1);
        loExecute = loDatabase.getWritableDatabase();
        int codigo = 1;

        Cursor fila = loExecute.rawQuery("select localendpoint, extendpoint, codbodega, descbodega, holding, conexiontype, dateini, dateend, dispositivoid, poderlecturarfid from parameterservice where codigo ="+codigo, null);


        if(fila.moveToFirst()){
            int indexLE;
            medEndPointLocal.setText(fila.getString(0));
            medEndPointExt.setText(fila.getString(1));

            medCodBodega.setText(fila.getString(2));
            medDesBodega.setText(fila.getString(3));
            medHolding.setText(fila.getString(4));
            if(fila.getString(5).equals("Local"))
            {
                indexLE = 0;
            }
            else {
                indexLE = 1;
            }
            mspinTipoConexion.setSelection(indexLE);
            medDateIni.setText(fila.getString(6));
            medDateFin.setText(fila.getString(7));
            medDispositivoId.setText(fila.getString(8));

            PowerStateRfid powerStateRfid = DeserializeJsonPowerStateRfid(fila.getString(9));

             /* mSeekBarPowerGEN, mSeekBarPowerGDE, mSeekBarPowerEM, mSeekBarPowerRM, mSeekBarPowerREP, mSeekBarPowerIP;
              mtvPowerGEN, mtvPowerGDE, mtvPowerEM, mtvPowerRM, mtvPowerREP, mtvPowerIP;*/
            ManagedSeekBarPower(mSeekBarPowerGEN, mtvPowerGEN, powerStateRfid.getGuiaEntrada());
            ManagedSeekBarPower(mSeekBarPowerGDE, mtvPowerGDE, powerStateRfid.getGuiaDespacho());
            ManagedSeekBarPower(mSeekBarPowerEM, mtvPowerEM, powerStateRfid.getEnvioMercaderia());
            ManagedSeekBarPower(mSeekBarPowerRM, mtvPowerRM, powerStateRfid.getEnvioMercaderia());
            ManagedSeekBarPower(mSeekBarPowerREP, mtvPowerREP, powerStateRfid.getReposicion());
            ManagedSeekBarPower(mSeekBarPowerIP, mtvPowerIP, powerStateRfid.getInventarioParticipante());


        }


        loExecute.close();
    }

    private void ManagedSeekBarPower(SeekBar seekBar, TextView textView, int power){
        seekBar.setProgress(power - 5);
        textView.setText(power+"");
    }

    private PowerStateRfid DeserializeJsonPowerStateRfid(String Json){
        Gson gson = new Gson();
        PowerStateRfid powerStateRfid = gson.fromJson(Json, PowerStateRfid.class);
        return powerStateRfid;
    }
    public void RegistrarModificar()
    {
        try{
            //AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(mContext,"dbBluebirdRFID", null, 1);
            loExecute = loDatabase.getWritableDatabase();

            String localEndpoint = medEndPointLocal.getText().toString();
            String extEndpoint = medEndPointExt.getText().toString();
            String codBodega =  medCodBodega.getText().toString();
            String descBodega = medDesBodega.getText().toString();
            String Holding = medHolding.getText().toString();
            String timeDuration= medTimeDuration.getText().toString();
            String DispId = medDispositivoId.getText().toString();

            String jsonPowerState = SerializeObjectPowerStateRfid();

            int codigo = 1;


            ContentValues registro = new ContentValues();
            if(localEndpoint.trim().isEmpty())
            {
                loDialogo.gMostrarMensajeAdvertencia(loVistaDialogo,"Falta definir el EndPoint Local");
                return;
            }
            if(extEndpoint.trim().isEmpty())
            {
                loDialogo.gMostrarMensajeAdvertencia(loVistaDialogo,"Falta definir el EndPoint Externo");
                return;
            }
            if(codBodega.trim().isEmpty())
            {
                loDialogo.gMostrarMensajeAdvertencia(loVistaDialogo,"Falta definir el Código Bodega");
                return;
            }
            if(descBodega.trim().isEmpty())
            {
                loDialogo.gMostrarMensajeAdvertencia(loVistaDialogo,"Falta definir la Descripción Bodega");
                return;
            }
            if(Holding.trim().isEmpty())
            {
                loDialogo.gMostrarMensajeAdvertencia(loVistaDialogo,"Falta definir el Holding");
                return;
            }
            if(DispId.trim().isEmpty())
            {
                loDialogo.gMostrarMensajeAdvertencia(loVistaDialogo,"Falta definir el Dispositivo ID");
                return;
            }
            //if (!localEndpoint.trim().isEmpty() && !extEndpoint.trim().isEmpty()  && !codBodega.trim().isEmpty() && !descBodega.trim().isEmpty() && !Holding.trim().isEmpty() && !DispId.trim().isEmpty() ){

            Cursor fila1 = loExecute.rawQuery("SELECT datetime('NOW', 'LOCALTIME')", null);
            Cursor fila2 = loExecute.rawQuery("SELECT datetime('NOW', 'LOCALTIME', '+"+timeDuration+" MINUTES')", null);

            String dateini_ = null, datefin_ = null;
            if(fila1.moveToFirst()) {
                dateini_ = fila1.getString(0);
            }
            if(fila2.moveToFirst()) {
                datefin_ = fila2.getString(0);
            }
            registro.put("codigo",1 );
            registro.put("localendpoint",localEndpoint );
            registro.put("extendpoint",extEndpoint );

            registro.put("codbodega",codBodega );
            registro.put("descbodega",descBodega);
            registro.put("holding",Holding);
            registro.put("conexiontype", mspinTipoConexion.getSelectedItem().toString());
            //registro.put("durationtime",timeDuration );
            registro.put("dateini",dateini_);
            registro.put("dateend",datefin_ );
            registro.put("dispositivoid",DispId);
            registro.put("poderlecturarfid",jsonPowerState);

            //}
            //else {
                //Toast.makeText(mContext,"Debe llenar Todos los Campos", Toast.LENGTH_LONG);
            //}

            Cursor fila = loExecute.rawQuery("select codigo from parameterservice where codigo ="+codigo, null);

            if(!fila.moveToFirst()){
                // insert

                final long parametro = loExecute.insert("parameterservice", null, registro);


                if(parametro > 0){
                    RegistrarPrimerInicioSession(loExecute);
                    //Toast.makeText(mContext,"Ingreso exitoso: "+ parametro, Toast.LENGTH_LONG).show();
                    loDialogo.gMostrarMensajeOk(loVistaDialogo, new Intent(ParameterActivity.this, LoginActivity.class));

                    //CleanControls();
                    //LLenarCampos();
                }
                else {
                    loDialogo.gMostrarMensajeAdvertencia(loVistaDialogo,"Registro Incorrecto");
                    //Toast.makeText(mContext,"Registro Incorrecto", Toast.LENGTH_LONG);
                }
                loExecute.close();
            }
            else
            {
                //update
                int cant = loExecute.update("parameterservice",registro, "codigo="+codigo, null);
                loExecute.close();

                if(cant == 1){
                    //Toast.makeText(mContext,"Modificado Exitosamente",Toast.LENGTH_SHORT).show();
                    loDialogo.gMostrarMensajeOk(loVistaDialogo, new Intent(ParameterActivity.this, LoginActivity.class));
                    //CleanControls();
                    //LLenarCampos();
                }
                else {
                    //Toast.makeText(mContext,"El Registro no existe",Toast.LENGTH_SHORT).show();
                    loDialogo.gMostrarMensajeAdvertencia(loVistaDialogo,"El Registro No Existe");
                }
            }
        }catch (Exception ex)
        {
            loDialogo.gMostrarMensajeError(loVistaDialogo,ex.toString());
        }



    }

    private String SerializeObjectPowerStateRfid(){

        PowerStateRfid powerStateRfid = new PowerStateRfid();
        powerStateRfid.setGuiaEntrada(Integer.parseInt(mtvPowerGEN.getText().toString()) );
        powerStateRfid.setGuiaDespacho(Integer.parseInt(mtvPowerGDE.getText().toString()) );
        powerStateRfid.setEnvioMercaderia(Integer.parseInt(mtvPowerEM.getText().toString()) );
        powerStateRfid.setRecepcionMercaderia(Integer.parseInt(mtvPowerRM.getText().toString()) );
        powerStateRfid.setReposicion(Integer.parseInt(mtvPowerREP.getText().toString()) );
        powerStateRfid.setInventarioParticipante(Integer.parseInt(mtvPowerIP.getText().toString()) );

        Gson gson = new Gson();
        String json = gson.toJson(powerStateRfid);
        return json;

    }

    public void RegistrarPrimerInicioSession(SQLiteDatabase sqlbase){
        ContentValues rowLogin = new ContentValues();

        Cursor fila1 = sqlbase.rawQuery("SELECT datetime('NOW', 'LOCALTIME')", null);
        String startdate = null;
        if(fila1.moveToFirst()){
            startdate = fila1.getString(0);
        }

        rowLogin.put("codigo",1 );
        rowLogin.put("startdate",startdate );
        rowLogin.put("user","xxxxx" );
        rowLogin.put("estado",0 );


        sqlbase.insert("paramlogin", null, rowLogin);

        /*sqlbase.rawQuery("select codigo from paramlogin where codigo ="+1, null);*/

    }


    // methods controls iterations
    private void CleanControls(){
        medEndPointLocal.setText("");
        medEndPointExt.setText("");
        medCodBodega.setText("");
        medDesBodega.setText("");
        medHolding.setText("");

        medDateIni.setText("");
        medDateFin.setText("");
        medDispositivoId.setText("");
    }

    private void InicializateControls(){

        medEndPointLocal = (EditText)findViewById(R.id.edEndPointLocal);
        medEndPointExt= (EditText)findViewById(R.id.edEndPointExt);

        medCodBodega= (EditText)findViewById(R.id.edCodBodega);
        medDesBodega= (EditText)findViewById(R.id.edDesBodega);
        medHolding= (EditText)findViewById(R.id.edHolding);
        medTimeDuration= (EditText)findViewById(R.id.edTimeDuration);
        medDateIni= (EditText)findViewById(R.id.edDateIni);
        medDateFin= (EditText)findViewById(R.id.edDateFin);
        medDispositivoId = (EditText)findViewById(R.id.edDispositivoId);
        mimgBtnSave = (Button) findViewById(R.id.imgBtnSave);
        mspinTipoConexion = (Spinner)findViewById(R.id.spinTipoConexion);

        Drawable myIcon = null;
        ColorFilter filter = null;

        myIcon = getResources().getDrawable( R.drawable.save );
        filter = new LightingColorFilter( Color.BLACK, Color.WHITE);
        myIcon.setColorFilter(filter);

        //SEEK_BAR

        mSeekBarPowerGEN = findViewById(R.id.SeekBarPowerGEN);
        mSeekBarPowerGDE = findViewById(R.id.SeekBarPowerGDE);
        mSeekBarPowerEM = findViewById(R.id.SeekBarPowerEM);
        mSeekBarPowerRM = findViewById(R.id.SeekBarPowerRM);
        mSeekBarPowerREP = findViewById(R.id.SeekBarPowerREP);
        mSeekBarPowerIP = findViewById(R.id.SeekBarPowerIP);


        mtvPowerGEN = findViewById(R.id.tvPowerGEN);
        mtvPowerGDE = findViewById(R.id.tvPowerGDE);
        mtvPowerEM = findViewById(R.id.tvPowerEM);
        mtvPowerRM = findViewById(R.id.tvPowerRM);
        mtvPowerREP = findViewById(R.id.tvPowerREP);
        mtvPowerIP = findViewById(R.id.tvPowerIP);

        //mimgBtnSave.setCompoundDrawablesWithIntrinsicBounds( null, null, myIcon, null);

        LLenarCampos();

        mSeekBarPowerGEN.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int val = mSeekBarPowerGEN.getProgress();
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                val = i + 5;
                mtvPowerGEN.setText(val+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mSeekBarPowerGDE.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int val = mSeekBarPowerGDE.getProgress();
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                val = i + 5;
                mtvPowerGDE.setText(val+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSeekBarPowerEM.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int val = mSeekBarPowerEM.getProgress();
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                val = i + 5;
                mtvPowerEM.setText(val+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSeekBarPowerRM.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int val = mSeekBarPowerRM.getProgress();
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                val = i + 5;
                mtvPowerRM.setText(val+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSeekBarPowerREP.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int val = mSeekBarPowerREP.getProgress();
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                val = i + 5;
                mtvPowerREP.setText(val+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSeekBarPowerIP.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int val = mSeekBarPowerIP.getProgress();
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                val = i + 5;
                mtvPowerIP.setText(val+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });




        mimgBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RegistrarModificar();

            }
        });

        mspinTipoConexion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position == 1){
                    medTimeDuration.setEnabled(true);
                }
                else {
                    medTimeDuration.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}
