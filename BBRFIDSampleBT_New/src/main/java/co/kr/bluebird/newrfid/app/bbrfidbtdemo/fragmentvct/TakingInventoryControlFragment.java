package co.kr.bluebird.newrfid.app.bbrfidbtdemo.fragmentvct;


import android.app.Dialog;
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

import java.util.List;

import co.kr.bluebird.newrfid.app.bbrfidbtdemo.R;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.DataSourceDto;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.GenericSpinnerDto;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.InventoryControl;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.service.RfidService;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.CustomListAdapterTakInventoryControl;

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
        rfidService =new RfidService(mContext);
        mpbLeidos = (ProgressBar)v.findViewById(R.id.pbLeidos);
        mpbLeidos.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#07BDAB")));
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

            if(genericSpinnerDto != null && genericSpinnerDto.estado != null && genericSpinnerDto.estado.getAuxiliar() != null){
                Toast.makeText(mContext, "No existen conteos vigentes...", Toast.LENGTH_SHORT).show();
            }
            else {
                if(genericSpinnerDto != null && genericSpinnerDto.estado != null && genericSpinnerDto.estado.codigo.equals("9999")){
                    Toast.makeText(mContext,genericSpinnerDto.getEstado().getDescripcion(), Toast.LENGTH_SHORT ).show();
                }
                else {
                    if (genericSpinnerDto != null && genericSpinnerDto.estado != null && genericSpinnerDto.estado.codigo.equals("00")) {
                        try {
                            mspinnerConteoSetItems();
                        } catch (Exception ex) {
                            Toast.makeText(mContext, ex.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(mContext, "Ocurrio un error al cargar los conteos: " + genericSpinnerDto.estado.getDescripcion(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }
    }

    private  class exWSConteoDetalleAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            //despatchGuide = rfidService.GuiaDespachoBodegasService();

            rfidService.SOAP_ACTION_ =  mWSparameterConteoDetalle[0];
            rfidService.METHOD_NAME_ =  mWSparameterConteoDetalle[1];
            rfidService.NAMESPACE_ = mWSparameterConteoDetalle[2];
            rfidService.URL_ = mWSparameterConteoDetalle[3];

            inventoryDetail = rfidService.WSInventoryControlCountDetail(mConteo);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);

            if(inventoryDetail != null && inventoryDetail.estado != null && inventoryDetail.estado.codigo.equals("00")){
                try{

                    mpbLeidos.setMax(inventoryDetail.getTotalEsperado());
                    mpbLeidos.setProgress( inventoryDetail.getTotalLeido() );
                    //mpbLeidos.setProgress( 5000 );
                    mtvContado.setText(String.valueOf(inventoryDetail.getTotalLeido()) + "/" + String.valueOf(inventoryDetail.getTotalEsperado()));
                    procesarlv_Replenishment();
                }
                catch (Exception ex){
                    Toast.makeText(mContext, ex.getMessage(), Toast.LENGTH_LONG).show();
                }


            }
            else {
                Toast.makeText(mContext, "Ocurrio un error en el Servicio: "+genericSpinnerDto.estado.getDescripcion() , Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void CleanControls(){
        mpbLeidos.setProgress(0);
        mlv_Replenishment.setAdapter(null);
        mLayoutInvC.setVisibility(View.GONE);
    }


    // prueba git inv_ctr_ux_ui

}
