package co.kr.bluebird.newrfid.app.bbrfidbtdemo.fragmentvct;


import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.TableLayout;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

import co.kr.bluebird.newrfid.app.bbrfidbtdemo.R;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.control.EntryGuide;
/*import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EntryGuide;*/
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.Guide;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.models.EntryGuideModel;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.service.RfidService;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.CustomListAdapterEntryGuideCheck;

/**
 * A simple {@link Fragment} subclass.
 */
public class EntryGuideCheckFragment extends Fragment {



    private Context mContext;
    public EntryGuideRead2Fragment mEntryGuideReadFragment;
    private FragmentManager mFragmentManager;
    private Button btnCargar;

    private TextView mtvCantidadOCGR;
    //private TextView mtvCantidadTotalval;
    private RfidService rfidService;
    private EditText mOrdenCompraGR;
    private String numeroOrdenCompra;
    private int cantGuide = 0;
    private ListView mlv_entriesGuide;
    private boolean first ;

    private String[] mWSParameters;
    private String NoOrdenCompra = null;


    private co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EntryGuide ResposeEG;

    public EntryGuideCheckFragment() {
        // Required empty public constructor
    }

    public static EntryGuideCheckFragment newInstance() {

        return new EntryGuideCheckFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.entryguidecheck_frag, container, false);


        mContext = inflater.getContext();
        rfidService = new RfidService(mContext);
        mOrdenCompraGR = (EditText)v.findViewById(R.id.et_nGuiaEntrada);
        btnCargar = (Button)v.findViewById(R.id.btnCargarCE);
        //metCantidadOCGR = (EditText)v.findViewById(R.id.edOrdenCompraGR);
        mtvCantidadOCGR = (TextView)v.findViewById(R.id.tvCantidadOCGR);
        //mtvCantidadTotalval = (TextView)v.findViewById(R.id.tvCantidadTotalval);
        mlv_entriesGuide = (ListView)v.findViewById(R.id.lv_entriesGuide);

        NoOrdenCompra = getArguments() != null ? getArguments().getString("NoOCompra") : "0";

        first = true;
        btnCargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CleanTablerow();
                btnCargar_extracted();

            }
        });

        return  v;
    }

    @Override
    public void onStart() {

        super.onStart();

        if(!mOrdenCompraGR.getText().toString().trim().equals("") || (NoOrdenCompra != null && !NoOrdenCompra.equals("0") )){

            if(NoOrdenCompra != null && !NoOrdenCompra.equals("0")){
                mOrdenCompraGR.setText(NoOrdenCompra);
            }
            btnCargar_extracted();
        }

    }

    private void btnCargar_extracted(){


        ResposeEG = null;
        cantGuide = 0;
        numeroOrdenCompra = mOrdenCompraGR.getText().toString();
        if(!numeroOrdenCompra.trim().equals("")){
            mWSParameters = getResources().getStringArray(R.array.WSparameter_GuiaEntradaOC);
            executeSoapAsync tarea = new executeSoapAsync();
            tarea.execute();
        }
        else {
            Toast.makeText(mContext, "Ingrese No. Orden de Compra....",Toast.LENGTH_SHORT).show();
        }
    }

    public void CleanControls(){
        mOrdenCompraGR.setText("");
    }

    private void LlenarGrid(){

        //createColumns();
        //CrearRow();
        ProcesarLvDetailGD();
        setTotales();
    }

    private void ProcesarLvDetailGD()
    {
        cantGuide = 0;
        if(ResposeEG != null && ResposeEG.data_ != null && ResposeEG.data_.guias != null &&  ResposeEG.data_.guias.size() > 0)
        {
            for (Guide guide :ResposeEG.data_.guias) {
                cantGuide += guide.cantidad;
            }

            mlv_entriesGuide.setAdapter(null);

            if(first){
                View headerview = View.inflate(mContext, R.layout.header_3column, null);
                View footerview = View.inflate(mContext, R.layout.footer_2column, null);
                TextView tvFCol1 =(TextView) footerview.findViewById(R.id.tvCol2);
                mlv_entriesGuide.addHeaderView(headerview,null,false);
                mlv_entriesGuide.addFooterView(footerview, null,false);
                tvFCol1.setText(String.valueOf(cantGuide));
                first = false;
            }

            mlv_entriesGuide.setAdapter(new CustomListAdapterEntryGuideCheck(mContext, ResposeEG.data_.guias));
            mlv_entriesGuide.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Guide i = (Guide) mlv_entriesGuide.getItemAtPosition(position);
                    /*item item_tagNoRead = findTagNoLeido(i.itemCodigo);
                    ProcesarLv_tagsNoEnc(item_tagNoRead.tagsNoLeidos.getEpc());*/

                    String nombre = i.descripcion;
                    String NoGuia = i.numero;
                    String NoOCompra = mOrdenCompraGR.getText().toString();
                    String NoGuiaCant = String.valueOf(i.getCantidad()) ;
                    AlertDialog.Builder alerta = new AlertDialog.Builder(mContext);
                    if(!nombre.equalsIgnoreCase("pendiente")){
                        alerta.setMessage("Desea Transaccionar con el No. Guia: "+NoGuia +", con estado "+nombre)
                                .setCancelable(false)
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        if (mEntryGuideReadFragment == null)
                                            mEntryGuideReadFragment = mEntryGuideReadFragment.newInstance();
                                        Bundle args = new Bundle();
                                        args.putString("NoGuia", NoGuia);
                                        args.putString("NoOCompra",NoOCompra);
                                        args.putString("NoGuiaCant", NoGuiaCant);

                                        mEntryGuideReadFragment.setArguments(args);

                                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                                        ft.replace(R.id.content, mEntryGuideReadFragment);
                                        ft.addToBackStack(null);

                                        //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                        //ft.addToBackStack(null);
                                        ft.commit();

                                        //Toast.makeText(mContext,"Selecciono el item: "+ nombre,Toast.LENGTH_LONG).show();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                    }
                    else {
                        alerta.setMessage("La Guia: "+NoGuia +", ya se encuentra procesada")
                                .setCancelable(true)
                                .setNeutralButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                    }

                    AlertDialog title = alerta.create();
                    title.setTitle("Información");
                    title.show();
                }
            });
        }

        //mlv_detailGD.setOnItemClickListener(listItemClickListener);
    }
    private void setTotales(){
        mtvCantidadOCGR.setText( String.valueOf(ResposeEG.data_.cantidadTotal) );
        //mtvCantidadTotalval.setText(String.valueOf(cantGuide));
    }



    private  class executeSoapAsync extends AsyncTask<Void, Void, Void> {

        //WSparameter_GuiaEntradaOC
        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Void... voids) {
            rfidService.SOAP_ACTION_ =  mWSParameters[0];
            rfidService.METHOD_NAME_ =  mWSParameters[1];
            rfidService.NAMESPACE_ = mWSParameters[2];
            rfidService.URL_ = mWSParameters[3];
            ResposeEG = rfidService.GuiaEntradaService(numeroOrdenCompra);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);

            progressDialog.cancel();
            if(ResposeEG.estado   != null ){
                if(ResposeEG.estado.codigo.equals("00")){
                    if(ResposeEG.data_ != null && ResposeEG.data_.guias.size() > 0){
                        LlenarGrid();
                    }
                    else {
                        Toast.makeText(mContext, "No hay informacion que mostrar..." , Toast.LENGTH_SHORT).show();
                    }
                }
                else if(ResposeEG.estado.codigo.equals("9999")){
                    Toast.makeText(mContext, ResposeEG.estado.descripcion, Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(mContext, ResposeEG.estado.descripcion , Toast.LENGTH_SHORT).show();
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
}
