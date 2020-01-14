package co.kr.bluebird.newrfid.app.bbrfidbtdemo.fragmentvct;


import android.app.AlertDialog;
import android.app.Dialog;
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
import android.support.v4.print.PrintHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.service.RfidService;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.CustomListAdapterDespatchGuide;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.QRCodeGenerator;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.RfidEpHomologacion;

/**
 * A simple {@link Fragment} subclass.
 */
public class DespatchGuideGenerateFragment extends Fragment {


    private Spinner mSpinnerDestino;
    //private TableLayout tblGDDestino;
    private Context mContext;
    private RfidService rfidService;
    private DespatchGuide despatchGuide;
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
                DialogProcesar();
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

            despatchGuide = rfidService.WSBodegasOrMotivosService(true, mContext, "GDE", null);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);

            if(despatchGuide != null && despatchGuide.getEstado() != null && despatchGuide.getEstado().getCodigo().equals("9999")){
                Toast.makeText(mContext,despatchGuide.getEstado().getDescripcion(), Toast.LENGTH_SHORT).show();
            }
            else {
                if (despatchGuide.estado != null && despatchGuide.estado.codigo.equals("00")) {
                    SpinnerComplete();
                    progressDialog.cancel();
                } else {
                    progressDialog.cancel();
                    Toast.makeText(mContext, "Llamada a Servicio Erroneo::Codigo: " + despatchGuide.estado.codigo, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(mContext, "Llamada a Servicio Erroneo:: " + egDetailResponse.status.descripcion, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(mContext, ex.getMessage() , Toast.LENGTH_SHORT).show();
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
                            PrintScrenQr(dto.auxiliar);
                        }catch (Exception ex){
                            //Toast.makeText(mContext, "Error al generar el codigo QR::"+ex.getMessage(),Toast.LENGTH_SHORT).show();
                            DialogMessage("Error al generar el codigo QR::"+ex.getMessage());
                        }
                    }
                    else {
                        //Toast.makeText(mContext,dto.getDescripcion(), Toast.LENGTH_SHORT).show();
                        DialogMessage(dto.getDescripcion());
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

    private void PrintScrenQr(String cadenaQr) {
        qrCodeGenerator = new QRCodeGenerator();
        Bitmap bitmap = qrCodeGenerator.QrCodePrint(cadenaQr);

        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_qrcode);

        ImageView imageView = (ImageView) dialog.findViewById(R.id.iv_qrcode);
        TextView tvTittle = (TextView) dialog.findViewById(R.id.tvTitle);
        Button mdialogBtnAceptar = (Button) dialog.findViewById(R.id.dialogBtnAceptar);
        imageView.setImageBitmap(bitmap);
        tvTittle.setText("Se ha generado correctamente la Guia de Despacho: Secuencia #: "+cadenaQr);

        mdialogBtnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //dialog.dismiss();

                doPhotoPrint(bitmap);
            }
        });
        dialog.show();
    }

    private void doPhotoPrint(Bitmap bitmap) {
        PrintHelper photoPrinter = new PrintHelper(mContext);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);


        /*Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.droids);*/
        photoPrinter.printBitmap("droids.jpg - test print", bitmap);
    }


    // Dialogs

    private void DialogProcesar(){
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
