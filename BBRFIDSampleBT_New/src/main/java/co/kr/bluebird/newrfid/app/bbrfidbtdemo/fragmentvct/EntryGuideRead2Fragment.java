package co.kr.bluebird.newrfid.app.bbrfidbtdemo.fragmentvct;


import java.lang.Object;

import co.kr.bluebird.newrfid.app.bbrfidbtdemo.Constants;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.control.BankMemoryRfid;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EGDetailGroupCod;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EGDetailResponse;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EGProcesado;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EGTagsResponseItem;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EntryGuideDetail;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.SendTags;
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
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.CustomListAdapterEntryGuide;
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
import android.graphics.Typeface;
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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.google.gson.Gson;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class EntryGuideRead2Fragment extends Fragment {


    public EntryGuideRead2Fragment() {
        // Required empty public constructor
    }


   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.entryguideread2_frag, container, false);
    }*/

    private static final String TAG = EntryGuideRead2Fragment.class.getSimpleName();

    private static final boolean D = Constants.INV_D;

    private StopwatchService mStopwatchSvc;

    private TagListAdapter mAdapter;

    //private ListView mRfidList;

    private TextView mBatteryText;

    private TextView mTimerText;

    private TextView mCountText;

    private TextView mSpeedCountText;

    private TextView mAvrSpeedCountTest;

    private Button mClearButton;

    private Button mInvenButton;

    private Button mStopInvenButton;

    private Button mclean_imgbtn;
    private Button mprocesar_imgbtn;

    /*private ImageButton mnext_imgbtn;*/

    private Switch mTurboSwitch;

    private Switch mRssiSwitch;

    private Switch mFilterSwitch;

    private Switch mSoundSwitch;

    private Switch mMaskSwitch;

    private Switch mToggleSwitch;

    private Switch mPCSwitch;

    private Switch mFileSwitch;

    private ProgressBar mProgressBar;

    private BTReader mReader;

    private Context mContext;

    private boolean mTagFilter = true;

    private boolean mSoundPlay = true;

    private boolean mMask = false;

    private boolean mInventory = false;

    private boolean mIsTurbo = true;

    private boolean mToggle = false;

    private boolean mIgnorePC = false;

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

    private LinearLayout mLocateLayout, layoutButtons;

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
    private EntryGuideDetail entryGuideDetail;

    private EGDetailResponse egDetailResponseLocating;
    private  EGTagsResponseItem egTagsResponseItem_;

    private UpdateStopwatchHandler mUpdateStopwatchHandler = new UpdateStopwatchHandler(this);


    private WebServiceHandler mWebServiceHandler = new WebServiceHandler(this);


    private InventoryHandler mInventoryHandler = new InventoryHandler(this);

    private Handler handlerStart ;
    private ProgressBar mprogress1;
    private TextView medOrdenCompraGR, metNumGuiaEntGR;
    private TextView mtvCantTotal, mtvCantItemLeidos;
    private String NoGuia, NoGuiaCantidad;
    private TableLayout tblItemDif,tblItemsNoEnc;
    //private TableLayout tblItemDif;
    private EGProcesado egProcesado;
    private List<String> ListEpcRead;
    private ListView mlv_itemsDif, mlv_tagsNoEnc;
    private ScrollView mscrollEntryGuideRead;

    //private boolean first ;
    private String[] mWSParameters;
    private LinearLayout mlayoutButtons ;
    private TableLayout mlayoutItemsLeidos;

    private LinearLayout mlayoutHeader;

    private  int getValueSBar = 17;
    private ImageButton mibtnPotencia;
    private boolean lectureHasPc = false;
    //private boolean isRunningRead;

    public static EntryGuideRead2Fragment newInstance() {
        return new EntryGuideRead2Fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        if (D) Log.d(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.entryguideread2_frag, container, false);

        mContext = inflater.getContext();

        mFragment = this;
        //first = true;

        mlayoutHeader = (LinearLayout) v.findViewById(R.id.layoutHeader);
        mOptionHandler = ((MainActivity)getActivity()).mUpdateConnectHandler;

        //mRfidList = (ListView)v.findViewById(R.id.rfid_list);

        //mRfidList.setOnItemClickListener(listItemClickListener);

        mlv_itemsDif = (ListView) v.findViewById(R.id.lv_itemsDif);
        mlv_tagsNoEnc = (ListView) v.findViewById(R.id.lv_tagsNoEnc);

        mLocateLayout = (LinearLayout)v.findViewById(R.id.tag_locate_container);

        mListLayout = (LinearLayout)v.findViewById(R.id.tag_list_container);

        mlayoutButtons = (LinearLayout) v.findViewById(R.id.layoutButtons);
        mlayoutItemsLeidos = (TableLayout) v.findViewById(R.id.layoutItemsLeidos) ;

        mLocateTv = (TextView)v.findViewById(R.id.tag_locate_text);
        mtag_locate_grupo1 = (TextView)v.findViewById(R.id.tag_locate_grupo1);
        mtag_locate_grupo2 = (TextView)v.findViewById(R.id.tag_locate_grupo2);
        mtag_locate_grupo3 = (TextView)v.findViewById(R.id.tag_locate_grupo3);
       /* mtag_locate_grupo4 = (TextView)v.findViewById(R.id.tag_locate_grupo4);
        mtag_locate_grupo5 = (TextView)v.findViewById(R.id.tag_locate_grupo5);*/

        mTagLocateProgress = (ProgressBar)v.findViewById(R.id.tag_locate_progress);

        mBackButton = (ImageButton)v.findViewById(R.id.back_button);
        mBackButton.setOnClickListener(sledListener);

        mTimerText = (TextView)v.findViewById(R.id.timer_text);

        mCountText = (TextView)v.findViewById(R.id.count_text);

        mSpeedCountText = (TextView)v.findViewById(R.id.speed_count_text);

        mAvrSpeedCountTest = (TextView)v.findViewById(R.id.speed_avr_count_text);

        tblItemDif = (TableLayout)v.findViewById(R.id.tablelayoutItemsDif);
        tblItemsNoEnc = (TableLayout)v.findViewById(R.id.tablelayoutItemsNoEnc);


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

        mPCSwitch = (Switch)v.findViewById(R.id.pc_switch);

        mFileSwitch = (Switch)v.findViewById(R.id.file_switch);

        mClearButton = (Button)v.findViewById(R.id.clear_button);
        mClearButton.setOnClickListener(clearButtonListener);



        /*mInvenButton = (Button)v.findViewById(R.id.inven_button);
        mInvenButton.setOnClickListener(sledListener);*/

        mibtnPotencia = (ImageButton) v.findViewById(R.id.ibtnPotencia);
        mibtnPotencia.setOnClickListener(onClickDialogPotencia);

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
        mStopInvenButton.setEnabled(false);
        mStopInvenButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D5D7D6")));
        mStopInvenButton.setOnClickListener(sledListener);

        myIcon = getResources().getDrawable( R.drawable.materialdelete );
        filter = new LightingColorFilter( Color.BLACK, Color.WHITE);
        myIcon.setColorFilter(filter);

        mclean_imgbtn = (Button)v.findViewById(R.id.clean_imgbtn);
        mclean_imgbtn.setCompoundDrawablesWithIntrinsicBounds( myIcon, null, null, null);
        mclean_imgbtn.setOnClickListener(sledListener);


        mprocesar_imgbtn = (Button)v.findViewById(R.id.procesar_imgbtn);
        mprocesar_imgbtn.setOnClickListener(sledListener);

        btnProcesarManagement(false);
        btnProcesarEnabledDisabled(false);



        /*mnext_imgbtn = (ImageButton)v.findViewById(R.id.next_imgbtn);
        mnext_imgbtn.setOnClickListener(sledListener);*/




        mProgressBar = (ProgressBar)v.findViewById(R.id.timer_progress);
        mProgressBar.setVisibility(View.INVISIBLE);

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


        mprogress1 = (ProgressBar)v.findViewById(R.id.pbLeidos);
        bindStopwatchSvc();

        medOrdenCompraGR = (TextView)v.findViewById(R.id.tvNOrdenCompra);
        metNumGuiaEntGR = (TextView)v.findViewById(R.id.tvNGuiaEntrada) ;
        mtvCantTotal = (TextView) v.findViewById(R.id.tvCantTotal) ;
        mtvCantItemLeidos = (TextView) v.findViewById(R.id.tvCantItemLeidos) ;
        NoGuia = getArguments() != null ? getArguments().getString("NoGuia") : "0";
        String NoOrdenCompra = getArguments() != null ? getArguments().getString("NoOCompra") : "0";
        NoGuiaCantidad = getArguments() != null ? getArguments().getString("NoGuiaCant") : "0";
        medOrdenCompraGR.setText(NoOrdenCompra);
        metNumGuiaEntGR.setText(NoGuia);

        mprogress1.setMax(Integer.parseInt(NoGuiaCantidad));
        mtvCantTotal.setText(NoGuiaCantidad);

        rfidService = new RfidService(mContext);
        entryGuideDetail = null;
        mscrollEntryGuideRead = (ScrollView)  v.findViewById(R.id.scrollEntryGuideRead);

        mlv_itemsDif.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mscrollEntryGuideRead.requestDisallowInterceptTouchEvent(true);
                int action = event.getActionMasked();
                switch (action) {
                    case MotionEvent.ACTION_UP:
                        mscrollEntryGuideRead.requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

        mlv_tagsNoEnc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mscrollEntryGuideRead.requestDisallowInterceptTouchEvent(true);
                int action = event.getActionMasked();
                switch (action) {
                    case MotionEvent.ACTION_UP:
                        mscrollEntryGuideRead.requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

        return v;
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


   private void btnProcesarManagement(boolean isProcesar ){

       Drawable myIcon = null;
       ColorFilter filter = null;

       if(isProcesar ){
           myIcon = getResources().getDrawable( R.drawable.materialprocesar );
           mprocesar_imgbtn.setText("Procesar");
       }
       else {
           myIcon = getResources().getDrawable( R.drawable.materialcompare18 );
           mprocesar_imgbtn.setText("Comparar");
       }

       filter = new LightingColorFilter( Color.BLACK, Color.WHITE);
       myIcon.setColorFilter(filter);


       mprocesar_imgbtn.setCompoundDrawablesWithIntrinsicBounds( myIcon, null, null, null);

   }

   private void btnProcesarEnabledDisabled(boolean isEnabled){
       mprocesar_imgbtn.setEnabled(isEnabled);
       mprocesar_imgbtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor( isEnabled? "#1895C0" : "#D5D7D6")));

   }



    private void switchLayout(boolean showList) {
        mLocate = !showList;
//        if (mLocate)
//            mReader.RF_SetRssiTrackingState(SDConsts.RFRssi.ON);
        if (showList) {
            mListLayout.setVisibility(View.VISIBLE);
            mLocateLayout.setVisibility(View.GONE);
            mlayoutButtons.setVisibility(View.VISIBLE);
            mlayoutItemsLeidos.setVisibility(View.VISIBLE);
            //mInvenButton.setText(R.string.inven_str);
            //mStopInvenButton.setText(R.string.stop_inven_str);
        }
        else {
            mTagLocateProgress.setProgress(0);
            mListLayout.setVisibility(View.GONE);
            mLocateLayout.setVisibility(View.VISIBLE);
            mlayoutButtons.setVisibility(View.GONE);
            mlayoutItemsLeidos.setVisibility(View.GONE);
            //mInvenButton.setText(R.string.track_str);
            //mStopInvenButton.setText(R.string.stop_track_str);
        }
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
            updateButtonState();
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
                        clearAll();
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
                            mInvenButton.setEnabled(false);
                            mInvenButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D5D7D6")));
                            mStopInvenButton.setEnabled(true);
                            mStopInvenButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EF3C10")));

                            mclean_imgbtn.setEnabled(false);
                            mclean_imgbtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D5D7D6")));

                            btnProcesarEnabledDisabled(false);
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
                        mInvenButton.setEnabled(true);
                        mInvenButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1F9375")));
                        mStopInvenButton.setEnabled(false);
                        mStopInvenButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D5D7D6")));

                        mclean_imgbtn.setEnabled(true);
                        mclean_imgbtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F38428")));

                        if(!mCountText.getText().equals("0")){
                            btnProcesarEnabledDisabled(true);
                        }

                        lectureHasPc = !mIgnorePC;

                    } else if (ret == SDConsts.RFResult.STOP_FAILED_TRY_AGAIN)
                        Toast.makeText(mContext, "Stop Inventory failed", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.clean_imgbtn:
                    //ArrLis = mAdapter.listaEtiquetasLeidas();
                    DialogCleanControls();
                    break;

                case R.id.procesar_imgbtn:

                    btnProcesar_extracted();

                    break;

                /*case R.id.next_imgbtn:

                    ListEpcRead = new ArrayList<>();
                    for (ListItem tag:ArrLis) {
                        ListEpcRead.add(tag.mUt);
                    }
                    mWSParameters = getResources().getStringArray(R.array.WSparameter_RfidTest);

                    SoapEnvioTagsGeneralAsync soapEnvioTagsGeneralAsync = new SoapEnvioTagsGeneralAsync();
                    soapEnvioTagsGeneralAsync.execute();

                    break;*/

            }
        }
    };


    private void btnProcesar_extracted(){
        DialogCompararProcesar(mprocesar_imgbtn.getText().equals("Comparar"));
    }

    private void DialogCleanControls(){
        AlertDialog.Builder alerta = new AlertDialog.Builder(mContext);
        alerta.setMessage("Esta seguro de realizar un limpieza se perderan todos los datos recolectados...")
                .setCancelable(false)
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
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
    private void CleanControls(){
        //medOrdenCompraGR metNumGuiaEntGR mCountText mtvCantTotal mtvCantItemLeidos mlv_itemsDif mlv_tagsNoEnc
        //medOrdenCompraGR.setText("");
        //metNumGuiaEntGR.setText("");
        ArrLis = null;
        mCountText.setText("0");
        //mtvCantTotal.setText("0");
        mtvCantItemLeidos.setText("0");
        mlv_itemsDif.setAdapter(null);
        //mlv_itemsDif.removeAllViews();
        mlayoutHeader.setVisibility(View.GONE);
        mprogress1.setProgress(0);
        mlv_tagsNoEnc.setAdapter(null);
        ListEpcRead = null;
        mAdapter.removeAllItem();


        mInvenButton.setEnabled(true);
        mInvenButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1F9375")));
        mStopInvenButton.setEnabled(false);
        mStopInvenButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D5D7D6")));

    }

    private void soapservice()
    {
        String SOAP_ACTION = "WebSithaction/AWSRFIDTEST.Execute";
        String METHOD_NAME = "WsRFIDTest.Execute";
        String NAMESPACE = "WebSith";
        String URL = "http://info.thgye.com.ec/awsrfidtest.aspx";
        ArrLis = mAdapter.listaEtiquetasLeidas();
        List<BankMemoryRfid> tagsRead = new ArrayList<>();

        String msgRespuesta = "";
        /*String json = toJson(ArrLis);*/

        int tarray =ArrLis.size();

        int i = 0;
        boolean itm = false;
        String epc = "";
        while (i < tarray){
            epc = ArrLis.get(i).mUt;
            tagsRead.add(new BankMemoryRfid(epc, epc,epc,"0000000000004321"));
            i++;
        }


        //SoapObject Object = new SoapObject(NAMESPACE,"sdt_rfid_tag");
        //SoapObject Object_sdt_rfid_tags = new SoapObject(NAMESPACE,"Sdt_rfid_tags") ;
        SoapObject Object_sdt_rfid_tag ;
        SoapObject Object_sdt_rfid_tag2 = new SoapObject(NAMESPACE, "sdt_rfid_tag");

        for(BankMemoryRfid tagBank: tagsRead) {

            Object_sdt_rfid_tag = new SoapObject(NAMESPACE, "sdt_rfid_tag");
            Object_sdt_rfid_tag.addProperty("epc", tagBank.epc);
            Object_sdt_rfid_tag.addProperty("tid", tagBank.tid);
            Object_sdt_rfid_tag.addProperty("user", tagBank.user);
            Object_sdt_rfid_tag.addProperty("reserved", tagBank.reserved_);


            Object_sdt_rfid_tag2.addProperty("sdt_rfid_tag",Object_sdt_rfid_tag);

        }

        try {
            SoapObject Request = new SoapObject(NAMESPACE,METHOD_NAME);
            Request.addProperty("Sdt_rfid_tags",Object_sdt_rfid_tag2);


            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.implicitTypes = true;
            soapEnvelope.dotNet = false;

            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transportSE = new HttpTransportSE(URL);
            transportSE.debug = true;


            transportSE.call(SOAP_ACTION,soapEnvelope);
            String requestDump = transportSE.requestDump;
            String responseDump = transportSE.responseDump;
            //SoapPrimitive resultString = (SoapPrimitive)soapEnvelope.getResponse();

            //SoapPrimitive resultRequestSOAP = (SoapPrimitive) soapEnvelope.getResponse();

            SoapObject resultRequestSOAP = (SoapObject) soapEnvelope.getResponse();

            SoapPrimitive SOData =(SoapPrimitive) ((SoapObject)resultRequestSOAP.getProperty("data")).getProperty("tags_quantity");

            //String SOData =   ((SoapObject) resultRequestSOAP.getProperty("data")).getPropertyAsString("tags_quantity");
            String SOState =  ((SoapObject) resultRequestSOAP.getProperty("state")).getPropertyAsString("status") ;

            String TagsRespose = SOData+"";

            if (SOState.equals("00")){

                msgRespuesta = "Los tags recibidos en el servidor es igual a: "+TagsRespose;
            }
            else {
                msgRespuesta = "La respuesta del webservice es fallida codigo recibido: "+SOState;
            }


           /* if (resultRequestSOAP.getValue().toString().equals("00")){
                //Toast.makeText(mContext, "Los datos de los tags leidos se enviaron al servidor correctamente", Toast.LENGTH_LONG);
                msgRespuesta = "Los datos de los tags leidos se enviaron al servidor correctamente";
            }
            else {
                //Toast.makeText(mContext, "La respuesta del webservice es fallida codigo recibido: "+resultRequestSOAP.getValue().toString(),Toast.LENGTH_SHORT);
                msgRespuesta = "La respuesta del webservice es fallida codigo recibido: "+resultRequestSOAP.getValue().toString();

            }*/

        } catch (Exception ex){
            //Toast.makeText(mContext, "Operacion Fallida ERROR: "+ex.getMessage(),Toast.LENGTH_SHORT);
            msgRespuesta = "Operacion Fallida ERROR: "+ex.getMessage();
        }
        finally {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("msgSoap",msgRespuesta);
            //mWebServiceHandler
            msg.setData(bundle);
            mWebServiceHandler.sendMessageDelayed(msg, 1000);

        }
    }

    private  class executeSoapAsync extends AsyncTask<Void, Void, Void>{


        @Override
        protected Void doInBackground(Void... voids) {
            soapservice();
            return null;
        }


    }

    private List<String> listaEpcReal(){
        String epcs = "30386B8541B0FE8000000001,30386B8541B0FE8000000002,30386B8541B0FE8000000003,30386B8541B0FE8000000004,30386B8541B0FE8000000005,30386B8541B0FE8000000006,30386B8541B0FE8000000007,30386B8541B0FE8000000008,30386B8541B0FE8000000009,30386B8541B0FE800000000A,30386B8541B0FE800000000B,30386B8541B0FE800000000C,30386B8541B0FE800000000D,30386B8541B0FEC000000001,30386B8541B0FEC000000002,30386B8541B0FEC000000003,30386B8541B0FEC000000004,30386B8541B0FEC000000005,30386B8541B0FEC000000006,30386B8541B0FEC000000007,30386B8541B0FEC000000008,30386B8541B0FEC000000009,30386B8541B0FEC00000000A,30386B8541B0FEC00000000B,30386B8541B0FEC00000000C,30386B8541B0FEC00000000D,30386B8541B0FEC00000000E,30386B8541B0FEC00000000F,30386B8541B0FEC000000010,30386B8541B0FEC000000011,30386B8541B0FEC000000012,30386B8541B0FEC000000013,30386B8541B0FEC000000014,30386B8541B0FEC000000015,30386B8541B0FEC000000016,30386B8541B0FEC000000017,30386B8541B0FF0000000001,30386B8541B0FF0000000002,30386B8541B1504000000001,30386B8541B1504000000002,30386B8541B1504000000003,30386B8541B1504000000004,30386B8541B1504000000005,30386B8541B1504000000006,30386B8541B1504000000007,30386B8541B1504000000008,30386B8541B1504000000009,30386B8541B150400000000A,30386B8541B150400000000B,30386B8541B150400000000C,30386B8541B150400000000D,30386B8541B150400000000E,30386B8541B150400000000F,30386B8541B1504000000010,30386B8541B1504000000011,30386B8541B1508000000001,30386B8541B1508000000002,30386B8541B1508000000003,30386B8541B1508000000004,30386B8541B1508000000005,30386B8541B1508000000006,30386B8541B1508000000007,30386B8541B1508000000008,30386B8541B1508000000009,30386B8541B150800000000A,30386B8541B150800000000B,30386B8541B150800000000C,30386B8541B150800000000D,30386B8541B150800000000E,30386B8541B150800000000F,30386B8541B1508000000010,30386B8541B1508000000011,30386B8541B1508000000012,30386B8541B1508000000013,30386B8541B1508000000014,30386B8541B150C000000001,30386B8541B150C000000002,30386B8541B150C000000003,30386B8541B150C000000004,30386B8541B150C000000005,30386B8541B150C000000006,30386B8541B150C000000007,30386B8541B150C000000008,30386B8541B150C000000009,30386B8541B150C00000000A,30386B8541B150C00000000B,30386B8541B150C00000000C,30386B8541B150C00000000D,30386B8541B150C00000000E,30386B8541B150C00000000F,30386B8541B150C000000010,30386B8541B150C000000011,30386B8541B1510000000001,30386B8541B1510000000002,30386B8541B1510000000003,30386B8541B1510000000004,30386B8541B1510000000005,30386B8541B1510000000006,30386B8541B1510000000007,30386B8541B1510000000008,30386B8541B1510000000009,30386B8541B151000000000A,30386B8541B1514000000001,30386B8541B1514000000002,30386B8541B1514000000003,30386B8541B1518000000001,30386B8541B1518000000002,30386B8541B1518000000003,30386B8541B1518000000004,30386B8541B1518000000005,30386B8541B1518000000006,30386B8541B1518000000007,30386B8541B1518000000008,30386B8541B1518000000009,30386B8541B151800000000A,30386B8541B151800000000B,30386B8541B151800000000C,30386B8541B151800000000D,30386B8541B151800000000E,30386B8541B151800000000F,30386B8541B1518000000010,30386B8541B1518000000011,30386B8541B151C000000001,30386B8541B151C000000002,30386B8541B151C000000003,30386B8541B151C000000004,30386B8541B151C000000005,30386B8541B151C000000006,30386B8541B151C000000007,30386B8541B151C000000008,30386B8541B151C000000009,30386B8541B151C00000000A,30386B8541B151C00000000B,30386B8541B151C00000000C,30386B8541B151C00000000D,30386B8541B151C00000000E,30386B8541B151C00000000F,30386B8541B151C000000010,30386B8541B151C000000011,30386B8541B151C000000012,30386B8541B151C000000013,30386B8541B151C000000014,30386B8541B1520000000001,30386B8541B1520000000002,30386B8541B1520000000003,30386B8541B1520000000004,30386B8541B1520000000005,30386B8541B1520000000006,30386B8541B1520000000007,30386B8541B1520000000008,30386B8541B1520000000009,30386B8541B152000000000A,30386B8541B152000000000B,30386B8541B152000000000C,30386B8541B152000000000D,30386B8541B152000000000E,30386B8541B152000000000F,30386B8541B1520000000010,30386B8541B1520000000011,30386B8541B1524000000001,30386B8541B1524000000002,30386B8541B1524000000003,30386B8541B1524000000004,30386B8541B1524000000005,30386B8541B1524000000006,30386B8541B1524000000007,30386B8541B1524000000008,30386B8541B1524000000009,30386B8541B152400000000A,30386B8541B1528000000001,30386B8541B1528000000002,30386B8541B1528000000003,30386B8541B152C000000001,30386B8541B152C000000002,30386B8541B152C000000003,30386B8541B152C000000004,30386B8541B152C000000005,30386B8541B152C000000006,30386B8541B152C000000007,30386B8541B152C000000008,30386B8541B152C000000009,30386B8541B152C00000000A,30386B8541B1530000000001,30386B8541B1530000000002,30386B8541B1530000000003,30386B8541B1530000000004,30386B8541B1530000000005,30386B8541B1530000000006,30386B8541B1530000000007,30386B8541B1530000000008,30386B8541B1530000000009,30386B8541B153000000000A,30386B8541B153000000000B,30386B8541B153000000000C,30386B8541B153000000000D,30386B8541B153000000000E,30386B8541B153000000000F,30386B8541B1534000000001,30386B8541B1534000000002,30386B8541B1534000000003,30386B8541B1534000000004,30386B8541B1534000000005,30386B8541B1534000000006,30386B8541B1534000000007,30386B8541B1534000000008,30386B8541B1534000000009,30386B8541B153400000000A,30386B8541B1538000000001,30386B8541B1538000000002,30386B8541B1538000000003,30386B8541B1538000000004,30386B8541B1538000000005,30386B8541991BC000000006,30386B8541991BC000000007,30386B8541991BC000000008,30386B8541991BC000000009,30386B8541991BC00000000A,30386B8541B0FF0000000003,30386B8541B0FF0000000004,30386B8541B0FF0000000005,30386B8541B0FF0000000006,30386B8541B0FF0000000007,30386B8541B0FF0000000008,30386B8541B0FF0000000009,30386B8541B0FF000000000A,30386B8541B0FF000000000B,30386B8541B0FF000000000C,30386B8541B0FF000000000D,30386B8541B0FF000000000E,30386B8541B0FF000000000F,30386B8541B0FF0000000010,30386B8541B0FF4000000001,30386B8541B0FF4000000002,30386B8541B0FF4000000003,30386B8541B0FF4000000004,30386B8541B0FF4000000005,30386B8541B0FF4000000006,30386B8541B0FF4000000007,30386B8541B0FF4000000008,30386B8541B0FF4000000009,30386B8541B0FF400000000A,30386B8541B0FF400000000B,30386B8541B0FF8000000001,30386B8541B0FF8000000002,30386B8541B0FF8000000003,30386B8541B0FF8000000004";

        String[] ArrayEpcs = epcs.split(",");
        List<String> listEpc = new ArrayList<String>();
        for (int i = 0; i< ArrayEpcs.length; i++){
            listEpc.add(ArrayEpcs[i]);
        }

        return listEpc;
    }

    private  class executeSoapGuiaEntradaDetalleAsync extends AsyncTask<Void, Void, Void> {

        EGDetailResponse egDetailResponse;
        ProgressDialog progressDialog  ;
        @Override
        protected Void doInBackground(Void... voids) {

            rfidService.SOAP_ACTION_ =  mWSParameters[0];
            rfidService.METHOD_NAME_ =  mWSParameters[1];
            rfidService.NAMESPACE_ = mWSParameters[2];
            rfidService.URL_ = mWSParameters[3];

            egDetailResponse = rfidService.GuiaEntradaDetalleService2(NoGuia,ListEpcRead);

            //egDetailResponse = rfidService.GuiaEntradaDetalleService2(NoGuia,listaEpcReal());

            //entryGuideDetail = rfidService.GuiaEntradaDetalleService(NoGuia);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);

            progressDialog.cancel();
            if(egDetailResponse != null){

                if(egDetailResponse.getStatus() != null ){
                    if(egDetailResponse.getStatus().getCodigo().equals("00")){
                        egDetailResponseLocating = egDetailResponse;
                        LlenarGrid(egDetailResponse);
                    }
                    else {
                        Toast.makeText(mContext, egDetailResponse.getStatus().getDescripcion() , Toast.LENGTH_SHORT).show();
                    }
                }

            }

           /* if(egDetailResponse.status != null && egDetailResponse.status.codigo.equals("00")){

                egDetailResponseLocating = egDetailResponse;
                //AgruparItemCodigoEpc();
                LlenarGrid(egDetailResponse);
                //Toast.makeText(mContext, "Llamada a Web Services Correctamente", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(mContext, egDetailResponse.status.getDescripcion() , Toast.LENGTH_SHORT).show();
            }*/
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

    /*private  class SoapEnvioTagsGeneralAsync extends AsyncTask<Void, Void, Void> {

        SendTags response;
        @Override
        protected Void doInBackground(Void... voids) {
            // mWSParameters = getResources().getStringArray(R.array.WSparameter_RfidTest);
            rfidService.SOAP_ACTION_ =  mWSParameters[0];
            rfidService.METHOD_NAME_ =  mWSParameters[1];
            rfidService.NAMESPACE_ = mWSParameters[2];
            rfidService.URL_ = mWSParameters[3];
            response = rfidService.EnvioTagsEpcGeneral(ListEpcRead);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

          Toast.makeText(mContext, response.getMessage(), Toast.LENGTH_LONG).show();
        }
    }*/

    private void LlenarGrid(EGDetailResponse detailResponse){

        //egProcesado = SimularWSEGProcess();

        //tblItemDif.removeAllViews();
        egProcesado = WSEGProcess(detailResponse);
        if(egProcesado != null && egProcesado.items != null && egProcesado.items.size() > 0)
        {
            mlayoutHeader.setVisibility(View.VISIBLE);
            ProcesarLvItemsDif();
        }
        else {
            Toast.makeText(mContext,"El Servicio no trajo informacion", Toast.LENGTH_SHORT).show();
        }

        //createColumns();
        //CrearRow();
    }

    private void LlenarGridTagNoEnc(String itemCodigo)
    {
        tblItemsNoEnc.removeAllViews();
        createColumnsTagNoEnc();
        item item_tagNoRead = findTagNoLeido(itemCodigo);
        createRowTagNoEnc(item_tagNoRead.tagsNoLeidos);
    }

    public item findTagNoLeido(String itemCodigo)
    {
        for(item item_ : egProcesado.items)
        {
            if(item_.getItemCodigo().equalsIgnoreCase(itemCodigo))
            {
                return  item_;
            }
        }
        return  null;
    }

    public EGTagsResponseItem findItemSeleccionado(String itemcodigo){
        for(EGTagsResponseItem item_ : egDetailResponseLocating.getItems())
        {
            if(item_.getItemCodigo().equalsIgnoreCase(itemcodigo))
            {
                return  item_;
            }
        }
        return  null;
    }

    private void createRowTagNoEnc(TagNoRead tags){
        for (String epc : tags.epc){
            TableRow tableRow = new TableRow(mContext);
            tableRow.setLayoutParams(
                    new TableRow.LayoutParams(
                            TableRow.LayoutParams.FILL_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                    )
            );

            tableRow.setOnClickListener( new View.OnClickListener(){
                                             @Override
                                             public  void  onClick(View view)
                                             {
                                                 TableRow currentRow = (TableRow) view;
                                                 String epc = ((TextView)currentRow.getChildAt(0)).getText().toString();

                                                 mLocateTag = epc;
                                                 mLocateStartPos = 0;
                                                 /*if (i.mHasPc)
                                                     mLocateEPC = mLocateTag.substring(4, mLocateTag.length());
                                                 else
                                                     mLocateEPC = mLocateTag;*/
                                                 mLocateEPC = mLocateTag.substring(4, mLocateTag.length());

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
                                                     }
                                                 });
                                                 alert.setNegativeButton(getString(R.string.no_str) ,new DialogInterface.OnClickListener() {
                                                     public void onClick(DialogInterface dialog, int whichButton) {
                                                     }
                                                 });
                                                 alert.show();
                                             }

                                         }

            );

            TextView textViewEntrada = new TextView(mContext);
            textViewEntrada.setText(epc);
            textViewEntrada.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            textViewEntrada.setPadding(5,5,5,0);
            tableRow.addView(textViewEntrada);

            tblItemsNoEnc.addView(tableRow, new TableLayout.LayoutParams(
                    TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));
        }
    }
    private void createColumnsTagNoEnc()
    {
        TableRow tableRow = new TableRow(mContext);
        tableRow.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.FILL_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                )
        );


        TextView textViewEntrada = new TextView(mContext);
        textViewEntrada.setText("Cod.EPC");
        textViewEntrada.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        textViewEntrada.setPadding(5,5,5,0);
        tableRow.addView(textViewEntrada);


        tblItemsNoEnc.addView(tableRow, new TableLayout.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));

        // add divider
        tableRow = new TableRow(mContext);
        tableRow.setLayoutParams(
                new TableRow.LayoutParams(
                        TableRow.LayoutParams.FILL_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                )
        );

        textViewEntrada = new TextView(mContext);
        textViewEntrada.setText("____________");
        textViewEntrada.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        textViewEntrada.setPadding(5,5,5,0);
        tableRow.addView(textViewEntrada);


        tblItemsNoEnc.addView(tableRow, new TableLayout.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));

    }

    private void createColumns()
    {
        TableRow tableRow = new TableRow(mContext);
        tableRow.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.FILL_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                )
        );


        TextView textViewEntrada = new TextView(mContext);
        textViewEntrada.setText("Cod.item");
        textViewEntrada.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        textViewEntrada.setPadding(5,5,5,0);
        tableRow.addView(textViewEntrada);


        TextView textViewCant = new TextView(mContext);
        textViewCant.setText("Cantidad");
        textViewCant.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        textViewCant.setPadding(5,5,5,0);
        tableRow.addView(textViewCant);

        TextView textViewStatus = new TextView(mContext);
        textViewStatus.setText("Leidos");
        textViewStatus.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        textViewStatus.setPadding(5,5,5,0);
        tableRow.addView(textViewStatus);


        tblItemDif.addView(tableRow, new TableLayout.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));

        // add divider
        tableRow = new TableRow(mContext);
        tableRow.setLayoutParams(
                new TableRow.LayoutParams(
                        TableRow.LayoutParams.FILL_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                )
        );

        textViewEntrada = new TextView(mContext);
        textViewEntrada.setText("____________");
        textViewEntrada.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        textViewEntrada.setPadding(5,5,5,0);
        tableRow.addView(textViewEntrada);

        // name column
        textViewCant = new TextView(mContext);
        textViewCant.setText("____________");
        textViewCant.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        textViewCant.setPadding(5,5,5,0);
        tableRow.addView(textViewCant);

        // name Price
        textViewStatus = new TextView(mContext);
        textViewStatus.setText("____________");
        textViewStatus.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        textViewStatus.setPadding(5,5,5,0);
        tableRow.addView(textViewStatus);


        tblItemDif.addView(tableRow, new TableLayout.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));

    }

    private void CrearRow(){
        for (item item_ : egProcesado.items){
            TableRow tableRow = new TableRow(mContext);
            tableRow.setLayoutParams(
                    new TableRow.LayoutParams(
                            TableRow.LayoutParams.FILL_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                    )
            );

            tableRow.setOnClickListener( new View.OnClickListener(){
                                             @Override
                                             public  void  onClick(View view){
                                                //LlenarGridTagNoEnc
                                                 TableRow currentRow = (TableRow) view;
                                                 String itemCodigo = ((TextView)currentRow.getChildAt(0)).getText().toString();
                                                 LlenarGridTagNoEnc(itemCodigo);
                                             }
                                         }

            );

            TextView textViewEntrada = new TextView(mContext);
            textViewEntrada.setText(item_.itemCodigo);
            textViewEntrada.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            textViewEntrada.setPadding(5,5,5,0);
            tableRow.addView(textViewEntrada);

            TextView textViewCant = new TextView(mContext);
            textViewCant.setText(String.valueOf(item_.cantidad));
            textViewCant.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            textViewCant.setPadding(5,5,5,0);
            tableRow.addView(textViewCant);

            TextView textViewStatus = new TextView(mContext);
            textViewStatus.setText(String.valueOf(item_.cantidadLeidos));
            textViewStatus.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            textViewStatus.setPadding(5,5,5,0);
            tableRow.addView(textViewStatus);



            tblItemDif.addView(tableRow, new TableLayout.LayoutParams(
                    TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));
        }

    }

    private void ProcesarLvItemsDif()
    {
        mlv_itemsDif.setAdapter(null);

        /*if(first){
            View headerview = View.inflate(mContext, R.layout.header_entry_guide, null);
            mlv_itemsDif.addHeaderView(headerview,null,false);
            first = false;
        }*/
        mlv_itemsDif.setAdapter(new CustomListAdapterEntryGuide(mContext, egProcesado.items));
        mlv_itemsDif.setOnItemClickListener(listItemClickListener);
    }

    private AdapterView.OnItemClickListener listItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            item i = (item) mlv_itemsDif.getItemAtPosition(position);
            item item_tagNoRead = findTagNoLeido(i.itemCodigo);
            LlenarDatosProgressBarLocalizacion(item_tagNoRead.itemCodigo);
            ProcesarLv_tagsNoEnc(item_tagNoRead.tagsNoLeidos.getEpc());
        }
    };

    private void LlenarDatosProgressBarLocalizacion(String skuSelecionado)
    {
        egTagsResponseItem_ = findItemSeleccionado(skuSelecionado);
    }

    private void ProcesarLv_tagsNoEnc(List<String> listEpc)
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, listEpc);
        mlv_tagsNoEnc.setAdapter(adapter);
        mlv_tagsNoEnc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String epc = (String) mlv_tagsNoEnc.getItemAtPosition(position);

                mLocateTag = epc;
                //mLocateStartPos = 0;
                mLocateStartPos = (lectureHasPc ? 0 : 4);
               /* if (i.mHasPc)
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

                //alert.setPositiveButton(getString(R.string.yes_str)
                alert.setPositiveButton(getString(R.string.yes_str), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        SelectionCriterias s = new SelectionCriterias();
                        s.makeCriteria(SelectionCriterias.SCMemType.EPC, mLocateTag,
                                mLocateStartPos, (mLocateEPC.length()+4) * 4,
                                SelectionCriterias.SCActionType.ASLINVA_DSLINVB);
                        mReader.RF_SetSelection(s);
                        switchLayout(false);
                        mLocateTv.setText(mLocateTag);

                        if(egTagsResponseItem_ != null){
                            mtag_locate_grupo1.setText(egTagsResponseItem_.getItemGrupo1());
                            mtag_locate_grupo2.setText(egTagsResponseItem_.getItemGrupo2());
                            mtag_locate_grupo3.setText(egTagsResponseItem_.getItemGrupo3());
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

    private EGProcesado SimularWSEGProcess()
    {
        EGProcesado egProcesado = new EGProcesado(null);
        List<String> epcs = new ArrayList<String>();
        epcs.add("42836557bf7194e4000001a85");
        epcs.add("E20000193010025616706B68");
        epcs.add("42836557bf7194e4000003a85");

        TagNoRead tagNoRead = new TagNoRead(epcs);
        item item1 = new item("M176531872ABLAR",100, 90,tagNoRead);
        List<item> itemList = new ArrayList<item>();
        itemList.add(item1);

        //M176531872ABMED
        epcs = new ArrayList<String>();
        epcs.add("303556022843A3C00000000D");
        epcs.add("15831457bf7194e4000002a85");
        epcs.add("15831457bf7194e4000006a85");

        tagNoRead = new TagNoRead(epcs);
        item1 = new item("M176531872ABMED",80, 78,tagNoRead);
        itemList.add(item1);

        egProcesado = new EGProcesado(itemList);

        return egProcesado;
    }


    private EGProcesado WSEGProcess(EGDetailResponse detailResponse)
    {
        EGProcesado egProcesado = new EGProcesado(null);
        item _item = null;
        List<item> itemList = new ArrayList<item>();
        TagNoRead tagNoRead = null ;
        List<String> taglectura = null;

        for (EGTagsResponseItem i :detailResponse.getItems()) {

            taglectura = new ArrayList<String>();

            for (String j :i.getDataNoLeido()) {
                taglectura.add(j);
            }
            tagNoRead = new TagNoRead(taglectura);
            _item = new item(i.itemCodigo,(i.CantidadLeidos + i.CantidadNoLeidos),i.CantidadLeidos,tagNoRead);
            itemList.add(_item);
        }
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


    /*public String someMethod() throws MalformedURLException, IOException {


        //Code to make a webservice HTTP request
        String responseString = "";
        String outputString = "";
        String wsURL = "<Endpoint of the webservice to be consumed>";
        URL url = new URL(wsURL);
        URLConnection connection = url.openConnection();
        HttpURLConnection httpConn = (HttpURLConnection) connection;
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        String xmlInput = "entire SOAP Request";

        byte[] buffer = new byte[xmlInput.length()];
        buffer = xmlInput.getBytes();
        bout.write(buffer);
        byte[] b = bout.toByteArray();
        String SOAPAction = "<SOAP action of the webservice to be consumed>";
        // Set the appropriate HTTP parameters.
        httpConn.setRequestProperty("Content-Length",
                String.valueOf(b.length));
        httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        httpConn.setRequestProperty("SOAPAction", SOAPAction);
        httpConn.setRequestMethod("POST");
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);
        OutputStream out = httpConn.getOutputStream();
        //Write the content of the request to the outputstream of the HTTP Connection.
        out.write(b);
        out.close();
        //Ready with sending the request.

        //Read the response.
        InputStreamReader isr = null;
        if (httpConn.getResponseCode() == 200) {
            isr = new InputStreamReader(httpConn.getInputStream());
        } else {
            isr = new InputStreamReader(httpConn.getErrorStream());
        }

        BufferedReader in = new BufferedReader(isr);

        //Write the SOAP message response to a String.
        while ((responseString = in.readLine()) != null) {
            outputString = outputString + responseString;
        }
        //Parse the String output to a org.w3c.dom.Document and be able to reach every node with the org.w3c.dom API.
        Document document = parseXmlFile(outputString); // Write a separate method to parse the xml input.
        NodeList nodeLst = document.getElementsByTagName("<TagName of the element to be retrieved>");
        String elementValue = nodeLst.item(0).getTextContent();
        System.out.println(elementValue);

        //Write the SOAP message formatted to the console.
        String formattedSOAPResponse = formatXML(outputString); // Write a separate method to format the XML input.
        System.out.println(formattedSOAPResponse);
        return elementValue;
    }*/

    private String toJson(Object myObj){
        Gson gson = new Gson();
        String json = gson.toJson(myObj);
        return  json;
    }

    /*private String toJson(){

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("id", getIdProducto());
            jsonObject.put("nombre", getNombre());
            jsonObject.put("precio", getPrecio());

            return jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }*/

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

                case R.id.pc_switch:
                    if (isChecked)
                        mIgnorePC = true;
                    else
                        mIgnorePC = false;
                    break;
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
        mPCSwitch.setEnabled(b);
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
        private final WeakReference<EntryGuideRead2Fragment> mExecutor;
        public UpdateStopwatchHandler(EntryGuideRead2Fragment f) {
            mExecutor = new WeakReference<>(f);
        }

        @Override
        public void handleMessage(Message msg) {
            EntryGuideRead2Fragment executor = mExecutor.get();
            if (executor != null) {
                executor.handleUpdateStopwatchHandler(msg);
            }
        }
    }



    private static class WebServiceHandler extends Handler {
        private final WeakReference<EntryGuideRead2Fragment> mExecutor;
        public WebServiceHandler(EntryGuideRead2Fragment f) {
            mExecutor = new WeakReference<>(f);
        }

        @Override
        public void handleMessage(Message msg) {
            EntryGuideRead2Fragment executor = mExecutor.get();
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

            String lmCountText = mCountText.getText().toString();
            mtvCantItemLeidos.setText(lmCountText);
            mprogress1.setProgress( Integer.parseInt(lmCountText) );

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
        private final WeakReference<EntryGuideRead2Fragment> mExecutor;
        public InventoryHandler(EntryGuideRead2Fragment f) {
            mExecutor = new WeakReference<>(f);
        }

        @Override
        public void handleMessage(Message msg) {
            EntryGuideRead2Fragment executor = mExecutor.get();
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
                            clearAll();
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
                                btnProcesarEnabledDisabled(false);
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

                            btnProcesarManagement(false);

                            if(!mCountText.getText().equals("0")){
                                btnProcesarEnabledDisabled(true);
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
        }
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
        /*CopyOnWriteArrayList<ListItem> ArrLis = mAdapter.listaEtiquetasLeidas();



        //Iterator iteratorVals = ArrLis.iterator();

        int tarray =ArrLis.size();

        int i = 0;
        boolean itm = false;
        while (i < tarray){
           String x = ArrLis.get(i).mUt;
           if (x.equals(data))
           {
               itm = true;
               break;
           }
           i++;
        }

        if(!itm)
        {
            mAdapter.addItem(-1, data, info, !mIgnorePC, mTagFilter);
            if (mFileManager != null && mFile)
                mFileManager.writeToFile(data);

            mRfidList.setSelection(mRfidList.getAdapter().getCount() - 1);
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
        }*/

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

        if (mPCSwitch != null)
            mPCSwitch.setOnCheckedChangeListener(sledcheckListener);

        if (mFileSwitch != null)
            mFileSwitch.setOnCheckedChangeListener(sledcheckListener);

        if (mSessionSpin != null)
            mSessionSpin.setOnItemSelectedListener(sessionListener);

        if (mSelFlagSpin != null)
            mSelFlagSpin.setOnItemSelectedListener(selFlagListener);
    }

    private void updateButtonState() {
        mPCSwitch.setChecked(mIgnorePC);

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
    private void DialogCompararProcesar(boolean isCompare){
        String alertMsj = "Se va a "+ (isCompare ? "Comparar" : "Procesar")+" la Guia de Entrada";
        AlertDialog.Builder alerta = new AlertDialog.Builder(mContext);
        alerta.setMessage(alertMsj)
                .setCancelable(false)
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(isCompare){
                            EntryGuideCompare();
                        }
                        else {
                            EntryGuideProcesar();
                        }
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


    private void EntryGuideCompare(){
        getListEpcsRead();
        if(ListEpcRead != null && ListEpcRead.size() > 0){
            mWSParameters = getResources().getStringArray(R.array.WSparameter_GuiaEntradaDetalle);
            mlv_tagsNoEnc.setAdapter(null);
            executeSoapGuiaEntradaDetalleAsync tarea = new executeSoapGuiaEntradaDetalleAsync();
            tarea.execute();
        }
        else {
            Toast.makeText(mContext,"No hay Etiquetas con que comparar...",Toast.LENGTH_SHORT).show();

        }
    }
    private void EntryGuideProcesar(){

    }
    private void getListEpcsRead()
    {
        ArrLis = mAdapter.listaEtiquetasLeidas();
        ListEpcRead = new ArrayList<>();
        for (ListItem item : ArrLis){
            ListEpcRead.add(item.mUt);
        }
    }

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

        mSeekBarPower.setProgress(getValueSBar - maxPower);
        mtvSelect.setText(getValueSBar+"");
        mSeekBarPower.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int val = mSeekBarPower.getProgress();
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                val = i + mSeekBarPowerCorrection;
                getValueSBar = val;
                mtvSelect.setText(getValueSBar+"");
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
                Toast.makeText(mContext,"El Valor es: "+getValueSBar+"", Toast.LENGTH_SHORT).show();
                mReader.RF_SetRadioPowerState(getValueSBar);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    /*

    * */

}
