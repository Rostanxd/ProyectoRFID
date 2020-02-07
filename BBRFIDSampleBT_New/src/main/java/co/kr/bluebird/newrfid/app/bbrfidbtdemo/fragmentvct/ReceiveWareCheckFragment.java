package co.kr.bluebird.newrfid.app.bbrfidbtdemo.fragmentvct;

import java.io.Serializable;

import co.kr.bluebird.newrfid.app.bbrfidbtdemo.Constants;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.DataSourceDto;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.DespatchGuide;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EGDetailGroupCod;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EGDetailResponse;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EGProcesado;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EGTagsResponseItem;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.GenericSpinnerDto;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ReceiveWareDetail;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ResponseVal;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.TagNoRead;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.item;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.fileutil.FileManager;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.control.ListItem;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.MainActivity;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.R;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.control.TagListAdapter;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.permission.PermissionHelper;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.service.RfidService;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.stopwatch.StopwatchService;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.CustomListAdapterReceiveWare;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.Validator;
import co.kr.bluebird.sled.BTReader;
import co.kr.bluebird.sled.SDConsts;
import co.kr.bluebird.sled.SelectionCriterias;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReceiveWareCheckFragment extends Fragment {


    public ReceiveWareCheckFragment() {
        // Required empty public constructor
    }


   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.entryguideread2_frag, container, false);
    }*/

    private static final String TAG = ReceiveWareCheckFragment.class.getSimpleName();

    private static final boolean D = Constants.INV_D;

    private StopwatchService mStopwatchSvc;

    private TagListAdapter mAdapter;

    //private ListView mRfidList;

    private ScrollView mscrollReceiveWare;

    private TextView mBatteryText;

    private TextView mTimerText;

    private TextView mCountText;

    private TextView mSpeedCountText;

    private TextView mAvrSpeedCountTest;

    private TextView mtvCantItemLeidos, mtvCantTotal;

    private Button mClearButton,mbtnConfirmar;

    private Button msearch_imgbutton, mscanQR_imgbutton, mclean_imgbtn, mStopInvenButton, mInvenButton;

    private Switch mTurboSwitch;

    private Switch mRssiSwitch;

    private Switch mFilterSwitch;

    private Switch mSoundSwitch;

    private Switch mMaskSwitch;

    private Switch mToggleSwitch;

    /*private Switch mPCSwitch;*/

    private Switch mFileSwitch;

    private ProgressBar mProgressBar;

    private BTReader mReader;

    private Context mContext, mDialogContext;

    private boolean mTagFilter = true;

    private boolean mSoundPlay = true;

    private boolean mMask = false;

    private boolean mInventory = false;

    private boolean mIsTurbo = true;

    private boolean mToggle = false;

    private boolean mIgnorePC = true;

    private boolean mRssi = false;

    private boolean mFile = false;

    private Handler mOptionHandler;

    private SoundPool mSoundPool;

    private int mSoundId;

    private float mSoundVolume;

    private boolean mSoundFileLoadState;

    private SoundTask mSoundTask;

    private double mOldTotalCount = 0;

    private double mOldSec = 0;

    private Fragment mFragment;

    private FileManager mFileManager;

    private String mLocateTag;

    private String mLocateEPC;

    private int mLocateStartPos;

    private LinearLayout mLocateLayout;

    private LinearLayout mListLayout;

    private ProgressBar mTagLocateProgress;

    private int mLocateValue;

    private ImageButton mBackButton;

    private TextView mLocateTv, mtag_locate_grupo1,mtag_locate_grupo2,mtag_locate_grupo3;

    private boolean mLocate;

    private TimerTask mLocateTimerTask;

    private Timer mClearLocateTimer;

    private Spinner mSessionSpin;
    private ArrayAdapter<CharSequence> mSessionChar;

    private Spinner mSelFlagSpin;
    private ArrayAdapter<CharSequence> mSelFlagChar;
    //private int mCurrentPower;

    private int mTickCount = 0;

    private CopyOnWriteArrayList<ListItem> ArrLis;
    private RfidService rfidService;
    private ReceiveWareDetail receiveWareDetail;

    private UpdateStopwatchHandler mUpdateStopwatchHandler = new UpdateStopwatchHandler(this);


    private WebServiceHandler mWebServiceHandler = new WebServiceHandler(this);


    private InventoryHandler mInventoryHandler = new InventoryHandler(this);

    private Handler handlerStart ;
    private ProgressBar mprogress1;
    //private EditText metDocOrigenRW,metDocDestinoRW,metMotivoRW ;
    private TextView metDocOrigenRW,metDocDestinoRW,metMotivoRW ;
    private String  doc_origen = "";
    //private TableLayout tblItemDif,tblItemsNoEnc;
    //private TableLayout tblItemDif;
    private EGProcesado egProcesado;

    private  ListView mlv_itemsEnc, mlv_tagsInconsistentes;
    //private boolean first ;
    private List<String> ListEpcRead;
    private EGDetailResponse egDetailResponse = null;
    private LinearLayout mlayoutHeaderRWC;

    private String[] spinnerArrayBodegas = null;
    private HashMap<Integer,String> spinnerMapBodegas = null;

    private String[] spinnerArrayTipos = null;
    private HashMap<Integer,String> spinnerMapTipos = null;

    private Spinner mspinTipo ;
    private Spinner mspinOrigen ;

    private ProgressDialog progressDialog;

    private  int RFPower = 17;
    private ImageButton mibtnPotencia;
    private boolean lectureHasPc = false;


    private EGTagsResponseItem itemEncontradoSeleccinado;
    private GenericSpinnerDto spinnerDto = null;
    private GenericSpinnerDto genericSpinnerDto = null;
    private String[] mWSParametersBodega, mWSParametersTipoMov, mWSparameterRecepcionDet, mWSParameterRecepcionCompara, mWSParameter_RecepcionProcesar;


    //private boolean isRunningRead;

    public ReceiveWareInconsistencyFragment mReceiveWareInconsistencyFragment;

    // Controles Recepcion Inconsistency
    private EditText EtDocOrigenRWI, EtDocDestinoRWI, EtDocMotivoRWI;
    private ListView mlv_itemsInconsistentes;
    private Button mbtnConfirmarRW, mbtnAddNota;
    private TextView metNotaRW;
    private LinearLayout mlayoutHeaderRWI, mlayaoutWareCheck, mlayaoutInconsistency;
    private ImageButton mBackButtonInconsistency;
    public boolean isOpenInconsistency = false;
    private List<EGTagsResponseItem> listInc = null;

    public static ReceiveWareCheckFragment newInstance() {
        return new ReceiveWareCheckFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        if (D) Log.d(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.receivewarecheck_frag, container, false);

        mContext = inflater.getContext();

        mFragment = this;

        mOptionHandler = ((MainActivity)getActivity()).mUpdateConnectHandler;

        mlayoutHeaderRWC = (LinearLayout) v.findViewById(R.id.layoutHeaderRWC);
        mLocateLayout = (LinearLayout)v.findViewById(R.id.tag_locate_container);

        mListLayout = (LinearLayout)v.findViewById(R.id.tag_list_container);

        mLocateTv = (TextView)v.findViewById(R.id.tag_locate_text);

        mtag_locate_grupo1 = (TextView)v.findViewById(R.id.tag_locate_grupo1);
        mtag_locate_grupo2 = (TextView)v.findViewById(R.id.tag_locate_grupo2);
        mtag_locate_grupo3 = (TextView)v.findViewById(R.id.tag_locate_grupo3);

        mTagLocateProgress = (ProgressBar)v.findViewById(R.id.tag_locate_progress);

        mBackButton = (ImageButton)v.findViewById(R.id.back_button);
        mBackButton.setOnClickListener(sledListener);

        mTimerText = (TextView)v.findViewById(R.id.timer_text);

        mCountText = (TextView)v.findViewById(R.id.count_text);

        mSpeedCountText = (TextView)v.findViewById(R.id.speed_count_text);

        mAvrSpeedCountTest = (TextView)v.findViewById(R.id.speed_avr_count_text);

      /*  tblItemDif = (TableLayout)v.findViewById(R.id.tablelayoutItemsDif);
        tblItemsNoEnc = (TableLayout)v.findViewById(R.id.tablelayoutItemsNoEnc);*/


        Activity activity = getActivity();

        if (activity != null) {
            String speedCountStr = activity.getString(R.string.speed_count_str) + activity.getString(R.string.speed_postfix_str);
            mSpeedCountText.setText(speedCountStr);
            mAvrSpeedCountTest.setText(speedCountStr);
        }

        mBatteryText = (TextView)v.findViewById(R.id.battery_text);

        mTurboSwitch = (Switch)v.findViewById(R.id.turbo_switch);

        mRssiSwitch = (Switch)v.findViewById(R.id.rssi_switch);

        mFilterSwitch = (Switch)v.findViewById(R.id.filter_switch);

        mSoundSwitch = (Switch)v.findViewById(R.id.sound_switch);

        mMaskSwitch = (Switch)v.findViewById(R.id.mask_switch);

        mToggleSwitch = (Switch)v.findViewById(R.id.toggle_switch);

        /*mPCSwitch = (Switch)v.findViewById(R.id.pc_switch);*/

        mFileSwitch = (Switch)v.findViewById(R.id.file_switch);

        mClearButton = (Button)v.findViewById(R.id.clear_button);
        mClearButton.setOnClickListener(clearButtonListener);

        /*mInvenButton = (Button)v.findViewById(R.id.inven_button);
        mInvenButton.setOnClickListener(sledListener);*/

        Drawable myIcon = null;
        ColorFilter filter = null;

        myIcon = getResources().getDrawable( R.drawable.materialplay );
        filter = new LightingColorFilter( Color.BLACK, Color.WHITE);
        myIcon.setColorFilter(filter);

        mInvenButton = (Button)v.findViewById(R.id.inven_imgbutton);
        mInvenButton.setCompoundDrawablesWithIntrinsicBounds( myIcon, null, null, null);
        mInvenButton.setOnClickListener(sledListener);



        myIcon = getResources().getDrawable( R.drawable.materialstop );
        filter = new LightingColorFilter( Color.BLACK, Color.WHITE);
        myIcon.setColorFilter(filter);

        mStopInvenButton = (Button)v.findViewById(R.id.stop_inven_imgbutton);
        mStopInvenButton.setCompoundDrawablesWithIntrinsicBounds( myIcon, null, null, null);
        mStopInvenButton.setOnClickListener(sledListener);


        myIcon = getResources().getDrawable( R.drawable.materialdelete );
        filter = new LightingColorFilter( Color.BLACK, Color.WHITE);
        myIcon.setColorFilter(filter);

        mclean_imgbtn = (Button)v.findViewById(R.id.clean_imgbtn);
        mclean_imgbtn.setCompoundDrawablesWithIntrinsicBounds( myIcon, null, null, null);
        mclean_imgbtn.setOnClickListener(sledListener);

        myIcon = getResources().getDrawable( R.drawable.materialsearch );
        filter = new LightingColorFilter( Color.BLACK, Color.WHITE);
        myIcon.setColorFilter(filter);

        msearch_imgbutton = (Button)v.findViewById(R.id.search_imgbutton);
        msearch_imgbutton.setCompoundDrawablesWithIntrinsicBounds( myIcon, null, null, null);
        msearch_imgbutton.setOnClickListener(sledListener);

        /*myIcon = getResources().getDrawable( R.drawable.qrcode18px );
        filter = new LightingColorFilter( Color.BLACK, Color.WHITE);
        myIcon.setColorFilter(filter);*/
        mscanQR_imgbutton = (Button)v.findViewById(R.id.scanQR_imgbutton);
        /* mscanQR_imgbutton.setCompoundDrawablesWithIntrinsicBounds( myIcon, null, null, null);*/
        //mscanQR_imgbutton.setOnClickListener(sledListener);




        /*myIcon = getResources().getDrawable( R.drawable.materialcheck18 );*/
        myIcon = getResources().getDrawable( R.drawable.materialcompare18 );
        filter = new LightingColorFilter( Color.BLACK, Color.WHITE);
        myIcon.setColorFilter(filter);
        mbtnConfirmar = (Button)v.findViewById(R.id.btnConfirmar);
        mbtnConfirmar.setCompoundDrawablesWithIntrinsicBounds( myIcon, null, null, null);
        mbtnConfirmar.setOnClickListener(sledListener);

        ActivateButtons(false);

        mProgressBar = (ProgressBar)v.findViewById(R.id.timer_progress);
        mProgressBar.setVisibility(View.INVISIBLE);


        mibtnPotencia = (ImageButton) v.findViewById(R.id.ibtnPotencia);
        mibtnPotencia.setOnClickListener(onClickDialogPotencia);

        mSessionSpin = (Spinner)v.findViewById(R.id.session_spin);
        mSessionChar = ArrayAdapter.createFromResource(mContext, R.array.session_array,
                android.R.layout.simple_spinner_dropdown_item);
        mSessionSpin.setAdapter(mSessionChar);

        mSelFlagSpin = (Spinner)v.findViewById(R.id.sel_flag_spin);
        mSelFlagChar = ArrayAdapter.createFromResource(mContext, R.array.sel_flag_array,
                android.R.layout.simple_spinner_dropdown_item);
        mSelFlagSpin.setAdapter(mSelFlagChar);

        mAdapter = new TagListAdapter(mContext);
        //mRfidList.setAdapter(mAdapter);
        mlv_itemsEnc = (ListView)v.findViewById(R.id.lv_itemsEnc);
        mlv_tagsInconsistentes = (ListView)v.findViewById(R.id.lv_tagsInconsistentes);
        mscrollReceiveWare = (ScrollView) v.findViewById(R.id.scrollReceiveWare);

        InicializarControlRInconsistency(v);

        ScrollManager();

        //first = true;

        mprogress1 = (ProgressBar)v.findViewById(R.id.pbLeidos);
        bindStopwatchSvc();

        metDocOrigenRW = (TextView) v.findViewById(R.id.tvDocOrigenRW);
        metDocDestinoRW = (TextView) v.findViewById(R.id.tvDocDestinoRW) ;
        metMotivoRW = (TextView) v.findViewById(R.id.tvMotivoRW) ;

        mtvCantItemLeidos = (TextView) v.findViewById(R.id.tvCantItemLeidos);
        mtvCantTotal = (TextView)v.findViewById(R.id.tvCantTotal);


        /* String NoOrdenCompra = getArguments() != null ? getArguments().getString("NoOCompra") : "0";*/
        //metDocOrigenRW.setText(NoOrdenCompra);
        rfidService = new RfidService(mContext);
        mWSparameterRecepcionDet = getResources().getStringArray(R.array.WSparameter_RecepcionDetalle);
        receiveWareDetail = null;

        return v;
    }


    private void InicializarControlRInconsistency(View v){

        EtDocOrigenRWI = (EditText)v.findViewById(R.id.etDocOrigenRW);
        EtDocDestinoRWI = (EditText)v.findViewById(R.id.etDocDestinoRW);
        EtDocMotivoRWI = (EditText)v.findViewById(R.id.etMotivoRW);
        mlv_itemsInconsistentes = (ListView) v.findViewById(R.id.lv_itemsInconsistentes);
        mbtnConfirmarRW = (Button) v.findViewById(R.id.btnConfirmarRW);
        mbtnAddNota = (Button) v.findViewById(R.id.btnAddNota);
        metNotaRW = (TextView) v.findViewById(R.id.tvNotaRW);

        mlayoutHeaderRWI = (LinearLayout) v.findViewById(R.id.layoutHeaderRWI);
        mlayoutHeaderRWI.setVisibility(View.GONE);


        mlayaoutWareCheck = (LinearLayout) v.findViewById(R.id.layaoutWareCheck);
        mlayaoutInconsistency = (LinearLayout) v.findViewById(R.id.layaoutInconsistency);

        mlayaoutWareCheck.setVisibility(View.VISIBLE);
        mlayaoutInconsistency.setVisibility(View.GONE);

        isOpenInconsistency = false;


        Drawable myIcon = null;
        ColorFilter filter = null;

        myIcon = getResources().getDrawable( R.drawable.materialcheck );
        filter = new LightingColorFilter( Color.BLACK, Color.WHITE);
        myIcon.setColorFilter(filter);

        mbtnConfirmarRW.setCompoundDrawablesWithIntrinsicBounds( null, null, myIcon, null);

        myIcon = getResources().getDrawable( R.drawable.materialadd18 );
        filter = new LightingColorFilter( Color.BLACK, Color.WHITE);
        myIcon.setColorFilter(filter);

        mBackButtonInconsistency = (ImageButton)v.findViewById(R.id.back_button_inconsistency);
        mBackButtonInconsistency.setOnClickListener(btnBackInconsistency);

        mbtnAddNota.setCompoundDrawablesWithIntrinsicBounds( myIcon, null, null, null);

        mbtnAddNota.setOnClickListener(btnAddNotaOnClick);

        mbtnConfirmarRW.setOnClickListener(btnConfirmarRWIOnClick);
    }

    private View.OnClickListener btnBackInconsistency = new OnClickListener() {
        @Override
        public void onClick(View view) {
            mlayaoutInconsistency.setVisibility(View.GONE);
            mlayaoutWareCheck.setVisibility(View.VISIBLE);

        }
    } ;

    public void SetVisibleWareCheck(){

        mlayaoutInconsistency.setVisibility(View.GONE);
        isOpenInconsistency = false;
        mlayaoutWareCheck.setVisibility(View.VISIBLE);
    }

    private View.OnClickListener btnAddNotaOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            DialogNota();
        }
    };

    private void DialogNota(){
        final Dialog dialog = new Dialog(mContext);
        //dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_input_text);
        mDialogContext = dialog.getContext();

        dialog.setTitle("Ingrese una nota");

        Button mdialogBtnAceptar = (Button) dialog.findViewById(R.id.btnDialogAceptar);
        EditText medNota = (EditText) dialog.findViewById(R.id.edNota_);

        mdialogBtnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                metNotaRW.setText(medNota.getText().toString());
                dialog.dismiss();

            }
        });
        dialog.show();
    }


    private void DrawButtonMode(){

        Drawable myIcon = null;
        ColorFilter filter = null;

        int mode = mReader.SD_GetTriggerMode();
        if (mode == SDConsts.SDTriggerMode.RFID){
            myIcon = getResources().getDrawable( R.drawable.moderfid18px );
        }
        if (mode == SDConsts.SDTriggerMode.BARCODE){
            myIcon = getResources().getDrawable( R.drawable.qrcode18px );
        }
        filter = new LightingColorFilter( Color.BLACK, Color.WHITE);
        myIcon.setColorFilter(filter);
        mscanQR_imgbutton.setCompoundDrawablesWithIntrinsicBounds( myIcon, null, null, null);
        mscanQR_imgbutton.setOnClickListener(sledListener);
    }

    private OnItemSelectedListener sessionListener = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position > 0)
                Toast.makeText(mContext, "If you want to use session 1 ~ 3 value, toggle off", Toast.LENGTH_SHORT).show();
            mReader.RF_SetSession(position);
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private OnItemSelectedListener selFlagListener = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mReader.RF_SetSelectionFlag(position + 1);
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private OnClickListener onClickDialogPotencia = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if(mReader.BT_GetConnectState() == SDConsts.BTConnectState.CONNECTED){
                DialogPowerState();
            }
            else {
                Toast.makeText(mContext,"El Dispositivo esta desconectado de la pistola RFID",Toast.LENGTH_SHORT).show();
            }

        }
    };

    private void DialogPowerState() {

        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_powerstate);
        int maxPower = 5;

        //getValueSBar = 17;


        SeekBar mSeekBarPower = (SeekBar) dialog.findViewById(R.id.SeekBarPower);
        TextView mtvSelect = (TextView) dialog.findViewById(R.id.tvSeleccionado);

        Button mdialogBtnAceptar = (Button) dialog.findViewById(R.id.btnDialogAceptar);

        final int mSeekBarPowerCorrection = 5;

        int realValueFromPersistentStorage = maxPower; //Get initial value from persistent storage, e.g., 100
        mSeekBarPower.setProgress(realValueFromPersistentStorage - mSeekBarPowerCorrection); //E.g., to convert real value of 100 to SeekBar value of 95.

        mSeekBarPower.setProgress(RFPower - maxPower);
        mtvSelect.setText(RFPower+"");
        mSeekBarPower.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int val = mSeekBarPower.getProgress();
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                val = i + mSeekBarPowerCorrection;
                RFPower = val;
                mtvSelect.setText(RFPower+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });


        mdialogBtnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dialog.dismiss();
                mReader.RF_SetRadioPowerState(RFPower);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

   /* private AdapterView.OnItemClickListener listItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ListItem i = (ListItem)mRfidList.getItemAtPosition(position);
            mLocateTag = i.mUt;
            mLocateStartPos = (i.mHasPc ? 0 : 4);
            if (i.mHasPc)
                mLocateEPC = mLocateTag.substring(4, mLocateTag.length());
            else
                mLocateEPC = mLocateTag;

            AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
            alert.setTitle(getString(R.string.locating_str));
            alert.setMessage(getString(R.string.want_tracking_str));

            alert.setPositiveButton(getString(R.string.yes_str), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    SelectionCriterias s = new SelectionCriterias();
                    s.makeCriteria(SelectionCriterias.SCMemType.EPC, mLocateTag,
                            mLocateStartPos, mLocateTag.length() * 4,
                            SelectionCriterias.SCActionType.ASLINVA_DSLINVB);
                    mReader.RF_SetSelection(s);
                    switchLayout(false);
                    mLocateTv.setText(mLocateTag);
                }
            });
            alert.setNegativeButton(getString(R.string.no_str) ,new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });
            alert.show();
        }
    };*/

    private void switchLayout(boolean showList) {
        mLocate = !showList;
//        if (mLocate)
//            mReader.RF_SetRssiTrackingState(SDConsts.RFRssi.ON);
        if (showList) {
            mListLayout.setVisibility(View.VISIBLE);
            mLocateLayout.setVisibility(View.GONE);
            //mInvenButton.setText(R.string.inven_str);
            //mStopInvenButton.setText(R.string.stop_inven_str);
        }
        else {
            mTagLocateProgress.setProgress(0);
            mListLayout.setVisibility(View.GONE);
            mLocateLayout.setVisibility(View.VISIBLE);
            //mInvenButton.setText(R.string.track_str);
            //mStopInvenButton.setText(R.string.stop_track_str);
        }
    }

    private void ScrollManager()
    {
        //mlv_itemsEnc mlv_tagsInconsistentes mscrollReceiveWare
        mlv_itemsEnc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mscrollReceiveWare.requestDisallowInterceptTouchEvent(true);
                int action = event.getActionMasked();
                switch (action) {
                    case MotionEvent.ACTION_UP:
                        mscrollReceiveWare.requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

        mlv_tagsInconsistentes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mscrollReceiveWare.requestDisallowInterceptTouchEvent(true);
                int action = event.getActionMasked();
                switch (action) {
                    case MotionEvent.ACTION_UP:
                        mscrollReceiveWare.requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });
        //mlv_itemsInconsistentes

        mlv_itemsInconsistentes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mscrollReceiveWare.requestDisallowInterceptTouchEvent(true);
                int action = event.getActionMasked();
                switch (action) {
                    case MotionEvent.ACTION_UP:
                        mscrollReceiveWare.requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });
    }

    private void createSoundPool() {
        boolean b = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            b = createNewSoundPool();
        else
            b = createOldSoundPool();
        if (b) {
            AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
            float actVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            mSoundVolume = actVolume / maxVolume;
            SoundLoadListener();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private boolean createNewSoundPool(){
        AudioAttributes attributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
        mSoundPool = new SoundPool.Builder().setAudioAttributes(attributes).setMaxStreams(5).build();
        if (mSoundPool != null)
            return true;
        return false;
    }

    @SuppressWarnings("deprecation")
    private boolean createOldSoundPool(){
        mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        if (mSoundPool != null)
            return true;
        return false;
    }

    private void SoundLoadListener() {
        if (mSoundPool != null) {
            mSoundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    // TODO Auto-generated method stub
                    mSoundFileLoadState = true;
                }
            });
            mSoundId = mSoundPool.load(mContext, R.raw.beep, 1);
        }
    }

    private class SoundTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            if (mLocate)
                mTagLocateProgress.setProgress(mLocateValue);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            if (mSoundPlay) {
                try {
                    if (mSoundFileLoadState) {
                        if (mSoundPool != null) {
                            mSoundPool.play(mSoundId, mSoundVolume, mSoundVolume, 0, 0, (48000.0f / 44100.0f));
                            try {
                                Thread.sleep(25);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (java.lang.NullPointerException e) {
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    };

    @Override
    public void onStart() {
        if (D) Log.d(TAG, "onStart");
        mSoundFileLoadState = false;

        createSoundPool();

        mOldTotalCount = 0;

        mOldSec = 0;

        mReader = BTReader.getReader(mContext, mInventoryHandler);
        if (mReader != null && mReader.BT_GetConnectState() == SDConsts.BTConnectState.CONNECTED) {
            enableControl(true);
            DrawButtonMode();
            updateButtonState();
            mReader.RF_SetRadioPowerState(RFPower);
        }
        else
            enableControl(false);

        mLocate = false;

        mInventory = false;

        addCheckListener();
        super.onStart();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        if (D) Log.d(TAG, "onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        if (D) Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        if (D) Log.d(TAG, "onStop");
        mReader.RF_StopInventory();
        pauseStopwatch();
        mInventory = false;
        if (mSoundPool != null)
            mSoundPool.release();
        mSoundFileLoadState = false;
        if (mFileManager != null) {
            mFileManager.closeFile();
            mFileManager = null;
        }
        stopStopwatch();

        unbindStopwatchSvc();
        super.onStop();
    }

    private OnClickListener clearButtonListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (D) Log.d(TAG, "clearButtonListener");
            clearAll();
        }
    };

    private void clearAll() {
        if (!mInventory) {
            mAdapter.removeAllItem();

            updateCountText();

            stopStopwatch();

            mOldTotalCount = 0;

            mOldSec = 0;

            updateSpeedCountText();

            updateAvrSpeedCountText();

            Activity activity = getActivity();
            if (activity != null)
                mSpeedCountText.setText("0" + activity.getString(R.string.speed_postfix_str));
        }
    }

    public void CleanControls()
    {
        metDocOrigenRW.setText("");
        metDocDestinoRW.setText("");
        metMotivoRW.setText("");
        mtvCantItemLeidos.setText("0");
        mtvCantTotal.setText("0");
        mprogress1.setProgress(0);
        mlv_itemsEnc.setAdapter(null);
        mlv_tagsInconsistentes.setAdapter(null);

        mlayoutHeaderRWC.setVisibility(View.GONE);
        ArrLis = null;

        egDetailResponse = null;
        ActivateButtons(false);
    }

    private OnClickListener sledListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (D) Log.d(TAG, "stopwatchListener");

            int id = v.getId();
            int ret;
            switch (id) {
                case R.id.back_button:
                    ret = mReader.RF_StopInventory();
                    if (ret == SDConsts.RFResult.SUCCESS || ret == SDConsts.RFResult.NOT_INVENTORY_STATE) {
                        mInventory = false;
                        enableControl(!mInventory);
                        pauseStopwatch();
                    } else if (ret == SDConsts.RFResult.STOP_FAILED_TRY_AGAIN)
                        Toast.makeText(mContext, "Stop Inventory failed", Toast.LENGTH_SHORT).show();

                    switchLayout(true);
                    mLocateTv.setText("");
                    mLocateTag = null;
                    clearAll();
                    break;
                case R.id.inven_imgbutton:
                    if (!mInventory) {
                        //clearAll();
                        openFile();
                        if (mLocate) {
                            //mCurrentPower = mReader.RF_GetRadioPowerState();
                            ret = mReader.RF_PerformInventoryForLocating(mLocateEPC);
                        } else
                            ret = mReader.RF_PerformInventoryWithLocating(mIsTurbo, mMask, mIgnorePC);
                        //ret = mReader.RF_READ(SDConsts.RFMemType.EPC, 1, 7, "00000000", false);
                        if (ret == SDConsts.RFResult.SUCCESS) {
                            //isRunningRead = true;
                            /*handlerStart = new Handler();
                            mprogress1.setMax(5);
                            Thread hilo = new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    while (isRunningRead == false){
                                        handlerStart.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                //txtCount.setText(i+" %");
                                                mprogress1.setProgress( Integer.parseInt(mCountText.getText().toString()) );
                                            }
                                        });
                                        try {
                                            Thread.sleep(100);
                                        }catch (InterruptedException e){
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                            hilo.start();*/

                            btnConfirmarManagement(true);

                            mInvenButton.setEnabled(false);
                            mInvenButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D5D7D6")));

                            mStopInvenButton.setEnabled(true);
                            mStopInvenButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EF3C10")));

                            mbtnConfirmar.setEnabled(false);
                            mbtnConfirmar.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D5D7D6")));

                            startStopwatch();
                            mInventory = true;
                            enableControl(!mInventory);
                        } else if (ret == SDConsts.RFResult.MODE_ERROR)
                            Toast.makeText(mContext, "Start Inventory failed, Please check RFR900 MODE", Toast.LENGTH_SHORT).show();
                        else if (ret == SDConsts.RFResult.LOW_BATTERY)
                            Toast.makeText(mContext, "Start Inventory failed, LOW_BATTERY", Toast.LENGTH_SHORT).show();
                        else
                        if (D) Log.d(TAG, "Start Inventory failed");
                    }
                    break;

                case R.id.stop_inven_imgbutton:
                    ret = mReader.RF_StopInventory();
                    if (ret == SDConsts.RFResult.SUCCESS || ret == SDConsts.RFResult.NOT_INVENTORY_STATE) {
                        mInventory = false;
                        enableControl(!mInventory);
                        pauseStopwatch();
                        mbtnConfirmar.setEnabled(true);
                        mbtnConfirmar.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1895C0")));
                        mInvenButton.setEnabled(true);
                        mInvenButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1F9375")));
                        lectureHasPc = !mIgnorePC;
                    } else if (ret == SDConsts.RFResult.STOP_FAILED_TRY_AGAIN)
                        Toast.makeText(mContext, "Stop Inventory failed", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.clean_imgbtn:
                    DialogCleanControls();
                    break;


                case  R.id.search_imgbutton:
                    callDialogInsertParameterManual();
                    break;
                case  R.id.scanQR_imgbutton:
                    scanQR_imgbutton_Extrated();

                    break;
                case R.id.btnConfirmar:
                    btnConfirmar_extrated();
                    break;

            }
        }
    };

    private void scanQR_imgbutton_Extrated(){
        DialogProcesar();
    }

    private void DialogProcesar()
    {

        AlertDialog.Builder alerta = new AlertDialog.Builder(mContext);
        int mode = mReader.SD_GetTriggerMode();

        if(mode == SDConsts.SDTriggerMode.BARCODE)
        {
            alerta.setMessage("El Modo Barcode, ya esta establecido, apunte el Handheld hacia el QR, luego presionar el gatillo de la pistola o el botón -Iniciar- hasta obtener la lectura QR...")
                    .setCancelable(false)
                    .setPositiveButton("Establecer modo RFID", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mReader.SD_SetTriggerMode(SDConsts.SDTriggerMode.RFID);
                            Drawable myIcon  = getResources().getDrawable( R.drawable.moderfid18px );
                            ColorFilter filter = new LightingColorFilter( Color.BLACK, Color.WHITE);
                            myIcon.setColorFilter(filter);
                            mscanQR_imgbutton.setCompoundDrawablesWithIntrinsicBounds( myIcon, null, null, null);
                            Toast.makeText(mContext,"Se establecio modo RFID",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Continuar Modo Barcode", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            alerta.show();
        }
        else {
            alerta.setMessage("El Handheld cambiara a modo de lectura Barcode, luego usted debera apuntar el dispositivo hacia el Codigo QR y presionar el gatillo hasta obtener la lectura QR...")
                    .setCancelable(false)
                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int ret;
                            int mode = mReader.SD_GetTriggerMode();
                            if (mode == SDConsts.SDTriggerMode.RFID){
                                ret = mReader.SD_SetTriggerMode(SDConsts.SDTriggerMode.BARCODE);

                                Drawable myIcon  = getResources().getDrawable( R.drawable.qrcode18px );
                                ColorFilter filter = new LightingColorFilter( Color.BLACK, Color.WHITE);
                                myIcon.setColorFilter(filter);
                                mscanQR_imgbutton.setCompoundDrawablesWithIntrinsicBounds( myIcon, null, null, null);

                                Toast.makeText(mContext,"Se establecio modo lectura Barcode", Toast.LENGTH_SHORT).show();
                            }
                            if (D) Log.d(TAG, "set barcode cur mode = " + mode + " ret = " + ret);
                        }
                    });

            alerta.show();
        }


    }

    private void btnConfirmar_extrated()
    {
        /*if(egDetailResponse == null){
            getListEpcsRead();
            if(ListEpcRead.size() > 0){
                DialogConfirmar();
            }
            else {
                Toast.makeText(mContext,"No hay Etiquetas con que comparar...",Toast.LENGTH_SHORT).show();
            }
        }
        else {
            // llamar a la otra pantalla...
            //InvocateReceiveWareInconsistence();

            InvocateReceiveWareInconsistence1();
        }*/

        if(mbtnConfirmar.getText().equals("Comparar")){
            getListEpcsRead();
            if(ListEpcRead.size() > 0){
                DialogConfirmar();
            }
            else {
                Toast.makeText(mContext,"No hay Etiquetas con que comparar...",Toast.LENGTH_SHORT).show();
            }
        }
        if(mbtnConfirmar.getText().equals("Confirmar")){
            InvocateReceiveWareInconsistence1();
        }

    }
    private void InvocateReceiveWareInconsistence()
    {

        if (mReceiveWareInconsistencyFragment == null)
            mReceiveWareInconsistencyFragment = mReceiveWareInconsistencyFragment.newInstance();

        ArrayList<String> epcsLeidos = mAdapter.listTagReadEpc();

        if (epcsLeidos.size() > 0) {
            Bundle args = new Bundle();
            List<EGTagsResponseItem> listInc = ListItemsIncosistentes();
            args.putSerializable("items", (Serializable) listInc);
            args.putString("DocOrigen", metDocOrigenRW.getText().toString());
            args.putString("DocDestino",metDocDestinoRW.getText().toString());
            args.putString("Motivo",metMotivoRW.getText().toString());

            mReceiveWareInconsistencyFragment.setArguments(args);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.content, mReceiveWareInconsistencyFragment);
            ft.addToBackStack(null);
            ft.commit();

        }
    }

    private void InvocateReceiveWareInconsistence1()
    {

        EtDocOrigenRWI.setText(metDocOrigenRW.getText().toString());
        EtDocDestinoRWI.setText(metDocDestinoRW.getText().toString());
        EtDocMotivoRWI.setText(metMotivoRW.getText().toString());
        //mWSParameter_RecepcionProcesar =  getResources().getStringArray(R.array.WSparameter_RecepcionProcesar);
        ProcesarLvItemsInc();

        mlayaoutWareCheck.setVisibility(View.GONE);
        mlayaoutInconsistency.setVisibility(View.VISIBLE);
        isOpenInconsistency = true;
    }

    private void ProcesarLvItemsInc()
    {
        listInc = ListItemsIncosistentes();
        mlv_itemsInconsistentes.setAdapter(null);
        if(listInc != null && listInc.size() > 0)
        {
            mlayoutHeaderRWI.setVisibility(View.VISIBLE);
            mlv_itemsInconsistentes.setAdapter(new CustomListAdapterReceiveWare(mContext, listInc, true));
        }
    }

    private List<EGTagsResponseItem> ListItemsIncosistentes()
    {
        List<EGTagsResponseItem> responseItemList = null;
        int cant_total = 0;
        if(egDetailResponse.getItems().size() > 0)
        {
            responseItemList = new ArrayList<EGTagsResponseItem>();
            for (EGTagsResponseItem item :egDetailResponse.getItems()) {
                /*cant_total = item.getCantidadLeidos() + item.getCantidadNoLeidos();*/

                /*if(cant_total != item.getCantidadLeidos()){
                    responseItemList.add(item);
                }*/

                if(item.getCantidadDoc() != item.getCantidadLeidos()){
                    responseItemList.add(item);
                }
            }

        }
        return responseItemList;
    }
    private void getListEpcsRead()
    {
        ArrLis = mAdapter.listaEtiquetasLeidas();
        ListEpcRead = new ArrayList<>();
        for (ListItem item : ArrLis){
            ListEpcRead.add(item.mUt);
        }
    }
    private void callDialogInsertParameterManual() {


        ProgressLoading();
        SetSpinners();


    }

    private void  postCallWSTiposMovOrigen(){
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_receiveware);
        mDialogContext = dialog.getContext();

        dialog.setTitle("Ingrese datos del Documento");

        Button mdialogBtnAceptar = (Button) dialog.findViewById(R.id.btnDialogAceptar);
        mspinTipo = (Spinner) dialog.findViewById(R.id.spinTipo);
        mspinOrigen = (Spinner) dialog.findViewById(R.id.spinOrigen);
        EditText medAnio = (EditText) dialog.findViewById(R.id.edAnio);
        EditText medNumero = (EditText) dialog.findViewById(R.id.edNumero);

        //Date currentTime = Calendar.getInstance().get

        int year= Calendar.getInstance().get(Calendar.YEAR);

        medAnio.setText(year+"");

        mspinTipo.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String tipoMov =  spinnerMapTipos.get(i);
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



        mdialogBtnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //dialog.findViewById(R.id.)
                //dialog.dismiss();

                //Capturar los DATOS Y LLAMAR AL WS
                String lCodOrigen = spinnerMapBodegas.get(mspinOrigen.getSelectedItemPosition());
                String lCodTipo = spinnerMapTipos.get(mspinTipo.getSelectedItemPosition());
                String lAnio = medAnio.getText().toString();
                String lNumero = medNumero.getText().toString();
                if(!lCodTipo.equals("0")){
                    if(lCodTipo.equals("GDE") ){
                        if(!lNumero.trim().equals("")){
                            procesoNoExistQR(lCodOrigen,lCodTipo,lAnio,lNumero);
                            dialog.dismiss();
                        }
                        else {
                            Toast.makeText(mContext, "Ingrese un numero de documento", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        if(!lCodOrigen.equals("0") && !lAnio.trim().equals("") && !lNumero.trim().equals("")){
                            procesoNoExistQR(lCodOrigen,lCodTipo,lAnio,lNumero);
                            dialog.dismiss();
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
        dialog.show();
    }

    private void procesoNoExistQR(String pcodorigen, String pcodigotipo, String panio, String pnumero)
    {
        //Toast.makeText(mContext, pcodorigen+";"+pcodigotipo+";"+panio+";"+pnumero, Toast.LENGTH_LONG).show();
        InvocateWSReceptionDetail(pcodorigen,pcodigotipo,panio,pnumero);
    }
    private void SetSpinners()
    {
        String exMsj = "";
        try {
            mWSParametersBodega = getResources().getStringArray(R.array.WSparameter_Bodegas);
            mWSParametersTipoMov = getResources().getStringArray(R.array.WSparameter_TiposMovimiento);

            exWSTipoYBodegaOrigenAsync tipoYBodegaOrigenAsync = new exWSTipoYBodegaOrigenAsync();
            tipoYBodegaOrigenAsync.execute();
        }
        catch (Exception ex){
            exMsj = ex.getMessage();
        }

    }

    private  class exWSTipoYBodegaOrigenAsync extends AsyncTask<Void, Void, Void> {

        //DespatchGuide despatchGuideBodega = null;
        //GenericSpinnerDto genericSpinnerDto = null;
        String ToastMessage = "";


        @Override
        protected Void doInBackground(Void... voids) {
            //despatchGuide = rfidService.GuiaDespachoBodegasService();

            if(spinnerDto == null)
            {
                rfidService.SOAP_ACTION_ =  mWSParametersBodega[0];
                rfidService.METHOD_NAME_ =  mWSParametersBodega[1];
                rfidService.NAMESPACE_ = mWSParametersBodega[2];
                rfidService.URL_ = mWSParametersBodega[3];

                spinnerDto = rfidService.WSBodegasOrMotivosService(true, "REC", null);
            }
            if(genericSpinnerDto == null)
            {
                //WSparameter_TiposMovimiento

                rfidService.SOAP_ACTION_ =  mWSParametersTipoMov[0];
                rfidService.METHOD_NAME_ =  mWSParametersTipoMov[1];
                rfidService.NAMESPACE_ = mWSParametersTipoMov[2];
                rfidService.URL_ = mWSParametersTipoMov[3];
                genericSpinnerDto = rfidService.WSTipoMovimientos();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);

            progressDialog.cancel();
            postCallWSTiposMovOrigen();

            if(spinnerDto != null && spinnerDto.getEstado() != null && spinnerDto.getEstado().getCodigo().equals("00")){

                if(spinnerDto.getColeccion() != null && spinnerDto.getColeccion().size() > 0){
                    SpinnerBodegaComplete();
                }
                else {
                    ToastMessage = "Bodegas de Origen vacias";
                }
            }
            else {
                Toast.makeText(mContext, spinnerDto.getEstado().getDescripcion(), Toast.LENGTH_SHORT).show();
            }


            if(genericSpinnerDto != null && genericSpinnerDto.getEstado() != null && genericSpinnerDto.getEstado().getCodigo().equals("00")){
                if(genericSpinnerDto.getColeccion() != null && genericSpinnerDto.getColeccion().size() > 0){
                    SpinnerTipoComplete();
                }
                else {
                    if (ToastMessage.isEmpty()) {
                        ToastMessage = "Tipos";
                    } else {
                        ToastMessage = ToastMessage + " y Tipos";
                    }
                }
            }
            else {
                Toast.makeText(mContext, genericSpinnerDto.getEstado().getDescripcion(), Toast.LENGTH_SHORT).show();
            }


            if(!ToastMessage.isEmpty()){
                Toast.makeText(mContext, "Llamada a Servicio Erroneo "+ToastMessage , Toast.LENGTH_SHORT).show();
            }
        }

        /*@Override
        protected void onPreExecute() {
            //super.onPreExecute();
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Cargando...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();

        }*/
    }

    private void ProgressLoading(){
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Cargando...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }
    private void SpinnerBodegaComplete(){
        //spinnerArrayBodegas spinnerMapBodegas mspinTipo mspinOrigen

        spinnerArrayBodegas = new String[spinnerDto.getColeccion().size()];
        spinnerMapBodegas = new HashMap<Integer, String>();

        int i = 0;
        String exmsj = "";
        for (DataSourceDto dto:spinnerDto.getColeccion()) {
            spinnerMapBodegas.put(i,dto.codigo);
            spinnerArrayBodegas[i] = dto.descripcion;
            i++;
        }

        try {
            ArrayAdapter<String> adapter1 =new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item, spinnerArrayBodegas);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mspinOrigen.setAdapter(adapter1);
        }
        catch (Exception ex)
        {
            Toast.makeText(mContext, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void SpinnerTipoComplete(){
        //spinnerArrayTipos spinnerMapTipos
        spinnerArrayTipos = new String[genericSpinnerDto.getColeccion().size()];
        spinnerMapTipos = new HashMap<Integer, String>();

        int i = 0;
        for (DataSourceDto dto:genericSpinnerDto.getColeccion()) {
            spinnerMapTipos.put(i,dto.codigo);
            spinnerArrayTipos[i] = dto.descripcion;
            i++;
        }


        ArrayAdapter<String> adapter1 =new ArrayAdapter<String>(mDialogContext,android.R.layout.simple_spinner_item, spinnerArrayTipos);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mspinTipo.setAdapter(adapter1);
    }

    /*private  class executeSoapAsync extends AsyncTask<Void, Void, Void>{


        @Override
        protected Void doInBackground(Void... voids) {
            soapservice();
            return null;
        }


    }*/


/*
    private void LlenarGrid(){

        egProcesado = SimularWSEGProcess();
        createColumns();
        CrearRow();
    }*/

    /*private void LlenarGridTagNoEnc(String itemCodigo)
    {
        createColumnsTagNoEnc();
        item item_tagNoRead = findTagNoLeido(itemCodigo);
        createRowTagNoEnc(item_tagNoRead.tagsNoLeidos);
    }*/

    public EGTagsResponseItem findTagNoLeido(String itemCodigo)
    {
        for(EGTagsResponseItem item_ : egDetailResponse.getItems())
        {
            if(item_.getItemCodigo().equalsIgnoreCase(itemCodigo))
            {
                return  item_;
            }
        }
        return  null;
    }



    private EGProcesado SimularWSEGProcess(){
        EGProcesado egProcesado = new EGProcesado(null);
        List<String> epcs = new ArrayList<String>();
        epcs.add("42836557bf7194e4000001a85");
        epcs.add("E20000193010025616706B68");
        epcs.add("42836557bf7194e4000003a85");
        epcs.add("42836557bf7194e4000003a87");
        epcs.add("42836557bf7194e4000003a94");


        TagNoRead tagNoRead = new TagNoRead(epcs);
        item item1 = new item("M176531872ABLAR",100, 90,tagNoRead);
        List<item> itemList = new ArrayList<item>();
        itemList.add(item1);

        //M176531872ABMED
        epcs = new ArrayList<String>();
        epcs.add("303556022843A3C00000000D");
        epcs.add("15831457bf7194e4000002a85");
        epcs.add("15831457bf7194e4000006a85");
        epcs.add("42836557bf7194e4000003a87");
        epcs.add("42836557bf7194e4000003a94");

        tagNoRead = new TagNoRead(epcs);
        item1 = new item("M176531872ABMED",80, 78,tagNoRead);
        itemList.add(item1);

        egProcesado = new EGProcesado(itemList);

        return egProcesado;
    }

    private void AgruparItemCodigoEpc()
    {

        EGDetailGroupCod egDetailGroupCod = null;
        List<String> epcs = new ArrayList<String>();
        epcs.add("42836557bf7194e4000001a85");
        epcs.add("42836557bf7194e4000002a85");
        epcs.add("42836557bf7194e4000003a85");
        epcs.add("42836557bf7194e4000004a85");

        List<EGDetailGroupCod> egDetailGroupCodList = new ArrayList<EGDetailGroupCod>() ;
        egDetailGroupCod = new EGDetailGroupCod("M176531872ABLAR",epcs);
        /*Map<String, List<Etiqueta>> groupByItemCod = entryGuideDetail.data_.etiquetas.stream().collect(Collectors.groupingBy(Etiqueta::getCodigo));*/
    }



    private void openFile() {
        if (mFile && !mInventory && !mLocate) {
            if (mFileManager == null)
                mFileManager = new FileManager(mContext);
            mFileManager.openFile();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (D) Log.d(TAG, "onRequestPermissionsResult");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            switch (requestCode) {
                case PermissionHelper.REQ_PERMISSION_CODE:
                    if (permissions != null) {
                        boolean hasResult = false;
                        for (String p : permissions) {
                            if (p.equals(PermissionHelper.mStoragePerms[0])) {
                                hasResult = true;
                                break;
                            }
                        }
                        if (hasResult) {
                            if (grantResults != null && grantResults.length != 0 &&
                                    grantResults[0] == PackageManager.PERMISSION_GRANTED)
                                mFile = true;
                            else
                                mFile = false;
                        }
                    }
                    break;
            }
            mFileSwitch.setChecked(mFile);
        }
    }

    private OnCheckedChangeListener sledcheckListener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // TODO Auto-generated method stub
            int id = buttonView.getId();
            switch (id) {
                case R.id.turbo_switch:
                    if (isChecked)
                        mIsTurbo = true;
                    else
                        mIsTurbo = false;
                    break;

                case R.id.file_switch:
                    if (isChecked) {
                        boolean b = PermissionHelper.checkPermission(mContext,PermissionHelper.mStoragePerms[0]);
                        if (!b)
                            PermissionHelper.requestPermission(getActivity(), PermissionHelper.mStoragePerms);
                        else
                            mFile = true;
                    }
                    else {
                        mFile = false;
                        if (mFileManager != null) {
                            mFileManager.closeFile();
                            mFileManager = null;
                        }
                    }
                    break;

                case R.id.rssi_switch:
                    if (isChecked) {
                        if (mReader.RF_SetRssiTrackingState(SDConsts.RFRssi.ON) == SDConsts.RFResult.SUCCESS)
                            mRssi = true;
                    }
                    else {
                        if (mReader.RF_SetRssiTrackingState(SDConsts.RFRssi.OFF) == SDConsts.RFResult.SUCCESS)
                            mRssi = false;
                    }
                    break;

                case R.id.filter_switch:
                    clearAll();
                /*if (isChecked)
                    mTagFilter = true;
                else
                    mTagFilter = false;*/
                    mTagFilter = true;
                    break;

                case R.id.sound_switch:
                    if (isChecked)
                        mSoundPlay = true;
                    else
                        mSoundPlay = false;
                    break;

                case R.id.mask_switch:
                    if (isChecked)
                        mMask = true;
                    else
                        mMask = false;
                    break;

                case R.id.toggle_switch:
                    if (isChecked) {
                        if (mReader.RF_SetToggle(SDConsts.RFToggle.ON) == SDConsts.RFResult.SUCCESS)
                            mToggle = true;
                    }
                    else {
                        if (mReader.RF_SetToggle(SDConsts.RFToggle.OFF) == SDConsts.RFResult.SUCCESS)
                            mToggle = false;
                    }
                    break;

                /*case R.id.pc_switch:
                    if (isChecked)
                        mIgnorePC = true;
                    else
                        mIgnorePC = false;
                    break;*/
            }
        }
    };

    private void startStopwatch() {
        if (D) Log.d(TAG, "startStopwatch");

        if (mStopwatchSvc != null && !mStopwatchSvc.isRunning())
            mStopwatchSvc.start();

        mProgressBar.setVisibility(View.VISIBLE);
        //isRunningRead = true;
    }

    private void pauseStopwatch() {
        if (D) Log.d(TAG, "pauseStopwatch");

        if (mStopwatchSvc != null && mStopwatchSvc.isRunning())
            mStopwatchSvc.pause();

        updateCountText();

        updateTimerText();

        updateSpeedCountText();

        updateAvrSpeedCountText();

        mProgressBar.setVisibility(View.INVISIBLE);
        //isRunningRead = false;
    }

    private void stopStopwatch() {
        if (D) Log.d(TAG, "stopStopwatch");

        if (mStopwatchSvc != null && mStopwatchSvc.isRunning())
            mStopwatchSvc.pause();

        if (mStopwatchSvc != null)
            mStopwatchSvc.reset();

        updateTimerText();

        updateAvrSpeedCountText();

        mProgressBar.setVisibility(View.INVISIBLE);
    }

    private void updateCountText() {
        if (D) Log.d(TAG, "updateCountText");
        String text = Integer.toString(mAdapter.getCount());
        mCountText.setText(text);
    }

    private void updateTimerText() {
        if (D) Log.d(TAG, "updateTimerText");
        if (mStopwatchSvc != null)
            mTimerText.setText(mStopwatchSvc.getFormattedElapsedTime());
    }

    private void updateSpeedCountText() {
        if (D) Log.d(TAG, "updateSpeedCountText");
        String speedStr = "";
        double value = 0;
        double totalCount = 0;
        double sec = 0;
        if (mStopwatchSvc != null) {
            sec = ((double)((int)(mStopwatchSvc.getElapsedTime() / 100))) / 10;

            if (!mTagFilter)
                totalCount = mAdapter.getTotalCount();
            else {
                totalCount = mAdapter.getTotalCount();
                for (int i = 0 ; i < mAdapter.getCount(); i++)
                    totalCount += mAdapter.getItemDupCount(i);
            }
            if (totalCount > 0 && sec - mOldSec >= 1) {
                value = (double)((int)(((totalCount - mOldTotalCount) / (sec - mOldSec)) * 10)) / 10;

                mOldTotalCount = totalCount;

                mOldSec = sec;
                Activity activity = getActivity();
                if (activity != null)
                    speedStr = Double.toString(value) + activity.getString(R.string.speed_postfix_str);
                mSpeedCountText.setText(speedStr);
            }
        }
    }

    private void updateAvrSpeedCountText() {
        if (D) Log.d(TAG, "updateAvrSpeedCountText");
        String speedStr = "";
        double value = 0;
        int totalCount = 0;
        double sec = 0;
        if (mStopwatchSvc != null) {
            sec = ((double)((int)(mStopwatchSvc.getElapsedTime() / 100))) / 10;

            if (!mTagFilter)
                totalCount = mAdapter.getTotalCount();
            else {
                totalCount = mAdapter.getTotalCount();
                for (int i = 0 ; i < mAdapter.getCount(); i++)
                    totalCount += mAdapter.getItemDupCount(i);
            }
            if (totalCount > 0 && sec >= 1)
                value = (double)((int)(((double)totalCount / sec) * 10)) / 10;

            Activity activity = getActivity();
            if (activity != null)
                speedStr = Double.toString(value) + activity.getString(R.string.speed_postfix_str);
            mAvrSpeedCountTest.setText(speedStr);
        }
    }

    private void enableControl(boolean b) {
        /*if (b)
            mRfidList.setOnItemClickListener(listItemClickListener);
        else
            mRfidList.setOnItemClickListener(null);*/
        mTurboSwitch.setEnabled(b);
        mRssiSwitch.setEnabled(b);
        mFilterSwitch.setEnabled(b);
        mSoundSwitch.setEnabled(b);
        mMaskSwitch.setEnabled(b);
        mToggleSwitch.setEnabled(b);
        /*mPCSwitch.setEnabled(b);*/
        mFileSwitch.setEnabled(b);
        mSessionSpin.setEnabled(b);
        mSelFlagSpin.setEnabled(b);
        mBackButton.setVisibility(b ? View.VISIBLE :View.INVISIBLE);

    }

    private ServiceConnection mStopwatchSvcConnection= new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            // TODO Auto-generated method stub
            if (D) Log.d(TAG, "onServiceConnected");

            mStopwatchSvc = ((StopwatchService.LocalBinder)arg1).getService(mUpdateStopwatchHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            // TODO Auto-generated method stub
            if (D) Log.d(TAG, "onServiceDisconnected");

            mStopwatchSvc = null;
        }
    };

    private void bindStopwatchSvc() {
        if (D) Log.d(TAG, "bindStopwatchSvc");
        mContext.bindService(new Intent(mContext, StopwatchService.class), mStopwatchSvcConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbindStopwatchSvc() {
        if (D) Log.d(TAG, "unbindStopwatchSvc");
        try {
            if (mStopwatchSvc != null)
                mContext.unbindService(mStopwatchSvcConnection);
        }
        catch (java.lang.IllegalArgumentException iae) {
            return;
        }
    }




    private static class UpdateStopwatchHandler extends Handler {
        private final WeakReference<ReceiveWareCheckFragment> mExecutor;
        public UpdateStopwatchHandler(ReceiveWareCheckFragment f) {
            mExecutor = new WeakReference<>(f);
        }

        @Override
        public void handleMessage(Message msg) {
            ReceiveWareCheckFragment executor = mExecutor.get();
            if (executor != null) {
                executor.handleUpdateStopwatchHandler(msg);
            }
        }
    }



    private static class WebServiceHandler extends Handler {
        private final WeakReference<ReceiveWareCheckFragment> mExecutor;
        public WebServiceHandler(ReceiveWareCheckFragment f) {
            mExecutor = new WeakReference<>(f);
        }

        @Override
        public void handleMessage(Message msg) {
            ReceiveWareCheckFragment executor = mExecutor.get();
            if (executor != null) {
                executor.handleWebServiceHandler(msg);
            }
        }
    }

    public void handleWebServiceHandler(Message msg) {

        Bundle bundle = msg.getData();
        Toast.makeText(mContext,"..."+bundle.getString("msgSoap"),Toast.LENGTH_LONG).show();
    }




    public void handleUpdateStopwatchHandler(Message m) {
        if (D) Log.d(TAG, "mUpdateStopwatchHandler");
        if (m.what == StopwatchService.TICK_WHAT) {
            if (D) Log.d(TAG, "received stopwatch message");

            mTickCount++;

            updateCountText();

            mtvCantItemLeidos.setText(mCountText.getText().toString());
            mprogress1.setProgress( Integer.parseInt(mCountText.getText().toString()) );


            updateSpeedCountText();

            if (mTickCount == 10) {
                updateAvrSpeedCountText();
                mTickCount = 0;
            }
            updateTimerText();

            mStopwatchSvc.update();

            //mRfidList.setSelection(mRfidList.getAdapter().getCount() - 1);

        }
    }

    private static class InventoryHandler extends Handler {
        private final WeakReference<ReceiveWareCheckFragment> mExecutor;
        public InventoryHandler(ReceiveWareCheckFragment f) {
            mExecutor = new WeakReference<>(f);
        }

        @Override
        public void handleMessage(Message msg) {
            ReceiveWareCheckFragment executor = mExecutor.get();
            if (executor != null) {
                executor.handleInventoryHandler(msg);
            }
        }
    }

    public void handleInventoryHandler(Message m) {
        if (D) Log.d(TAG, "mInventoryHandler");
        if (D) Log.d(TAG, "m arg1 = " + m.arg1 + " arg2 = " + m.arg2);
        switch (m.what) {
            case SDConsts.Msg.SDMsg:
                switch(m.arg1) {
                    case SDConsts.SDCmdMsg.TRIGGER_PRESSED:
                        if (!mInventory) {
                            //clearAll();
                            openFile();
                            //+++NTNS
                            int ret;
                            if (mLocate) {
                                //mCurrentPower = mReader.RF_GetRadioPowerState();
                                ret = mReader.RF_PerformInventoryForLocating(mLocateEPC);
                            } else
                                ret = mReader.RF_PerformInventory(mIsTurbo, mMask, mIgnorePC);
                            //ret = mReader.RF_READ(SDConsts.RFMemType.EPC, 1, 7, "00000000", false);
                            if (ret == SDConsts.RFResult.SUCCESS) {
                                startStopwatch();
                                mInventory = true;
                                mbtnConfirmar.setEnabled(false);
                                mbtnConfirmar.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D5D7D6")));

                                enableControl(!mInventory);
                            } else if (ret == SDConsts.RFResult.MODE_ERROR)
                                Toast.makeText(mContext, "Start Inventory failed, Please check RFR900 MODE", Toast.LENGTH_SHORT).show();
                            else if (ret == SDConsts.RFResult.LOW_BATTERY)
                                Toast.makeText(mContext, "Start Inventory failed, LOW_BATTERY", Toast.LENGTH_SHORT).show();
                            else
                            if (D) Log.d(TAG, "Start Inventory failed");
                        }
                        break;

                    case SDConsts.SDCmdMsg.SLED_INVENTORY_STATE_CHANGED:
                        mInventory = false;
                        enableControl(!mInventory);
                        pauseStopwatch();
                        // In case of low battery on inventory, reason value is LOW_BATTERY
                        Toast.makeText(mContext, "Inventory Stopped reason : " + m.arg2, Toast.LENGTH_SHORT).show();

                        mAdapter.addItem(-1, "Inventory Stopped reason : " + m.arg2,  Integer.toString(m.arg2), !mIgnorePC, mTagFilter);
                        break;

                    case SDConsts.SDCmdMsg.TRIGGER_RELEASED:
                        if (mReader.RF_StopInventory() == SDConsts.SDResult.SUCCESS) {
                            mInventory = false;
                            enableControl(!mInventory);

                            btnConfirmarManagement(true);

                            if(Integer.parseInt(mtvCantItemLeidos.getText().toString()) != 0 && !metDocOrigenRW.getText().equals("")){
                                mbtnConfirmar.setEnabled(true);
                                mbtnConfirmar.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1895C0")));
                            }
                        }
                        lectureHasPc = !mIgnorePC;
                        pauseStopwatch();
                        break;
                    //You can receive this message every a minute. SDConsts.SDCmdMsg.SLED_BATTERY_STATE_CHANGED
                    case SDConsts.SDCmdMsg.SLED_BATTERY_STATE_CHANGED:
                        //Toast.makeText(mContext, "Battery state = " + m.arg2, Toast.LENGTH_SHORT).show();
                        if (D) Log.d(TAG, "Battery state = " + m.arg2);
                        mBatteryText.setText("" + m.arg2 + "%");
                        break;
                    case SDConsts.SDCmdMsg.SLED_HOTSWAP_STATE_CHANGED:
                        if (m.arg2 == SDConsts.SDHotswapState.HOTSWAP_STATE)
                            Toast.makeText(mContext, "HOTSWAP STATE CHANGED = HOTSWAP_STATE", Toast.LENGTH_SHORT).show();
                        else if (m.arg2 == SDConsts.SDHotswapState.NORMAL_STATE)
                            Toast.makeText(mContext, "HOTSWAP STATE CHANGED = NORMAL_STATE", Toast.LENGTH_SHORT).show();
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.detach(mFragment).attach(mFragment).commit();
                        break;
                    case SDConsts.SDCmdMsg.SLED_MODE_CHANGED:
                        String msjMode = "Se ha establecido el modo";
                        if(m.arg2 == SDConsts.SDTriggerMode.RFID){
                            msjMode = msjMode+" RFID";
                            //mInvenButton.setImageResource(R.drawable.iniciar);
                        }else if(m.arg2 == SDConsts.SDTriggerMode.BARCODE){
                            msjMode = msjMode+" BARCODE";
                            //mInvenButton.setImageResource(R.drawable.qrcode);
                        }
                        Toast.makeText(mContext, msjMode, Toast.LENGTH_SHORT).show();
                        break;
                }
                break;

            case SDConsts.Msg.RFMsg:
                switch(m.arg1) {
                    case SDConsts.RFCmdMsg.INVENTORY:
                    case SDConsts.RFCmdMsg.READ:
                        if (m.arg2 == SDConsts.RFResult.SUCCESS) {
                            if (m.obj != null  && m.obj instanceof String) {
                                String data = (String) m.obj;
                                if (data != null)
                                    processReadData(data);
                            }
                        }
                        break;
                    case SDConsts.RFCmdMsg.LOCATE:
                        if (m.arg2 == SDConsts.RFResult.SUCCESS) {
                            if (m.obj != null  && m.obj instanceof Integer)
                                processLocateData((int) m.obj);
                        }
                        break;
                }
                break;
            case SDConsts.Msg.BTMsg:
                if (m.arg1 == SDConsts.BTCmdMsg.SLED_BT_CONNECTION_STATE_CHANGED) {
                    if (D) Log.d(TAG, "SLED_BT_CONNECTION_STATE_CHANGED = " + m.arg2);
                    if (mReader.BT_GetConnectState() != SDConsts.BTConnectState.CONNECTED) {
                        if (mInventory) {
                            pauseStopwatch();
                            mInventory = false;
                        }
                        enableControl(false);
                    }
                    if (mOptionHandler != null)
                        mOptionHandler.obtainMessage(MainActivity.MSG_OPTION_CONNECT_STATE_CHANGED).sendToTarget();
                }
                else if  (m.arg1 == SDConsts.BTCmdMsg.SLED_BT_DISCONNECTED || m.arg1 == SDConsts.BTCmdMsg.SLED_BT_CONNECTION_LOST) {
                    if (mInventory) {
                        pauseStopwatch();
                        mInventory = false;
                    }
                    enableControl(false);
                    if (mOptionHandler != null)
                        mOptionHandler.obtainMessage(MainActivity.MSG_OPTION_CONNECT_STATE_CHANGED).sendToTarget();
                }
                break;
            case SDConsts.Msg.BCMsg:
                StringBuilder readData = new StringBuilder();
                if (m.arg1 == SDConsts.BCCmdMsg.BARCODE_TRIGGER_PRESSED)
                    Toast.makeText(mContext, " BARCODE_TRIGGER_PRESSED", Toast.LENGTH_SHORT).show();
                else if (m.arg1 == SDConsts.BCCmdMsg.BARCODE_TRIGGER_RELEASED)
                    Toast.makeText(mContext, " BARCODE_TRIGGER_RELEASED", Toast.LENGTH_SHORT).show();
                else if (m.arg1 == SDConsts.BCCmdMsg.BARCODE_READ) {
                    if (D) Log.d(TAG, "BC_MSG_BARCODE_READ");
                    if (m.arg2 == SDConsts.BCResult.SUCCESS)
                        readData.append(" " + "BC_MSG_BARCODE_READ");
                    else if (m.arg2 == SDConsts.BCResult.ACCESS_TIMEOUT)
                        readData.append(" " + "BC_MSG_BARCODE_ACCESS_TIMEOUT");
                    if (m.obj != null  && m.obj instanceof String) {
                        readData.append("\n" + (String)m.obj);
                        //metDocDestinoRW.setText(" " + (String)m.obj);

                        // call Method WS http://info.thgye.com.ec/awsrfidrecepciondetalle.aspx

                        try {
                            InvocateWSReceptionDetail(" " + (String)m.obj);
                        }
                        catch (Exception ex){
                            String msd = ex.getMessage();
                        }


                    }
                    //mMessageTextView.setText(" " + readData.toString());
                    //Toast.makeText(mContext, readData.toString(), Toast.LENGTH_SHORT).show();
                }
                else if (m.arg1 == SDConsts.BCCmdMsg.BARCODE_ERROR) {
                    if (D) Log.d(TAG, "BARCODE_ERROR");
                    if (m.arg2 == SDConsts.BCResult.LOW_BATTERY)
                        readData.append(" " + "BARCODE_ERROR Low battery");
                    else
                        readData.append(" " + "BARCODE_ERROR ");
                    readData.append("barcode pasue");
                }
                if (D) Log.d(TAG, "data = " + readData.toString());
                break;
        }
    }

    private void InvocateWSReceptionDetail(String qrcode)
    {
        doc_origen = "";
        if(qrcode != null && !qrcode.trim().isEmpty())
        {
            String[] parts = qrcode.trim().split(";");
            doc_origen = parts[0];
        }
        exWSRecepcionDetailAsync recepcionDetailAsync = new exWSRecepcionDetailAsync();
        recepcionDetailAsync.execute(new String[0]);
    }

    private void InvocateWSReceptionDetail(String pBodOrigen, String pTipoOrigen, String pAnioOrigen, String pNumOrigen )
    {
        exWSRecepcionDetailAsync recepcionDetailAsync = new exWSRecepcionDetailAsync();
        recepcionDetailAsync.execute(new String[]{pTipoOrigen,pBodOrigen,pAnioOrigen,pNumOrigen});
    }

    // METODOS ASYNC
    private  class exWSRecepcionDetailAsync extends AsyncTask<String[], Void, Void> {

        boolean error = false;

        @Override
        protected Void doInBackground(String[]... params) {

            String[] dataNoQr = new String[4];

            rfidService.SOAP_ACTION_ =  mWSparameterRecepcionDet[0];
            rfidService.METHOD_NAME_ =  mWSparameterRecepcionDet[1];
            rfidService.NAMESPACE_ = mWSparameterRecepcionDet[2];
            rfidService.URL_ = mWSparameterRecepcionDet[3];

            if(params[0].length > 0)
            {
                receiveWareDetail = rfidService.WSRecepcionMercaderiaDetail( null,true, params[0]);
            }
            else {
                receiveWareDetail = rfidService.WSRecepcionMercaderiaDetail( doc_origen,false,null);
            }


            return null;
        }

        @Override
        //protected void onPostExecute(Void aVoid) {
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);

            progressDialog.cancel();
            Validator validator = new Validator();

            ResponseVal responseVal =  validator.getValidateDataSourceDto(receiveWareDetail.getEstado());

            if(responseVal.isValidAccess()){
                if(doc_origen.isEmpty() && receiveWareDetail.getDocOrigen() != null)
                {
                    doc_origen = receiveWareDetail.getDocOrigen().trim();
                }
                metDocOrigenRW.setText(doc_origen);
                metDocDestinoRW.setText(receiveWareDetail.getDocDestino());
                metMotivoRW.setText(receiveWareDetail.getMotDescription());
                mprogress1.setMax(receiveWareDetail.getCantidadTotal());
                mtvCantTotal.setText(receiveWareDetail.getCantidadTotal()+"");
                ProcesarLvItemsDif(false);
                ActivateButtons(true);
                DialogIndicadorComparacion(doc_origen);
            }
            else {
                Toast.makeText(mContext, responseVal.getErrorMsg() , Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            ProgressLoading();

        }
    }

    private void ActivateButtons(boolean isActivate){

        if(isActivate){
            mInvenButton.setEnabled(isActivate);
            mStopInvenButton.setEnabled(isActivate);
            mbtnConfirmar.setEnabled(isActivate);
            mInvenButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1F9375")));
            mStopInvenButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EF3C10")));
            mbtnConfirmar.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1895C0")));
        }
        else {
            mInvenButton.setEnabled(isActivate);
            mStopInvenButton.setEnabled(isActivate);
            mbtnConfirmar.setEnabled(isActivate);
            mInvenButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D5D7D6")));
            mStopInvenButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D5D7D6")));
            mbtnConfirmar.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D5D7D6")));
        }


    }

    private  class exWSRecepcionComparaAsync extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {

            rfidService.SOAP_ACTION_ =  mWSParameterRecepcionCompara[0];
            rfidService.METHOD_NAME_ =  mWSParameterRecepcionCompara[1];
            rfidService.NAMESPACE_ = mWSParameterRecepcionCompara[2];
            rfidService.URL_ = mWSParameterRecepcionCompara[3];

            egDetailResponse = rfidService.WSRecepcionMercaderiaCompara(doc_origen,ListEpcRead);


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);

            progressDialog.cancel();


            Validator validator = new Validator();
            ResponseVal responseVal = validator.getValidateDataSourceDto(egDetailResponse.getStatus());

            if(responseVal.isValidAccess()){
                if(egDetailResponse.getItems() != null && egDetailResponse.getItems().size() > 0){
                    btnConfirmarManagement(false);
                    ProcesarLvItemsDif(true);
                }
                else {
                    Toast.makeText(mContext, "No hay items con que comparar..." , Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(mContext, responseVal.getErrorMsg() , Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            ProgressLoading();

        }
    }

    private void btnConfirmarManagement(boolean isComparar){

        Drawable myIcon = null;
        ColorFilter filter = null;

        if(isComparar){
            myIcon = getResources().getDrawable( R.drawable.materialcompare18 );
            mbtnConfirmar.setText("Comparar");

        }
        else {
            myIcon = getResources().getDrawable( R.drawable.materialcheck18);
            mbtnConfirmar.setText("Confirmar");
        }

        filter = new LightingColorFilter( Color.BLACK, Color.WHITE);
        myIcon.setColorFilter(filter);
        mbtnConfirmar.setCompoundDrawablesWithIntrinsicBounds( myIcon, null, null, null);
    }

    private void ProcesarLvItemsDif(boolean isComplete)
    {
        mlv_itemsEnc.setAdapter(null);

       /* if(first){
            View headerview = View.inflate(mContext, R.layout.header_4column, null);
            mlv_itemsEnc.addHeaderView(headerview,null,false);
            first = false;
        }*/
        if(isComplete){
            if(egDetailResponse != null && egDetailResponse.getItems() != null && egDetailResponse.getItems().size() > 0)
            {
                mlayoutHeaderRWC.setVisibility(View.VISIBLE);
                mlv_itemsEnc.setAdapter(new CustomListAdapterReceiveWare(mContext, egDetailResponse.getItems(), isComplete));
                mlv_itemsEnc.setOnItemClickListener(listItemClickListener);
            }

        }
        else {
            if(receiveWareDetail != null && receiveWareDetail.detalle != null && receiveWareDetail.detalle.size() > 0)
            {
                mlayoutHeaderRWC.setVisibility(View.VISIBLE);
                mlv_itemsEnc.setAdapter(new CustomListAdapterReceiveWare(mContext, receiveWareDetail.detalle, isComplete));
            }
        }

        //mlv_itemsEnc.setOnItemClickListener(listItemClickListener);
    }

    private AdapterView.OnItemClickListener listItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            EGTagsResponseItem i = (EGTagsResponseItem) mlv_itemsEnc.getItemAtPosition(position);
            EGTagsResponseItem item_tagNoRead = findTagNoLeido(i.getItemCodigo());
            itemEncontradoSeleccinado = item_tagNoRead;

            /*if(!item_tagNoRead.getItemCodigo().equalsIgnoreCase("OTROS") && item_tagNoRead.getDataNoLeido() != null && item_tagNoRead.getDataNoLeido().size() > 0){
                ProcesarLv_tagsInconsistentes(item_tagNoRead.getDataNoLeido());
            }
            else {
                Toast.makeText(mContext, "No existen etiquetas no leidas", Toast.LENGTH_SHORT).show();
            }*/

            if(item_tagNoRead.getItemCodigo().equalsIgnoreCase("OTROS")){
                if(item_tagNoRead.getDataLeido() != null && item_tagNoRead.getDataLeido().size() > 0){
                    ProcesarLv_tagsInconsistentes(item_tagNoRead.getDataLeido());
                }
                else {
                    Toast.makeText(mContext, "No existen otras etiquetas", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                if(item_tagNoRead.getDataLeido() != null && item_tagNoRead.getDataNoLeido().size() > 0){
                    ProcesarLv_tagsInconsistentes(item_tagNoRead.getDataNoLeido());
                }
                else {
                    Toast.makeText(mContext, "No existen etiquetas no leidas", Toast.LENGTH_SHORT).show();
                }

            }
        }
    };


    private void ProcesarLv_tagsInconsistentes(List<String> listEpc)
    {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, listEpc);
        mlv_tagsInconsistentes.setAdapter(adapter);
        mlv_tagsInconsistentes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String epc = (String) mlv_tagsInconsistentes.getItemAtPosition(position);

                mLocateTag = epc;
                mLocateStartPos = (lectureHasPc ? 0 : 4);
                                                 /*if (i.mHasPc)
                                                     mLocateEPC = mLocateTag.substring(4, mLocateTag.length());
                                                 else
                                                     mLocateEPC = mLocateTag;*/

                if (lectureHasPc)
                    mLocateEPC = mLocateTag.substring(4, mLocateTag.length());
                else
                    mLocateEPC = mLocateTag;
                //mLocateEPC = mLocateTag.substring(4, mLocateTag.length());

                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                alert.setTitle(getString(R.string.locating_str));
                alert.setMessage(getString(R.string.want_tracking_str));

                alert.setPositiveButton(getString(R.string.yes_str), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        SelectionCriterias s = new SelectionCriterias();
                        s.makeCriteria(SelectionCriterias.SCMemType.EPC, mLocateTag,
                                mLocateStartPos, (mLocateEPC.length()+4) * 4,
                                SelectionCriterias.SCActionType.ASLINVA_DSLINVB);
                        mReader.RF_SetSelection(s);
                        switchLayout(false);
                        mLocateTv.setText(mLocateTag);

                        if(itemEncontradoSeleccinado != null){
                            mtag_locate_grupo1.setText(itemEncontradoSeleccinado.getItemGrupo1());
                            mtag_locate_grupo2.setText(itemEncontradoSeleccinado.getItemGrupo2());
                            mtag_locate_grupo3.setText(itemEncontradoSeleccinado.getItemGrupo3());
                            /*mtag_locate_grupo4.setText(egTagsResponseItem_.getItemGrupo4());
                            mtag_locate_grupo5.setText(egTagsResponseItem_.getItemGrupo5());*/
                        }
                    }
                });
                alert.setNegativeButton(getString(R.string.no_str) ,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                alert.show();
            }
        });
    }

    private void processLocateData(int data) {
        startLocateTimer();
        mLocateValue = data;
        //mTagLocateProgress.setProgress(data);
        if (mSoundTask == null) {
            mSoundTask = new SoundTask();
            mSoundTask.execute();
        }
        else {
            if (mSoundTask.getStatus() == AsyncTask.Status.FINISHED) {
                mSoundTask.cancel(true);
                mSoundTask = null;
                mSoundTask = new SoundTask();
                mSoundTask.execute();
            }
        }
    }

    private void processReadData(String data) {
        //updateCountText();
        StringBuilder tagSb = new StringBuilder();
        tagSb.setLength(0);
        String info = "";
        String originalData = data;
        if (data.contains(";")) {
            if (D) Log.d(TAG, "full tag = " + data);
            //full tag example(with optional value)
            //1) RF_PerformInventory => "3000123456783333444455556666;rssi:-54.8"
            //2) RF_PerformInventoryWithLocating => "3000123456783333444455556666;loc:64"
            int infoTagPoint = data.indexOf(';');
            info = data.substring(infoTagPoint, data.length());
            int infoPoint = info.indexOf(':') + 1;
            info = info.substring(infoPoint, info.length());
            if (D) Log.d(TAG, "info tag = " + info);
            data = data.substring(0, infoTagPoint);
            if (D) Log.d(TAG, "data tag = " + data);
        }

        if (info != "") {
            Activity activity = getActivity();
            String prefix = "";
            if (originalData.contains("rssi")) {
                if (activity != null)
                    prefix = activity.getString(R.string.rssi_str);
            }
            else if (originalData.contains("loc")){
                if (activity != null)
                    prefix = activity.getString(R.string.loc_str);
            }
            if (activity != null)
                info = prefix + info;
        }

        ArrLis = mAdapter.listaEtiquetasLeidas();

        mAdapter.addItem(-1, data, info, !mIgnorePC, mTagFilter);
        if (mFileManager != null && mFile)
            mFileManager.writeToFile(data);

        //mRfidList.setSelection(mRfidList.getAdapter().getCount() - 1);
        if (!mInventory) {
            updateCountText();
            updateSpeedCountText();
            updateAvrSpeedCountText();
        }

        if (mSoundTask == null) {
            mSoundTask = new SoundTask();
            mSoundTask.execute();
        }
        else {
            if (mSoundTask.getStatus() == AsyncTask.Status.FINISHED) {
                mSoundTask.cancel(true);
                mSoundTask = null;
                mSoundTask = new SoundTask();
                mSoundTask.execute();
            }
        }

    }

    private void addCheckListener() {
        if (mTurboSwitch != null)
            mTurboSwitch.setOnCheckedChangeListener(sledcheckListener);

        if (mRssiSwitch != null)
            mRssiSwitch.setOnCheckedChangeListener(sledcheckListener);

        if (mFilterSwitch != null)
            mFilterSwitch.setOnCheckedChangeListener(sledcheckListener);

        if (mSoundSwitch != null)
            mSoundSwitch.setOnCheckedChangeListener(sledcheckListener);

        if (mMaskSwitch != null)
            mMaskSwitch.setOnCheckedChangeListener(sledcheckListener);

        if (mToggleSwitch != null)
            mToggleSwitch.setOnCheckedChangeListener(sledcheckListener);

        /*if (mPCSwitch != null)
            mPCSwitch.setOnCheckedChangeListener(sledcheckListener);*/

        if (mFileSwitch != null)
            mFileSwitch.setOnCheckedChangeListener(sledcheckListener);

        if (mSessionSpin != null)
            mSessionSpin.setOnItemSelectedListener(sessionListener);

        if (mSelFlagSpin != null)
            mSelFlagSpin.setOnItemSelectedListener(selFlagListener);
    }

    private void updateButtonState() {
        /*mPCSwitch.setChecked(mIgnorePC);*/

        mFileSwitch.setChecked(mFile);

        mFilterSwitch.setChecked(mTagFilter);

        mSoundSwitch.setChecked(mSoundPlay);

        mMaskSwitch.setChecked(mMask);

        mTurboSwitch.setChecked(mIsTurbo);


        if (mReader != null) {
            int toggle = mReader.RF_GetToggle();
            if (toggle == SDConsts.RFToggle.ON)
                mToggle = true;
            else
                mToggle = false;
            mToggleSwitch.setChecked(mToggle);

            int session = mReader.RF_GetSession();
            if (session != mSessionSpin.getSelectedItemPosition())
                mSessionSpin.setSelection(session);

            int flag = mReader.RF_GetSelectionFlag();
            if (flag != mSelFlagSpin.getSelectedItemPosition() + 1)
                mSelFlagSpin.setSelection(flag - 1);

            int rssi = mReader.RF_GetRssiTrackingState();
            if (rssi == SDConsts.RFRssi.ON)
                mRssi = true;
            else
                mRssi = false;
            mRssiSwitch.setChecked(mRssi);

            int battery = mReader.SD_GetBatteryStatus();
            if (!(battery < 0 || battery > 100))
                mBatteryText.setText("" + battery + "%");

        }
    }

    private void startLocateTimer() {
        stopLocateTimer();

        mLocateTimerTask = new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                locateTimeout();
            }
        };
        mClearLocateTimer = new Timer();
        mClearLocateTimer.schedule(mLocateTimerTask, 500);
    }

    private void stopLocateTimer() {
        if (mClearLocateTimer != null ) {
            mClearLocateTimer.cancel();
            mClearLocateTimer = null;
        }
    }

    private void locateTimeout() {
        mTagLocateProgress.setProgress(0);
    }

    // Dialog

    private void DialogCleanControls(){
        AlertDialog.Builder alerta = new AlertDialog.Builder(mContext);
        alerta.setMessage("Esta seguro de realizar un limpieza se perderan todos los datos recolectados...")
                .setCancelable(false)
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        clearAll();
                        CleanControls();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        alerta.show();
    }

    private void DialogConfirmar(){
        AlertDialog.Builder alerta = new AlertDialog.Builder(mContext);
        alerta.setMessage("Esta seguro de realizar la comparación entre las prendas descritas en el doc.Origen y las recibidas(Leidas por el HandHeld)...")
                .setCancelable(false)
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        mWSParameterRecepcionCompara = getResources().getStringArray(R.array.WSparameter_RecepcionCompara);
                        exWSRecepcionComparaAsync recepcionComparaAsync = new exWSRecepcionComparaAsync();
                        recepcionComparaAsync.execute();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        alerta.show();
    }

    private void DialogIndicadorComparacion(String DocOrigen){
        AlertDialog.Builder alerta = new AlertDialog.Builder(mContext);
        alerta.setMessage("Se ha cargado la información del Doc de Origen: "+DocOrigen+". Cambie el modo de lectura a RFID, luego presione el Gatillo o el botón -Iniciar-, posterior a eso presione el botón -confirmar- para comparar la orden vs lo Leido")
                .setCancelable(false)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setPositiveButton("Cambiar a Modo RFID", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mReader.SD_SetTriggerMode(SDConsts.SDTriggerMode.RFID);
                        Drawable myIcon  = getResources().getDrawable( R.drawable.moderfid18px );
                        ColorFilter filter = new LightingColorFilter( Color.BLACK, Color.WHITE);
                        myIcon.setColorFilter(filter);
                        mscanQR_imgbutton.setCompoundDrawablesWithIntrinsicBounds( myIcon, null, null, null);
                        Toast.makeText(mContext,"Se establecio modo RFID",Toast.LENGTH_SHORT).show();
                    }
                });
        alerta.show();
    }


    //WSRecepcionMercaderiaProcesar
    private  class exWSRecepcionProcesarAsync extends AsyncTask<Void, Void, Void> {

        String doc_origen = EtDocOrigenRWI.getText().toString();
        String nota = metNotaRW.getText().toString();
        DataSourceDto dtoResponse = null;
        //ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Void... voids) {
            rfidService.SOAP_ACTION_ =  mWSParameter_RecepcionProcesar[0];
            rfidService.METHOD_NAME_ =  mWSParameter_RecepcionProcesar[1];
            rfidService.NAMESPACE_ = mWSParameter_RecepcionProcesar[2];
            rfidService.URL_ = mWSParameter_RecepcionProcesar[3];

            dtoResponse = rfidService.WSRecepcionMercaderiaProcesar(doc_origen,nota,listInc);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);

            progressDialog.cancel();
            Validator validator = new Validator();
            ResponseVal responseVal = validator.getValidateDataSourceDto(dtoResponse);

            if(responseVal.isValidAccess()){
                InvocarAlertIngresoMercaderia("Se ha generado correctamente el ingreso de la mercaderia", true);
            }
            else {
                InvocarAlertIngresoMercaderia(responseVal.getErrorMsg(), false);
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

    private void InvocarAlertIngresoMercaderia(String mensaje_, boolean isExitoso)
    {
        AlertDialog.Builder alerta = new AlertDialog.Builder(mContext);
        alerta.setMessage(mensaje_)
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        if(isExitoso){

                            clearAll();
                            CleanControls();
                            SetVisibleWareCheck();
                            btnConfirmarManagement(true);
                            dialog.dismiss();
                        }
                        else {
                            dialog.dismiss();
                        }
                    }
                });
                /*.setNegativeButton("No", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                })*/
        alerta.show();
    }

    private View.OnClickListener btnConfirmarRWIOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            DialogBtnConfirmarRWI();
        }
    };


    private void DialogBtnConfirmarRWI(){
        AlertDialog.Builder alerta = new AlertDialog.Builder(mContext);
        alerta.setMessage("Esta seguro realizar el ingreso de Mercaderia...")
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
                        mWSParameter_RecepcionProcesar = getResources().getStringArray(R.array.WSparameter_RecepcionProcesar);
                        exWSRecepcionProcesarAsync recepcionProcesarAsync = new exWSRecepcionProcesarAsync();
                        recepcionProcesarAsync.execute();
                    }
                });
        alerta.show();
    }


}
