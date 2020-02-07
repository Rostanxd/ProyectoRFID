package co.kr.bluebird.newrfid.app.bbrfidbtdemo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import co.kr.bluebird.newrfid.app.bbrfidbtdemo.Database.AdminSQLiteOpenHelper;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.Database.clsDatabase;

public class ParameterActivity extends Activity {

    private Context mContext;
    private EditText medEndPointLocal,medEndPointExt,medCodBodega,medDesBodega,medHolding, medTimeDuration,medDateIni,medDispositivoId,medDateFin;
    private Spinner mspinTipoConexion;
    private Button mimgBtnSave;
    private clsDatabase loDatabase;
    private SQLiteDatabase loExecute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameter);

        mContext = this;
        loDatabase = new clsDatabase(mContext);
        InicializateControls();
    }



    // funciones de transaccion sqlite
    private void LLenarCampos(){
        //AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(mContext,"dbBluebirdRFID", null, 1);
        loExecute = loDatabase.getWritableDatabase();
        int codigo = 1;

        Cursor fila = loExecute.rawQuery("select localendpoint, extendpoint, codbodega, descbodega, holding, conexiontype, dateini, dateend, dispositivoid from parameterservice where codigo ="+codigo, null);


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

        }


        loExecute.close();
    }

    public void RegistrarModificar()
    {

        //AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(mContext,"dbBluebirdRFID", null, 1);
        loExecute = loDatabase.getWritableDatabase();

        String localEndpoint = medEndPointLocal.getText().toString();
        String extEndpoint = medEndPointExt.getText().toString();
        String codBodega =  medCodBodega.getText().toString();
        String descBodega = medDesBodega.getText().toString();
        String Holding = medHolding.getText().toString();
        String timeDuration= medTimeDuration.getText().toString();
        String DispId = medDispositivoId.getText().toString();

        int codigo = 1;


        ContentValues registro = new ContentValues();
        if (!localEndpoint.trim().isEmpty() && !extEndpoint.trim().isEmpty()  && !codBodega.trim().isEmpty()
                && !descBodega.trim().isEmpty() && !Holding.trim().isEmpty() && !DispId.trim().isEmpty() ){
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

        }
        else {
            Toast.makeText(mContext,"Debe llenar Todos los Campos", Toast.LENGTH_LONG);
        }

        Cursor fila = loExecute.rawQuery("select codigo from parameterservice where codigo ="+codigo, null);

        if(!fila.moveToFirst()){
            // insert

            final long parametro = loExecute.insert("parameterservice", null, registro);


            if(parametro > 0){
                RegistrarPrimerInicioSession(loExecute);
                Toast.makeText(mContext,"Ingreso exitoso: "+ parametro, Toast.LENGTH_LONG).show();
                CleanControls();
                LLenarCampos();
            }
            else {
                Toast.makeText(mContext,"Registro Incorrecto", Toast.LENGTH_LONG);
            }
            loExecute.close();
        }
        else
        {
            //update
            int cant = loExecute.update("parameterservice",registro, "codigo="+codigo, null);
            loExecute.close();

            if(cant == 1){
                Toast.makeText(mContext,"Modificado Exitosamente",Toast.LENGTH_SHORT).show();
                CleanControls();
                LLenarCampos();
            }
            else {
                Toast.makeText(mContext,"El Registro no existe",Toast.LENGTH_SHORT).show();
            }
        }


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

        mimgBtnSave.setCompoundDrawablesWithIntrinsicBounds( null, null, myIcon, null);

        LLenarCampos();


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
