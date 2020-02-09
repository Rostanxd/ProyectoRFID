package co.kr.bluebird.newrfid.app.bbrfidbtdemo.fragmentvct;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.support.v4.print.PrintHelper;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import co.kr.bluebird.newrfid.app.bbrfidbtdemo.R;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.DGDetalle;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.DataSourceDto;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.DespatchGuide;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EGDetailResponse;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EGProcesado;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EGTagsResponseItem;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.GenericSpinnerDto;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.LoginData;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.QrData;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.service.RfidService;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.CustomListAdapterDespatchGuide;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.PersistenceDataIteration;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.QRCodeGenerator;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.ReportsTemplate;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.RfidEpHomologacion;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.UtilityFuntions;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.clsMensaje;

/**
 * A simple {@link Fragment} subclass.
 */
public class DespatchGuideGenerateFragment extends Fragment {


    private Spinner mSpinnerDestino;
    //private TableLayout tblGDDestino;
    private Context mContext;
    private RfidService rfidService;
    private GenericSpinnerDto spinnerDtoBodega;
    private Button mprocesar_imgbtn;
    private QRCodeGenerator qrCodeGenerator;
    private EditText met_nGuiaEntrada;
    private ArrayList<String> epcs;
    private ListView mlv_detailGD;
    private EGProcesado egProcesado;
    private RfidEpHomologacion mRfidEpHomologacion;
    private boolean first ;
    private EGDetailResponse egDetailResponse;

    private  String gGuiaEntrada;

    private String[] spinnerArray = null;
    private HashMap<Integer,String> spinnerMap = null;
    private String[] mWSParametersBodega, mWSParametersGuiaDespachoProc;
    private WebView mWb_qrcode;
    private clsMensaje loDialogo;
    private ViewGroup loVistaDialogo;



    public DespatchGuideGenerateFragment() {
        // Required empty public constructor
    }

    public static DespatchGuideGenerateFragment newInstance() {

        return new DespatchGuideGenerateFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.despatchguidegenerate_frag, container, false);
        View v = inflater.inflate(R.layout.despatchguidegenerate_frag, container, false);
        mContext = inflater.getContext();
        //##################### CLASE MENSAJE (DIALOGO)######################
        loDialogo = new clsMensaje(mContext);
        loVistaDialogo = v.findViewById(android.R.id.content);
        //###################################################################
        mSpinnerDestino = (Spinner)v.findViewById(R.id.spinnerDestino);
        //tblGDDestino = (TableLayout)v.findViewById(R.id.tablelayoutDetalleGD);
        mlv_detailGD = (ListView)v.findViewById(R.id.lv_detailGD);
        mprocesar_imgbtn = (Button) v.findViewById(R.id.procesar_imgbtn);

        Drawable myIcon = null;
        ColorFilter filter = null;

        myIcon = getResources().getDrawable( R.drawable.ejecutar );
        filter = new LightingColorFilter( Color.BLACK, Color.WHITE);
        myIcon.setColorFilter(filter);


        mprocesar_imgbtn.setCompoundDrawablesWithIntrinsicBounds( null, null, myIcon, null);

        met_nGuiaEntrada = (EditText)v.findViewById(R.id.et_nGuiaEntrada);
        first = true;

        epcs = getArguments() != null ? getArguments().getStringArrayList("epcsLeidos") : new ArrayList<String>();

        rfidService = new RfidService(mContext);

        //rfidService.GuiaDespachoDetalleService(epcs);
        executeSoapGuiaDespachoDetalleAsync despachoDetalleAsync = new executeSoapGuiaDespachoDetalleAsync();
        despachoDetalleAsync.execute();

        //LlenarGrid();
        LLenarSpinnerDestino();
        //SpinnerComplete();
        clickBtnProcesar();

        return v;
    }

    private  void clickBtnProcesar()
    {
        mprocesar_imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String lCodBodega = spinnerMap.get(mSpinnerDestino.getSelectedItemPosition());
                if(!met_nGuiaEntrada.getText().toString().equals("") && !lCodBodega.equals("0") ){
                    DialogProcesar();
                }
                else {
                    loDialogo.gMostrarMensajeAdvertencia(loVistaDialogo, "Ingrese un número de Guia y/o Seleccione un destino");
                    //Toast.makeText(mContext, "Ingrese un número de Guia y/o seleccione un destino ", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void LLenarSpinnerDestino()
    {
        //DespatchGuide despatchGuide = rfidService.GuiaDespachoBodegasService();
        mWSParametersBodega = getResources().getStringArray(R.array.WSparameter_Bodegas);
        executeSoapGuiaDespachoAsync tarea = new executeSoapGuiaDespachoAsync();
        tarea.execute();

    }
    private  void SpinnerComplete()
    {

        /*ArrayAdapter <DataSourceDto> adapter = new ArrayAdapter<DataSourceDto>(mContext,R.layout.spinner_item,despatchGuide.bodegas);
        mSpinnerDestino.setAdapter(adapter);*/


        spinnerArray = new String[spinnerDtoBodega.getColeccion().size()];
        spinnerMap = new HashMap<Integer, String>();

        int i = 0;
        for (DataSourceDto dto:spinnerDtoBodega.getColeccion()) {
            spinnerMap.put(i,dto.codigo);
            spinnerArray[i] = dto.descripcion;
            i++;
        }


        ArrayAdapter<String> adapter1 =new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item, spinnerArray);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerDestino.setAdapter(adapter1);

    }

    private  class executeSoapGuiaDespachoAsync extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;
        @Override
        protected Void doInBackground(Void... voids) {
            //despatchGuide = rfidService.GuiaDespachoBodegasService();

            rfidService.SOAP_ACTION_ =  mWSParametersBodega[0];
            rfidService.METHOD_NAME_ =  mWSParametersBodega[1];
            rfidService.NAMESPACE_ = mWSParametersBodega[2];
            rfidService.URL_ = mWSParametersBodega[3];

            spinnerDtoBodega = rfidService.WSBodegasOrMotivosService(true, "GDE", null);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);

            if(spinnerDtoBodega != null && spinnerDtoBodega.getEstado() != null && spinnerDtoBodega.getEstado().getCodigo().equals("9999")){
                //Toast.makeText(mContext,spinnerDtoBodega.getEstado().getDescripcion(), Toast.LENGTH_SHORT).show();
                loDialogo.gMostrarMensajeAdvertencia(loVistaDialogo, spinnerDtoBodega.getEstado().getDescripcion());
            }
            else {
                if (spinnerDtoBodega.estado != null && spinnerDtoBodega.estado.codigo.equals("00")) {
                    SpinnerComplete();
                    progressDialog.cancel();
                } else {
                    progressDialog.cancel();
                    loDialogo.gMostrarMensajeAdvertencia(loVistaDialogo, spinnerDtoBodega.estado.codigo);
                    //Toast.makeText(mContext,  spinnerDtoBodega.estado.codigo, Toast.LENGTH_SHORT).show();
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


    private  class executeSoapGuiaDespachoDetalleAsync extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;
        @Override
        protected Void doInBackground(Void... voids) {
            egDetailResponse= rfidService.EPCHomologacionService(epcs);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);
            progressDialog.cancel();
            if(egDetailResponse != null && egDetailResponse.getStatus() != null && egDetailResponse.getStatus().getCodigo().equals("9999")){
                Toast.makeText(mContext,egDetailResponse.getStatus().getDescripcion(),Toast.LENGTH_SHORT).show();
            }
            else {
                if (egDetailResponse.status != null && egDetailResponse.status.codigo.equals("00")) {
                    //AgruparItemCodigoEpc();
                    LlenarGrid(egDetailResponse);
                    //Toast.makeText(mContext, "Llamada a Web Services Correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    loDialogo.gMostrarMensajeAdvertencia(loVistaDialogo, "Llamada a Servicio Erroneo:: " + egDetailResponse.status.descripcion);
                    //Toast.makeText(mContext, "Llamada a Servicio Erroneo:: " + egDetailResponse.status.descripcion, Toast.LENGTH_SHORT).show();
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

    private void LlenarGrid(EGDetailResponse detailResponse){

        mRfidEpHomologacion = new RfidEpHomologacion();

        egProcesado = mRfidEpHomologacion.DetailResponseToEGProcesado(detailResponse);
        ProcesarLvDetailGD();
        //createColumns();
        //CrearRow();
    }

    private void ProcesarLvDetailGD()
    {
        mlv_detailGD.setAdapter(null);

        if(first){
            //View headerview = View.inflate(mContext, R.layout.header_2column, null);
            View footerview = View.inflate(mContext, R.layout.footer_2column, null);
            TextView tvFCol1 =(TextView) footerview.findViewById(R.id.tvCol2);
            //mlv_detailGD.addHeaderView(headerview);
            mlv_detailGD.addFooterView(footerview);
            tvFCol1.setText(String.valueOf(egProcesado.totalcant));
            first = false;
        }
        mlv_detailGD.setAdapter(new CustomListAdapterDespatchGuide(mContext, egProcesado.items));
        //mlv_detailGD.setOnItemClickListener(listItemClickListener);
    }

   /* private void createColumns()
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

        tblGDDestino.addView(tableRow, new TableLayout.LayoutParams(
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

        tblGDDestino.addView(tableRow, new TableLayout.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));

    }

    private void CrearRow(){
        List<DGDetalle> dgDetalleList = GuiaDespachoSimular();
        for (DGDetalle item_ : dgDetalleList){
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

                                             }
                                         }

            );

            TextView textViewEntrada = new TextView(mContext);
            textViewEntrada.setText(item_.codigoItem);
            textViewEntrada.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            textViewEntrada.setPadding(5,5,5,0);
            tableRow.addView(textViewEntrada);

            TextView textViewCant = new TextView(mContext);
            textViewCant.setText(String.valueOf(item_.cantidad));
            textViewCant.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            textViewCant.setPadding(5,5,5,0);
            tableRow.addView(textViewCant);


            tblGDDestino.addView(tableRow, new TableLayout.LayoutParams(
                    TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));
        }
    }*/

   //metodos Asyncronos...

    private  class ExecSoapGuiaDespachoProcesarAsync extends AsyncTask<Void, Void, Void> {

        String lCodBodega = spinnerMap.get(mSpinnerDestino.getSelectedItemPosition());
        String mdestino = mSpinnerDestino.getSelectedItem().toString();
        DataSourceDto dto;
        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Void... voids) {
            if(egDetailResponse.status.codigo.equals("00")){

                try {
                    //mWSParametersGuiaDespachoProc
                    rfidService.SOAP_ACTION_ =  mWSParametersGuiaDespachoProc[0];
                    rfidService.METHOD_NAME_ =  mWSParametersGuiaDespachoProc[1];
                    rfidService.NAMESPACE_ = mWSParametersGuiaDespachoProc[2];
                    rfidService.URL_ = mWSParametersGuiaDespachoProc[3];
                    dto= rfidService.GuiaDespachoProcesarService(egDetailResponse.getItems(),gGuiaEntrada,lCodBodega);

                } catch (Exception ex){
                    dto = null;
                    loDialogo.gMostrarMensajeError(loVistaDialogo, ex.getMessage());
                    //Toast.makeText(mContext, ex.getMessage() , Toast.LENGTH_SHORT).show();
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);
            progressDialog.cancel();

            if(dto != null){
                if(dto.getCodigo() != null ){
                    if(dto.getCodigo().equals("00")){
                        try {
                            int cantidadtotal = 0 ;
                            for (EGTagsResponseItem item :egDetailResponse.getItems()) {
                                cantidadtotal += item.getCantidadLeidos();
                            }
                            PrintScrenQr(dto.auxiliar, mdestino, cantidadtotal);
                        }catch (Exception ex){

                            //DialogMessage("Error al generar el codigo QR::"+ex.getMessage());
                            loDialogo.gMostrarMensajeError(loVistaDialogo, "Error al generar el codigo QR::"+ex.getMessage());
                        }
                    }
                    else {
                        //DialogMessage(dto.getDescripcion());
                        loDialogo.gMostrarMensajeAdvertencia(loVistaDialogo, dto.getDescripcion());
                    }
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

    // metodos generales

    private void PrintScrenQr(String cadenaQr, String destino, int cantidadleidos) {

        UtilityFuntions utilityFuntions = new UtilityFuntions();
        ReportsTemplate reportsTemplate = new ReportsTemplate();
        QrData qrData = new QrData();
        PersistenceDataIteration persistenceDataIteration = new PersistenceDataIteration(mContext);
        LoginData loginData = persistenceDataIteration.LoginDataPersistence();


        qrCodeGenerator = new QRCodeGenerator();
        Bitmap bitmap = qrCodeGenerator.QrCodePrint(cadenaQr);

        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_qrcode);
        dialog.setCancelable(false);

        ImageView imageView = (ImageView) dialog.findViewById(R.id.iv_qrcode);
        TextView tvTittle = (TextView) dialog.findViewById(R.id.tvTitle);
        mWb_qrcode = (WebView) dialog.findViewById(R.id.webview_qrcode);
        Button mdialogBtnAceptar = (Button) dialog.findViewById(R.id.dialogBtnAceptar);
        Button mdialogBtnCancelar = (Button) dialog.findViewById(R.id.dialogBtnCancelar);


        Bitmap bitmap1 = utilityFuntions.getResizedBitmap(bitmap, 100);

        qrData.setContenidoQr(cadenaQr);
        qrData.setImageQrBase64(utilityFuntions.BitmapToBase64(bitmap1));
        qrData.setUsuario(loginData.getUsuario().getCodigo().toUpperCase());
        qrData.setMotivo(destino);
        qrData.setCantidad(cantidadleidos+"");

        String html = reportsTemplate.PlantillaHtmlQR(qrData);

        mWb_qrcode.getSettings().setJavaScriptEnabled(true);
        tvTittle.setText("Se ha generado correctamente la Guia de Despacho");
        mWb_qrcode.loadData(html, "text/html", "UTF-8");
        //imageView.setImageBitmap(bitmap);
        /*tvTittle.setText("Se ha generado correctamente la Guia de Despacho: Secuencia #: "+cadenaQr);*/


        mdialogBtnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //dialog.dismiss();
                /*doPhotoPrint(bitmap);*/
                createWebPrintJob(mWb_qrcode) ;
            }
        });

        mdialogBtnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialogo_confirmacion, loVistaDialogo, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.myDialog));
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                Button btnOk = dialogView.findViewById(R.id.btnConfirmar);
                Button btnCancelar = dialogView.findViewById(R.id.btnCancelar);
                TextView poLabelTexto = dialogView.findViewById(R.id.lblTextoLabel);
                poLabelTexto.setText("¿Esta seguro que desea cancelar la impresión?");


                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                AlertDialog.Builder alerta = new AlertDialog.Builder(mContext);
                alerta.setMessage("Esta seguro de cancelar la impresion")
                        .setCancelable(false)
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog_, int which){
                                dialog.dismiss();
                                dialog_.dismiss();
                            }
                        });

                alerta.show();
                */
            }
        });
        dialog.show();
    }

    private void createWebPrintJob(WebView webView)
    {
        PrintManager printManager = (PrintManager) getActivity().getSystemService(Context.PRINT_SERVICE);
        String jobName = getString(R.string.app_name) + " Guia de Despacho";
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter(jobName);
        PrintAttributes.Builder builder = new PrintAttributes.Builder();
        builder.setMediaSize(PrintAttributes.MediaSize.ISO_A6);
        PrintJob printJob = printManager.print(jobName, printAdapter,builder.build());

        if(printJob.isCompleted()){
            //Toast.makeText(mContext, "Impresion completada", Toast.LENGTH_LONG).show();
            loDialogo.gMostrarMensajeOk(loVistaDialogo, null);
        }
        else if(printJob.isFailed()) {
            //Toast.makeText(mContext, "Impresion erronea", Toast.LENGTH_LONG).show();
            loDialogo.gMostrarMensajeAdvertencia(loVistaDialogo, "Se presento probleas en la Impresión.");
        }

    }


   /* private void doPhotoPrint(Bitmap bitmap) {
        PrintHelper photoPrinter = new PrintHelper(mContext);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);

        *//*Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.droids);*//*
        photoPrinter.printBitmap("droids.jpg - test print", bitmap);
    }*/


    // Dialogs

    private void DialogProcesar(){
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialogo_confirmacion, loVistaDialogo, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.myDialog));
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        Button btnOk = dialogView.findViewById(R.id.btnConfirmar);
        Button btnCancelar = dialogView.findViewById(R.id.btnCancelar);
        TextView poLabelTexto = dialogView.findViewById(R.id.lblTextoLabel);
        poLabelTexto.setText("¿Esta seguro que desea Generar la Guia de Salida?");


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                gGuiaEntrada = met_nGuiaEntrada.getText().toString();

                if(!gGuiaEntrada.trim().isEmpty()){
                    mWSParametersGuiaDespachoProc = getResources().getStringArray(R.array.WSparameter_GuiaDespachoProcesar);
                    ExecSoapGuiaDespachoProcesarAsync tarea = new ExecSoapGuiaDespachoProcesarAsync();
                    tarea.execute();
                }else {

                    loDialogo.gMostrarMensajeAdvertencia(loVistaDialogo, "Ingrese un Numero de Guia");
                    //Toast.makeText(mContext,"Ingrese un Numero de Guia",Toast.LENGTH_SHORT).show();
                }

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
        alerta.setMessage("Esta seguro de Generar la Guia de Salida...")
                .setCancelable(false)
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        gGuiaEntrada = met_nGuiaEntrada.getText().toString();

                        if(!gGuiaEntrada.trim().isEmpty()){
                            mWSParametersGuiaDespachoProc = getResources().getStringArray(R.array.WSparameter_GuiaDespachoProcesar);
                            ExecSoapGuiaDespachoProcesarAsync tarea = new ExecSoapGuiaDespachoProcesarAsync();
                            tarea.execute();
                        }else {

                            Toast.makeText(mContext,"Ingrese un Numero de Guia",Toast.LENGTH_SHORT).show();
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
        */
    }

    private void DialogMessage(String msj){
        AlertDialog.Builder alerta = new  AlertDialog.Builder(mContext);
        alerta.setMessage(msj)
        .setCancelable(false)
        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alerta.show();
    }
}
