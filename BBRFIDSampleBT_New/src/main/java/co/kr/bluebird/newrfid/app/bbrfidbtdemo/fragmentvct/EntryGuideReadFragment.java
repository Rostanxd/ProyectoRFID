package co.kr.bluebird.newrfid.app.bbrfidbtdemo.fragmentvct;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import co.kr.bluebird.newrfid.app.bbrfidbtdemo.R;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.control.EntryGuide;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.models.EntryGuideModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class EntryGuideReadFragment extends Fragment {


    private Context mContext;
    private TableLayout tableLayout;
    private Button mbtnIniciarGR;

    private TextView txtCount;
    private ProgressBar mprogress1;
    Handler handler = new Handler();

    private int i;
    private co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EntryGuide ResposeEG;

    public EntryGuideReadFragment() {
        // Required empty public constructor
    }


    public static EntryGuideReadFragment newInstance() {

        return new EntryGuideReadFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.entryguideread_frag, container, false);

        View v = inflater.inflate(R.layout.entryguideread_frag, container, false);
        mContext = inflater.getContext();

        tableLayout = (TableLayout)v.findViewById(R.id.tablelayoutItemsDif);
        mprogress1 = (ProgressBar)v.findViewById(R.id.pbLeidos);
        mbtnIniciarGR = (Button) v.findViewById(R.id.btnIniciarGR);
        txtCount = (TextView) v.findViewById(R.id.tvCantItemLeidos);

        if(getArguments() != null)
        {
            ResposeEG = (co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EntryGuide)getArguments().getSerializable("objectResponse");
        }
        mbtnIniciarGR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread hilo = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (i<= 100){
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    txtCount.setText(i+" %");
                                    mprogress1.setProgress(i);
                                }
                            });
                            try {
                                Thread.sleep(100);
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }


                            i++;
                        }
                    }
                });
                hilo.start();
            }
        });

        createColumns();
        fillData();




        return  v;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void createColumns()
    {
        TableRow tableRow = new TableRow(mContext);
        tableRow.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.FILL_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                )
        );

        // Id Column
        TextView textViewEntrada = new TextView(mContext);
        textViewEntrada.setText("Entrada");
        textViewEntrada.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        textViewEntrada.setPadding(5,5,5,0);
        tableRow.addView(textViewEntrada);

        // name column
        TextView textViewCant = new TextView(mContext);
        textViewCant.setText("Cantidad");
        textViewCant.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        textViewCant.setPadding(5,5,5,0);
        tableRow.addView(textViewCant);

        // name Price
        TextView textViewStatus = new TextView(mContext);
        textViewStatus.setText("Estado");
        textViewStatus.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        textViewStatus.setPadding(5,5,5,0);
        tableRow.addView(textViewStatus);


        tableLayout.addView(tableRow, new TableLayout.LayoutParams(
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




        // Id Column
        textViewEntrada = new TextView(mContext);
        textViewEntrada.setText("------------");
        textViewEntrada.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        textViewEntrada.setPadding(5,5,5,0);
        tableRow.addView(textViewEntrada);

        // name column
        textViewCant = new TextView(mContext);
        textViewCant.setText("------------");
        textViewCant.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        textViewCant.setPadding(5,5,5,0);
        tableRow.addView(textViewCant);

        // name Price
        textViewStatus = new TextView(mContext);
        textViewStatus.setText("------------");
        textViewStatus.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        textViewStatus.setPadding(5,5,5,0);
        tableRow.addView(textViewStatus);


        tableLayout.addView(tableRow, new TableLayout.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));

    }

    private void fillData() {
        EntryGuideModel entryGuideModel = new EntryGuideModel();
        for (EntryGuide entryGuide : entryGuideModel.findAll()){
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
                                                 TableRow currentRow = (TableRow) view;
                                                 /*TextView textViewId = (TextView)currentRow.getChildAt(0);
                                                 String id = textViewId.getText().toString();
                                                 Toast.makeText(mContext, id, Toast.LENGTH_SHORT).show();
                                                 Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                                                 intent.putExtra("id", id);
                                                 startActivity(intent);*/
                                                 String nombre = ((TextView)currentRow.getChildAt(2)).getText().toString();
                                                 AlertDialog.Builder alerta = new AlertDialog.Builder(mContext);
                                                 alerta.setMessage(nombre)
                                                         .setCancelable(false)
                                                         .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                                             @Override
                                                             public void onClick(DialogInterface dialogInterface, int i) {
                                                                 Toast.makeText(mContext,"Selecciono el item: "+ nombre,Toast.LENGTH_LONG).show();
                                                             }
                                                         })
                                                         .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                             @Override
                                                             public void onClick(DialogInterface dialogInterface, int i) {
                                                                 dialogInterface.cancel();
                                                             }
                                                         });
                                                 AlertDialog title = alerta.create();
                                                 title.setTitle("salida");
                                                 title.show();

                                             }
                                         }

            );

            TextView textViewEntrada = new TextView(mContext);
            textViewEntrada.setText(entryGuide.getNum_entrada());
            textViewEntrada.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            textViewEntrada.setPadding(5,5,5,0);
            tableRow.addView(textViewEntrada);

            TextView textViewCant = new TextView(mContext);
            textViewCant.setText(entryGuide.getCantidad());
            textViewCant.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            textViewCant.setPadding(5,5,5,0);
            tableRow.addView(textViewCant);

            TextView textViewStatus = new TextView(mContext);
            textViewStatus.setText(String.valueOf(entryGuide.getStatus()) );
            textViewStatus.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            textViewStatus.setPadding(5,5,5,0);
            tableRow.addView(textViewStatus);



            tableLayout.addView(tableRow, new TableLayout.LayoutParams(
                    TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));
        }


    }

}
