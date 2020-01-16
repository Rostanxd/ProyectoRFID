package co.kr.bluebird.newrfid.app.bbrfidbtdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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
import java.util.List;

/*import co.kr.bluebird.newrfid.app.bbrfidbtdemo.control.RfidAutentication;*/

import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.DataSourceDto;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.LoginData;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ParamLectorRfid;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ParamLogin;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.RAAcceso;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.RAData;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.RAPrograma;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.RfidAutentication;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.service.RfidService;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.ParamRfidIteration;


public class LoginActivity extends Activity {

    private Button mbtnIngresarLogin;
    private ParamLectorRfid paramLectorRfid_ = null;
    private ParamRfidIteration paramRfidIteration = null;
    private EditText mtxtUser , mtxtPass;
    private Context mcontext;
    private RfidAutentication rfidAutentication;
    private WebServiceHandler mWebServiceHandler = new WebServiceHandler(this);
    private String msgRespuesta = "";
    private Switch switchAdmin;
    private boolean isAdmin;
    private RfidService rfidService;
    private String[] mWSLoginParameters;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        paramRfidIteration = new ParamRfidIteration();
        paramLectorRfid_ =  paramRfidIteration.ConsultarParametros(mcontext);

        switchAdmin = (Switch)findViewById(R.id.switch1);

        if(ExistLoggeo()){
            // si existe loggeed
        }
        else {
            if(paramLectorRfid_ != null){

            }
            else {
                // modo config

                switchAdmin.setChecked(true);
                switchAdmin.setEnabled(false);
            }
        }


        mbtnIngresarLogin = (Button)findViewById(R.id.btnIngresarLogin);
        EnabledDisabledBtnLogin(false);

        mtxtUser = (EditText)findViewById(R.id.etusuarioLogin) ;
        mtxtPass = (EditText)findViewById(R.id.etPassLogin) ;

        mtxtUser.addTextChangedListener(addTextWatcherUser);
        mtxtPass.addTextChangedListener(addTextWatcherPass);

        mcontext = this;
        rfidService = new RfidService(mcontext);

        mbtnIngresarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                executeWSLoginAsync tarea = new executeWSLoginAsync();
                tarea.execute();
            }
        });
    }

    private void EnabledDisabledBtnLogin(boolean isEnabled){
        mbtnIngresarLogin.setEnabled(isEnabled);
        mbtnIngresarLogin.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor((isEnabled ? "#00897B":"#D5D7D6"))));
    }

    private boolean ExistLoggeo(){

        boolean existLogeo = false;

        if(paramLectorRfid_ != null){
            ParamLogin paramLogin  = paramRfidIteration.ConsultarParametrosLogin(mcontext);
            if(paramLogin != null){
                if(paramLogin.isValidseccion()){
                    // si esta logeado
                    existLogeo = true;
                }
            }

        }
        return existLogeo;
    }


    private void LoginService()
    {
        String SOAP_ACTION = "WebSithaction/AWSRFIDAUTENTICACION.Execute";
        String METHOD_NAME = "WsRfidAutenticacion.Execute";
        String NAMESPACE = "WebSith";
        String URL = "http://info.thgye.com.ec/awsrfidautenticacion.aspx";


        String sDataId = "";
        String sDataNombre = "";

        DataSourceDto raEstado ;
        DataSourceDto raUsuario;
        DataSourceDto raRol ;
        RAPrograma raPrograma;
        //RfidAutentication.RAPrograma raPrograma = new RfidAutentication.RAPrograma("Guia de Entrada","activity_guia_entrada");
        //RfidAutentication.RAPrograma raPrograma1 = new RfidAutentication.RAPrograma("Guia de Despacho","activity_guia_despacho");
        List<RAPrograma> raProgramaList ;
        //raProgramaList.add(raPrograma);
        //raProgramaList.add(raPrograma1);
        RAAcceso raAcceso ;
        RAData raData ;

       /* RfidAutentication rfidAutentication = new RfidAutentication( raEstado,raData );*/

        try {
            SoapObject Request = new SoapObject(NAMESPACE,METHOD_NAME);
            Request.addProperty("Usrcodigo",mtxtUser.getText().toString());
            Request.addProperty("Usrclave",mtxtPass.getText().toString());


            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            //soapEnvelope.implicitTypes = true;
            soapEnvelope.dotNet = true;

            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transportSE = new HttpTransportSE(URL);
            transportSE.debug = true;


            transportSE.call(SOAP_ACTION,soapEnvelope);
            String requestDump = transportSE.requestDump;
            String responseDump = transportSE.responseDump;

            //SoapPrimitive resultString = (SoapPrimitive)soapEnvelope.getResponse();

            //SoapPrimitive resultRequestSOAP = (SoapPrimitive) soapEnvelope.getResponse();

            SoapObject resultRequestSOAP = (SoapObject) soapEnvelope.getResponse();

            SoapPrimitive spEstado =(SoapPrimitive) ((SoapObject)resultRequestSOAP.getProperty("estado")).getProperty("codigo");
            SoapPrimitive spMensaje =(SoapPrimitive) ((SoapObject)resultRequestSOAP.getProperty("estado")).getProperty("mensaje");
            raEstado = new DataSourceDto(spEstado.getValue().toString(),spMensaje.getValue().toString(),null);
            if(spEstado.getValue().toString().equals("00")){

                sDataId =  ((SoapObject) ((SoapObject)resultRequestSOAP.getProperty("data")).getProperty("usuario")).getPropertyAsString("id");
                sDataNombre = ((SoapObject) ((SoapObject)resultRequestSOAP.getProperty("data")).getProperty("usuario")).getPropertyAsString("nombre");
                raUsuario = new DataSourceDto(sDataId,sDataNombre, null);

                sDataId = "";
                sDataNombre = "";

                sDataId =  ((SoapObject) ((SoapObject)resultRequestSOAP.getProperty("data")).getProperty("rol")).getPropertyAsString("id");
                sDataNombre = ((SoapObject) ((SoapObject)resultRequestSOAP.getProperty("data")).getProperty("rol")).getPropertyAsString("nombre");
                raRol = new DataSourceDto(sDataId,sDataNombre,null);

                String sDataRuta = "";
                sDataNombre = "";
                raProgramaList = new ArrayList<RAPrograma>();

                SoapObject soAccesos = ((SoapObject) ((SoapObject)resultRequestSOAP.getProperty("data")).getProperty("accesos"));

                for (int x=0; x<soAccesos.getPropertyCount()-1;x++){
                    SoapObject soPrograma = (SoapObject) soAccesos.getProperty(x);
                    sDataNombre = soPrograma.getPropertyAsString("nombre");
                    sDataRuta =  soPrograma.getPropertyAsString("ruta");
                    raPrograma = new RAPrograma(sDataNombre,sDataRuta);
                    raProgramaList.add(raPrograma);
                }

                raAcceso = new RAAcceso(raProgramaList);
                raData = new RAData(raUsuario,raRol, raAcceso);
                rfidAutentication = new RfidAutentication( raEstado,raData );

            }
            else {
                rfidAutentication = new RfidAutentication( raEstado,null );
            }


            //new RfidAutentication.RAEstado("00","Exitoso");
            //String SOData =   ((SoapObject) resultRequestSOAP.getProperty("data")).getPropertyAsString("tags_quantity");

            //String SOState =  ((SoapObject) resultRequestSOAP.getProperty("data")).getPropertyAsString("status") ;

            /*String TagsRespose = SOData+"";

            if (SOData.getValue().toString().equals("00")){

                msgRespuesta = "Respuesta Exitosa: "+TagsRespose;
            }
            else {
                msgRespuesta = "La respuesta del webservice es fallida codigo recibido: "+SOData.getValue().toString();
            }*/

        } catch (Exception ex){
            //Toast.makeText(mContext, "Operacion Fallida ERROR: "+ex.getMessage(),Toast.LENGTH_SHORT);
            msgRespuesta = "Operacion Fallida ERROR: "+ex.getMessage();
        }
        finally {
           /* Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("msgSoap",msgRespuesta);
            //mWebServiceHandler
            msg.setData(bundle);
            mWebServiceHandler.sendMessageDelayed(msg, 1000);*/

        }

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


            if(rfidService.ExistDataBase()){
                loginData = rfidService.WSLogin(user, pass, isAdmin);
            }
            else {
                if(!isAdmin){
                    RequiereAdmin = true;
                }
                else {
                    loginData = rfidService.WSLogin(user, pass, isAdmin);
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.cancel();

            if(paramLectorRfid_ == null){
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
            }

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
            if(charSequence.length() > 0 && mtxtPass.length() > 0){

            }
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

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}
