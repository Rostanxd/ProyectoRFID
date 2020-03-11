package co.kr.bluebird.newrfid.app.bbrfidbtdemo.fragmentvct;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import co.kr.bluebird.newrfid.app.bbrfidbtdemo.R;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.DataSourceDto;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EGTagsResponseItem;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.service.RfidService;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.CustomListAdapterReceiveWare;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.clsMensaje;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReceiveWareInconsistencyFragment extends Fragment {

    private Context mContext, mDialogContext;
    private List<EGTagsResponseItem> responseItems;
    private ListView mlv_itemsInconsistentes;
    //private boolean first;
    private EditText  metDocOrigenRW, metDocDestinoRW, metMotivoRW;
    private TextView metNotaRW;
    private Button mbtnConfirmarRW, mbtnAddNota;
    private RfidService rfidService;
    private String DocOrigen,DocDestino,Motivo;
    private String[] mWSParameter_RecepcionProcesar;

    private ViewGroup loVistaDialogo;
    private clsMensaje loDialogo;

    private LinearLayout mlayoutHeaderRWI;

    public ReceiveWareInconsistencyFragment() {
        // Required empty public constructor
    }
    public static ReceiveWareInconsistencyFragment newInstance() {

        return new ReceiveWareInconsistencyFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.receivewareinconsistency_frag, container, false);
        View v = inflater.inflate(R.layout.receivewareinconsistency_frag, container, false);
        mContext = inflater.getContext();

        //##################### CLASE MENSAJE (DIALOGO)######################
        loDialogo = new clsMensaje(mContext);
        loVistaDialogo = v.findViewById(android.R.id.content);
        //###################################################################

        mlv_itemsInconsistentes = (ListView) v.findViewById(R.id.lv_itemsInconsistentes);
        metNotaRW = (TextView) v.findViewById(R.id.tvNotaRW);
        metDocOrigenRW= (EditText) v.findViewById(R.id.etDocOrigenRW);
        metDocDestinoRW= (EditText) v.findViewById(R.id.etDocDestinoRW);
        metMotivoRW= (EditText) v.findViewById(R.id.etMotivoRW);
        mlayoutHeaderRWI = (LinearLayout) v.findViewById(R.id.layoutHeaderRWI);
        mlayoutHeaderRWI.setVisibility(View.GONE);

        //DocOrigen,DocDestino,Motivo

        mbtnConfirmarRW = (Button)v.findViewById(R.id.btnConfirmarRW);
        mbtnAddNota = (Button) v.findViewById(R.id.btnAddNota);



        Drawable myIcon = null;
        ColorFilter filter = null;

        myIcon = getResources().getDrawable( R.drawable.materialcheck );
        filter = new LightingColorFilter( Color.BLACK, Color.WHITE);
        myIcon.setColorFilter(filter);

        mbtnConfirmarRW.setCompoundDrawablesWithIntrinsicBounds( null, null, myIcon, null);

        myIcon = getResources().getDrawable( R.drawable.materialadd18 );
        filter = new LightingColorFilter( Color.BLACK, Color.WHITE);
        myIcon.setColorFilter(filter);

        mbtnAddNota.setCompoundDrawablesWithIntrinsicBounds( myIcon, null, null, null);

        mbtnAddNota.setOnClickListener(btnAddNotaOnClick);


        mbtnConfirmarRW.setOnClickListener(btnConfirmarOnClick);
        rfidService =  new RfidService(mContext);
        responseItems = getArguments() != null ? (List<EGTagsResponseItem>) getArguments().getSerializable("items") : new ArrayList<EGTagsResponseItem>();

        DocOrigen = getArguments() != null ? getArguments().getString("DocOrigen") : "";
        DocDestino = getArguments() != null ? getArguments().getString("DocDestino") : "";
        Motivo = getArguments() != null ? getArguments().getString("Motivo") : "";

        SetParametros();
        //first = true;
        ProcesarLvItemsInc();

        return v;

    }

    private void SetParametros()
    {
        metDocOrigenRW.setText(DocOrigen);
        metDocDestinoRW.setText(DocDestino);
        metMotivoRW.setText(Motivo);
    }

    private View.OnClickListener btnConfirmarOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            DialogBtnConfirmar();
        }
    };

    private void DialogBtnConfirmar(){
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

    private View.OnClickListener btnAddNotaOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            DialogNota();
        }
    };

    private void DialogNota(){
        /*final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_input_text);
        mDialogContext = dialog.getContext();

        dialog.setTitle("Ingrese una nota");

        Button mdialogBtnAceptar = (Button) dialog.findViewById(R.id.btnDialogAceptar);
        EditText medNota = (EditText) dialog.findViewById(R.id.edNota_);*/

        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_input_text, loVistaDialogo, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.myDialog));
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        mDialogContext = alertDialog.getContext();

        Button mdialogBtnAceptar = (Button) dialogView.findViewById(R.id.btnDialogConfirmar);
        Button mdialogBtnCancelar = (Button) dialogView.findViewById(R.id.btnDialogCancelar);
        Button mdialogBtnLimpiar = (Button) dialogView.findViewById(R.id.btnDialogLimpiar);
        EditText medNota = (EditText) dialogView.findViewById(R.id.edNota_);

        mdialogBtnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                metNotaRW.setText(medNota.getText().toString());
                alertDialog.dismiss();

            }
        });
        mdialogBtnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        mdialogBtnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                medNota.setText("");
            }
        });
        alertDialog.show();
    }

    private void ProcesarLvItemsInc()
    {
        mlv_itemsInconsistentes.setAdapter(null);

       /* if(first){
            View headerview = View.inflate(mContext, R.layout.header_4column, null);
            mlv_itemsInconsistentes.addHeaderView(headerview,null,false);
            first = false;
        }*/
        if(responseItems != null && responseItems.size() > 0)
        {
            mlayoutHeaderRWI.setVisibility(View.VISIBLE);
            mlv_itemsInconsistentes.setAdapter(new CustomListAdapterReceiveWare(mContext, responseItems, true));
        }

        //mlv_itemsInconsistentes.setOnItemClickListener(listItemClickListener);

        //mlv_itemsEnc.setOnItemClickListener(listItemClickListener);
    }

    // ASYS Task
    //WSRecepcionMercaderiaProcesar
    private  class exWSRecepcionProcesarAsync extends AsyncTask<Void, Void, Void> {

        String doc_origen = metDocOrigenRW.getText().toString();
        String nota = metNotaRW.getText().toString();
        DataSourceDto dtoResponse = null;
        ProgressDialog progressDialog;

        boolean error = false;
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                rfidService.SOAP_ACTION_ =  mWSParameter_RecepcionProcesar[0];
                rfidService.METHOD_NAME_ =  mWSParameter_RecepcionProcesar[1];
                rfidService.NAMESPACE_ = mWSParameter_RecepcionProcesar[2];
                rfidService.URL_ = mWSParameter_RecepcionProcesar[3];

                dtoResponse = rfidService.WSRecepcionMercaderiaProcesar(doc_origen,nota,responseItems, "");
            }
            catch (Exception ex){
                error = true;
                Toast.makeText(mContext,ex.getMessage(),Toast.LENGTH_SHORT ).show();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);

            progressDialog.cancel();
            if(!error){
                if(dtoResponse != null && dtoResponse.getCodigo() != null && dtoResponse.getCodigo().equals("00")){

                    //llamar al Dialog
                    InvocarAlert();
                }
                else{

                    Toast.makeText(mContext, "Error del servicio: "+ dtoResponse.getDescripcion() , Toast.LENGTH_SHORT).show();
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

    private void InvocarAlert()
    {
        AlertDialog.Builder alerta = new AlertDialog.Builder(mContext);
        alerta.setMessage("Se ha generado correctamente el ingreso de la mercaderia")
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.dismiss();
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

}
