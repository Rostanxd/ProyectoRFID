package co.kr.bluebird.newrfid.app.bbrfidbtdemo.fragmentvct;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import co.kr.bluebird.newrfid.app.bbrfidbtdemo.R;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.DataSourceDto;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.GenericSpinnerDto;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ICSeccion;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.InventoryControl;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ResponseVal;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.service.RfidService;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.CustomListAdapterTakInventoryControl;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.Validator;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.clsMensaje;

/**
 * A simple {@link Fragment} subclass.
 */
public class TakingInventoryControlFragment extends Fragment {

    private Spinner  mspinnerConteo;
    private RfidService rfidService;
    private Context mContext;
    private ListView mlv_Replenishment;
    private boolean first ;
    private GenericSpinnerDto genericSpinnerDto = null;
    private InventoryControl inventoryDetail;
    private String mConteo = null;
    private ProgressBar mpbLeidos;
    private TextView mtvContado;
    private String[] mWSparameterConteos, mWSparameterConteoDetalle;
    private LinearLayout mLayoutInvC;
    private Validator validator;

    private clsMensaje loDialogo;
    private ViewGroup loVistaDialogo;


    /*
    * mpbLeidos = (ProgressBar)v.findViewById(R.id.pbLeidos);
mpbLeidos.setMax(5);
mpbLeidos.setProgress( Integer.parseInt(mCountText.getText().toString()) );
    * */

    public TakingInventoryControlFragment() {
        // Required empty public constructor
    }

    public static TakingInventoryControlFragment newInstance() {
        return new TakingInventoryControlFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.taking_inventory_control_frag, container, false);
        //return inflater.inflate(R.layout.taking_inventory_control_frag, container, false);

        first = true;
        mContext = inflater.getContext();

        //##################### CLASE MENSAJE (DIALOGO)######################
        loDialogo = new clsMensaje(mContext);
        loVistaDialogo = v.findViewById(android.R.id.content);
        //###################################################################
        rfidService =new RfidService(mContext);
        validator = new Validator();
        mpbLeidos = (ProgressBar)v.findViewById(R.id.pbLeidos);
        //mpbLeidos.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#07BDAB")));
        mtvContado = (TextView)v.findViewById(R.id.tvContado);
        mLayoutInvC = (LinearLayout) v.findViewById(R.id.LayoutInvC);
        //InicializarAtributos(v);

        mspinnerConteo = (Spinner)v.findViewById(R.id.spinnerConteo);
        mlv_Replenishment = (ListView) v.findViewById(R.id.lv_Replenishment);

        mWSparameterConteos = getResources().getStringArray(R.array.WSparameter_conteos);


        exWSConteoAsync wsConteoAsync = new exWSConteoAsync();
        wsConteoAsync.execute();


        return  v;
    }

    /*private void InicializarAtributos(View v) {
        mspinnerConteo = (Spinner)v.findViewById(R.id.spinnerConteo);
        mlv_Replenishment = (ListView) v.findViewById(R.id.lv_Replenishment);
    }*/

    private void procesarlv_Replenishment(){



        //InventoryControl inventoryControls = rfidService.WSInventoryControlCountDetail("");

        //final ListView lv = (ListView) v.findViewById(R.id.lv_Replenishment);
        mlv_Replenishment.setAdapter(null);


        //lv.addHeaderView(null);
        //ViewGroup headerview = (ViewGroup) getLayoutInflater().inflate(R.layout.header_reposicion,lv,false);

        /*if(first){
            View headerview = View.inflate(mContext, R.layout.header_inventory_control, null);
            mlv_Replenishment.addHeaderView(headerview);

            first = false;
        }*/




        if(inventoryDetail != null && inventoryDetail.getSecciones() != null &&  inventoryDetail.getSecciones().size() > 0){
            mLayoutInvC.setVisibility(View.VISIBLE);

            mlv_Replenishment.setAdapter(new CustomListAdapterTakInventoryControl(mContext, inventoryDetail.getSecciones()));
            /*mlv_Replenishment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                    ReplenishmentWareResult replenishmentWareResult = (ReplenishmentWareResult) lv.getItemAtPosition(position);
                    Toast.makeText(mContext, "Selected :" + " " + replenishmentWareResult.getItems()+", "+ replenishmentWareResult.getReplenishmentCount(), Toast.LENGTH_SHORT).show();
                }
            });*/

        }
    }

    private void mspinnerConteoSetItems()
    {
        ArrayAdapter<DataSourceDto> adapter1 =new ArrayAdapter<DataSourceDto>(mContext,android.R.layout.simple_spinner_item, genericSpinnerDto.getColeccion());
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mspinnerConteo.setAdapter(adapter1);


        mspinnerConteo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mConteo = mspinnerConteo.getSelectedItem().toString();
                if(!mConteo.equals("- Seleccione -")){
                    //procesarListview();
                    mWSparameterConteoDetalle = getResources().getStringArray(R.array.WSparameter_conteoDetalle);


                    exWSConteoDetalleAsync conteoDetalleAsync = new exWSConteoDetalleAsync();
                    conteoDetalleAsync.execute();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    // Metodos Async
    private  class exWSConteoAsync extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;
        ResponseVal responseVal;
        @Override
        protected Void doInBackground(Void... voids) {
            //despatchGuide = rfidService.GuiaDespachoBodegasService();

            //WSparameter_conteos

            rfidService.SOAP_ACTION_ =  mWSparameterConteos[0];
            rfidService.METHOD_NAME_ =  mWSparameterConteos[1];
            rfidService.NAMESPACE_ = mWSparameterConteos[2];
            rfidService.URL_ = mWSparameterConteos[3];

            genericSpinnerDto = rfidService.WSConteo();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);
            progressDialog.cancel();
            responseVal = validator.getValidateGenericDto(genericSpinnerDto);
            if (responseVal.isValidAccess()) {
                if (responseVal.isFullCollection()) {
                    mspinnerConteoSetItems();
                } else {
                    Toast.makeText(mContext, R.string.INF_NoConteo, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(mContext, responseVal.getErrorMsg(), Toast.LENGTH_LONG).show();
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

    private  class exWSConteoDetalleAsync extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;
        @Override
        protected Void doInBackground(Void... voids) {
            //despatchGuide = rfidService.GuiaDespachoBodegasService();

            rfidService.SOAP_ACTION_ =  mWSparameterConteoDetalle[0];
            rfidService.METHOD_NAME_ =  mWSparameterConteoDetalle[1];
            rfidService.NAMESPACE_ = mWSparameterConteoDetalle[2];
            rfidService.URL_ = mWSparameterConteoDetalle[3];

            inventoryDetail = rfidService.WSInventoryControlCountDetail(mConteo);
            //inventoryDetail = DummyData();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);

            progressDialog.cancel();
            Validator validator = new Validator();
            ResponseVal responseVal = validator.getValidateDataSourceDto(inventoryDetail.getEstado());

            if(responseVal.isValidAccess()){
                try{

                    mpbLeidos.setMax(inventoryDetail.getTotalEsperado());
                    mpbLeidos.setProgress( inventoryDetail.getTotalLeido() );
                    //mpbLeidos.setProgress( 5000 );
                    mtvContado.setText(String.valueOf(inventoryDetail.getTotalLeido()) + "/" + String.valueOf(inventoryDetail.getTotalEsperado()));
                    procesarlv_Replenishment();
                }
                catch (Exception ex){
                    //Toast.makeText(mContext, ex.getMessage(), Toast.LENGTH_LONG).show();
                    loDialogo.gMostrarMensajeError(loVistaDialogo, ex.getMessage());
                }
            }
            else {
                //Toast.makeText(mContext, responseVal.getErrorMsg() , Toast.LENGTH_SHORT).show();
                loDialogo.gMostrarMensajeError(loVistaDialogo, responseVal.getErrorMsg() );
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

    public void CleanControls(){
        mpbLeidos.setProgress(0);
        mlv_Replenishment.setAdapter(null);
        mLayoutInvC.setVisibility(View.GONE);
    }

    private InventoryControl DummyData(){
        InventoryControl inventoryControl = new InventoryControl();
        List<ICSeccion> icSeccionList = new ArrayList<>();


        inventoryControl.setEstado(new DataSourceDto("00", "Exitoso", null));
        inventoryControl.setTotalEsperado(200);
        inventoryControl.setTotalLeido(190);

        for(int i = 1; i<1050; i++){
            ICSeccion icSeccion = new ICSeccion();
            icSeccion.setNombre("Seccion000"+i);
            icSeccion.setLeido(i);
            icSeccion.setPorcentaje(i+2.2/100);
            icSeccionList.add(icSeccion);
        }
        inventoryControl.setSecciones(icSeccionList);

        return inventoryControl;
    }

}
