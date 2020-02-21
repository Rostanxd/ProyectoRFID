
package co.kr.bluebird.newrfid.app.bbrfidbtdemo.fragmentvct;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.Constants;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.control.BankMemoryRfid;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.DataSourceDto;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EntryGuideDetail;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.GenericSpinnerDto;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.Replenishment;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ReplenishmentSale;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ReplenishmentWareResult;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.Replenishments;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ReplenismentSaleDetailsDto;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ResponseVal;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.fileutil.FileManager;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.control.ListItem;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.MainActivity;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.R;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.control.TagListAdapter;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.permission.PermissionHelper;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.service.RfidService;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.stopwatch.StopwatchService;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.CustomListAdapter;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.CustomListAdapterReplenishment;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.CustomListAdapterReplenishmentSale;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.Validator;
import co.kr.bluebird.sled.BTReader;
import co.kr.bluebird.sled.SDConsts;

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
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;

import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Collection;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReplenishmentWareFragment extends Fragment {


    public ReplenishmentWareFragment() {
        // Required empty public constructor
    }




    private static final String TAG = ReplenishmentWareFragment.class.getSimpleName();

    private static final boolean D = Constants.INV_D;

    private StopwatchService mStopwatchSvc;

    private TagListAdapter mAdapter;

    private TextView mBatteryText;

    private TextView mTimerText;

    private TextView mCountText;

    private TextView mSpeedCountText;

    private TextView mAvrSpeedCountTest;

    private Button mClearButton;

    private Button mInvenButton, mStopInvenButton, mclean_imgbtn,mresult_imgbtn,msnapShot_imgbtn;

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

    private Context mContext;

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

    private TextView mLocateTv;

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

    private UpdateStopwatchHandler mUpdateStopwatchHandler = new UpdateStopwatchHandler(this);


    private WebServiceHandler mWebServiceHandler = new WebServiceHandler(this);


    private InventoryHandler mInventoryHandler = new InventoryHandler(this);

    private Handler handlerStart ;


    private String NoGuia;

    private ShippingWareGenerateFragment mShippingWareGenerateFragment;

    private Spinner /*mspinnerUbicacionRW,*/ mspinnerSeccionRW;
    private String[] /*mWSparameterUbicacion,*/ mWSparameterSeccion, mWsparameterReposicion, mWsparameterReposicionSaldos, mWsparameterSnapShot;
    private GenericSpinnerDto  /*genericSpinnerDtoUbicacion,*/ genericSpinnerDtoSeccion ;

    private String[] /*spinnerArray = null,*/ spinnerArraySeccion = null;
    private HashMap<Integer,String> /*spinnerMap = null,*/ spinnerMapSeccion = null;

    private  int RFPower = 30;
    private ViewGroup loVistaDialogo;

    //private boolean isRunningRead;

    public static ReplenishmentWareFragment newInstance() {
        return new ReplenishmentWareFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        if (D) Log.d(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.replenishmentware_frag, container, false);

        mContext = inflater.getContext();

        mFragment = this;

        mOptionHandler = ((MainActivity)getActivity()).mUpdateConnectHandler;

        //mRfidList = (ListView)v.findViewById(R.id.rfid_list);

        //mRfidList.setOnItemClickListener(listItemClickListener);

        mLocateLayout = (LinearLayout)v.findViewById(R.id.tag_locate_container);

        mListLayout = (LinearLayout)v.findViewById(R.id.tag_list_container);

        mLocateTv = (TextView)v.findViewById(R.id.tag_locate_text);

        mTagLocateProgress = (ProgressBar)v.findViewById(R.id.tag_locate_progress);

        mBackButton = (ImageButton)v.findViewById(R.id.back_button);
        mBackButton.setOnClickListener(sledListener);

        mTimerText = (TextView)v.findViewById(R.id.timer_text);

        mCountText = (TextView)v.findViewById(R.id.count_text);

        mSpeedCountText = (TextView)v.findViewById(R.id.speed_count_text);

        mAvrSpeedCountTest = (TextView)v.findViewById(R.id.speed_avr_count_text);





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

        myIcon = getResources().getDrawable( R.drawable.ic_materialplay );
        filter = new LightingColorFilter( Color.BLACK, Color.WHITE);
        myIcon.setColorFilter(filter);


        mInvenButton = (Button) v.findViewById(R.id.inven_imgbutton);
        mInvenButton.setCompoundDrawablesWithIntrinsicBounds( myIcon, null, null, null);
        mInvenButton.setOnClickListener(sledListener);

        myIcon = getResources().getDrawable( R.drawable.ic_materialstop );
        filter = new LightingColorFilter( Color.BLACK, Color.WHITE);
        myIcon.setColorFilter(filter);


        mStopInvenButton = (Button) v.findViewById(R.id.stop_inven_imgbutton);
        mStopInvenButton.setCompoundDrawablesWithIntrinsicBounds( myIcon, null, null, null);
        mStopInvenButton.setEnabled(false);
        mStopInvenButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D5D7D6")));
        mStopInvenButton.setOnClickListener(sledListener);

        myIcon = getResources().getDrawable( R.drawable.ic_materialdelete );
        filter = new LightingColorFilter( Color.BLACK, Color.WHITE);
        myIcon.setColorFilter(filter);

        mclean_imgbtn = (Button) v.findViewById(R.id.clean_imgbtn);
        mclean_imgbtn.setCompoundDrawablesWithIntrinsicBounds( myIcon, null, null, null);
        mclean_imgbtn.setOnClickListener(sledListener);



        myIcon = getResources().getDrawable( R.drawable.ic_viewresult );
        filter = new LightingColorFilter( Color.BLACK, Color.WHITE);
        myIcon.setColorFilter(filter);


        mresult_imgbtn = (Button)v.findViewById(R.id.result_imgbtn);
        mresult_imgbtn.setOnClickListener(sledListener);

        mresult_imgbtn.setCompoundDrawablesWithIntrinsicBounds( myIcon, null, null, null);

        myIcon = getResources().getDrawable( R.drawable.ic_snapshot );
        filter = new LightingColorFilter( Color.BLACK, Color.WHITE);
        myIcon.setColorFilter(filter);

        msnapShot_imgbtn = (Button)v.findViewById(R.id.snapShot_imgbtn);
        msnapShot_imgbtn.setOnClickListener(sledListener);

        msnapShot_imgbtn.setCompoundDrawablesWithIntrinsicBounds( myIcon, null, null, null);

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

        //mspinnerUbicacionRW = (Spinner) v.findViewById(R.id.spinnerUbicacionRW);
        mspinnerSeccionRW = (Spinner) v.findViewById(R.id.spinnerSeccionRW);

        mspinnerSeccionRW.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(i != 0 && mAdapter.listTagReadEpc().size() > 0){
                    mresult_imgbtn.setEnabled(true);
                    mresult_imgbtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0097a7")));

                    msnapShot_imgbtn.setEnabled(true);
                    msnapShot_imgbtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0b4096")));

                }
                else {
                    mresult_imgbtn.setEnabled(false);
                    mresult_imgbtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D5D7D6")));

                    msnapShot_imgbtn.setEnabled(false);
                    msnapShot_imgbtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D5D7D6")));

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        bindStopwatchSvc();

        NoGuia = getArguments() != null ? getArguments().getString("NoGuia") : "0";
        //mWSparameterUbicacion = getResources().getStringArray(R.array.WSparameter_ubicacion);
        mWSparameterSeccion =  getResources().getStringArray(R.array.WSparameter_seccion);
        mWsparameterReposicion = getResources().getStringArray(R.array.Wsparameter_Reposicion);
        mWsparameterReposicionSaldos = getResources().getStringArray(R.array.Wsparameter_ReposicionSaldos);
        mWsparameterSnapShot = getResources().getStringArray(R.array.Wsparameter_Snapshot);

        rfidService = new RfidService(mContext);
        exWSUbicacionYSeccionAsync ubicacionYSeccionAsync = new exWSUbicacionYSeccionAsync();
        ubicacionYSeccionAsync.execute();
        entryGuideDetail = null;
        loVistaDialogo = v.findViewById(android.R.id.content);

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

                            mInvenButton.setEnabled(false);
                            mInvenButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D5D7D6")));
                            mStopInvenButton.setEnabled(true);
                            mStopInvenButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EF3C10")));

                            mclean_imgbtn.setEnabled(false);
                            mclean_imgbtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D5D7D6")));

                            mresult_imgbtn.setEnabled(false);
                            mresult_imgbtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D5D7D6")));

                            msnapShot_imgbtn.setEnabled(false);
                            msnapShot_imgbtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D5D7D6")));

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

                        if(mspinnerSeccionRW.getSelectedItemPosition() !=0  && mAdapter.listTagReadEpc().size() > 0){
                            mresult_imgbtn.setEnabled(true);
                            mresult_imgbtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0097a7")));

                            msnapShot_imgbtn.setEnabled(true);
                            msnapShot_imgbtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0b4096")));

                        }


                       /* mresult_imgbtn.setEnabled(true);
                        mresult_imgbtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0097a7")));

                        msnapShot_imgbtn.setEnabled(true);
                        msnapShot_imgbtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0b4096")));*/


                    } else if (ret == SDConsts.RFResult.STOP_FAILED_TRY_AGAIN)
                        Toast.makeText(mContext, "Stop Inventory failed", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.clean_imgbtn:
                    //ArrLis = null;
                    //clearAll();
                    DialogCleanControls();
                    break;
                case R.id.result_imgbtn:
                    result_imgbtn_Extrated2();
                    break;
                case R.id.snapShot_imgbtn:
                    snapShot_imgbtn_Extrated();
                    break;
            }
        }
    };
    private void result_imgbtn_Extrated2(){
        if(mAdapter.listTagReadEpc().size() > 0)
        {
            String codSeccion = spinnerMapSeccion.get(mspinnerSeccionRW.getSelectedItemPosition());
            if(!codSeccion.equals("0")){
                exWSReposicionAsync reposicionAsync = new exWSReposicionAsync();
                reposicionAsync.execute();
            }else {
                Toast.makeText(mContext,"Seleccione una Seccion...", Toast.LENGTH_SHORT).show();
            }

        }
        else {
            Toast.makeText(mContext,"No hay Datos que procesar...", Toast.LENGTH_SHORT).show();
        }

    }
    //OnItemSelectedListener



    private void DialogCleanControls(){
        AlertDialog.Builder alerta = new AlertDialog.Builder(mContext);
        alerta.setMessage("Esta seguro de realizar un limpieza se perderan todos los datos recolectados...")
                .setCancelable(false)
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        clearAll();
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

    private void setDialogSale(List<ReplenishmentSale> replenishmentSaleList)
    {
        /*final Dialog dialog = new Dialog(mContext);
        dialog.setTitle("Saldos en otros locales");
        dialog.setContentView(R.layout.dialog_reposicion_saldo);

        final ListView lv = (ListView) dialog.findViewById(R.id.lv_Replenishment);
        //ViewGroup headerview = (ViewGroup) getLayoutInflater().inflate(R.layout.header_reposicion,lv,false);
        View headerview = View.inflate(mContext, R.layout.header_reposicion_sale, null);
        lv.addHeaderView(headerview);
        lv.setAdapter(new CustomListAdapterReplenishmentSale(mContext, replenishmentSaleList));
        dialog.show();*/

        View dialogView1 = LayoutInflater.from(mContext).inflate(R.layout.dialog_reposicion_saldo, loVistaDialogo, false);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.myDialog));
        builder1.setView(dialogView1);
        final AlertDialog alertDialog1 = builder1.create();

        final ListView lv =  dialogView1.findViewById(R.id.lv_Replenishment);
        final ImageView ivClose = dialogView1.findViewById(R.id.iv_close);

        ivClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog1.dismiss();
            }
        });
        lv.setAdapter(new CustomListAdapterReplenishmentSale(mContext, replenishmentSaleList));
        alertDialog1.show();
    }

    private void SetDataDialog(List<Replenishment> replenishmentList){


       /* final Dialog dialog = new Dialog(mContext);
        dialog.setTitle("Resultados de Reposicion");
        dialog.setContentView(R.layout.dialog_reposicion);

        final ListView lv = (ListView) dialog.findViewById(R.id.lv_Replenishment);
        //ViewGroup headerview = (ViewGroup) getLayoutInflater().inflate(R.layout.header_reposicion,lv,false);
        View headerview = View.inflate(mContext, R.layout.header_reposicion2, null);
        lv.addHeaderView(headerview);

        lv.setAdapter(new CustomListAdapterReplenishment(mContext, replenishmentList));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Replenishment replenishment = (Replenishment) lv.getItemAtPosition(position);

                exWSReposicionSaldoAsync reposicionSaldoAsync = new exWSReposicionSaldoAsync();
                reposicionSaldoAsync.execute(replenishment.getItmCodigo());
            }
        });
        dialog.show();*/

        View dialogView1 = LayoutInflater.from(mContext).inflate(R.layout.dialog_reposicion, loVistaDialogo, false);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.myDialog));
        builder1.setView(dialogView1);
        final AlertDialog alertDialog1 = builder1.create();

        final ListView lv =  dialogView1.findViewById(R.id.lv_Replenishment);
        final ImageView ivClose = dialogView1.findViewById(R.id.iv_close);

        ivClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog1.dismiss();
            }
        });
        lv.setAdapter(new CustomListAdapterReplenishment(mContext, replenishmentList));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Replenishment replenishment = (Replenishment) lv.getItemAtPosition(position);

                exWSReposicionSaldoAsync reposicionSaldoAsync = new exWSReposicionSaldoAsync();
                reposicionSaldoAsync.execute(replenishment.getItmCodigo());
            }
        });

        alertDialog1.show();

    }

    private void snapShot_imgbtn_Extrated()
    {
        //tomar la info de combos + los epc contados se envian en el request
        if(mAdapter.listTagReadEpc().size() > 0){

            String codSeccion = spinnerMapSeccion.get(mspinnerSeccionRW.getSelectedItemPosition());
            if(!codSeccion.equals("0")){
                exWSSnapShotAsync snapShotAsync = new exWSSnapShotAsync();
                snapShotAsync.execute();
            }else {
                Toast.makeText(mContext,"Seleccione una Seccion...", Toast.LENGTH_SHORT).show();
            }


        }else {
            Toast.makeText(mContext,"No hay Datos que procesar...", Toast.LENGTH_SHORT).show();
        }

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
        private final WeakReference<ReplenishmentWareFragment> mExecutor;
        public UpdateStopwatchHandler(ReplenishmentWareFragment f) {
            mExecutor = new WeakReference<>(f);
        }

        @Override
        public void handleMessage(Message msg) {
            ReplenishmentWareFragment executor = mExecutor.get();
            if (executor != null) {
                executor.handleUpdateStopwatchHandler(msg);
            }
        }
    }



    private static class WebServiceHandler extends Handler {
        private final WeakReference<ReplenishmentWareFragment> mExecutor;
        public WebServiceHandler(ReplenishmentWareFragment f) {
            mExecutor = new WeakReference<>(f);
        }

        @Override
        public void handleMessage(Message msg) {
            ReplenishmentWareFragment executor = mExecutor.get();
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
        private final WeakReference<ReplenishmentWareFragment> mExecutor;
        public InventoryHandler(ReplenishmentWareFragment f) {
            mExecutor = new WeakReference<>(f);
        }

        @Override
        public void handleMessage(Message msg) {
            ReplenishmentWareFragment executor = mExecutor.get();
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
                            //String codSeccion = spinnerMapSeccion.get(mspinnerSeccionRW.getSelectedItemPosition());
                            if(mspinnerSeccionRW.getSelectedItemPosition() !=0  && mAdapter.listTagReadEpc().size() > 0){
                                mresult_imgbtn.setEnabled(true);
                                mresult_imgbtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0097a7")));

                                msnapShot_imgbtn.setEnabled(true);
                                msnapShot_imgbtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0b4096")));

                            }
                            else {
                                mresult_imgbtn.setEnabled(false);
                                mresult_imgbtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D5D7D6")));

                                msnapShot_imgbtn.setEnabled(false);
                                msnapShot_imgbtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D5D7D6")));

                            }
                        }
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

    //+++NTNS
//    private void processReadDataCustom(String data) {
//        updateCountText();
//        StringBuilder tagSb = new StringBuilder();
//        tagSb.setLength(0);
//        String rssi = "";
//        String customData = "";
//        if (data.contains(";")) {
//            if (D) Log.d(TAG, "full tag = " + data);
//            //full tag example = "3000123456783333444455556666;rssi:-54.8^custom=2348920384"
//            int customdDataPoint = data.indexOf('^');
//            customData = data.substring(customdDataPoint, data.length());
//            int customPoint = customData.indexOf('=') + 1;
//            customData = customData.substring(customPoint, customData.length());
//            if (D) Log.d(TAG, "custom data = " + customData);
//            data = data.substring(0, customdDataPoint);
//
//            int rssiTagPoint = data.indexOf(';');
//            rssi = data.substring(rssiTagPoint, data.length());
//            int rssiPoint = rssi.indexOf(':') + 1;
//            rssi = rssi.substring(rssiPoint, rssi.length());
//            if (D) Log.d(TAG, "rssi tag = " + rssi);
//            data = data.substring(0, rssiTagPoint);
//
//            if (D) Log.d(TAG, "data tag = " + data);
//            data = data + "\n" + customData;
//        }
//        if (rssi != "") {
//            Activity activity = getActivity();
//            if (activity != null)
//                rssi = activity.getString(R.string.rssi_str) + rssi;
//        }
//        mAdapter.addItem(-1, data, rssi, mTagFilter);
//        if (mSoundPlay) {
//            if (mSoundTask == null) {
//                mSoundTask = new SoundTask();
//                mSoundTask.execute();
//            }
//            else {
//                if (mSoundTask.getStatus() == Status.FINISHED) {
//                    mSoundTask.cancel(true);
//                    mSoundTask = null;
//                    mSoundTask = new SoundTask();
//                    mSoundTask.execute();
//                }
//            }
//        }
//        mRfidList.setSelection(mRfidList.getAdapter().getCount() - 1);
//        if (!mInventory) {
//            updateCountText();
//            updateSpeedCountText();
//            updateAvrSpeedCountText();
//        }
//    }
    //---NTNS

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

    // metodos asyncronos

    private  class exWSUbicacionYSeccionAsync extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog progressDialog;
        @Override
        protected Void doInBackground(Void... voids) {

            rfidService.SOAP_ACTION_ =  mWSparameterSeccion[0];
            rfidService.METHOD_NAME_ =  mWSparameterSeccion[1];
            rfidService.NAMESPACE_ = mWSparameterSeccion[2];
            rfidService.URL_ = mWSparameterSeccion[3];

            genericSpinnerDtoSeccion = rfidService.WSSeccion();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.cancel();

            if(genericSpinnerDtoSeccion != null && genericSpinnerDtoSeccion.estado != null && genericSpinnerDtoSeccion.estado.codigo.equals("00")){
                try{
                    mspinnerSeccionSetItems();
                }
                catch (Exception ex){
                    Toast.makeText(mContext, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(mContext, "Ocurrio un error al cargar las Secciones: "+genericSpinnerDtoSeccion.estado.getDescripcion() , Toast.LENGTH_SHORT).show();
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


    private  class exWSReposicionAsync extends AsyncTask<Void, Void, Void>
    {
        //String codUbicacion = spinnerMap.get(mspinnerUbicacionRW.getSelectedItemPosition());
        String codSeccion = spinnerMapSeccion.get(mspinnerSeccionRW.getSelectedItemPosition());
        Replenishments replenishmentList;
        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Void... voids) {


            rfidService.SOAP_ACTION_ =  mWsparameterReposicion[0];
            rfidService.METHOD_NAME_ =  mWsparameterReposicion[1];
            rfidService.NAMESPACE_ = mWsparameterReposicion[2];
            rfidService.URL_ = mWsparameterReposicion[3];

            //replenishmentList = rfidService.WSReposicion(mAdapter.listTagReadEpc(),codSeccion, "");
            replenishmentList = DummyDataReposicion();

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {

            Validator validator = new Validator();

            ResponseVal responseVal = validator.getValidateDataSourceDto(replenishmentList.getEstado());

            if(responseVal.isValidAccess()){
                SetDataDialog(replenishmentList.getReplenishments());
            }
            else {
                Toast.makeText(mContext,replenishmentList.getEstado().getDescripcion(),Toast.LENGTH_LONG).show();
            }

            progressDialog.cancel();
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


    private  class exWSReposicionSaldoAsync extends AsyncTask<String, Void, Void>
    {
        ProgressDialog progressDialog;
        ReplenismentSaleDetailsDto replenishmentSales;

        @Override
        protected Void doInBackground(String... itemCodigo) {


            rfidService.SOAP_ACTION_ =  mWsparameterReposicionSaldos[0];
            rfidService.METHOD_NAME_ =  mWsparameterReposicionSaldos[1];
            rfidService.NAMESPACE_ = mWsparameterReposicionSaldos[2];
            rfidService.URL_ = mWsparameterReposicionSaldos[3];

            //replenishmentSales = rfidService.WsReposicionSaldoDetalle(itemCodigo[0]);
            replenishmentSales = DummyDataReposicionSale();

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {

            Validator validator = new Validator();
            ResponseVal responseVal =  validator.getValidateDataSourceDto(replenishmentSales.getEstado());
            if(responseVal.isValidAccess()){
                if(replenishmentSales.getReplenishmentSales() != null && replenishmentSales.getReplenishmentSales().size() > 0){
                    setDialogSale(replenishmentSales.getReplenishmentSales());
                }
                else {
                    Toast.makeText(mContext, "No existen saldos que mostrar", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(mContext, responseVal.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }

            progressDialog.cancel();
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


    private  class exWSSnapShotAsync extends AsyncTask<Void, Void, Void>
    {
        String codSeccion = spinnerMapSeccion.get(mspinnerSeccionRW.getSelectedItemPosition());
        DataSourceDto dtoSnapShot;
        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Void... voids) {


            rfidService.SOAP_ACTION_ =  mWsparameterSnapShot[0];
            rfidService.METHOD_NAME_ =  mWsparameterSnapShot[1];
            rfidService.NAMESPACE_ = mWsparameterSnapShot[2];
            rfidService.URL_ = mWsparameterSnapShot[3];

            dtoSnapShot = rfidService.WsSnapShot(mAdapter.listTagReadEpc(),codSeccion);

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {

            Validator validator = new Validator();
            ResponseVal responseVal = validator.getValidateDataSourceDto(dtoSnapShot);

            if(responseVal.isValidAccess()){
                Toast.makeText(mContext, "El SnapShot fue Exitoso",Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(mContext, responseVal.getErrorMsg(),Toast.LENGTH_LONG).show();
            }

            progressDialog.cancel();
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

    /*private void mspinnerUbicacionSetItems()
    {

        spinnerArray = new String[genericSpinnerDtoUbicacion.getColeccion().size()];
        spinnerMap = new HashMap<Integer, String>();

        int i = 0;
        for (DataSourceDto dto:genericSpinnerDtoUbicacion.getColeccion()) {
            spinnerMap.put(i,dto.codigo);
            spinnerArray[i] = dto.descripcion;
            i++;
        }
        ArrayAdapter<String> adapter1 =new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item,spinnerArray);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mspinnerUbicacionRW.setAdapter(adapter1);

    }*/

    private void mspinnerSeccionSetItems()
    {

        spinnerArraySeccion = new String[genericSpinnerDtoSeccion.getColeccion().size()];
        spinnerMapSeccion = new HashMap<Integer, String>();

        int i = 0;
        for (DataSourceDto dto:genericSpinnerDtoSeccion.getColeccion()) {
            spinnerMapSeccion.put(i,dto.codigo);
            spinnerArraySeccion[i] = dto.descripcion;
            i++;
        }


        ArrayAdapter<String> adapter1 =new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item,spinnerArraySeccion);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mspinnerSeccionRW.setAdapter(adapter1);

    }

    private Replenishments DummyDataReposicion(){

        List<Replenishment> replenishmentList = new ArrayList<Replenishment>();

        for (int i = 1 ; i< 1000; i++){
            Replenishment replenishment = new Replenishment();
            replenishment.setItmCodigo("Item00000"+i);
            replenishment.setStock_local(100+i);
            replenishment.setStock_otros(100+i);
            replenishment.setCantidad_ventas(100+i);
            replenishment.setCantidad_egresos(100+i);
            replenishment.setDiferencia(100+i);
            replenishment.setCantidad_anterior(100+i);
            replenishment.setCantidad_actual(100+i);

            replenishmentList.add(replenishment);
        }

        Replenishments replenishments = new Replenishments();
        replenishments.setEstado(new DataSourceDto("00", "Exitoso", null));
        replenishments.setReplenishments(replenishmentList);
        return replenishments;
    }

    private ReplenismentSaleDetailsDto DummyDataReposicionSale(){

        ReplenismentSaleDetailsDto replenismentSaleDetailsDto = new ReplenismentSaleDetailsDto();
        List<ReplenishmentSale> replenishmentSaleList = new ArrayList<ReplenishmentSale>();
        replenismentSaleDetailsDto.setEstado(new DataSourceDto("00", "Exitoso", null));

        for(int i=0; i< 1000; i++){
            ReplenishmentSale replenishmentSale = new ReplenishmentSale();
            replenishmentSale.setCantidad(99999+i);
            replenishmentSale.setLocalname("Local Comercial 00"+i);
            replenishmentSaleList.add(replenishmentSale);
        }
        replenismentSaleDetailsDto.setReplenishmentSales(replenishmentSaleList);
        return replenismentSaleDetailsDto;

    }
}

