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

public class ParameterActivity extends Activity {

    private Context mContext;
    private EditText medLocalHost, medLocalPort,medExtHost,medExtPort,medCodBodega,medDesBodega,medHolding, medTimeDuration,medDateIni,medDispositivoId,medDateFin;
    private Spinner mspinTipoConexion;
    private Button mimgBtnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameter);

        mContext = this;
        InicializateControls();
    }



    // funciones de transaccion sqlite
    private void LLenarCampos(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(mContext,"dbBluebirdRFID", null, 1);
        SQLiteDatabase base = admin.getWritableDatabase();
        int codigo = 1;

        Cursor fila = base.rawQuery("select localhost,localport, exthost, extport, codbodega, descbodega, holding, conexiontype, dateini, dateend, dispositivoid from parameterservice where codigo ="+codigo, null);


        if(fila.moveToFirst()){
            int indexLE;
            medLocalHost.setText(fila.getString(0));
            medLocalPort.setText(fila.getString(1));
            medExtHost.setText(fila.getString(2));

            medExtPort.setText(fila.getString(3));
            medCodBodega.setText(fila.getString(4));
            medDesBodega.setText(fila.getString(5));
            medHolding.setText(fila.getString(6));
            if(fila.getString(7).equals("Local"))
            {
                indexLE = 0;
            }
            else {
                indexLE = 1;
            }
            mspinTipoConexion.setSelection(indexLE);
            medDateIni.setText(fila.getString(8));
            medDateFin.setText(fila.getString(9));
            medDispositivoId.setText(fila.getString(10));

        }


        base.close();
    }

    public void RegistrarModificar()
    {

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(mContext,"dbBluebirdRFID", null, 1);
        SQLiteDatabase base = admin.getWritableDatabase();

        String localHost = medLocalHost.getText().toString();
        String localPort = medLocalPort.getText().toString();
        String extHost =  medExtHost.getText().toString();
        String extPort = medExtPort.getText().toString();
        String codBodega =  medCodBodega.getText().toString();
        String descBodega = medDesBodega.getText().toString();
        String Holding = medHolding.getText().toString();
        String timeDuration= medTimeDuration.getText().toString();
        String DispId = medDispositivoId.getText().toString();

        int codigo = 1;


        ContentValues registro = new ContentValues();
        if (!localHost.trim().isEmpty() && !localPort.trim().isEmpty() && !extHost.trim().isEmpty() && !extPort.trim().isEmpty() && !codBodega.trim().isEmpty()
                && !descBodega.trim().isEmpty() && !Holding.trim().isEmpty() && !DispId.trim().isEmpty() ){
            Cursor fila1 = base.rawQuery("SELECT datetime('NOW', 'LOCALTIME')", null);
            Cursor fila2 = base.rawQuery("SELECT datetime('NOW', 'LOCALTIME', '+"+timeDuration+" MINUTES')", null);

            String dateini_ = null, datefin_ = null;
            if(fila1.moveToFirst()) {
                dateini_ = fila1.getString(0);
            }
            if(fila2.moveToFirst()) {
                datefin_ = fila2.getString(0);
            }
            registro.put("codigo",1 );
            registro.put("localhost",localHost );
            registro.put("localport",localPort );
            registro.put("exthost",extHost );
            registro.put("extport",extPort );

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

        Cursor fila = base.rawQuery("select codigo from parameterservice where codigo ="+codigo, null);

        if(!fila.moveToFirst()){
            // insert

            final long parametro = base.insert("parameterservice", null, registro);


            if(parametro > 0){
                RegistrarPrimerInicioSession(base);
                Toast.makeText(mContext,"Ingreso exitoso: "+ parametro, Toast.LENGTH_LONG).show();
                CleanControls();
                LLenarCampos();
            }
            else {
                Toast.makeText(mContext,"Registro Incorrecto", Toast.LENGTH_LONG);
            }
            base.close();
        }
        else
        {
            //update
            int cant = base.update("parameterservice",registro, "codigo="+codigo, null);
            base.close();

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
        medLocalHost.setText("");
        medLocalPort.setText("");
        medExtHost.setText("");

        medExtPort.setText("");
        medCodBodega.setText("");
        medDesBodega.setText("");
        medHolding.setText("");

        medDateIni.setText("");
        medDateFin.setText("");
        medDispositivoId.setText("");
    }

    private void InicializateControls(){

        medLocalHost = (EditText)findViewById(R.id.edLocalHost);
        medLocalPort= (EditText)findViewById(R.id.edLocalPort);
        medExtHost= (EditText)findViewById(R.id.edExtHost);
        medExtPort= (EditText)findViewById(R.id.edExtPort);
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
