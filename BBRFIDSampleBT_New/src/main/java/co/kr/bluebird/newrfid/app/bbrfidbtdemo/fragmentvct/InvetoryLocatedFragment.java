package co.kr.bluebird.newrfid.app.bbrfidbtdemo.fragmentvct;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.ref.WeakReference;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.R;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.LocatedInvData;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.SoundPoolData;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.CustomListAdapterInvLocated;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.LocatedTimer;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.SonidoBeep;
import co.kr.bluebird.sled.BTReader;
import co.kr.bluebird.sled.SDConsts;
import co.kr.bluebird.sled.SelectionCriterias;

/**
 * A simple {@link Fragment} subclass.
 */
public class InvetoryLocatedFragment extends Fragment {


    private Context mContext;
    private Fragment mFragment;
    private LocatedInvData locatedInvData;
    private TextView mtvOrdenCompra, mtvGuia, mtvItemSku, mtv_doc_origen, mtv_doc_destino, mtv_motivo;
    private ListView mlvEpcs;
    private BTReader mReader;
    private ViewGroup viewGroup1;
    private ProgressBar mLocateProgress;
    private int mLocateValue;
    private SoundPoolData soundPoolData;
    private SoundTask mSoundTask;
    private String epc_;
    private boolean isOpenAlertDialog ;
    private LinearLayout mlayoutGuiaEntrada, mlayoutRecepcion;

    /*private TimerTask mLocateTimerTask;
    private Timer mClearLocateTimer;*/

    private LocatedTimer locatedTimer;

    public InvetoryLocatedFragment() {
        // Required empty public constructor
    }

    private InventoryHandler mInventoryHandler = new InventoryHandler(this);

    public static InvetoryLocatedFragment newInstance() {
        return new InvetoryLocatedFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.inventorylocated_frag, container, false);

        View v = inflater.inflate(R.layout.inventorylocated_frag, container, false);
        mContext = inflater.getContext();
        mFragment = this;
        locatedInvData = (LocatedInvData) getArguments().getSerializable("LocatedInv");
        InicializateControls(v);

        mReader = BTReader.getReader(mContext, mInventoryHandler);
        return v;
    }

    private void InicializateControls(View view){
        mlayoutGuiaEntrada = view.findViewById(R.id.layoutGuiaEntrada);
        mlayoutRecepcion = view.findViewById(R.id.layoutRecepcion);

        mtvOrdenCompra = view.findViewById(R.id.tv_orden_compra);
        mtvGuia = view.findViewById(R.id.tv_guia);

        mtv_doc_origen = view.findViewById(R.id.tv_doc_origen);
        mtv_doc_destino = view.findViewById(R.id.tv_doc_destino);
        mtv_motivo = view.findViewById(R.id.tv_motivo);



        mtvItemSku = view.findViewById(R.id.tv_item_sku);
        mlvEpcs = view.findViewById(R.id.lv_epcs);

        viewGroup1 = view.findViewById(android.R.id.content);
    }

    private void setValuesControl(){
        if(locatedInvData != null){
            if(locatedInvData.getOrdenCompra() != null && locatedInvData.getNumeroGuia() != null ){
                mlayoutGuiaEntrada.setVisibility(View.VISIBLE);
                mlayoutRecepcion.setVisibility(View.GONE);
                mtvOrdenCompra.setText(locatedInvData.getOrdenCompra() != null ? locatedInvData.getOrdenCompra() : "" );
                mtvGuia.setText(locatedInvData.getNumeroGuia() != null ? locatedInvData.getNumeroGuia() : "" );
            }
            else {
                mtv_doc_origen.setText(locatedInvData.getDocOrigen() != null ? locatedInvData.getDocOrigen() : "" );
                mtv_doc_destino.setText(locatedInvData.getDocDestino() != null ? locatedInvData.getDocDestino() : "" );
                mtv_motivo.setText(locatedInvData.getMotivo() != null ? locatedInvData.getMotivo() : "" );

                mlayoutGuiaEntrada.setVisibility(View.GONE);
                mlayoutRecepcion.setVisibility(View.VISIBLE);
            }

            mtvItemSku.setText(locatedInvData.getItemSku() != null ? locatedInvData.getItemSku() : "" );
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        SonidoBeep sonidoBeep = new SonidoBeep(mContext, R.raw.beep);
        soundPoolData = sonidoBeep.getSoundPool();
        setValuesControl();
        SetDataListView();
        isOpenAlertDialog = false;

    }

    private void SetDataListView(){
        mlvEpcs.setAdapter(null);
        mlvEpcs.setAdapter(new CustomListAdapterInvLocated(mContext, locatedInvData.getEpcs()));
        mlvEpcs.setOnItemClickListener(listItemClickListener);
    }

    private AdapterView.OnItemClickListener listItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            String epc = (String) mlvEpcs.getItemAtPosition(position);

            SelectionCriterias s = new SelectionCriterias();
            s.makeCriteria(SelectionCriterias.SCMemType.EPC, epc,
                    4, (epc.length()+4) * 4,
                    SelectionCriterias.SCActionType.ASLINVA_DSLINVB);
            epc_ = epc;
            //Toast.makeText(mContext,epc_,Toast.LENGTH_SHORT).show();
            mReader.RF_SetSelection(s);


            InvAlertProgressBarLocate();

        }
    };

    //**************************************************

    private static class InventoryHandler extends Handler {
        private final WeakReference<InvetoryLocatedFragment> mExecutor;
        public InventoryHandler(InvetoryLocatedFragment f) {
            mExecutor = new WeakReference<>(f);
        }

        @Override
        public void handleMessage(Message msg) {
            InvetoryLocatedFragment executor = mExecutor.get();
            if (executor != null) {
                executor.handleInventoryHandler(msg);
            }
        }
    }

    public void handleInventoryHandler(Message m) {
        if(isOpenAlertDialog){
            switch (m.what) {
                case SDConsts.Msg.SDMsg:
                    switch(m.arg1) {
                        case SDConsts.SDCmdMsg.TRIGGER_PRESSED:
                            mLocateProgress.setProgress(0);
                            mReader.RF_PerformInventoryForLocating(epc_);
                            break;
                        case SDConsts.SDCmdMsg.TRIGGER_RELEASED:
                            mReader.RF_StopInventory();
                            //mLocateProgress.setProgress(0);
                            break;
                    }
                    break;
                case SDConsts.Msg.RFMsg:
                    switch(m.arg1) {
                        case SDConsts.RFCmdMsg.LOCATE:
                            if (m.arg2 == SDConsts.RFResult.SUCCESS) {
                                if (m.obj != null  && m.obj instanceof Integer) {
                                    //
                                    //mLocateProgress.setProgress((int) m.obj);
                                    processLocateData((int) m.obj);
                                }
                            }
                            break;
                    }
                    break;
            }
        }
    }

    //*********************************
    private void InvAlertProgressBarLocate()
    {

        View dialogView1 = LayoutInflater.from(mContext).inflate(R.layout.dialog_tag_located, viewGroup1, false);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.myDialog));
        builder1.setView(dialogView1);
        final AlertDialog alertDialog1 = builder1.create();


        mLocateProgress = dialogView1.findViewById(R.id.tag_locate_progress);
        mLocateProgress.setProgress(0);
        locatedTimer = new LocatedTimer(mLocateProgress);

        //

        TextView mtvCodigoItem = dialogView1.findViewById(R.id.tvCodigoItem);
        TextView mtvGrupo1 = dialogView1.findViewById(R.id.tvGrupo1);
        TextView mtvGrupo2 = dialogView1.findViewById(R.id.tvGrupo2);
        TextView mtvGrupo3 = dialogView1.findViewById(R.id.tvGrupo3);
        TextView mtvEpc = dialogView1.findViewById(R.id.tvEpc);

        /*Button btnAceptar = dialogView1.findViewById(R.id.btnDialogAceptar);
        Button btnCancelar = dialogView1.findViewById(R.id.btnDialogCancelar);
        Button btnLimpiar = dialogView1.findViewById(R.id.btnDialogLimpiar);*/
        Button btnCancelar = dialogView1.findViewById(R.id.btnDialogCancelar);

        mtvCodigoItem.setText(locatedInvData.getItemSku());
        mtvGrupo1.setText(locatedInvData.getSkuData().getGrupo1());
        mtvGrupo2.setText(locatedInvData.getSkuData().getGrupo2());
        mtvGrupo3.setText(locatedInvData.getSkuData().getGrupo3());
        mtvEpc.setText(epc_);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog1.dismiss();
            }
        });


        alertDialog1.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                //Toast.makeText(mContext, "Modal Open", Toast.LENGTH_SHORT).show();
                isOpenAlertDialog = true;
            }
        });
        alertDialog1.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                isOpenAlertDialog = false;
                //Toast.makeText(mContext, "Modal close", Toast.LENGTH_SHORT).show();
            }
        });


        /*adapterHashMap =  getAdapaterSpinner(gMovientos);
        if(adapterHashMap != null){
            spinnerMapTipos = adapterHashMap.getHashMap();
            mspinTipo.setAdapter(adapterHashMap.getAdapter());
        }

        adapterHashMap =  getAdapaterSpinner(gBodega);
        if(adapterHashMap != null){
            spinnerMapBodegas = adapterHashMap.getHashMap();
            mspinOrigen.setAdapter(adapterHashMap.getAdapter());
        }

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();
            }
        });*/
        alertDialog1.show();
    }

    //***************

    private void processLocateData(int data) {
        mLocateValue = data;
        //startLocateTimer();

        locatedTimer.startLocateTimer();

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
    private class SoundTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mLocateProgress.setProgress(mLocateValue);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                if (soundPoolData != null && soundPoolData.getSoundPool() != null) {
                    soundPoolData.getSoundPool().play(soundPoolData.getSoundId(), soundPoolData.getSoundVolumen(), soundPoolData.getSoundVolumen(), 0, 0, (48000.0f / 44100.0f));
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            } catch (java.lang.NullPointerException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    };

}
