package co.kr.bluebird.newrfid.app.bbrfidbtdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.DataSourceDto;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.LoginData;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.LoginProgram;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ParamLectorRfid;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ParamLogin;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.fragmentvct.ParameterFragment;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.service.RfidService;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.ParamRfidIteration;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.clsMensaje;


public class LoginActivity extends Activity {

    private Button mbtnIngresarLogin;
    private ParamLectorRfid paramLectorRfid_ = null;
    private ParamRfidIteration paramRfidIteration = null;
    private EditText mtxtUser , mtxtPass;
    private Context mcontext;
    private WebServiceHandler mWebServiceHandler = new WebServiceHandler(this);
    private String msgRespuesta = "";
    private Switch switchAdmin;
    private boolean isAdmin;
    private RfidService rfidService;
    private String[] mWSLoginParameters;

    private boolean isLogeoForzoso = false;



    private boolean ExistParametrizacion = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mcontext = this;
        rfidService = new RfidService(mcontext);

        paramRfidIteration = new ParamRfidIteration(mcontext);
        /*paramLectorRfid_ =  paramRfidIteration.ConsultarParametros(mcontext);*/

        switchAdmin = (Switch)findViewById(R.id.switch1);

        /*if(ExistLoggeo()){
            // si existe loggeed
        }
        else {
            if(paramLectorRfid_ != null){
                // ya existe parametrizacion
                ExistParametrizacion  = true;
            }
            else {
                // no existe parametrizacion
                // modo config

                switchAdmin.setChecked(true);
                switchAdmin.setEnabled(false);
            }
        }*/


        mbtnIngresarLogin = (Button)findViewById(R.id.btnIngresarLogin);
        EnabledDisabledBtnLogin(false);

        mtxtUser = (EditText)findViewById(R.id.etusuarioLogin) ;
        mtxtPass = (EditText)findViewById(R.id.etPassLogin) ;

        mtxtUser.addTextChangedListener(addTextWatcherUser);
        mtxtPass.addTextChangedListener(addTextWatcherPass);



        mbtnIngresarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Prueba para mostrar mensajes
                //clsMensaje poMensaje = new clsMensaje(LoginActivity.this);
                //ViewGroup poVistaContent = findViewById(android.R.id.content);
                //poMensaje.gMostrarMensajeOk(poVistaContent);
               /* if(ValidarUsuario(mtxtUser.getText().toString(), mtxtPass.getText().toString()))
                {
                    Intent intent = new Intent(mcontext, MainActivity.class);
                    startActivity(intent);
                }
                else {

                    Toast.makeText(mcontext, "Usuario/password no validos...", Toast.LENGTH_SHORT).show();
                }*/

               isAdmin = switchAdmin.isChecked();

               /*if(isAdmin){
                   executeSoapAsync tarea = new executeSoapAsync();
                   tarea.execute();
               }
               else {

               }*/

               if(isAdmin){
                   mWSLoginParameters = getResources().getStringArray(R.array.WSparameter_LoginAdministrador);
               }
               else {
                   mWSLoginParameters = getResources().getStringArray(R.array.WSparameter_Login);
               }


               if((!ExistParametrizacion && switchAdmin.isChecked()) || isLogeoForzoso){
                   if(ValidadPrimerInicioSeccionAdmin()){
                       InvocarActivity(ParameterActivity.class);
                   }
                   else {
                       Toast.makeText(mcontext, "usuario o clave de configuracion inicial incorrecta...", Toast.LENGTH_LONG).show();
                   }
               }
               else {
                   executeWSLoginAsync tarea = new executeWSLoginAsync();
                   tarea.execute();
               }
            }
        });
    }

    private boolean ValidadPrimerInicioSeccionAdmin(){
        return (mtxtUser.getText().toString().equalsIgnoreCase("admin") && mtxtPass.getText().toString().equalsIgnoreCase("admin"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(mcontext, "onResume", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        OnStart_Extrated();


        rfidService = new RfidService(mcontext);
        //Toast.makeText(mcontext, "onStart", Toast.LENGTH_LONG).show();
    }




    private void OnStart_Extrated(){

        isLogeoForzoso = false;
        paramLectorRfid_ =  paramRfidIteration.ConsultarParametros();

        ExistParametrizacion = VerificarCamposParametrizacionLlenos();

        if(ExistLoggeo() ){
            // si existe loggeed
            //Toast.makeText(mcontext, "Ya Existe un Logeo", Toast.LENGTH_LONG).show(

            InvocarActivity(MainActivity.class);
        }
        else {
            if(VerificarCamposParametrizacionLlenos()){
                ExistParametrizacion  = true;
                switchAdmin.setChecked(false);
                switchAdmin.setEnabled(true);
            }
            else {
                switchAdmin.setChecked(true);
                switchAdmin.setEnabled(false);
            }

        }
    }

    private boolean VerificarCamposParametrizacionLlenos(){

        return ( paramLectorRfid_ != null && !paramLectorRfid_.getEndpoint().trim().equals("")  && !paramLectorRfid_.getCodbodega().trim().equals("") && !paramLectorRfid_.getDescBodega().trim().equals("") && !paramLectorRfid_.getDispositivoid().trim().equals(""));
    }

    private void EnabledDisabledBtnLogin(boolean isEnabled){
        mbtnIngresarLogin.setEnabled(isEnabled);
        mbtnIngresarLogin.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor((isEnabled ? "#3bbdfa":"#D5D7D6"))));
    }

    private boolean ExistLoggeo(){

        boolean existLogeo = false;

        if(paramLectorRfid_ != null){
            ParamLogin paramLogin  = paramRfidIteration.ConsultarParametrosLogin();
            if(paramLogin != null){
                if(paramLogin.isValidseccion() && paramLogin.getEstado() != 0){
                    // si esta logeado
                    existLogeo = true;
                }
            }

        }
        return existLogeo;
    }

    private static class WebServiceHandler extends Handler {
        private final WeakReference<LoginActivity> mExecutor;
        public WebServiceHandler(LoginActivity f) {
            mExecutor = new WeakReference<>(f);
        }

        @Override
        public void handleMessage(Message msg) {
            LoginActivity executor = mExecutor.get();
            if (executor != null) {
                executor.handleWebServiceHandler(msg);
            }
        }
    }

    public void handleWebServiceHandler(Message msg) {

        Bundle bundle = msg.getData();
        Toast.makeText(mcontext,"..."+bundle.getString("msgSoap"),Toast.LENGTH_LONG).show();
    }

    /*private  class executeSoapAsync extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            LoginService();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);

            *//*Intent intent = new Intent(mcontext, MainActivity.class);
            intent.putExtra("isAdmin", isAdmin);
            startActivity(intent);*//*

            if (msgRespuesta.equals("")){
                if(rfidAutentication.estado.codigo.equals("00"))
                {
                    Intent intent = new Intent(mcontext, MainActivity.class);
                    intent.putExtra("isAdmin", isAdmin);
                    startActivity(intent);
                }
                else {

                    Toast.makeText(mcontext, rfidAutentication.estado.descripcion, Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(mcontext, "Error al intentar Iniciar Session: "+ msgRespuesta, Toast.LENGTH_SHORT).show();
            }

        }
    }*/


    private  class executeWSLoginAsync extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String user =  mtxtUser.getText().toString();
        String pass = mtxtPass.getText().toString();
        LoginData loginData;
        boolean RequiereAdmin = false;
        @Override
        protected Void doInBackground(Void... voids) {


            rfidService.SOAP_ACTION_ =  mWSLoginParameters[0];
            rfidService.METHOD_NAME_ =  mWSLoginParameters[1];
            rfidService.NAMESPACE_ = mWSLoginParameters[2];
            rfidService.URL_ = mWSLoginParameters[3];


            /*if(ExistParametrizacion){

            }*/

            loginData = rfidService.WSLogin(user, pass, isAdmin);


            /*if(rfidService.ExistDataBase()){
                loginData = rfidService.WSLogin(user, pass, isAdmin);
            }
            else {
                if(!isAdmin){
                    RequiereAdmin = true;
                }
                else {
                    loginData = rfidService.WSLogin(user, pass, isAdmin);
                }
            }*/


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.cancel();


            if(loginData != null && loginData.getEstado() != null){
                if(loginData.getEstado().getCodigo().equals("00")){

                    if(ExistParametrizacion && !switchAdmin.isChecked()){

                        ParamLogin dlogin = new ParamLogin();
                        dlogin.setEstado(1);
                        dlogin.setUsuario(loginData.getUsuario().getCodigo());

                        paramRfidIteration.RegistrarModificarParamLogin(dlogin,false);

                        PersistirDatosUsuario(loginData);
                        InvocarActivity(MainActivity.class);

                    }
                    else {
                        // pasar Directamente al framgment Parametrizador

                        /*Intent intent = new Intent(mcontext, ParameterActivity.class);
                        startActivity(intent);*/
                        InvocarActivity(ParameterActivity.class);
                    }
                }
                else {

                    if(loginData.getEstado().getDescripcion().equals("ERRORHOST")){
                        Toast.makeText(mcontext, "Existe un Error de Host, vuelva a logerse con admin y corriga el end-point", Toast.LENGTH_LONG).show();
                        LogeoForzoso();
                    }
                    else {
                        Toast.makeText(mcontext, loginData.getEstado().getDescripcion(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            /*if(paramLectorRfid_ == null){
                paramLectorRfid_ =  paramRfidIteration.ConsultarParametros(mcontext);
            }

            if(paramLectorRfid_ != null || isAdmin){
                Intent intent = new Intent(mcontext, MainActivity.class);
                intent.putExtra("isAdmin", isAdmin);
                intent.putExtra("isExistParametrizacion", (paramLectorRfid_ != null));
                startActivity(intent);
            }
            else {
                InvocarAlert("Lo sentimos, inicie session como Administrador, y dirigirse a la opción parametrizador...");
            }*/




            /*if(!RequiereAdmin){
                if(loginData != null && loginData.getEstado() != null )
                {
                    if(loginData.getEstado().getCodigo().equals("00")){
                        Intent intent = new Intent(mcontext, MainActivity.class);
                        intent.putExtra("isAdmin", isAdmin);
                        startActivity(intent);
                    }
                    else {

                        Toast.makeText(mcontext, loginData.getEstado().getDescripcion(),Toast.LENGTH_LONG).show();
                    }

                }
            }
            else {
                Toast.makeText(mcontext,"No se Realizado parametrizacion para el primer inicio, Contactese con el usuario Administrador", Toast.LENGTH_LONG).show();
            }*/


        }
        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            progressDialog = new ProgressDialog(mcontext);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Cargando...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();

        }
    }

    private void LogeoForzoso(){
        mtxtUser.setText("");
        mtxtPass.setText("");

        isLogeoForzoso = true;

        switchAdmin.setChecked(true);
        switchAdmin.setEnabled(false);

    }


    private void InvocarActivity(Class<?> cls){
        Intent intent = new Intent(mcontext, cls);
        intent.putExtra("isExistParametrizacion", ExistParametrizacion);
        intent.putExtra("isAdmin", isAdmin);

        startActivity(intent);
    }

    private void InvocarAlert(String msj)
    {
        AlertDialog.Builder alerta = new AlertDialog.Builder(mcontext);
        alerta.setMessage(msj)
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        mtxtUser.setText("");
                        mtxtPass.setText("");
                        dialog.dismiss();
                    }
                });

        alerta.show();
    }

    // eventos

    /*
    private OnClickListener sledListener = new OnClickListener() {
     metEstiloItemIPS.addTextChangedListener(new TextWatcher() {
    */

    private TextWatcher addTextWatcherUser = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            EnabledDisabledBtnLogin((charSequence.length() > 0 && mtxtPass.length() > 0));

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private TextWatcher addTextWatcherPass = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            EnabledDisabledBtnLogin(charSequence.length() > 0 && mtxtUser.length() > 0);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    private void PersistirDatosUsuario(LoginData loginData){

        if(loginData != null){

            SharedPreferences prefs = getSharedPreferences("shared_login_data",   Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            editor.clear();

            editor.putString("userid", loginData.getUsuario().getCodigo());
            editor.putString("username", loginData.getUsuario().getDescripcion());

            editor.putString("rolid", loginData.getRol().getCodigo());
            editor.putString("rolname", loginData.getRol().getDescripcion());

            Set<String > hash_Set = new HashSet<String>();


            for (LoginProgram program:loginData.getAccesos()) {
                hash_Set.add(program.getNombre()+"|"+program.getRuta());
            }

            editor.putStringSet("accesos", hash_Set);

            editor.commit();
        }

    }
}
