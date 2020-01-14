package co.kr.bluebird.newrfid.app.bbrfidbtdemo.fragmentvct;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import co.kr.bluebird.newrfid.app.bbrfidbtdemo.Database.AdminSQLiteOpenHelper;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParameterFragment extends Fragment {


    private Context mContext;
    private EditText medLocalHost, medLocalPort,medExtHost,medExtPort,medCodBodega,medDesBodega,medHolding, medTimeDuration,medDateIni,medDispositivoId,medDateFin;
    private Spinner mspinTipoConexion;
    private Button mimgBtnSave;
    private int dia, mes, anio,hora, minutos;
    public ParameterFragment() {
        // Required empty public constructor
    }

    public static ParameterFragment newInstance() {

        return new ParameterFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.parameter_frag, container, false);

        View v = inflater.inflate(R.layout.parameter_frag, container, false);
        mContext = inflater.getContext();

        InicialiarAtributos(v);

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

        /*medDateIni.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {

                    final Calendar c = Calendar.getInstance();
                    dia = c.get(Calendar.DAY_OF_MONTH);
                    mes = c.get(Calendar.MONTH);
                    anio = c.get(Calendar.YEAR);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                            medDateIni.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
                        }
                    },dia,mes,anio);
                    datePickerDialog.show();

                }
            }
        });*/

        /*medTimeIni.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    final Calendar c = Calendar.getInstance();
                    hora = c.get(Calendar.HOUR_OF_DAY);
                    minutos = c.get(Calendar.MINUTE);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                            medTimeIni.setText(hourOfDay+":"+minute);
                        }
                    },hora,minutos,false);
                    timePickerDialog.show();
                }

            }
        });*/

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
        return v;


    }

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

    private void InicialiarAtributos(View v)
    {
        medLocalHost = (EditText)v.findViewById(R.id.edLocalHost);
        medLocalPort= (EditText)v.findViewById(R.id.edLocalPort);
        medExtHost= (EditText)v.findViewById(R.id.edExtHost);
        medExtPort= (EditText)v.findViewById(R.id.edExtPort);
        medCodBodega= (EditText)v.findViewById(R.id.edCodBodega);
        medDesBodega= (EditText)v.findViewById(R.id.edDesBodega);
        medHolding= (EditText)v.findViewById(R.id.edHolding);
        medTimeDuration= (EditText)v.findViewById(R.id.edTimeDuration);
        medDateIni= (EditText)v.findViewById(R.id.edDateIni);
        medDateFin= (EditText)v.findViewById(R.id.edDateFin);

        medDispositivoId = (EditText)v.findViewById(R.id.edDispositivoId);

        //medTimeIni = (EditText)v.findViewById(R.id.edTimeIni);

        mimgBtnSave = (Button) v.findViewById(R.id.imgBtnSave);

        mspinTipoConexion = (Spinner)v.findViewById(R.id.spinTipoConexion);



    }


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
       /* String dateIni = medDateIni.getText().toString();
        String dateFin = medDateFin.getText().toString();*/
        int codigo = 1;


        ContentValues registro = new ContentValues();
        if (!localHost.trim().isEmpty() && !localPort.trim().isEmpty() && !extHost.trim().isEmpty() && !extPort.trim().isEmpty() && !codBodega.trim().isEmpty()
                && !descBodega.trim().isEmpty() && !Holding.trim().isEmpty() && !DispId.trim().isEmpty() ){
            //codigo int primary key, localhost text, localport text, exthost, extport, codbodega, descbodega, holding, conexiontype, durationtime, dateini, dateend

            /*String dateini_ = "(SELECT datetime('NOW', 'LOCALTIME'))";
            String datefin_ = "(SELECT datetime('NOW', 'LOCALTIME', '"+timeDuration+" MINUTES'))";*/

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
            base.close();

            if(parametro > 0){
                Toast.makeText(mContext,"Registro correcto: "+ parametro, Toast.LENGTH_LONG);
                CleanControls();
                LLenarCampos();
            }
            else {
                Toast.makeText(mContext,"Registro Incorrecto", Toast.LENGTH_LONG);
            }
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

    private void cleanControls(){

    }

}
