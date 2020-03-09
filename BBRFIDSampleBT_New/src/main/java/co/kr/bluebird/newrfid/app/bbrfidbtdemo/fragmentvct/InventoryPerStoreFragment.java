package co.kr.bluebird.newrfid.app.bbrfidbtdemo.fragmentvct;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import co.kr.bluebird.newrfid.app.bbrfidbtdemo.MainActivity;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.R;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.Garment;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.GarmentSale;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.GarmentSaleObj;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.GarmentSize;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.StoreExistence;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.StoreExistenceObj;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.service.RfidService;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.CustomListAdapterInvPerStoreOtherSale;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.ExpandableListAdapterInvPerStoreSaldos;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.InventoryImageAdapter;
import co.kr.bluebird.sled.BTReader;
import co.kr.bluebird.sled.SDConsts;

/**
 * A simple {@link Fragment} subclass.
 */
public class InventoryPerStoreFragment extends Fragment {

    private Button  mbtn_saldosIPS, nbtn_infoIPS, mbtnClear, mbtnRead;
    private Context mContext;
    private RfidService rfidService;
    private ImageView imGarment;
    private ViewPager mvPagerGarment;
    private TextView mtvLine, mtvProduct, mtvAnio, mtvPrice, mtvTotalGLocal, mtvTotalGOtros;
    private Garment garment;
    //private List<GarmentSale> garmentSaleList;
    private GarmentSaleObj garmentSaleObj;
    private ExpandableListView mexpLV_Saldos;
    private LinearLayout mLayoutSaldos, mLayoutInfo, mlayoutHeaderSale, mlayoutFooterSale;
    private String[] mFunctionsString;
    private String mItemCodigo = null;
    private StoreExistenceObj storeExistenceObj = null;
    private String[] mWSparameterInvTiendaInfo, mWSparameterInvTiendaSaldo;

    private String mMessageTextView;
    private Fragment mFragment;
    private BTReader mReader;
    private ViewGroup loVistaDialogo;

    private final BCBarcodeHandler mBCBarcodeHandler = new BCBarcodeHandler(this);

    //ExpandableListView expandableListView;
    ExpandableListAdapterInvPerStoreSaldos expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<GarmentSize>> expandableListDetail;



    private EditText metEstiloItemIPS;

    public InventoryPerStoreFragment() {
        // Required empty public constructor
    }

    public static InventoryPerStoreFragment newInstance() {

        return new InventoryPerStoreFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.inventoryperstore_frag, container, false);
        //return inflater.inflate(R.layout.inventoryperstore_frag, container, false);
        mContext = inflater.getContext();

        InicialiarAtributos(v);
        rfidService = new RfidService(mContext);

        //mbtnCargarIPS.setOnClickListener(ProcesarOnclick);
        mbtnRead.setOnClickListener(StartQrOnClick);

        mFragment = this;

        /*mprocesar_imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rfidService = new RfidService(mContext);
                garment = rfidService.SimularServiceInventoryPerStore("0");
                imGarment.setImageResource(R.drawable.polo2);

                setGarment(garment);

            }
        });*/

        mbtn_saldosIPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //WSparameter_InventarioPorTiendaSaldo
                if(metEstiloItemIPS != null &&  !metEstiloItemIPS.getText().toString().trim().equals(""))
                {
                    if(mWSparameterInvTiendaSaldo == null){
                        mWSparameterInvTiendaSaldo = getResources().getStringArray(R.array.WSparameter_InventarioPorTiendaSaldo);
                    }

                    exWSInventarioPorTiendaSaldosAsync tiendaSaldosAsync = new exWSInventarioPorTiendaSaldosAsync();
                    tiendaSaldosAsync.execute();
                }
                else {
                    Toast.makeText(mContext, "Ingrese un codigo de item...", Toast.LENGTH_SHORT).show();
                }

            }
        });

        nbtn_infoIPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayoutInfo.setVisibility(View.VISIBLE);
                mLayoutSaldos.setVisibility(View.GONE);
                EnableDisableBtnFooter(false, true);
            }
        });
        loVistaDialogo = v.findViewById(android.R.id.content);

        return v;
    }



    @Override
    public void onStart() {

        mReader = BTReader.getReader(mContext, mBCBarcodeHandler);

        super.onStart();
    }

    @Override
    public void onDestroy() {
        metEstiloItemIPS.setText("");
        super.onDestroy();
    }

    private View.OnClickListener StartQrOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String btn = mbtnRead.getText().toString();
            if(btn.equals("LEER QR")){
                if(mReader != null){
                    int mode = mReader.SD_GetTriggerMode();
                    if (mode == SDConsts.SDTriggerMode.RFID)
                        mReader.SD_SetTriggerMode(SDConsts.SDTriggerMode.BARCODE);

                    mReader.BC_SetTriggerState(true);
                }
            }
            else {
                if(metEstiloItemIPS != null && !metEstiloItemIPS.getText().toString().trim().equals("")){
                    CleanImageDesc();
                    if(mWSparameterInvTiendaInfo == null){
                        mWSparameterInvTiendaInfo = getResources().getStringArray(R.array.WSparameter_InventarioPorTiendaInfo);
                    }

                    garmentSaleObj = null;

                    exWSInventarioPorTiendaAsync inventarioPorTiendaAsync = new exWSInventarioPorTiendaAsync();
                    inventarioPorTiendaAsync.execute();
                }
                else {
                    Toast.makeText(mContext, "Ingrese un codigo de item...", Toast.LENGTH_SHORT).show();
                }
            }


        }
    };

    /*private View.OnClickListener ProcesarOnclick = new View.OnClickListener() {

        @Override
        public void onClick(View v){

            //garment = rfidService.WSInventoryPerStore(metEstiloItemIPS.getText().toString().trim());
            //imGarment.setImageResource(R.drawable.polo2);

            //WSparameter_InventarioPorTiendaInfo

            if(mbtn_saldosIPS.getVisibility() == View.VISIBLE){

            }

            if(metEstiloItemIPS != null && !metEstiloItemIPS.getText().toString().trim().equals("")){
                CleanImageDesc();
                if(mWSparameterInvTiendaInfo == null){
                    mWSparameterInvTiendaInfo = getResources().getStringArray(R.array.WSparameter_InventarioPorTiendaInfo);
                }

                exWSInventarioPorTiendaAsync inventarioPorTiendaAsync = new exWSInventarioPorTiendaAsync();
                inventarioPorTiendaAsync.execute();
            }
            else {
                Toast.makeText(mContext, "Ingrese un codigo de item...", Toast.LENGTH_SHORT).show();
            }
            //setGarment(garment);
        }
    };*/

    private void CleanExpLV_Saldos(){

        mexpLV_Saldos.setAdapter((BaseExpandableListAdapter)null);
        mlayoutHeaderSale.setVisibility(View.GONE);
        mlayoutFooterSale.setVisibility(View.GONE);
    }


    private void  InicialiarAtributos(View v)
    {
        //mprocesar_imgbtn = (ImageButton)v.findViewById(R.id.procesar_imgbtn);

        //mbtnCargarIPS = (Button) v.findViewById(R.id.btnCargarIPS);
        mbtnClear = (Button) v.findViewById(R.id.btnClear);
        mbtn_saldosIPS = (Button)v.findViewById(R.id.btn_saldosIPS);
        nbtn_infoIPS = (Button)v.findViewById(R.id.btn_infoIPS);

        mbtnRead = v.findViewById(R.id.btnRead);
        mbtnRead.setText("LEER QR");


        //imGarment = (ImageView)v.findViewById(R.id.ivGarment);
        mvPagerGarment = (ViewPager) v.findViewById(R.id.vPagerGarment);
        mtvLine = (TextView) v.findViewById(R.id.tvLine);
        mtvProduct = (TextView) v.findViewById(R.id.tvProduct);
        mtvAnio = (TextView) v.findViewById(R.id.tvanio);
        mtvPrice = (TextView) v.findViewById(R.id.tvprice);

        mtvTotalGLocal = (TextView) v.findViewById(R.id.tvTotalGLocal);
        mtvTotalGOtros = (TextView) v.findViewById(R.id.tvTotalGOtros);

        metEstiloItemIPS = (EditText) v.findViewById(R.id.etEstiloItemIPS);
        mexpLV_Saldos = (ExpandableListView) v.findViewById(R.id.expLV_Saldos);
        mLayoutSaldos = (LinearLayout) v.findViewById(R.id.LayoutSaldos);
        mLayoutInfo = (LinearLayout) v.findViewById(R.id.LayoutInfo);

        mlayoutHeaderSale = (LinearLayout) v.findViewById(R.id.layoutHeaderSale);
        mlayoutFooterSale = (LinearLayout) v.findViewById(R.id.layoutFooterSale);

        enabledDisabledBtnClear(false);


        metEstiloItemIPS.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 5){
                    //mbtnCargarIPS.setVisibility(View.VISIBLE);
                    setTextIconBtnRead(false);
                }
                else{
                    setTextIconBtnRead(true);
                    //mbtnCargarIPS.setVisibility(View.GONE);
                }
                if(charSequence.length() != 0){
                    /*mbtnClear.setVisibility(View.VISIBLE);
                    mbtnscanQR.setVisibility(View.GONE);*/
                    enabledDisabledBtnClear(true);
                }
                else {
                    enabledDisabledBtnClear(false);
                    EnableDisableBtnFooter(false,false);
                    /*mbtnClear.setVisibility(View.GONE);
                    mbtnscanQR.setVisibility(View.VISIBLE);*/
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mbtnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                metEstiloItemIPS.setText("");
                garmentSaleObj = null;
                mvPagerGarment.setAdapter(null);
                mexpLV_Saldos.setAdapter((BaseExpandableListAdapter)null);
                mtvLine.setText("");
                mtvProduct.setText("");
                mtvAnio.setText("");
                mtvPrice.setText("");

                mLayoutSaldos.setVisibility(View.GONE);
                mLayoutInfo.setVisibility(View.VISIBLE);
            }
        });

        //style buttons

        setIconButtons();

        /*Drawable myIcon = null;
        ColorFilter filter = null;

        myIcon = getResources().getDrawable( R.drawable.materialsale48 );
        filter = new LightingColorFilter( Color.BLACK, Color.WHITE);
        myIcon.setColorFilter(filter);
        mbtn_saldosIPS.setCompoundDrawablesWithIntrinsicBounds( null, null, myIcon, null);

        myIcon = getResources().getDrawable( R.drawable.materialinfo48 );
        filter = new LightingColorFilter( Color.BLACK, Color.WHITE);
        myIcon.setColorFilter(filter);
        nbtn_infoIPS.setCompoundDrawablesWithIntrinsicBounds( null, null, myIcon, null);*/

        EnableDisableBtnFooter(false, false);
    }

    private void enabledDisabledBtnClear(boolean isEnabled){
        mbtnClear.setEnabled(isEnabled);
        if(isEnabled){
            mbtnClear.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F38428")));
        }
        else {
            mbtnClear.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D5D7D6")));
        }
    }
    private void setTextIconBtnRead(boolean isQr){
        Drawable myIcon = null;
        ColorFilter filter = null;

        if(isQr){
            myIcon = getResources().getDrawable( R.drawable.qrcode18px );
            filter = new LightingColorFilter( Color.BLACK, Color.WHITE);
            myIcon.setColorFilter(filter);
            mbtnRead.setCompoundDrawablesWithIntrinsicBounds( myIcon, null, null, null);
            mbtnRead.setText("LEER QR");
        }
        else {
            myIcon = getResources().getDrawable( R.drawable.ic_materialprocesar );
            filter = new LightingColorFilter( Color.BLACK, Color.WHITE);
            myIcon.setColorFilter(filter);
            mbtnRead.setCompoundDrawablesWithIntrinsicBounds( myIcon, null, null, null);
            mbtnRead.setText("CARGAR");
        }
    }

    private void setIconButtons(){
        Drawable myIcon = null;
        ColorFilter filter = null;

        myIcon = getResources().getDrawable( R.drawable.qrcode18px );
        filter = new LightingColorFilter( Color.BLACK, Color.WHITE);
        myIcon.setColorFilter(filter);
        mbtnRead.setCompoundDrawablesWithIntrinsicBounds( myIcon, null, null, null);


        myIcon = getResources().getDrawable( R.drawable.ic_materialdelete );
        filter = new LightingColorFilter( Color.BLACK, Color.WHITE);
        myIcon.setColorFilter(filter);
        mbtnClear.setCompoundDrawablesWithIntrinsicBounds( myIcon, null, null, null);


        myIcon = getResources().getDrawable( R.drawable.ic_materialinfo );
        filter = new LightingColorFilter( Color.BLACK, Color.WHITE);
        myIcon.setColorFilter(filter);
        nbtn_infoIPS.setCompoundDrawablesWithIntrinsicBounds( myIcon, null, null, null);

        //myIcon = getResources().getDrawable( R.drawable.marerialsale );
        myIcon = getResources().getDrawable( R.drawable.ic_style );
        filter = new LightingColorFilter( Color.BLACK, Color.WHITE);
        myIcon.setColorFilter(filter);
        mbtn_saldosIPS.setCompoundDrawablesWithIntrinsicBounds( myIcon, null, null, null);

    }


    private void EnableDisableBtnFooter(boolean isEnabledInfo, boolean isEnabledSale){
        nbtn_infoIPS.setEnabled(isEnabledInfo);
        mbtn_saldosIPS.setEnabled(isEnabledSale);

        if(isEnabledInfo){
            nbtn_infoIPS.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0097a7")));
        }
        else {
            nbtn_infoIPS.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D5D7D6")));

        }

        if(isEnabledSale){
            mbtn_saldosIPS.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0b4096")));
        }
        else {
            mbtn_saldosIPS.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D5D7D6")));
        }
    }
    private void setGarment()
    {
        if(garment != null && garment.getEstado() != null &&  garment.getEstado().getCodigo() != null )
        {
            if(garment.getEstado().getCodigo().equals("00")){
                mtvLine.setText(garment.getLinea());
                mtvProduct.setText(garment.getProducto());
                mtvAnio.setText(garment.getTemporada());
                mtvPrice.setText("c/IVA $"+String.valueOf(garment.getPrecioIva()) +" | s/IVA $"+String.valueOf(garment.getPrecioSinIva()));
                //imGarment.setImageBitmap(ConvertBase64toBitmap());
                InventoryImageAdapter inventoryImageAdapter = new InventoryImageAdapter(mContext,ConvertBase64toBitmap());
                mvPagerGarment.setAdapter(inventoryImageAdapter);
                //mbtn_saldosIPS.setVisibility(View.VISIBLE);
                EnableDisableBtnFooter(false, true);
            }
            else {
                Toast.makeText(mContext, garment.getEstado().getDescripcion(), Toast.LENGTH_LONG).show();
                //mbtn_saldosIPS.setVisibility(View.INVISIBLE);
                EnableDisableBtnFooter(false, false);
            }

        }
        else {
            Toast.makeText(mContext, "Error Desconocido...", Toast.LENGTH_LONG).show();
            //mbtn_saldosIPS.setVisibility(View.INVISIBLE);
            EnableDisableBtnFooter(false, false);
        }

    }

    /*private Bitmap ConvertBase64toBitmap(){
        String [] encodedImage = new String[garment.getListImagenes().size()];

        int i = 0;
        for (String item:garment.getListImagenes()) {
            encodedImage[i] = item;
            i++;
        }
        byte[] decodedString = Base64.decode(encodedImage[0], Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }*/

    private Bitmap[] ConvertBase64toBitmap(){
        String [] encodedImage = null;

        int i = 0;
        if( garment != null && garment.getListImagenes() != null && garment.getListImagenes().size() > 0 ){
            encodedImage = new String[garment.getListImagenes().size()];
            for (String item:garment.getListImagenes()) {
                encodedImage[i] = item;
                i++;
            }
        }
        else {
            // se muestra imagen por Default
            encodedImage = new String[1];
            encodedImage [0] = getString(R.string.IPS_IMAGENOTAVIABLE);
            //encodedImage[0] = "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";
        }

       /* byte[] decodedString = Base64.decode(encodedImage[0], Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);*/


        byte[] decodedString;
        Bitmap decodedByte;
        int j = 0;
        Bitmap[] ArrayBitmap = null;
        int c = encodedImage.length;
        if(encodedImage.length > 0){
            ArrayBitmap = new Bitmap[encodedImage.length];
            for (String encode:encodedImage) {
                decodedString = Base64.decode(encode, Base64.DEFAULT);
                decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                ArrayBitmap[j] =  decodedByte;
                j++;
            }
        }

        return ArrayBitmap;
    }

    private GarmentSaleObj OrdenarGarmentSale(){
        int i = 0;
        int indiceBusca = 0;

        GarmentSaleObj ngarmentSaleObj = new GarmentSaleObj();
        GarmentSale ngarmentSale = new GarmentSale();
        List<GarmentSale> garmentSaleList = new ArrayList<>();


        for (GarmentSale garmentSale : garmentSaleObj.getGarmentSaleList()) {
            for (GarmentSize garmentSize:garmentSale.getSizes()) {
                if(garmentSize.isBusca()){
                    ngarmentSale.setColorName(garmentSale.getColorName());
                    ngarmentSale.setSizes(garmentSale.getSizes());
                    indiceBusca = i;
                }
            }
            i++;
        }

        if(indiceBusca != 0){
            garmentSaleList.add(ngarmentSale);
            garmentSaleObj.getGarmentSaleList().remove(indiceBusca);
        }



        for (GarmentSale garmentSale : garmentSaleObj.getGarmentSaleList()) {
            garmentSaleList.add(garmentSale);
        }

        ngarmentSaleObj.setStatus(garmentSaleObj.getStatus());
        ngarmentSaleObj.setGarmentSaleList(garmentSaleList);

        return ngarmentSaleObj;

    }

    private LinkedHashMap<String, List<GarmentSize>> getData_()
    {
        LinkedHashMap<String, List<GarmentSize>> ParentItem = new LinkedHashMap<String, List<GarmentSize>>();
        List<GarmentSize> garmentSizeList = null;

        int totalLocal = 0, totalOtros = 0;
        garmentSaleObj = OrdenarGarmentSale();
        //for (GarmentSale garmentSale:garmentSaleObj.getGarmentSaleList()) {
        for (GarmentSale garmentSale:garmentSaleObj.getGarmentSaleList()) {
            garmentSizeList = new ArrayList<GarmentSize>();
            for (GarmentSize garmentSize:garmentSale.getSizes()) {
                garmentSizeList.add(garmentSize);
                if(garmentSize.getItmCodigo().equals("00")){
                    totalLocal += garmentSize.getStockLocal();
                    totalOtros += garmentSize.getStockOtros();
                }

            }
            ParentItem.put(garmentSale.getColorName(),garmentSizeList);
        }

        mtvTotalGLocal.setText(totalLocal+"");
        mtvTotalGOtros.setText(totalOtros+"");

        return ParentItem;
    }

    // METODOS ASYN

    private  class exWSInventarioPorTiendaAsync extends AsyncTask<Void, Void, Void> {


        String mEstiloItem = metEstiloItemIPS.getText().toString().trim();
        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Void... voids) {

            //ArrayList<String> epcsLeidos = mAdapter.listTagReadEpc();
            rfidService.SOAP_ACTION_ =  mWSparameterInvTiendaInfo[0];
            rfidService.METHOD_NAME_ =  mWSparameterInvTiendaInfo[1];
            rfidService.NAMESPACE_ = mWSparameterInvTiendaInfo[2];
            rfidService.URL_ = mWSparameterInvTiendaInfo[3];

            garment = rfidService.WSInventoryPerStore(mEstiloItem);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);
            CleanExpLV_Saldos();
            mLayoutSaldos.setVisibility(View.GONE);
            mLayoutInfo.setVisibility(View.VISIBLE);
            progressDialog.cancel();
            setGarment();

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

    //WSInventoryPerStoreSale


    private  class exWSInventarioPorTiendaSaldosAsync extends AsyncTask<Void, Void, Void> {


        ProgressDialog progressDialog = null;
        String mEstiloItem = metEstiloItemIPS.getText().toString().trim();
        @Override
        protected Void doInBackground(Void... voids) {

            //ArrayList<String> epcsLeidos = mAdapter.listTagReadEpc();
            rfidService.SOAP_ACTION_ =  mWSparameterInvTiendaSaldo[0];
            rfidService.METHOD_NAME_ =  mWSparameterInvTiendaSaldo[1];
            rfidService.NAMESPACE_ = mWSparameterInvTiendaSaldo[2];
            rfidService.URL_ = mWSparameterInvTiendaSaldo[3];

            if(garmentSaleObj == null){
                garmentSaleObj = rfidService.WSInventoryPerStoreSale(mEstiloItem);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);
            progressDialog.cancel();

            if(garmentSaleObj != null && garmentSaleObj.getStatus().getCodigo().equals("00")){
                if(garmentSaleObj.getGarmentSaleList() != null && garmentSaleObj.getGarmentSaleList().size() > 0){
                    /*nbtn_infoIPS.setVisibility(View.VISIBLE);*/

                    EnableDisableBtnFooter(true, false);
                    mlayoutHeaderSale.setVisibility(View.VISIBLE);
                    mlayoutFooterSale.setVisibility(View.VISIBLE);

                    ExpandableListViewSaldos();
                    mLayoutInfo.setVisibility(View.GONE);
                    mLayoutSaldos.setVisibility(View.VISIBLE);
                }
                else {
                    mlayoutHeaderSale.setVisibility(View.GONE);
                    mlayoutFooterSale.setVisibility(View.GONE);
                    /*nbtn_infoIPS.setVisibility(View.INVISIBLE);*/
                    EnableDisableBtnFooter(false, true);
                    Toast.makeText(mContext, "Lo sentimos el item: "+ mEstiloItem+", no registra saldos ", Toast.LENGTH_LONG).show();

                }

            }
            else {
                mlayoutHeaderSale.setVisibility(View.GONE);
                mlayoutFooterSale.setVisibility(View.GONE);
                /*nbtn_infoIPS.setVisibility(View.INVISIBLE);*/
                EnableDisableBtnFooter(false, true);
                Toast.makeText(mContext, garmentSaleObj.getStatus().getDescripcion(), Toast.LENGTH_SHORT).show();
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

    private void InvoquePerStoreExistencePlaceAsync(){
        WSInventoryPerStoreExistencePlaceAsync existencePlaceAsync = new WSInventoryPerStoreExistencePlaceAsync();
        existencePlaceAsync.execute();
    }

    private class WSInventoryPerStoreExistencePlaceAsync extends AsyncTask<Void, Void, Void>
    {
        /*String mEstiloItem = metEstiloItemIPS.getText().toString().trim();*/

        ProgressDialog progressDialog = null;
        @Override
        protected Void doInBackground(Void... voids) {

            //ArrayList<String> epcsLeidos = mAdapter.listTagReadEpc();


            rfidService.SOAP_ACTION_ =  mFunctionsString[0];
            rfidService.METHOD_NAME_ =  mFunctionsString[1];
            rfidService.NAMESPACE_ = mFunctionsString[2];
            rfidService.URL_ = mFunctionsString[3];

            storeExistenceObj = rfidService.WSInventoryPerStoreExistencePlace(mItemCodigo);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);
            progressDialog.cancel();
            // falta pedir a Robert que en el WS Agrege el estado
            if(storeExistenceObj != null && storeExistenceObj.getEstado() != null && storeExistenceObj.getEstado().getCodigo().equals("00")){

                if(storeExistenceObj.getExistencias() != null && storeExistenceObj.getExistencias().size() > 0){
                    InvoqueDialogSaldoOtros();
                }
                else {
                    Toast.makeText(mContext, "No hay existencias que mostrar...", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(mContext, storeExistenceObj.getEstado().getDescripcion(), Toast.LENGTH_SHORT).show();
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

    private void ExpandableListViewSaldos(){

        mFunctionsString = getResources().getStringArray(R.array.WSparameter_saldoLocales);
        expandableListDetail = getData_();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new ExpandableListAdapterInvPerStoreSaldos(mContext, expandableListTitle, expandableListDetail);
        mexpLV_Saldos.setAdapter(expandableListAdapter);
        for(int i=0; i < expandableListAdapter.getGroupCount() ; i++){
            mexpLV_Saldos.expandGroup(i);
        }
        mexpLV_Saldos.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                /*Toast.makeText(mContext,
                        expandableListTitle.get(groupPosition) + " ListView Open.",
                        Toast.LENGTH_SHORT).show();*/
            }
        });

        mexpLV_Saldos.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                /*Toast.makeText(mContext,
                        expandableListTitle.get(groupPosition) + " ListView Closed.",
                        Toast.LENGTH_SHORT).show();*/

            }
        });

        mexpLV_Saldos.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                Object obj = expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition);

                mItemCodigo = ((GarmentSize) obj).getItmCodigo();
                int saldoOtros = ((GarmentSize) obj).getStockOtros();

                if(saldoOtros != 0){
                    if(!mItemCodigo.equals("00")){
                        InvoquePerStoreExistencePlaceAsync();
                    }
                }
                else {
                    if(!mItemCodigo.equals("00")){
                        Toast.makeText(mContext, "No se registran existencias en otros locales...", Toast.LENGTH_SHORT).show();
                    }

                }


                //WSInventoryPerStoreExistencePlaceAsync existencePlaceAsync = new WSInventoryPerStoreExistencePlaceAsync();
                //existencePlaceAsync.execute();
                //Toast.makeText(mContext, "El Item Codigo: "+((GarmentSize) obj).getItmCodigo(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private void InvoqueDialogSaldoOtros()
    {
        View dialogView1 = LayoutInflater.from(mContext).inflate(R.layout.dialog_saldos_otros, loVistaDialogo, false);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.myDialog));
        builder1.setView(dialogView1);
        final AlertDialog alertDialog1 = builder1.create();

        final ListView lv =  dialogView1.findViewById(R.id.lv_otherSale);
        final TextView mtvItem =  dialogView1.findViewById(R.id.tvItem);
        mtvItem.setText(mItemCodigo);
        lv.setAdapter(new CustomListAdapterInvPerStoreOtherSale(mContext, storeExistenceObj.getExistencias()));

        alertDialog1.show();

    }

    public void CleanControl(){
        metEstiloItemIPS.setText("");
    }

    public void CleanImageDesc(){
        mvPagerGarment.setAdapter(null);
        mtvLine.setText("");
        mtvProduct.setText("");
        mtvAnio.setText("");
        mtvPrice.setText(""); ;
    }


    private static class BCBarcodeHandler extends Handler {
        private final WeakReference<InventoryPerStoreFragment> mExecutor;
        public BCBarcodeHandler(InventoryPerStoreFragment f) {
            mExecutor = new WeakReference<>(f);
        }

        @Override
        public void handleMessage(Message msg) {
            InventoryPerStoreFragment executor = mExecutor.get();
            if (executor != null) {
                executor.handleMessage(msg);
            }
        }
    }

    public void handleMessage(Message m) {


        switch (m.what) {
            case SDConsts.Msg.SDMsg:
                if (m.arg1 == SDConsts.SDCmdMsg.TRIGGER_PRESSED)
                    mMessageTextView = " " + "TRIGGER_PRESSED";

                else if (m.arg1 == SDConsts.SDCmdMsg.TRIGGER_RELEASED)
                    mMessageTextView = " " + "TRIGGER_RELEASED";
                else if (m.arg1 == SDConsts.SDCmdMsg.SLED_MODE_CHANGED)
                    mMessageTextView = " " + "SLED_MODE_CHANGED " + m.arg2;
                else if (m.arg1 == SDConsts.SDCmdMsg.SLED_UNKNOWN_DISCONNECTED)
                    mMessageTextView = " " + "SLED_UNKNOWN_DISCONNECTED";
                else if (m.arg1 == SDConsts.SDCmdMsg.SLED_HOTSWAP_STATE_CHANGED) {
                    if (m.arg2 == SDConsts.SDHotswapState.HOTSWAP_STATE)
                        Toast.makeText(mContext, "HOTSWAP STATE CHANGED = HOTSWAP_STATE", Toast.LENGTH_SHORT).show();
                    else if (m.arg2 == SDConsts.SDHotswapState.NORMAL_STATE)
                        Toast.makeText(mContext, "HOTSWAP STATE CHANGED = NORMAL_STATE", Toast.LENGTH_SHORT).show();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(mFragment).attach(mFragment).commit();
                }
                break;

            case SDConsts.Msg.BCMsg:
                StringBuilder readData = new StringBuilder();
                if (m.arg1 == SDConsts.BCCmdMsg.BARCODE_TRIGGER_PRESSED)
                    mMessageTextView = " " + "BARCODE_TRIGGER_PRESSED";
                else if (m.arg1 == SDConsts.BCCmdMsg.BARCODE_TRIGGER_RELEASED)
                    mMessageTextView = " " + "BARCODE_TRIGGER_RELEASED";
                else if (m.arg1 == SDConsts.BCCmdMsg.BARCODE_READ) {

                    if (m.arg2 == SDConsts.BCResult.SUCCESS)
                        readData.append(" " + "BC_MSG_BARCODE_READ");
                    else if (m.arg2 == SDConsts.BCResult.ACCESS_TIMEOUT)
                        readData.append(" " + "BC_MSG_BARCODE_ACCESS_TIMEOUT");
                    if (m.obj != null  && m.obj instanceof String) {
                        readData.append("\n" + (String)m.obj);

                        String qrcode = (String)m.obj;
                        if(qrcode != null && !qrcode.trim().isEmpty())
                        {
                            String[] parts = qrcode.trim().split(";");
                            qrcode = parts[0];
                            metEstiloItemIPS.setText(qrcode);

                            if(metEstiloItemIPS != null && !metEstiloItemIPS.getText().equals("")){
                                InvoqueInventoryPerStoreInfo();
                            }
                            else {
                                Toast.makeText(mContext, "Ingrese un codigo de item...", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                    mMessageTextView = " " + readData.toString();
                }
                else if (m.arg1 == SDConsts.BCCmdMsg.BARCODE_ERROR) {
                    if (m.arg2 == SDConsts.BCResult.LOW_BATTERY)
                        readData.append(" " + "BARCODE_ERROR Low battery");
                    else
                        readData.append(" " + "BARCODE_ERROR ");
                    readData.append("barcode pasue");
                }
                break;

            /*case SDConsts.Msg.BTMsg:
                if (m.arg1 == SDConsts.BTCmdMsg.SLED_BT_CONNECTION_STATE_CHANGED) {
                    if (D) Log.d(TAG, "SLED_BT_CONNECTION_STATE_CHANGED = " + m.arg2);
                    if (mOptionHandler != null)
                        mOptionHandler.obtainMessage(MainActivity.MSG_OPTION_CONNECT_STATE_CHANGED).sendToTarget();
                }
                break;*/
        }
    }

    private void InvoqueInventoryPerStoreInfo(){
        CleanImageDesc();
        if(metEstiloItemIPS != null && !metEstiloItemIPS.getText().toString().trim().equals("")) {
            if(mWSparameterInvTiendaInfo == null){
                mWSparameterInvTiendaInfo = getResources().getStringArray(R.array.WSparameter_InventarioPorTiendaInfo);
            }
            garmentSaleObj = null;
            exWSInventarioPorTiendaAsync inventarioPorTiendaAsync = new exWSInventarioPorTiendaAsync();
            inventarioPorTiendaAsync.execute();
        }
    }

    public boolean isOpenLayoutSale(){

        return (mLayoutSaldos.getVisibility() == View.VISIBLE);
    }
    public void backLayoutInfo(){
        mLayoutSaldos.setVisibility(View.GONE);
        mLayoutInfo.setVisibility(View.VISIBLE);

        EnableDisableBtnFooter(false, true);
    }
}
