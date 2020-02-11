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

import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.LoginData;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ParamLogin;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.fragment.*;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.fragmentvct.DespatchGuideReadFragment;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.fragmentvct.EntryGuideCheckFragment;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.fragmentvct.EntryGuideRead2Fragment;
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
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.clsMensaje;
import co.kr.bluebird.sled.BTReader;
import co.kr.bluebird.sled.SDConsts;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Point;
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
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final boolean D = Constants.MAIN_D;

    public static final int MSG_OPTION_CONNECT_STATE_CHANGED = 0;

    public static final int MSG_BACK_PRESSED = 2;

    private String[] mFunctionsString;

    private DrawerLayout mDrawerLayout;

    //private ListView mDrawerList;

    private ActionBarDrawerToggle mDrawerToggle;

    private BTReader mReader;

    private Context mContext;

    private FragmentManager mFragmentManager;

    private boolean mIsConnected;

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
    private CardView mcvParametrizador,mcvConectividad,mcvConfiguracion,mcvGuiaEntrada,mcvGuiaDespacho, mcvEnvioMercaderia,mcvRecepcionMercaderia,mcvInventariotienda, mcvReposicion, mcvTomaInventarioControl,mcvTomaInventarioParticipante;
    private GridLayout mainGrid;

    public  String pato;
    private  int PositionFrag;
    private String mDispositivoBTRfidSelect ;
    private final MainHandler mMainHandler = new MainHandler(this);
    public final UpdateConnectHandler mUpdateConnectHandler = new UpdateConnectHandler(this);
    private RfidService rfidService;
    private co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EntryGuide ResposeEG;
    private String[] mWSParameters;
    private String lsNumeroOrden;
    private clsMensaje loDialogo;
    private ViewGroup loVistaDialogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (D) Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        rfidService = new RfidService(mContext);
        mlayouttvParam = (LinearLayout) findViewById(R.id.layouttvParam);
        mtvParam1 = (TextView) findViewById(R.id.tvParam1);

        mcvParametrizador  = (CardView)findViewById(R.id.cvParametrizador);
        mcvConectividad = (CardView)findViewById(R.id.cvConectividad);
        mcvConfiguracion  = (CardView)findViewById(R.id.cvConfiguracion);
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
            mcvConfiguracion.setVisibility(View.GONE);
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
                        case R.id.cvConfiguracion:
                            id = 6;
                            break;
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
        if (mIsConnected)
            menu.getItem(0).setVisible(true);
        else
            menu.getItem(0).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id != 16908332){
            if (id == R.id.action_connected) {
                Toast.makeText(this, getString(R.string.sled_connected_str), Toast.LENGTH_SHORT).show();
            }
            else if (id == R.id.action_home) {
                switchToHome();
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
                if (mDespatchGuideReadFragment == null) {
                    mDespatchGuideReadFragment = DespatchGuideReadFragment.newInstance();

                }
                mCurrentFragment = mDespatchGuideReadFragment;

                break;

            case 14:
                //##################### ENVIO DE MERCADERIA ###############################
                if (mShippingWareReadFragment == null)
                    mShippingWareReadFragment = ShippingWareReadFragment.newInstance();
                mCurrentFragment = mShippingWareReadFragment;
                break;
            case 15:
                if (mReceiveWareCheckFragment == null)
                    mReceiveWareCheckFragment = ReceiveWareCheckFragment.newInstance();
                mCurrentFragment = mReceiveWareCheckFragment;
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
            /*
            AlertDialog.Builder alerta = new AlertDialog.Builder(mContext);
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
            title.show();
            */
        }
    }


    private void OnBackPressedValidate(){

        boolean isFirstFragment = true;
        boolean execFragmentTransaction = false;
        String noOrdenComprar = null;

        if(mCurrentFragment.equals(mBTConnectivityFragment)){
            mDispositivoBTRfidSelect = mBTConnectivityFragment.mDeviceSelect;
        }


        if (mCurrentFragment.equals(mEntryGuideCheckFragment) ) //(mEntryGuideCheckFragment instanceof EntryGuideCheckFragment)
        {
            //mEntryGuideCheckFragment.mEntryGuideReadFragment = (EntryGuideRead2Fragment)mCurrentFragment;
            if(mEntryGuideCheckFragment.mEntryGuideReadFragment != null){
                //if(mEntryGuideCheckFragment.mEntryGuideReadFragment == null)
                    //mEntryGuideCheckFragment = (EntryGuideCheckFragment) mCurrentFragment;

                noOrdenComprar = mEntryGuideCheckFragment.mEntryGuideReadFragment.getOrdenCompra();
                ResposeEG = mEntryGuideCheckFragment.mEntryGuideReadFragment.getResposeEG();
                isFirstFragment = false;
                //mDrawerLayout.closeDrawer(mDrawerList);
                if (mEntryGuideCheckFragment != null) {
                    //mEntryGuideCheckFragment = EntryGuideCheckFragment.newInstance();
                    Bundle poArgumentos = new Bundle();
                    poArgumentos.putSerializable("objectResponse", ResposeEG);
                    mEntryGuideCheckFragment.setArguments(poArgumentos);
                    poArgumentos.putString("NoOCompra", noOrdenComprar);
                    mEntryGuideCheckFragment.setArguments(poArgumentos);


                    FragmentTransaction ft = mFragmentManager.beginTransaction();
                    //ft.remove(mEntryGuideCheckFragment.mEntryGuideReadFragment);
                    ft.commit();
                    mEntryGuideCheckFragment.mEntryGuideReadFragment = null;

                    execFragmentTransaction = true;
                }
                //mDespatchGuideGenerateFragment
            }

        }
        if(mCurrentFragment.equals(mDespatchGuideReadFragment) && mDespatchGuideReadFragment.mDespatchGuideGenerateFragment != null)
        {
            isFirstFragment = false;
            //mDrawerLayout.closeDrawer(mDrawerList);
            if (mDespatchGuideReadFragment != null) {
                FragmentTransaction ft = mFragmentManager.beginTransaction();
                ft.remove(mDespatchGuideReadFragment.mDespatchGuideGenerateFragment);
                ft.commit();
                mDespatchGuideReadFragment.mDespatchGuideGenerateFragment = null;

                execFragmentTransaction = true;
            }
        }
        if(mCurrentFragment.equals(mShippingWareReadFragment) && mShippingWareReadFragment.mShippingWareGenerateFragment != null)
        {
            isFirstFragment = false;
            //mDrawerLayout.closeDrawer(mDrawerList);
            if (mShippingWareReadFragment != null) {
                FragmentTransaction ft = mFragmentManager.beginTransaction();
                ft.remove(mShippingWareReadFragment.mShippingWareGenerateFragment);
                ft.commit();
                mShippingWareReadFragment.mShippingWareGenerateFragment = null;

                execFragmentTransaction = true;
            }
        }

        if(mCurrentFragment.equals(mReceiveWareCheckFragment))
        {
            //mReceiveWareCheckFragment.CleanControls();
            if(mReceiveWareCheckFragment.mReceiveWareInconsistencyFragment != null){
                isFirstFragment = false;
                //mDrawerLayout.closeDrawer(mDrawerList);
                if (mReceiveWareCheckFragment != null) {

                    FragmentTransaction ft = mFragmentManager.beginTransaction();
                    ft.remove(mReceiveWareCheckFragment.mReceiveWareInconsistencyFragment);
                    ft.commit();
                    mReceiveWareCheckFragment.mReceiveWareInconsistencyFragment = null;

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
    }

    public void  syh(){

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
                if (m.arg1 == SDConsts.SDCmdMsg.SLED_HOTSWAP_STATE_CHANGED) {
                    if (m.arg2 == SDConsts.SDHotswapState.HOTSWAP_STATE)
                        Toast.makeText(mContext, "HOTSWAP STATE CHANGED = HOTSWAP_STATE", Toast.LENGTH_SHORT).show();
                    else if (m.arg2 == SDConsts.SDHotswapState.NORMAL_STATE)
                        Toast.makeText(mContext, "HOTSWAP STATE CHANGED = NORMAL_STATE", Toast.LENGTH_SHORT).show();
                }
                break;
            case SDConsts.Msg.RFMsg:
                break;
            case SDConsts.Msg.BCMsg:
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

                if(mCurrentFragment.equals(mReceiveWareCheckFragment))
                {
                    mReceiveWareCheckFragment.CleanControls();
                    if(mReceiveWareCheckFragment.mReceiveWareInconsistencyFragment != null){
                        /*mReceiveWareCheckFragment.CleanControls();*/
                        FragmentTransaction ft = mFragmentManager.beginTransaction();
                        ft.remove(mReceiveWareCheckFragment.mReceiveWareInconsistencyFragment);
                        ft.commit();
                        mReceiveWareCheckFragment.mReceiveWareInconsistencyFragment = null;
                    }
                }

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
            mIsConnected = true;
        else
            mIsConnected = false;
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
                        if (mEntryGuideCheckFragment == null)
                        {
                            Bundle poArgumentos = new Bundle();
                            poArgumentos.putSerializable("objectResponse", ResposeEG);
                            mEntryGuideCheckFragment = EntryGuideCheckFragment.newInstance();
                            mEntryGuideCheckFragment.setArguments(poArgumentos);

                            poArgumentos.putString("NoOCompra", lsNumeroOrden);
                            mEntryGuideCheckFragment.setArguments(poArgumentos);
                        }
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
    //############################################################################
}