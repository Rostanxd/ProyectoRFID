/*
 * Copyright (C) 2015 - 2017 Bluebird Inc, All rights reserved.
 *
 * http://www.bluebirdcorp.com/
 *
 * Author : Bogon Jun
 *
 * Date : 2016.04.04
 */

package co.kr.bluebird.newrfid.app.bbrfidbtdemo;

import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.AdapterHashMap;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.DataSourceDto;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.GenericSpinnerDto;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.LoginData;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ParamLectorRfid;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ParamLogin;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ReceiveWareDetail;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ResponseVal;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.fragment.*;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.fragmentvct.DespatchGuideReadFragment;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.fragmentvct.EntryGuideCheckFragment;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.fragmentvct.InventarioFragment;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.fragmentvct.InventoryPerStoreFragment;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.fragmentvct.ParameterFragment;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.fragmentvct.ReceiveWareCheckFragment;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.fragmentvct.ReceiveWareInconsistencyFragment;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.fragmentvct.ReplenishmentWareFragment;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.fragmentvct.ShippingWareReadFragment;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.fragmentvct.TakingInventoryControlFragment;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.fragmentvct.TakingInventoryParticipantFragment;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.fragmentvct.TestFragment;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.service.RfidService;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.ParamRfidIteration;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.PersistenceDataIteration;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.Validator;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.clsMensaje;
import co.kr.bluebird.sled.BTReader;
import co.kr.bluebird.sled.SDConsts;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.HashMap;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final boolean D = Constants.MAIN_D;

    public static final int MSG_OPTION_CONNECT_STATE_CHANGED = 0;

    public static final int MSG_BACK_PRESSED = 2;

    public static ParamLectorRfid PARAM_LECTOR_RFID =  null;

    private String[] mFunctionsString;

    private DrawerLayout mDrawerLayout;

    //private ListView mDrawerList;

    private ActionBarDrawerToggle mDrawerToggle;

    private BTReader mReader;

    private Context mContext;

    private FragmentManager mFragmentManager;

    private boolean mIsConnected, mIsModeRFID;

    private BTConnectivityFragment mBTConnectivityFragment;
    private SDFragment mSDFragment;
    private RFAccessFragment mRFAccessFragment;
    private RFConfigFragment mRFConfigFragment;
    private RFSelectionFragment mRFSelectionFragment;
    private RapidFragment mRapidFragment;
    private InventoryFragment mInventoryFragment;
    private BCBarcodeFragment mBCBarcodeFragment;
    private SBBarcodeFragment mSBBarcodeFragment;
    private InfoFragment mInfoFragment;
    private BatteryFragment mBatteryFragment;

    private InventarioFragment mInventarioFragment;

    private TestFragment mTestFragment;
    private EntryGuideCheckFragment mEntryGuideCheckFragment;
    private DespatchGuideReadFragment mDespatchGuideReadFragment;
    private ShippingWareReadFragment mShippingWareReadFragment;
    private ReceiveWareCheckFragment mReceiveWareCheckFragment;
    private InventoryPerStoreFragment mInventoryPerStoreFragment;
    private ParameterFragment mParameterFragment;
    private ReplenishmentWareFragment mReplenishmentWareFragment;
    private TakingInventoryControlFragment mTakingInventoryControlFragment;
    private TakingInventoryParticipantFragment mTakingInventoryParticipantFragment;

    private LinearLayout mUILayout, mlayouttvParam;
    private TextView mtvParam1;
    private Fragment mCurrentFragment;
    private boolean isAdmin, isExistParametrizacion;
    private CardView mcvParametrizador,mcvConectividad,/*mcvConfiguracion,*/ mcvGuiaEntrada,mcvGuiaDespacho, mcvEnvioMercaderia,mcvRecepcionMercaderia,mcvInventariotienda, mcvReposicion, mcvTomaInventarioControl,mcvTomaInventarioParticipante;
    private GridLayout mainGrid;

    private  int PositionFrag;
    private String mDispositivoBTRfidSelect ;
    private final MainHandler mMainHandler = new MainHandler(this);
    public final UpdateConnectHandler mUpdateConnectHandler = new UpdateConnectHandler(this);
    public final  BackHandler mBackHandler = new BackHandler(this);
    private boolean isBackForce = false;
    private RfidService rfidService;
    private co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EntryGuide ResposeEG;
    private String[] mWSParameters;
    private String lsNumeroOrden;
    private clsMensaje loDialogo;
    private ViewGroup loVistaDialogo;
    private  int RFPower = 0;

    private boolean isOptionRecepcionMercaderia = false;
    private ProgressDialog progressDialog;

    private boolean execFragmentTransaction = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (D) Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        ParamRfidIteration paramRfidIteration = new ParamRfidIteration(mContext);
        PARAM_LECTOR_RFID = paramRfidIteration.ConsultarParametros();

        rfidService = new RfidService(mContext);
        mlayouttvParam = (LinearLayout) findViewById(R.id.layouttvParam);
        mtvParam1 = (TextView) findViewById(R.id.tvParam1);

        try {
            SharedPreferences prefs = mContext.getSharedPreferences("shared_recepcion_mercaderia",   Context.MODE_PRIVATE);
            if(prefs != null){
                prefs.edit().clear().commit();
            }

            SharedPreferences prefsGE = mContext.getSharedPreferences("shared_guia_entrada",   Context.MODE_PRIVATE);
            if(prefsGE != null){
                prefsGE.edit().clear().commit();
            }

        }
        catch (Exception ex ){
            if (D) Log.d(TAG, ex.getMessage());
        }

        mcvParametrizador  = (CardView)findViewById(R.id.cvParametrizador);
        mcvConectividad = (CardView)findViewById(R.id.cvConectividad);
        //mcvConfiguracion  = (CardView)findViewById(R.id.cvConfiguracion);
        mcvGuiaEntrada = (CardView)findViewById(R.id.cvGuiaEntrada);
        mcvGuiaDespacho = (CardView)findViewById(R.id.cvGuiaDespacho);
        mcvEnvioMercaderia = (CardView)findViewById(R.id.cvEnvioMercaderia);
        mcvRecepcionMercaderia = (CardView)findViewById(R.id.cvRecepcionMercaderia);
        mcvInventariotienda = (CardView)findViewById(R.id.cvInventariotienda);
        mcvReposicion = (CardView)findViewById(R.id.cvReposicion);
        mcvTomaInventarioControl = (CardView)findViewById(R.id.cvTomaInventarioControl);
        mcvTomaInventarioParticipante = (CardView)findViewById(R.id.cvTomaInventarioParticipante);


        isAdmin = getIntent().getBooleanExtra("isAdmin", false);
        isExistParametrizacion = getIntent().getBooleanExtra("isExistParametrizacion", false);



        if(!isExistParametrizacion){
            mlayouttvParam.setVisibility(View.GONE);
            mtvParam1.setVisibility(View.VISIBLE);
            mcvConectividad.setVisibility(View.GONE);
            //mcvConfiguracion.setVisibility(View.GONE);
            mcvGuiaEntrada.setVisibility(View.GONE);
            mcvGuiaDespacho.setVisibility(View.GONE);
            mcvEnvioMercaderia.setVisibility(View.GONE);
            mcvRecepcionMercaderia.setVisibility(View.GONE);
            mcvInventariotienda.setVisibility(View.GONE);
            mcvReposicion.setVisibility(View.GONE);
            mcvTomaInventarioControl.setVisibility(View.GONE);
            mcvTomaInventarioParticipante.setVisibility(View.GONE);
        }

        //VerOptionByRol();


        if(isAdmin){
            mcvParametrizador.setVisibility(View.VISIBLE);
        }
        else {
            mcvParametrizador.setVisibility(View.INVISIBLE);
        }


        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.gColorGris_n800)));
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //mDrawerList = (ListView) findViewById(R.id.left_drawer);

        //mDrawerList.setVisibility(View.GONE);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mUILayout = (LinearLayout)findViewById(R.id.ui_layout);

        mCurrentFragment = null;

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int buttonHeight = size.x / 3;
        mainGrid = (GridLayout) findViewById(R.id.mainGrid);

        //Set Event
        setSingleEvent(mainGrid);

        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout, R.drawable.ic_launcher, R.string.drawer_open, R.string.drawer_close) {
            String mDrawerTitle = "Functions";

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mDrawerTitle);
            }
        };

        mFunctionsString = getResources().getStringArray(R.array.functions_array);
        //mDrawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mFunctionsString));
        //mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mFragmentManager = getFragmentManager();

        mIsConnected = false;
        //##################### CLASE MENSAJE (DIALOGO)######################
        loDialogo = new clsMensaje(mContext);
        loVistaDialogo = findViewById(android.R.id.content);
        //###################################################################
    }

    private void VerOptionByRol(){

        PersistenceDataIteration persistenceDataIteration = new PersistenceDataIteration(mContext);
        LoginData loginData = persistenceDataIteration.LoginDataPersistence();

        if(loginData != null){
            Toast.makeText(mContext,loginData.getRol().getDescripcion(),Toast.LENGTH_LONG).show();
        }

    }

    private void setSingleEvent(GridLayout mainGrid) {
        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int id = 0;
                    switch(view.getId()) {
                        case R.id.cvConectividad:
                            id = 0;
                            break;
                       /* case R.id.cvConfiguracion:
                            id = 6;
                            break;*/
                        case R.id.cvGuiaEntrada:
                            id = 12;
                            break;
                        case R.id.cvGuiaDespacho:
                            id = 13;
                            break;
                        case R.id.cvEnvioMercaderia:
                            id = 14;
                            break;
                        case R.id.cvRecepcionMercaderia:
                            id = 15;
                            break;
                        case R.id.cvInventariotienda:
                            id = 16;
                            break;
                        case R.id.cvParametrizador:
                            id=17;
                            break;
                        case R.id.cvReposicion:
                            id=18;
                            break;
                        case R.id.cvTomaInventarioControl:
                            id=19;
                            break;
                        case R.id.cvTomaInventarioParticipante:
                            id=20;
                            break;


                    }
                    selectItem(id);

                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        if (mIsConnected) {
            menu.getItem(0).setVisible(true);
            if(mIsModeRFID){
                menu.getItem(0).setIcon(R.drawable.ic_rfid_green);

            }
            else {
                menu.getItem(0).setIcon(R.drawable.ic_qrcode_green);
            }

        }
        else
            menu.getItem(0).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id != 16908332){
            if (id == R.id.action_connected) {
                //Toast.makeText(this, getString(R.string.sled_connected_str), Toast.LENGTH_SHORT).show();
                loDialogo.gMostrarMensajeInformacion(loVistaDialogo, getString(R.string.sled_connected_str));
            }
            else if (id == R.id.action_home) {
                switchToHome();
            }
            else if(id == R.id.action_reg_intensidad){
                if(mReader.BT_GetConnectState() == SDConsts.BTConnectState.CONNECTED){
                    DialogPowerState();
                }
                else {
                    loDialogo.gMostrarMensajeInformacion(loVistaDialogo, "El Dispositivo esta desconectado de la pistola RFID");
                    //Toast.makeText(mContext,"El Dispositivo esta desconectado de la pistola RFID",Toast.LENGTH_SHORT).show();
                }
            }else  if(id == R.id.action_info){
                DialogInformacionApp();
            }

            /*if(id == R.id.){}*/

            if (mDrawerToggle.onOptionsItemSelected(item)) {
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
        return false;
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        switch (position) {
            case 0:
                //################# CONECTIVIDAD ################################

                /*ViewGroup viewGroup1 = findViewById(android.R.id.content);
                View dialogView1 = LayoutInflater.from(mContext).inflate(R.layout.dialogo_reposicion, viewGroup1, false);
                AlertDialog.Builder builder1 = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.myDialog));
                builder1.setView(dialogView1);
                final AlertDialog alertDialog1 = builder1.create();
                Button btnOk1 = dialogView1.findViewById(R.id.buttonOk);
                //TextView poLabelTexto = dialogView.findViewById(R.id.txtMensaje);
                Spinner mspinnerSeccionRW = (Spinner) dialogView1.findViewById(R.id.spinnerSeccionRW);
                String[] spinnerArraySeccion=null;
                spinnerArraySeccion = getResources().getStringArray(R.array.functions_array);

                ArrayAdapter<String> adapter1 =new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item,spinnerArraySeccion);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mspinnerSeccionRW.setAdapter(adapter1);



                btnOk1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog1.dismiss();
                    }
                });
                alertDialog1.show();*/

                if (mBTConnectivityFragment == null)
                    mBTConnectivityFragment = BTConnectivityFragment.newInstance();
                mCurrentFragment = mBTConnectivityFragment;

                break;
            case 1:

                if (mSDFragment == null)
                    mSDFragment = SDFragment.newInstance();
                mCurrentFragment = mSDFragment;
                break;
            case 2:
                if (mRFConfigFragment == null)
                    mRFConfigFragment = RFConfigFragment.newInstance();
                mCurrentFragment = mRFConfigFragment;
                break;
            case 3:
                if (mRFAccessFragment == null)
                    mRFAccessFragment = RFAccessFragment.newInstance();
                mCurrentFragment = mRFAccessFragment;
                break;
            case 4:
                if (mRFSelectionFragment == null)
                    mRFSelectionFragment = RFSelectionFragment.newInstance();
                mCurrentFragment = mRFSelectionFragment;
                break;
            case 5:
                if (mRapidFragment == null)
                    mRapidFragment = RapidFragment.newInstance();
                mCurrentFragment = mRapidFragment;
                break;
            case 6:
                //################# RF CONFIGURACION ########################
                if (mInventoryFragment == null)
                    mInventoryFragment = InventoryFragment.newInstance();
                mCurrentFragment = mInventoryFragment;
                break;
                /*if (mInventarioFragment == null)
                    mInventarioFragment = InventarioFragment.newInstance();
                mCurrentFragment = mInventarioFragment;
                break;*/
            case 7:
                if (mBCBarcodeFragment == null)
                    mBCBarcodeFragment = BCBarcodeFragment.newInstance();
                mCurrentFragment = mBCBarcodeFragment;
                break;
            case 8:
                if (mSBBarcodeFragment == null)
                    mSBBarcodeFragment = SBBarcodeFragment.newInstance();
                mCurrentFragment = mSBBarcodeFragment;
                break;
            case 9:
                if (mBatteryFragment == null)
                    mBatteryFragment = BatteryFragment.newInstance();
                mCurrentFragment = mBatteryFragment;
                break;
            case 10:
                if (mInfoFragment == null)
                    mInfoFragment = InfoFragment.newInstance();
                mCurrentFragment = mInfoFragment;
                break;

            case 11:
                if (mInventarioFragment == null)
                    mInventarioFragment = InventarioFragment.newInstance();
                mCurrentFragment = mInventarioFragment;
                break;

            case 12:
                //################# GUIA DE ENTRADA ###############################
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialogo_ingresotexto, viewGroup, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                Button btnOk = dialogView.findViewById(R.id.btnConfirmar);
                Button btnCancelar = dialogView.findViewById(R.id.btnCancelar);
                EditText poTxtTexto = dialogView.findViewById(R.id.txtTextoIngreso);
                TextView poLabelTexto = dialogView.findViewById(R.id.lblTextoLabel);
                poLabelTexto.setText("Por favor, ingrese un número de guía válido");


                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(poTxtTexto.getText().toString() !="")
                        {
                            lsNumeroOrden = poTxtTexto.getText().toString();
                            mWSParameters = getResources().getStringArray(R.array.WSparameter_GuiaEntradaOC);
                            clsSoapAsync tarea = new clsSoapAsync();
                            tarea.execute();
                        }else{
                            Toast.makeText(mContext, "Ingrese No. Orden de Compra....",Toast.LENGTH_SHORT).show();
                        }
                        alertDialog.dismiss();
                    }
                });
                btnCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                /*
                if (mEntryGuideCheckFragment == null){
                    mEntryGuideCheckFragment = EntryGuideCheckFragment.newInstance();
                }
                mCurrentFragment = mEntryGuideCheckFragment;
                */
                break;



            case 13:

                if(PARAM_LECTOR_RFID.getCodbodega().equals("01")){
                    if (mDespatchGuideReadFragment == null) {
                        mDespatchGuideReadFragment = DespatchGuideReadFragment.newInstance();
                    }
                    mCurrentFragment = mDespatchGuideReadFragment;
                }
                else {
                    loDialogo.gMostrarMensajeInformacion(loVistaDialogo,"La bodega donde se encuentra no puede realizar Guias de Despacho");
                }


                break;

            case 14:
                //##################### ENVIO DE MERCADERIA ###############################
                if (mShippingWareReadFragment == null)
                    mShippingWareReadFragment = ShippingWareReadFragment.newInstance();
                mCurrentFragment = mShippingWareReadFragment;
                break;
            case 15:
                /*if (mReceiveWareCheckFragment == null)
                    mReceiveWareCheckFragment = ReceiveWareCheckFragment.newInstance();
                mCurrentFragment = mReceiveWareCheckFragment;*/
                InvocarDialogQrScanner();
                break;
            case 16:
                if (mInventoryPerStoreFragment == null)
                    mInventoryPerStoreFragment = InventoryPerStoreFragment.newInstance();
                mCurrentFragment = mInventoryPerStoreFragment;
                break;

            case 17:
                if (mParameterFragment == null)
                    mParameterFragment = ParameterFragment.newInstance();
                mCurrentFragment = mParameterFragment;
                break;
            case 18:
                if (mReplenishmentWareFragment == null)
                    mReplenishmentWareFragment = ReplenishmentWareFragment.newInstance();
                mCurrentFragment = mReplenishmentWareFragment;
                break;
                /*if (mInventoryFragment == null)
                    mInventoryFragment = InventoryFragment.newInstance();
                mCurrentFragment = mInventoryFragment;
                break;*/
            case 19:
                if (mTakingInventoryControlFragment == null)
                    mTakingInventoryControlFragment = TakingInventoryControlFragment.newInstance();
                mCurrentFragment = mTakingInventoryControlFragment;
                break;
            case 20:
                if (mTakingInventoryParticipantFragment == null)
                    mTakingInventoryParticipantFragment = TakingInventoryParticipantFragment.newInstance();
                mCurrentFragment = mTakingInventoryParticipantFragment;
                break;
            default:
                return;
        }
        if(mCurrentFragment !=null)
        {
            PositionFrag = position;
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.replace(R.id.content, mCurrentFragment);
            ft.commit();
            setTitle(mFunctionsString[position]);
            mUILayout.setVisibility(View.GONE);
        }

    }

    @Override
    public void setTitle(CharSequence title) {
        getActionBar().setTitle(title);
    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onStart() {
        System.out.println("onStart!");
        // TODO Auto-generated method stub
        if (D) Log.d(TAG, " onStart");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        boolean openResult = false;
        mReader = BTReader.getReader(mContext, mMainHandler);
        if (mReader != null)
            openResult = mReader.SD_Open();
        if (openResult == SDConsts.RF_OPEN_SUCCESS) {
            Log.i(TAG, "Reader opened");
        }
        else if (openResult == SDConsts.RF_OPEN_FAIL)
            if (D) Log.e(TAG, "Reader open failed");

        updateConnectState();

        super.onStart();
    }

    @Override
    public void onResume() {
        System.out.println("onResume!");

        mReader = BTReader.getReader(mContext, mMainHandler);
        if (mReader != null && !(mReader.BT_GetConnectState() == SDConsts.BTConnectState.CONNECTED)) {
            mReader.BT_Connect(mDispositivoBTRfidSelect);
        }
        //mReader.SD_Open();


        // TODO Auto-generated method stub
        if (D) Log.d(TAG, "onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        System.out.println("onPause!");
        // TODO Auto-generated method stub
        if (D) Log.d(TAG, " onPause");
        super.onPause();
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    @Override
    protected void onStop() {
        System.out.println("onStop!");
        if (D) Log.d(TAG, " onStop");
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mReader = BTReader.getReader(mContext, mMainHandler);
        if (mReader != null && mReader.BT_GetConnectState() == SDConsts.BTConnectState.CONNECTED) {
            mReader.BT_Disconnect();
        }
        mReader.SD_Close();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (D) Log.d(TAG, "onRequestPermissionsResult");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mCurrentFragment != null)
                mCurrentFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onBackPressed() {
        if (mCurrentFragment != null) {
            OnBackPressedValidate();
        }
        else{

            //super.onBackPressed();
            //getFragmentManager().popBackStack();

            View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialogo_confirmacion, loVistaDialogo, false);
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.myDialog));
            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.create();
            Button btnOk = dialogView.findViewById(R.id.btnConfirmar);
            Button btnCancelar = dialogView.findViewById(R.id.btnCancelar);
            TextView poLabelTexto = dialogView.findViewById(R.id.lblTextoLabel);
            poLabelTexto.setText("¿Esta seguro que desea Cerrar Sesión?");


            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    ParamRfidIteration paramRfidIteration = new ParamRfidIteration(mContext);
                    ParamLogin paramLogin = new ParamLogin();
                    paramLogin.setEstado(0);
                    paramRfidIteration.RegistrarModificarParamLogin(paramLogin, true);

                    finish();

                }
            });
            btnCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();

            /*AlertDialog.Builder alerta = new AlertDialog.Builder(mContext);
            alerta.setMessage("Desea Cerrar Session...")
                    .setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {



                            // logica de estado de cierre de seccion manual...
                            ParamRfidIteration paramRfidIteration = new ParamRfidIteration(mContext);
                            ParamLogin paramLogin = new ParamLogin();
                            paramLogin.setEstado(0);
                            paramRfidIteration.RegistrarModificarParamLogin(paramLogin, true);

                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            AlertDialog title = alerta.create();
            title.setTitle("salida");
            title.show();*/

        }
    }


    private void OnBackPressedValidate(){

        boolean is3thFrag= false;
        boolean isFirstFragment = true;
        execFragmentTransaction = false;
        String noOrdenComprar = null;

        if(mCurrentFragment.equals(mBTConnectivityFragment)){
            mDispositivoBTRfidSelect = mBTConnectivityFragment.mDeviceSelect;
        }

        if (mCurrentFragment.equals(mEntryGuideCheckFragment) )
        {
            //mEntryGuideCheckFragment.CleanControls();
            if(mEntryGuideCheckFragment.mEntryGuideReadFragment != null){
                isFirstFragment = false;

                if(mEntryGuideCheckFragment.mEntryGuideReadFragment.mInvetoryLocatedFragment != null){

                    FragmentTransaction ft = mFragmentManager.beginTransaction();
                    ft.remove(mEntryGuideCheckFragment.mEntryGuideReadFragment.mInvetoryLocatedFragment);
                    ft.commit();
                    mEntryGuideCheckFragment.mEntryGuideReadFragment.mInvetoryLocatedFragment = null;
                    execFragmentTransaction = false;
                    is3thFrag = true;

                }
                else {
                    //isFirstFragment = false;
                    if (mEntryGuideCheckFragment != null) {

                        mEntryGuideCheckFragment.setValueOnBackPressedInvoque();
                        FragmentTransaction ft = mFragmentManager.beginTransaction();
                        ft.remove(mEntryGuideCheckFragment.mEntryGuideReadFragment);
                        ft.commit();
                        mEntryGuideCheckFragment.mEntryGuideReadFragment = null;

                        execFragmentTransaction = true;
                    }
                }
                //mDespatchGuideGenerateFragment
            }

        }
        if(mCurrentFragment.equals(mDespatchGuideReadFragment) && mDespatchGuideReadFragment.mDespatchGuideGenerateFragment != null)
        {
            isFirstFragment = false;
            //mDrawerLayout.closeDrawer(mDrawerList);
            if (mDespatchGuideReadFragment != null) {

                if(isBackForce){
                    execFragmentTransaction = true;
                    FragmentTransaction ft = mFragmentManager.beginTransaction();
                    ft.remove(mDespatchGuideReadFragment.mDespatchGuideGenerateFragment);
                    ft.commit();
                    mDespatchGuideReadFragment.mDespatchGuideGenerateFragment = null;
                }
                else {
                    View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialogo_confirmacion, loVistaDialogo, false);
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.myDialog));
                    builder.setView(dialogView);
                    final AlertDialog alertDialog = builder.create();
                    Button btnOk = dialogView.findViewById(R.id.btnConfirmar);
                    Button btnCancelar = dialogView.findViewById(R.id.btnCancelar);
                    TextView poLabelTexto = dialogView.findViewById(R.id.lblTextoLabel);
                    poLabelTexto.setText("Esta seguro salir de la pantalla");
                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            execFragmentTransaction = false;
                            FragmentTransaction ft = mFragmentManager.beginTransaction();
                            ft.remove(mDespatchGuideReadFragment.mDespatchGuideGenerateFragment);
                            ft.commit();
                            mDespatchGuideReadFragment.mDespatchGuideGenerateFragment = null;
                            ReplaceFragment();
                            alertDialog.dismiss();
                        }
                    });
                    btnCancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            execFragmentTransaction = false;
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }

            }


        }
        if(mCurrentFragment.equals(mShippingWareReadFragment) && mShippingWareReadFragment.mShippingWareGenerateFragment != null)
        {
            isFirstFragment = false;
            //mDrawerLayout.closeDrawer(mDrawerList);

            if (mShippingWareReadFragment != null) {

                if(isBackForce){
                    execFragmentTransaction = true;
                    FragmentTransaction ft = mFragmentManager.beginTransaction();
                    ft.remove(mShippingWareReadFragment.mShippingWareGenerateFragment);
                    ft.commit();
                    mShippingWareReadFragment.mShippingWareGenerateFragment = null;
                    //ReplaceFragment();

                }
                else {
                    View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialogo_confirmacion, loVistaDialogo, false);
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.myDialog));
                    builder.setView(dialogView);
                    final AlertDialog alertDialog = builder.create();
                    Button btnOk = dialogView.findViewById(R.id.btnConfirmar);
                    Button btnCancelar = dialogView.findViewById(R.id.btnCancelar);
                    TextView poLabelTexto = dialogView.findViewById(R.id.lblTextoLabel);
                    poLabelTexto.setText("Esta seguro salir de la pantalla");
                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            execFragmentTransaction = false;
                            FragmentTransaction ft = mFragmentManager.beginTransaction();
                            ft.remove(mShippingWareReadFragment.mShippingWareGenerateFragment);
                            ft.commit();
                            mShippingWareReadFragment.mShippingWareGenerateFragment = null;
                            ReplaceFragment();
                            alertDialog.dismiss();


                        }
                    });
                    btnCancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            execFragmentTransaction = false;
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
            }


        }

        /*if(mCurrentFragment.equals(mReceiveWareCheckFragment))
        {
            if(mReceiveWareCheckFragment.mReceiveWareInconsistencyFragment != null){
                isFirstFragment = false;
                if (mReceiveWareCheckFragment != null) {

                    FragmentTransaction ft = mFragmentManager.beginTransaction();
                    ft.remove(mReceiveWareCheckFragment.mReceiveWareInconsistencyFragment);
                    ft.commit();
                    mReceiveWareCheckFragment.mReceiveWareInconsistencyFragment = null;

                    execFragmentTransaction = true;
                }
            }

        }*/


        if(mCurrentFragment.equals(mReceiveWareCheckFragment))
        {
            if(mReceiveWareCheckFragment.mInvetoryLocatedFragment != null){
                isFirstFragment = false;
                if (mReceiveWareCheckFragment != null) {

                    FragmentTransaction ft = mFragmentManager.beginTransaction();
                    ft.remove(mReceiveWareCheckFragment.mInvetoryLocatedFragment);
                    ft.commit();
                    mReceiveWareCheckFragment.mInvetoryLocatedFragment = null;

                    execFragmentTransaction = true;
                }
            }

        }

        /*if(mCurrentFragment.equals(mReceiveWareCheckFragment.mReceiveWareInconsistencyFragment)){

            FragmentTransaction transaction = mFragmentManager.beginTransaction();

            FragmentTransaction currentFragment = mFragmentManager.findFragmentByTag(ReceiveWareInconsistencyFragment.)
            val oldFragment = supportFragmentManager.findFragmentByTag(oldFragmentTag)

            if (currentFragment.isVisible && oldFragment.isHidden) {
                transaction.hide(currentFragment).show(oldFragment)
            }

            transaction.commit()
        }*/

        /*if(mCurrentFragment.equals(mInventoryPerStoreFragment)){
            mInventoryPerStoreFragment.CleanControl();
        }*/

        if(mCurrentFragment.equals(mTakingInventoryControlFragment)){
            mTakingInventoryControlFragment.CleanControls();
        }

        //ShippingWareGenerateFragment
        if(isFirstFragment)
        {
            if(mCurrentFragment.equals(mReceiveWareCheckFragment)){
                if(mReceiveWareCheckFragment != null){
                    if(mReceiveWareCheckFragment.isOpenInconsistency){
                        mReceiveWareCheckFragment.SetVisibleWareCheck();

                        if(mReceiveWareCheckFragment.processFinalExitoso){
                            switchToHome();
                        }
                    }
                    else {
                        DialogConfirmacionBack("Esta seguro de regresar al menu principal, si lo hace perdera todo el trabajo realizado...");
                    }
                }
            }
            else {
                if(mCurrentFragment.equals(mInventoryPerStoreFragment)  ){

                    if(mInventoryPerStoreFragment.isOpenLayoutSale()){
                        mInventoryPerStoreFragment.backLayoutInfo();
                    }
                    else {
                        mInventoryPerStoreFragment.CleanControl();
                        switchToHome();
                    }
                }
                else {
                    switchToHome();
                }
            }

        }

        if(execFragmentTransaction)
        {
            FragmentTransaction ft1 = mFragmentManager.beginTransaction();

           /* if(mCurrentFragment.equals(mEntryGuideCheckFragment)){
                mEntryGuideCheckFragment = EntryGuideCheckFragment.newInstance();
                Bundle args = new Bundle();
                args.putString("NoOCompra", noOrdenComprar);
                mEntryGuideCheckFragment.setArguments(args);

                mCurrentFragment = mDespatchGuideReadFragment;
            }*/

            ft1.replace(R.id.content, mCurrentFragment);
            ft1.commit();
            //mDrawerList.setItemChecked(PositionFrag, true);
            setTitle(mFunctionsString[PositionFrag]);
            //mDrawerLayout.closeDrawer(mDrawerList);
            mUILayout.setVisibility(View.GONE);

        }

        if(is3thFrag){

            FragmentTransaction ft1 = mFragmentManager.beginTransaction();
            ft1.replace(R.id.content, mEntryGuideCheckFragment.mEntryGuideReadFragment);
            ft1.commit();
            setTitle(mFunctionsString[PositionFrag]);
            mUILayout.setVisibility(View.GONE);
        }
    }

    private void ReplaceFragment(){
        FragmentTransaction ft1 = mFragmentManager.beginTransaction();
        ft1.replace(R.id.content, mCurrentFragment);
        ft1.commit();
        setTitle(mFunctionsString[PositionFrag]);
        mUILayout.setVisibility(View.GONE);
    }


    private void DialogConfirmacionBack(String msj_){
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialogo_confirmacion, loVistaDialogo, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.myDialog));
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        Button btnOk = dialogView.findViewById(R.id.btnConfirmar);
        Button btnCancelar = dialogView.findViewById(R.id.btnCancelar);
        TextView poLabelTexto = dialogView.findViewById(R.id.lblTextoLabel);
        poLabelTexto.setText(msj_);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                switchToHome();

            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
        /*
        AlertDialog.Builder alerta = new AlertDialog.Builder(mContext);
        alerta.setMessage(msj_)
                .setCancelable(false)
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switchToHome();
                    }
                });
        alerta.show();
        */
    }


    /*private void AlertSessionClose(){

        AlertDialog.Builder alerta = new AlertDialog.Builder(mContext);
        alerta.setMessage("Desea Cerrar Session...")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        onBackPressed();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog title = alerta.create();
        title.setTitle("salida");
        title.show();

    }*/

    private static class MainHandler extends Handler {
        private final WeakReference<MainActivity> mExecutor;
        public MainHandler(MainActivity ac) {
            mExecutor = new WeakReference<>(ac);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity executor = mExecutor.get();
            if (executor != null) {
                executor.handleMessage(msg);
            }
        }
    }

    public void handleMessage(Message m) {
        if (D) Log.d(TAG, "mMainHandler");
        if (D) Log.d(TAG, "command = " + m.arg1 + " result = " + m.arg2 + " obj = data");
        switch (m.what) {
            case SDConsts.Msg.SDMsg:
                if(m.arg1 == SDConsts.SDCmdMsg.SLED_MODE_CHANGED){
                    updateConnectState();
                }
                if (m.arg1 == SDConsts.SDCmdMsg.SLED_HOTSWAP_STATE_CHANGED) {
                    if (m.arg2 == SDConsts.SDHotswapState.HOTSWAP_STATE)
                        Toast.makeText(mContext, "HOTSWAP STATE CHANGED = HOTSWAP_STATE", Toast.LENGTH_SHORT).show();
                    else if (m.arg2 == SDConsts.SDHotswapState.NORMAL_STATE)
                        Toast.makeText(mContext, "HOTSWAP STATE CHANGED = NORMAL_STATE", Toast.LENGTH_SHORT).show();
                }
                break;
            case SDConsts.Msg.RFMsg:
                break;
            /*case SDConsts.Msg.BCMsg:
                break;*/

            case SDConsts.Msg.BCMsg:

                if(isOptionRecepcionMercaderia){
                    if (m.arg1 == SDConsts.BCCmdMsg.BARCODE_READ) {

                        if (m.obj != null  && m.obj instanceof String) {

                            String qrcode = (String)m.obj;
                            if(qrcode != null && !qrcode.trim().isEmpty())
                            {
                                String[] parts = qrcode.trim().split(";");
                                qrcode = parts[0];
                                //Toast.makeText(mContext, "Codigo Scaneado: "+qrcode, Toast.LENGTH_LONG).show();
                                //String[] sqrcode
                                InvocateWSReceptionDetail(new String[]{qrcode});
                            }

                        }
                    }
                    else if (m.arg1 == SDConsts.BCCmdMsg.BARCODE_ERROR) {
                        if (m.arg2 == SDConsts.BCResult.LOW_BATTERY)
                            Toast.makeText(mContext, "Bateria Baja", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(mContext, "Error de lector de Barras", Toast.LENGTH_LONG).show();

                    }
                }

                break;
        }
    }

    private void switchToHome() {
        try {
            //mDrawerLayout.closeDrawer(mDrawerList);

            if (mCurrentFragment != null) {

                if(mCurrentFragment.equals(mBTConnectivityFragment)){
                    mDispositivoBTRfidSelect = mBTConnectivityFragment.mDeviceSelect;
                }

                if (mCurrentFragment.equals(mEntryGuideCheckFragment) )
                {
                    //mEntryGuideCheckFragment.CleanControls();
                    if(mEntryGuideCheckFragment.mEntryGuideReadFragment != null){
                        if (mEntryGuideCheckFragment != null) {
                            //mEntryGuideCheckFragment.CleanControls();
                            FragmentTransaction ft = mFragmentManager.beginTransaction();
                            ft.remove(mEntryGuideCheckFragment.mEntryGuideReadFragment);
                            ft.commit();
                            mEntryGuideCheckFragment.mEntryGuideReadFragment = null;
                        }
                    }

                }
                if(mCurrentFragment.equals(mDespatchGuideReadFragment) && mDespatchGuideReadFragment.mDespatchGuideGenerateFragment != null)
                {
                    FragmentTransaction ft = mFragmentManager.beginTransaction();
                    ft.remove(mDespatchGuideReadFragment.mDespatchGuideGenerateFragment);
                    ft.commit();
                    mDespatchGuideReadFragment.mDespatchGuideGenerateFragment = null;
                }

                if(mCurrentFragment.equals(mShippingWareReadFragment) && mShippingWareReadFragment.mShippingWareGenerateFragment != null)
                {
                    FragmentTransaction ft = mFragmentManager.beginTransaction();
                    ft.remove(mShippingWareReadFragment.mShippingWareGenerateFragment);
                    ft.commit();
                    mShippingWareReadFragment.mShippingWareGenerateFragment = null;

                }

                /*if(mCurrentFragment.equals(mReceiveWareCheckFragment))
                {
                    mReceiveWareCheckFragment.CleanControls();
                    *//*if(mReceiveWareCheckFragment.mReceiveWareInconsistencyFragment != null){
                        FragmentTransaction ft = mFragmentManager.beginTransaction();
                        ft.remove(mReceiveWareCheckFragment.mReceiveWareInconsistencyFragment);
                        ft.commit();
                        mReceiveWareCheckFragment.mReceiveWareInconsistencyFragment = null;
                    }*//*
                    FragmentTransaction ft = mFragmentManager.beginTransaction();
                    ft.remove(mReceiveWareCheckFragment);
                    ft.commit();
                }*/

                FragmentTransaction ft = mFragmentManager.beginTransaction();
                ft.remove(mCurrentFragment);
                ft.commit();
                mCurrentFragment = null;
                mReader = BTReader.getReader(mContext, mMainHandler);
            }
            setTitle(getString(R.string.app_name));
            if (mUILayout.getVisibility() != View.VISIBLE)
                mUILayout.setVisibility(View.VISIBLE);
        }
        catch (java.lang.IllegalStateException e) {
        }
        return;
    }

    private void updateConnectState() {
        if (mReader.BT_GetConnectState() == SDConsts.BTConnectState.CONNECTED)
        {
            int mode = mReader.SD_GetTriggerMode();
            if (mode == SDConsts.SDTriggerMode.RFID)
                mIsModeRFID = true;
            else
                mIsModeRFID = false;
            mIsConnected = true;
        }
        else {
            mIsConnected = false;
        }
        invalidateOptionsMenu();
    }

    private static class UpdateConnectHandler extends Handler {
        private final WeakReference<MainActivity> mExecutor;
        public UpdateConnectHandler(MainActivity ac) {
            mExecutor = new WeakReference<>(ac);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity executor = mExecutor.get();
            if (executor != null) {
                executor.handleUpdateConnectHandler(msg);
            }
        }
    }

    public void handleUpdateConnectHandler(Message m) {
        if (m.what == MSG_OPTION_CONNECT_STATE_CHANGED) {
            updateConnectState();
        }
        else if (m.what == MSG_BACK_PRESSED)
            switchToHome();
    }

    //################### Handler Personalizado #################################
    private static class BackHandler extends Handler {
        private final WeakReference<MainActivity> mExecutor;
        public BackHandler(MainActivity ac) {
            mExecutor = new WeakReference<>(ac);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity executor = mExecutor.get();
            if (executor != null) {
                executor.handleBackHandler(msg);
            }
        }
    }

    public void handleBackHandler(Message m) {
        if (m.what == 1) {
            isBackForce = true;
            OnBackPressedValidate();
            isBackForce = false;
        }
        /*else if (m.what == MSG_BACK_PRESSED)
            switchToHome();*/
    }


    //#################### PROCESO DE CONSUMIR WEB SERVICES #####################
    private  class clsSoapAsync extends AsyncTask<Void, Void, Void> {

        //WSparameter_GuiaEntradaOC
        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Void... voids) {
            rfidService.SOAP_ACTION_ =  mWSParameters[0];
            rfidService.METHOD_NAME_ =  mWSParameters[1];
            rfidService.NAMESPACE_ = mWSParameters[2];
            rfidService.URL_ = mWSParameters[3];
            ResposeEG = rfidService.WSGuiaEntradaByOrdenCompra(lsNumeroOrden);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);

            progressDialog.cancel();
            if(ResposeEG.estado   != null ){
                if(ResposeEG.estado.codigo.equals("00")){
                    if(ResposeEG.data_ != null && ResposeEG.data_.guias != null && ResposeEG.data_.guias.size() > 0){
                        //Aqui se muestra la siguiente actividad
                        /*if (mEntryGuideCheckFragment == null)
                        {
                            Bundle poArgumentos = new Bundle();
                            poArgumentos.putSerializable("objectResponse", ResposeEG);
                            mEntryGuideCheckFragment = EntryGuideCheckFragment.newInstance();
                            mEntryGuideCheckFragment.setArguments(poArgumentos);

                            poArgumentos.putString("NoOCompra", lsNumeroOrden);
                            mEntryGuideCheckFragment.setArguments(poArgumentos);
                        }*/

                        Bundle poArgumentos = new Bundle();
                        poArgumentos.putString("NoOCompra", lsNumeroOrden);
                        poArgumentos.putSerializable("objectResponse", ResposeEG);
                        mEntryGuideCheckFragment = EntryGuideCheckFragment.newInstance();
                        mEntryGuideCheckFragment.setArguments(poArgumentos);

                        mCurrentFragment = mEntryGuideCheckFragment;
                        if(mCurrentFragment !=null)
                        {
                            PositionFrag = 12;
                            FragmentTransaction ft = mFragmentManager.beginTransaction();
                            ft.replace(R.id.content, mCurrentFragment);
                            ft.commit();
                            //mDrawerList.setItemChecked(position, true);
                            setTitle(mFunctionsString[12]);
                            //mDrawerLayout.closeDrawer(mDrawerList);
                            mUILayout.setVisibility(View.GONE);
                        }


                    }
                    else {
                        loDialogo.gMostrarMensajeAdvertencia(loVistaDialogo, "No hay informacion que mostrar");
                        //Toast.makeText(mContext, "No hay informacion que mostrar..." , Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    loDialogo.gMostrarMensajeAdvertencia(loVistaDialogo, ResposeEG.estado.descripcion);
                    //Toast.makeText(mContext, ResposeEG.estado.descripcion , Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Cargando...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();

        }
    }


    //####################### RECEPCION MERCADERIA #############################
    private void InvocarDialogQrScanner(){

        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialogo_qr_scanner, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        Button btnOk = dialogView.findViewById(R.id.btnConfirmar);
        Button btnBusqManual = dialogView.findViewById(R.id.btnBusqManual);
        Button btnCancelar = dialogView.findViewById(R.id.btnCancelar);
        TextView poLabelTexto = dialogView.findViewById(R.id.lblTextoLabel);
        poLabelTexto.setText(R.string.INF_ModoIngresoRecMerc);

        isOptionRecepcionMercaderia = true;
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(mReader != null){
                    int mode = mReader.SD_GetTriggerMode();
                    if (mode == SDConsts.SDTriggerMode.RFID) {
                        mReader.SD_SetTriggerMode(SDConsts.SDTriggerMode.BARCODE);
                        mUpdateConnectHandler.obtainMessage(MSG_OPTION_CONNECT_STATE_CHANGED).sendToTarget();
                    }

                    mReader.BC_SetTriggerState(true);
                }
                alertDialog.dismiss();

            }
        });
        btnBusqManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOptionRecepcionMercaderia = false;
                //InvAlertIngMercManual();
                try {
                    alertDialog.dismiss();
                    InvAlertIngMercManual2();
                }
                catch (Exception ex){
                    loDialogo.gMostrarMensajeError(loVistaDialogo, ex.getMessage());
                }


            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOptionRecepcionMercaderia = false;
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void InvAlertIngMercManual(GenericSpinnerDto gBodega, GenericSpinnerDto gMovientos)
    {

        HashMap<Integer,String> spinnerMapBodegas = null;
        HashMap<Integer,String> spinnerMapTipos = null;
        AdapterHashMap adapterHashMap;

        ViewGroup viewGroup1 = findViewById(android.R.id.content);
        View dialogView1 = LayoutInflater.from(mContext).inflate(R.layout.dialog_receiveware, viewGroup1, false);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.myDialog));
        builder1.setView(dialogView1);
        final AlertDialog alertDialog1 = builder1.create();
        //TextView poLabelTexto = dialogView.findViewById(R.id.txtMensaje);
        Spinner mspinTipo = dialogView1.findViewById(R.id.spinTipo);
        Spinner mspinOrigen = dialogView1.findViewById(R.id.spinOrigen);
        EditText medAnio = dialogView1.findViewById(R.id.edAnio);
        EditText medNumero = dialogView1.findViewById(R.id.edNumero);

        Button btnAceptar = dialogView1.findViewById(R.id.btnDialogAceptar);
        Button btnCancelar = dialogView1.findViewById(R.id.btnDialogCancelar);
        Button btnLimpiar = dialogView1.findViewById(R.id.btnDialogLimpiar);

        int year= Calendar.getInstance().get(Calendar.YEAR);


        adapterHashMap =  getAdapaterSpinner(gMovientos);
        if(adapterHashMap != null){
            spinnerMapTipos = adapterHashMap.getHashMap();
            mspinTipo.setAdapter(adapterHashMap.getAdapter());
        }

        adapterHashMap =  getAdapaterSpinner(gBodega);
        if(adapterHashMap != null){
            spinnerMapBodegas = adapterHashMap.getHashMap();
            mspinOrigen.setAdapter(adapterHashMap.getAdapter());
        }

        HashMap<Integer, String> finalSpinnerMapBodegas = spinnerMapBodegas;
        HashMap<Integer, String> finalSpinnerMapTipos = spinnerMapTipos;

        mspinTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String tipoMov =  finalSpinnerMapTipos.get(i);
                if(tipoMov.equals("GDE")){
                    mspinOrigen.setEnabled(false);
                    mspinOrigen.setSelection(0);
                    medAnio.setText("");
                    medAnio.setEnabled(false);

                }else {
                    mspinOrigen.setEnabled(true);
                    medAnio.setText(year+"");
                    medAnio.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog1.dismiss();
                //Capturar los DATOS Y LLAMAR AL WS
                String lCodOrigen = finalSpinnerMapBodegas.get(mspinOrigen.getSelectedItemPosition());
                String lCodTipo = finalSpinnerMapTipos.get(mspinTipo.getSelectedItemPosition());
                String lAnio = medAnio.getText().toString();
                String lNumero = medNumero.getText().toString();
                if(!lCodTipo.equals("0")){
                    if(lCodTipo.equals("GDE") ){
                        if(!lNumero.trim().equals("")){
                            procesoNoExistQR(lCodOrigen,lCodTipo,lAnio,lNumero);
                            alertDialog1.dismiss();
                        }
                        else {
                            Toast.makeText(mContext, "Ingrese un numero de documento", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        if(!lCodOrigen.equals("0") && !lAnio.trim().equals("") && !lNumero.trim().equals("")){
                            procesoNoExistQR(lCodOrigen,lCodTipo,lAnio,lNumero);
                            alertDialog1.dismiss();
                        }
                        else {
                            Toast.makeText(mContext, "Seleccione y/o complete todos los campos", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                else {
                    Toast.makeText(mContext, "Seleccione un tipo de Movimiento", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog1.dismiss();
            }
        });

        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mspinTipo.setSelection(0);
                mspinOrigen.setSelection(0);
                medNumero.setText("");
            }
        });
        alertDialog1.show();
    }

    private void InvAlertIngMercManual2() {
        ProgressLoading();
        setSpinnersDialogReceiveWare();


    }

    private void setSpinnersDialogReceiveWare()
    {
        exWSTipoYBodegaOrigenAsync tipoYBodegaOrigenAsync = new exWSTipoYBodegaOrigenAsync();
        tipoYBodegaOrigenAsync.execute();

    }

    private AdapterHashMap getAdapaterSpinner(GenericSpinnerDto spinnerDto){

        String[] spinnerArray = new String[spinnerDto.getColeccion().size()];
        HashMap<Integer,String> spinnerMap = new HashMap<Integer, String>();
        AdapterHashMap adapterHashMap;

        int i = 0;

        for (DataSourceDto dto:spinnerDto.getColeccion()) {
            spinnerMap.put(i,dto.codigo);
            spinnerArray[i] = dto.descripcion;
            i++;
        }

        try {
            ArrayAdapter<String> adapter1 =new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item, spinnerArray);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapterHashMap = new AdapterHashMap();
            adapterHashMap.setAdapter(adapter1);
            adapterHashMap.setHashMap(spinnerMap);


        }
        catch (Exception ex)
        {
            adapterHashMap = null;
        }
        return adapterHashMap;

    }


    //####################### LOADING #############################
    private void ProgressLoading(){
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Cargando...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    //####################### ASYNC METODOS INVOQUE WS #############################

    private  class exWSTipoYBodegaOrigenAsync extends AsyncTask<Void, Void, Void> {

        String ToastMessage = "";
        private String[] mWSParameters;
        GenericSpinnerDto gDtoBodegas, gDtoMovimiento;

        @Override
        protected Void doInBackground(Void... voids) {

            if(gDtoBodegas == null)
            {
                mWSParameters = getResources().getStringArray(R.array.WSparameter_Bodegas);
                rfidService.SOAP_ACTION_ =  mWSParameters[0];
                rfidService.METHOD_NAME_ =  mWSParameters[1];
                rfidService.NAMESPACE_ = mWSParameters[2];
                rfidService.URL_ = mWSParameters[3];

                gDtoBodegas = rfidService.WSBodegasOrMotivosService(true, "REC", null);
            }
            if(gDtoMovimiento == null)
            {
                mWSParameters = getResources().getStringArray(R.array.WSparameter_TiposMovimiento);

                rfidService.SOAP_ACTION_ =  mWSParameters[0];
                rfidService.METHOD_NAME_ =  mWSParameters[1];
                rfidService.NAMESPACE_ = mWSParameters[2];
                rfidService.URL_ = mWSParameters[3];
                gDtoMovimiento = rfidService.WSTipoMovimientos();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.cancel();
            Validator validator = new Validator();
            ResponseVal responseVal =  validator.getValidateGenericDto(gDtoBodegas);
            ResponseVal responseVal1 =  validator.getValidateGenericDto(gDtoMovimiento);
            if(responseVal.isValidAccess() && responseVal1.isValidAccess()){
                if(responseVal.isFullCollection() && responseVal1.isFullCollection()){
                    InvAlertIngMercManual(gDtoBodegas, gDtoMovimiento);
                }
                else {
                    String msj = !responseVal.isFullCollection() ? "Lista de Bodegas" : "" + (!responseVal1.isFullCollection() ?" y Tipos de Movimientos vacios" : "vacios");
                    loDialogo.gMostrarMensajeInformacion(loVistaDialogo, msj);
                }
            }
            else {
                loDialogo.gMostrarMensajeError(loVistaDialogo, responseVal.getErrorMsg());
            }

        }

    }


    //##################### INVOCAR ALERTAS (DIALOGO)######################

    private void DialogInformacionApp()
    {
        View dialogView1 = LayoutInflater.from(mContext).inflate(R.layout.dialog_info_app, loVistaDialogo, false);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.myDialog));
        builder1.setView(dialogView1);
        final AlertDialog alertDialog1 = builder1.create();

        ParamRfidIteration paramRfidIteration = new ParamRfidIteration(mContext);
        ParamLectorRfid paramLectorRfid = paramRfidIteration.ConsultarParametros();
        PersistenceDataIteration persistenceDataIteration = new PersistenceDataIteration(mContext);
        LoginData loginData = persistenceDataIteration.LoginDataPersistence();

        TextView mtvUsuario = dialogView1.findViewById(R.id.tvUsuario);
        TextView mtvLocal = dialogView1.findViewById(R.id.tvLocal);
        TextView mtvTipoConexion = dialogView1.findViewById(R.id.tvTipoConexion);
        Button btnCerrar = dialogView1.findViewById(R.id.btnDialogCerrar);

        mtvTipoConexion.setText(paramLectorRfid.getEndpointSelect());
        mtvLocal.setText(paramLectorRfid.getDescBodega());
        mtvUsuario.setText(loginData.getUsuario().getDescripcion());

        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog1.dismiss();
            }
        });

        alertDialog1.show();
    }

    private void DialogPowerState()
    {
        int maxPower = 5;
        RFPower = mReader.RF_GetRadioPowerState();
        /*Toast.makeText(mContext,"getpowerstate: "+ RFPower+"", Toast.LENGTH_SHORT).show();*/
        View dialogView1 = LayoutInflater.from(mContext).inflate(R.layout.dialog_powerstate, loVistaDialogo, false);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.myDialog));
        builder1.setView(dialogView1);
        final AlertDialog alertDialog1 = builder1.create();


        SeekBar mSeekBarPower = dialogView1.findViewById(R.id.SeekBarPower);
        TextView mtvSeleccionado = dialogView1.findViewById(R.id.tvSeleccionado);

        /*TextView mtvpGED = dialogView1.findViewById(R.id.tvpGED);
        TextView mtvpERM = dialogView1.findViewById(R.id.tvpERM);
        TextView mtvpRepoInvPart = dialogView1.findViewById(R.id.tvpRepoInvPart);

        mtvpGED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RFPower = 17;
                ManagedSeekBarPower(mSeekBarPower, maxPower, mtvSeleccionado, 17);
            }
        });

        mtvpERM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RFPower = 17;
                ManagedSeekBarPower(mSeekBarPower, maxPower, mtvSeleccionado, 17 );
            }
        });
        mtvpRepoInvPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RFPower = 30;
                ManagedSeekBarPower(mSeekBarPower, maxPower, mtvSeleccionado, 30 );
            }
        });
*/

        Button btnAceptar = dialogView1.findViewById(R.id.btnDialogAceptar);
        Button btnCancelar = dialogView1.findViewById(R.id.btnDialogCancelar);

        final int mSeekBarPowerCorrection = 5;

        int realValueFromPersistentStorage = maxPower; //Get initial value from persistent storage, e.g., 100
        mSeekBarPower.setProgress(realValueFromPersistentStorage - mSeekBarPowerCorrection); //E.g., to convert real value of 100 to SeekBar value of 95.

        mSeekBarPower.setProgress(RFPower - maxPower);
        mtvSeleccionado.setText(RFPower+"");
        mSeekBarPower.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int val = mSeekBarPower.getProgress();
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                val = i + mSeekBarPowerCorrection;
                RFPower = val;
                mtvSeleccionado.setText(RFPower+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });


        /*alertDialog1.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Toast.makeText(mContext, "Modal Open", Toast.LENGTH_SHORT).show();

            }
        });
        alertDialog1.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Toast.makeText(mContext, "Modal close", Toast.LENGTH_SHORT).show();
            }
        });*/

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dialog.dismiss();
                mReader.RF_SetRadioPowerState(RFPower);
                alertDialog1.dismiss();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog1.dismiss();
            }
        });

        alertDialog1.show();
    }

    /*private void ManagedSeekBarPower(SeekBar mSeekBarPower, int maxPower, TextView mtvSeleccionado, int power){
        final int mSeekBarPowerCorrection = 5;

        int realValueFromPersistentStorage = maxPower; //Get initial value from persistent storage, e.g., 100
        mSeekBarPower.setProgress(realValueFromPersistentStorage - mSeekBarPowerCorrection); //E.g., to convert real value of 100 to SeekBar value of 95.

        mSeekBarPower.setProgress(power - maxPower);
        mtvSeleccionado.setText(power+"");
    }*/

    //RECEPCION DE MERCADERIA

    private void InvocateWSReceptionDetail(String[] qrcode)
    {
        exWSRecepcionDetailAsync recepcionDetailAsync = new exWSRecepcionDetailAsync();
        recepcionDetailAsync.execute(qrcode);
    }
    private void procesoNoExistQR(String pcodorigen, String pcodigotipo, String panio, String pnumero)
    {
        //Toast.makeText(mContext, pcodorigen+";"+pcodigotipo+";"+panio+";"+pnumero, Toast.LENGTH_LONG).show();
        InvocateWSReceptionDetail(new String[]{pcodorigen,pcodigotipo,panio,pnumero} );
    }


    private  class exWSRecepcionDetailAsync extends AsyncTask<String[], Void, Void> {

        ReceiveWareDetail receiveWareDetail;
        String[] mWSparameterRecepcionDet = getResources().getStringArray(R.array.WSparameter_RecepcionDetalle);

        @Override
        protected Void doInBackground(String[]... params) {

            rfidService.SOAP_ACTION_ =  mWSparameterRecepcionDet[0];
            rfidService.METHOD_NAME_ =  mWSparameterRecepcionDet[1];
            rfidService.NAMESPACE_ = mWSparameterRecepcionDet[2];
            rfidService.URL_ = mWSparameterRecepcionDet[3];

            if(params[0].length == 4 )
            {
                receiveWareDetail = rfidService.WSRecepcionMercaderiaDetail( null,true, params[0]);
            }
            else {
                String[] doc_origen = params[0];
                receiveWareDetail = rfidService.WSRecepcionMercaderiaDetail( doc_origen[0],false,null);
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            progressDialog.cancel();
            Validator validator = new Validator();

            ResponseVal responseVal =  validator.getValidateDataSourceDto(receiveWareDetail.getEstado());

            if(responseVal.isValidAccess()){

               /* if (mReceiveWareCheckFragment == null)
                    mReceiveWareCheckFragment = mReceiveWareCheckFragment.newInstance();
                try {
                    Bundle args = new Bundle();
                    args.putSerializable("receiveWareDetail", receiveWareDetail);
                    mReceiveWareCheckFragment.setArguments(args);

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content, mReceiveWareCheckFragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                catch (Exception e){
                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                }*/


                /*if (mReceiveWareCheckFragment == null)
                {
                    Bundle poArgumentos = new Bundle();
                    poArgumentos.putSerializable("receiveWareDetail", receiveWareDetail);
                    mReceiveWareCheckFragment = ReceiveWareCheckFragment.newInstance();
                    mReceiveWareCheckFragment.setArguments(poArgumentos);
                }*/

                Bundle poArgumentos = new Bundle();
                poArgumentos.putSerializable("receiveWareDetail", receiveWareDetail);
                mReceiveWareCheckFragment = ReceiveWareCheckFragment.newInstance();
                mReceiveWareCheckFragment.setArguments(poArgumentos);

                mCurrentFragment = mReceiveWareCheckFragment;
                if(mCurrentFragment !=null)
                {
                    PositionFrag = 15;
                    FragmentTransaction ft = mFragmentManager.beginTransaction();
                    ft.replace(R.id.content, mCurrentFragment);
                    ft.commit();
                    //mDrawerList.setItemChecked(position, true);
                    setTitle(mFunctionsString[15]);
                    //mDrawerLayout.closeDrawer(mDrawerList);
                    mUILayout.setVisibility(View.GONE);
                }

            }
            else {
                //Toast.makeText(mContext, responseVal.getErrorMsg() , Toast.LENGTH_LONG).show();
                loDialogo.gMostrarMensajeError(loVistaDialogo,responseVal.getErrorMsg());
            }
        }

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            ProgressLoading();

        }
    }

}