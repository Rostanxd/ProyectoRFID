package co.kr.bluebird.newrfid.app.bbrfidbtdemo.fragmentvct;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
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
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import co.kr.bluebird.newrfid.app.bbrfidbtdemo.R;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.DGDetalle;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.DataSourceDto;

import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.DataSourceDtoEx;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.DespatchGuide;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EGDetailResponse;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EGProcesado;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EGTagsResponseItem;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.LoginData;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ParamLectorRfid;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.QrData;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.service.RfidService;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.CustomListAdapterDespatchGuide;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.ParamRfidIteration;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.PersistenceDataIteration;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.QRCodeGenerator;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.ReportsTemplate;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.RfidEpHomologacion;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.UtilityFuntions;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShippingWareGenerateFragment extends Fragment {


    private Spinner mSpinnerMotivo, mSpinnerBodAlmacenamiento;
    //private TableLayout tblGDDestino;
    private Context mContext, mDialogContext;
    private RfidService rfidService;
    private List<DataSourceDto> listaMotivo;
    private Button mprocesar_imgbtn, mbtnAddNota;
    private QRCodeGenerator qrCodeGenerator;
    private TextView mtvNota;
    private ArrayList<String> epcs;
    private EGDetailResponse egDetailResponse;
    private RfidEpHomologacion mRfidEpHomologacion;
    private EGProcesado egProcesado;
    private ListView mlv_detailSW;
    private boolean first ;
    private DespatchGuide despatchGuide;
    private String gNota, CodBodAlmacenamiento = null;
    private String[] mWSParametersMotivos, mWSParametersEnvioMercaderiaProc, mWSParameterBodegas;

    private ParamRfidIteration paramRfidIteration;
    private ParamLectorRfid paramLectorRfid_;

    private String[] spinnerArray = null, spinnerArrayAlm = null;
    private HashMap<Integer,String> spinnerMap = null;
    private HashMap<Integer,String> spinnerMapAlm = null;

    private WebView wb_qrcode;

    private LinearLayout mLayoutBodAlmacenamiento;

    public ShippingWareGenerateFragment() {
        // Required empty public constructor
    }

    public static ShippingWareGenerateFragment newInstance() {

        return new ShippingWareGenerateFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.shippingwaregenerate_frag, container, false);
        mContext = inflater.getContext();
        mSpinnerMotivo = (Spinner)v.findViewById(R.id.spinnerMotivo);
        mSpinnerBodAlmacenamiento = (Spinner)v.findViewById(R.id.spinnerBodAlmacenamiento);
        //tblGDDestino = (TableLayout)v.findViewById(R.id.tablelayoutDetalleGD);
        mprocesar_imgbtn = (Button) v.findViewById(R.id.procesar_imgbtn);

        Drawable myIcon = null;
        ColorFilter filter = null;

        myIcon = getResources().getDrawable( R.drawable.ejecutar );
        filter = new LightingColorFilter( Color.BLACK, Color.WHITE);
        myIcon.setColorFilter(filter);
        mprocesar_imgbtn.setCompoundDrawablesWithIntrinsicBounds( null, null, myIcon, null);

        mbtnAddNota = (Button) v.findViewById(R.id.btnAddNota);

        myIcon = getResources().getDrawable( R.drawable.materialadd18 );
        filter = new LightingColorFilter( Color.BLACK, Color.WHITE);
        myIcon.setColorFilter(filter);

        mbtnAddNota.setCompoundDrawablesWithIntrinsicBounds( myIcon, null, null, null);

        mbtnAddNota.setOnClickListener(btnAddNotaOnClick);

        mtvNota = (TextView) v.findViewById(R.id.tvNota);

        mlv_detailSW = (ListView)v.findViewById(R.id.lv_detailSW);



        epcs = getArguments() != null ? getArguments().getStringArrayList("epcsLeidos") : new ArrayList<String>();

        first = true;
        rfidService = new RfidService(mContext);

        paramRfidIteration = new ParamRfidIteration(mContext);
        paramLectorRfid_ =  paramRfidIteration.ConsultarParametros();

        mLayoutBodAlmacenamiento = (LinearLayout)  v.findViewById(R.id.LayoutBodAlmacenamiento);

        executeSoapEnvioMercaderiaDetAsync envioMercaderiaDetAsync = new executeSoapEnvioMercaderiaDetAsync();
        envioMercaderiaDetAsync.execute();

        if(paramLectorRfid_.getCodbodega().equals("01") )
        {
            mSpinnerBodAlmacenamiento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    CodBodAlmacenamiento = spinnerMapAlm.get(i);
                    LLenarSpinnerDestino();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            mLayoutBodAlmacenamiento.setVisibility(View.VISIBLE);
            LlenarSpinnerBodegas();
        }
        else {
            mLayoutBodAlmacenamiento.setVisibility(View.GONE);
            LLenarSpinnerDestino();
        }




        //SpinnerComplete();
        clickBtnProcesar();

        return v;
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
                mtvNota.setText(medNota.getText().toString());
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    private  void clickBtnProcesar()
    {
        mprocesar_imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogProcesar();
            }
        });
    }


    private void LLenarSpinnerDestino()
    {

        mWSParametersMotivos = getResources().getStringArray(R.array.WSparameter_Motivos);
        //DespatchGuide despatchGuide = rfidService.GuiaDespachoBodegasService();
        executeSoapListMotivoAsync tarea = new executeSoapListMotivoAsync();
        tarea.execute();

    }

    private void LlenarSpinnerBodegas()
    {
        mWSParameterBodegas = getResources().getStringArray(R.array.WSparameter_Bodegas);
        executeSoapListBodegaAsync tarea = new executeSoapListBodegaAsync();
        tarea.execute();
    }

    private  void SpinnerComplete(){

        spinnerArray = new String[despatchGuide.bodegas.size()];
        spinnerMap = new HashMap<Integer, String>();

        int i = 0;
        for (DataSourceDto dto:despatchGuide.bodegas) {
            spinnerMap.put(i,dto.codigo);
            spinnerArray[i] = dto.descripcion;
            i++;
        }


        ArrayAdapter<String> adapter1 =new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item, spinnerArray);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerMotivo.setAdapter(adapter1);
    }

    private  void SpinnerBodegasComplete(){

        spinnerArrayAlm = new String[despatchGuide.bodegas.size()];
        spinnerMapAlm = new HashMap<Integer, String>();

        int i = 0;
        for (DataSourceDto dto:despatchGuide.bodegas) {
            spinnerMapAlm.put(i,dto.codigo);
            spinnerArrayAlm[i] = dto.descripcion;
            i++;
        }


        ArrayAdapter<String> adapter1 =new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item, spinnerArrayAlm);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerBodAlmacenamiento.setAdapter(adapter1);
    }

    private  class executeSoapListMotivoAsync extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;
        @Override
        protected Void doInBackground(Void... voids) {
            //listaMotivo = rfidService.SimularListaMotivos();
            rfidService.SOAP_ACTION_ =  mWSParametersMotivos[0];
            rfidService.METHOD_NAME_ =  mWSParametersMotivos[1];
            rfidService.NAMESPACE_ = mWSParametersMotivos[2];
            rfidService.URL_ = mWSParametersMotivos[3];

            despatchGuide = rfidService.WSBodegasOrMotivosService(false, mContext, null, CodBodAlmacenamiento);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);
            //SpinnerComplete();

            progressDialog.cancel();
            if(despatchGuide != null && despatchGuide.getEstado() != null && despatchGuide.getEstado().getCodigo().equals("9999")){
                Toast.makeText(mContext,despatchGuide.getEstado().getDescripcion(), Toast.LENGTH_SHORT).show();
            }
            else {
                if(despatchGuide.estado != null && despatchGuide.estado.codigo.equals("00")){
                    SpinnerComplete();
                }
                else {
                    Toast.makeText(mContext, "Llamada a Servicio Erroneo::Codigo: "+despatchGuide.estado.codigo , Toast.LENGTH_SHORT).show();
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


    private  class executeSoapListBodegaAsync extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;
        @Override
        protected Void doInBackground(Void... voids) {
            //listaMotivo = rfidService.SimularListaMotivos();
            rfidService.SOAP_ACTION_ =  mWSParameterBodegas[0];
            rfidService.METHOD_NAME_ =  mWSParameterBodegas[1];
            rfidService.NAMESPACE_ = mWSParameterBodegas[2];
            rfidService.URL_ = mWSParameterBodegas[3];

            despatchGuide = rfidService.WSBodegasOrMotivosService(true, mContext, "ENV", null);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);
            //SpinnerComplete();

            progressDialog.cancel();
            if(despatchGuide != null && despatchGuide.getEstado() != null && despatchGuide.getEstado().getCodigo().equals("9999")){
                Toast.makeText(mContext,despatchGuide.getEstado().getDescripcion(), Toast.LENGTH_SHORT).show();
            }
            else {
                if(despatchGuide.estado != null && despatchGuide.estado.codigo.equals("00")){
                    SpinnerBodegasComplete();
                }
                else {
                    Toast.makeText(mContext, "Llamada a Servicio Erroneo::Codigo: "+despatchGuide.estado.codigo , Toast.LENGTH_SHORT).show();
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

    // Metodos Asyncronos
    private  class executeSoapEnvioMercaderiaDetAsync extends AsyncTask<Void, Void, Void> {
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
                if(egDetailResponse.status != null && egDetailResponse.status.codigo.equals("00")){
                    //AgruparItemCodigoEpc();
                    LlenarGrid(egDetailResponse);

                    //Toast.makeText(mContext, "Llamada a Web Services Correctamente", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(mContext, "Llamada a Servicio Erroneo:: "+egDetailResponse.getStatus().getDescripcion() , Toast.LENGTH_SHORT).show();
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

    private  class ExecSoapEnvioMercaderiaProcesarAsync extends AsyncTask<Void, Void, Void> {

        String lMotivo = spinnerMap.get(mSpinnerMotivo.getSelectedItemPosition());
        String motivoDesc = mSpinnerMotivo.getSelectedItem().toString();

        DataSourceDtoEx dto;
        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Void... voids) {
            if(egDetailResponse.status.codigo.equals("00")){

                /*try {
                    dto= rfidService.WSEnvioMercaderiaProcesarService(mContext, egDetailResponse.getItems(),lMotivo,gNota);

                } catch (Exception ex){
                    dto = null;
                    //Toast.makeText(mContext, ex.getMessage() , Toast.LENGTH_SHORT).show();
                }*/


                rfidService.SOAP_ACTION_ =  mWSParametersEnvioMercaderiaProc[0];
                rfidService.METHOD_NAME_ =  mWSParametersEnvioMercaderiaProc[1];
                rfidService.NAMESPACE_ = mWSParametersEnvioMercaderiaProc[2];
                rfidService.URL_ = mWSParametersEnvioMercaderiaProc[3];


                dto= rfidService.WSEnvioMercaderiaProcesarService(mContext, egDetailResponse.getItems(),lMotivo,gNota);


            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);
            progressDialog.cancel();
            if(!dto.getHandlerException().isExceptionExist()){
                if(dto.getInformationDto() != null &&dto.getInformationDto().getCodigo().equals("00") ){
                    try {
                        int cantidadtotal = 0;
                        for (EGTagsResponseItem item :egDetailResponse.getItems()) {

                            cantidadtotal += item.getCantidadLeidos();

                        }
                        PrintScrenQr(dto.getInformationDto().getAuxiliar(), motivoDesc, cantidadtotal);

                    }catch (Exception ex){
                        Toast.makeText(mContext, "Error al generar el codigo QR::"+ex.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(mContext, "Llamada a Servicio Erroneo:: "+dto.getInformationDto().getDescripcion() , Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(mContext, "Llamada a Servicio Erroneo:: "+dto.getHandlerException().getExceptionMessage() , Toast.LENGTH_SHORT).show();
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



    //

    private void PrintScrenQr(String cadenaQr, String motivoDesc, int CantLeidos ) {

        UtilityFuntions utilityFuntions = new UtilityFuntions();
        ReportsTemplate reportsTemplate = new ReportsTemplate();

        QrData qrData = new QrData();
        PersistenceDataIteration persistenceDataIteration = new PersistenceDataIteration(mContext);
        LoginData loginData = persistenceDataIteration.LoginDataPersistence();


        qrCodeGenerator = new QRCodeGenerator();
        Bitmap bitmap = qrCodeGenerator.QrCodePrint(cadenaQr);

        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_qrcode);

        ImageView imageView = (ImageView) dialog.findViewById(R.id.iv_qrcode);
        TextView tvTittle = (TextView) dialog.findViewById(R.id.tvTitle);
        wb_qrcode = (WebView) dialog.findViewById(R.id.webview_qrcode);
        Button mdialogBtnAceptar = (Button) dialog.findViewById(R.id.dialogBtnAceptar);

        Bitmap bitmap1 = utilityFuntions.getResizedBitmap(bitmap, 100);

        qrData.setContenidoQr(cadenaQr);
        qrData.setImageQrBase64(utilityFuntions.BitmapToBase64(bitmap1));
        qrData.setUsuario(loginData.getUsuario().getCodigo().toUpperCase());
        qrData.setMotivo(motivoDesc);
        qrData.setCantidad(CantLeidos+"");

        String html = reportsTemplate.PlantillaHtmlQR(qrData);

        wb_qrcode.getSettings().setJavaScriptEnabled(true);
        //webView.loadUrl("http://www.google.com");

        //String customHtml = "<html><body><h1>Hello, WebView</h1></body></html>";

        tvTittle.setText("Se ha generado correctamente el egreso de mercaderia");
        wb_qrcode.loadData(html, "text/html", "UTF-8");


        //doWebViewPrint(html);
        /*imageView.setImageBitmap(bitmap);
        tvTittle.setText("Se ha generado correctamente el egreso de mercaderia: Secuencia #: "+cadenaQr);*/

        mdialogBtnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dialog.dismiss();
                //doPhotoPrint(bitmap);

                createWebPrintJob(wb_qrcode);
            }
        });
        dialog.show();
    }


    private void createWebPrintJob(WebView webView) {

        // Get a PrintManager instance
        PrintManager printManager = (PrintManager) getActivity()
                .getSystemService(Context.PRINT_SERVICE);

        String jobName = getString(R.string.app_name) + " Envio de Mercaderia";

        // Get a print adapter instance
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter(jobName);

        PrintAttributes.Builder builder = new PrintAttributes.Builder();
        builder.setMediaSize(PrintAttributes.MediaSize.ISO_A6);

        // Create a print job with name and adapter instance
        PrintJob printJob = printManager.print(jobName, printAdapter,
                builder.build());

        if(printJob.isCompleted()){
            Toast.makeText(mContext, "Impresion completada", Toast.LENGTH_LONG).show();
        }
        else if(printJob.isFailed()){
            Toast.makeText(mContext, "Impresion erronea", Toast.LENGTH_LONG).show();
        }
    }


    /*private void doPhotoPrint(Bitmap bitmap) {
        PrintHelper photoPrinter = new PrintHelper(mContext);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        *//*Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.droids);*//*
        photoPrinter.printBitmap("droids.jpg - test print", bitmap);
    }*/



    private void LlenarGrid(EGDetailResponse detailResponse){

        mRfidEpHomologacion = new RfidEpHomologacion();

        egProcesado = mRfidEpHomologacion.DetailResponseToEGProcesado(detailResponse);
        ProcesarLvDetailSW();
        //createColumns();
        //CrearRow();
    }

    private void ProcesarLvDetailSW()
    {
        mlv_detailSW.setAdapter(null);

        if(first){
            //View headerview = View.inflate(mContext, R.layout.header_2column, null);
            View footerview = View.inflate(mContext, R.layout.footer_2column, null);
            TextView tvFCol1 =(TextView) footerview.findViewById(R.id.tvCol2);
            //mlv_detailSW.addHeaderView(headerview);
            mlv_detailSW.addFooterView(footerview);
            tvFCol1.setText(String.valueOf(egProcesado.totalcant));
            first = false;
        }
        mlv_detailSW.setAdapter(new CustomListAdapterDespatchGuide(mContext, egProcesado.items));
        //mlv_detailGD.setOnItemClickListener(listItemClickListener);
    }

    // Dialogs

    private void DialogProcesar(){
        AlertDialog.Builder alerta = new AlertDialog.Builder(mContext);
        alerta.setMessage("Esta seguro realizar el Egreso de Mercaderia...")
                .setCancelable(false)
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        gNota = mtvNota.getText().toString();

                        if(!gNota.trim().isEmpty()){
                            try {
                                //WSparameter_EnvioMercaderiaProcesar
                                mWSParametersEnvioMercaderiaProc = getResources().getStringArray(R.array.WSparameter_EnvioMercaderiaProcesar);
                                ExecSoapEnvioMercaderiaProcesarAsync tarea = new ExecSoapEnvioMercaderiaProcesarAsync();
                                tarea.execute();
                            }
                            catch (Exception ex){
                                Toast.makeText(mContext, ex.getMessage(), Toast.LENGTH_LONG).show();
                            }

                        }else {

                            Toast.makeText(mContext,"Ingrese una nota",Toast.LENGTH_SHORT).show();
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

}
