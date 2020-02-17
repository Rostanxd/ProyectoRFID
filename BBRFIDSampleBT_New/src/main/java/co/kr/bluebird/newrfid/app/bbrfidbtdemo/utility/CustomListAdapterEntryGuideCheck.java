/*
package co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility;

public class CustomListAdapterEntryGuideCheck {
}
*/

package co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import co.kr.bluebird.newrfid.app.bbrfidbtdemo.R;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.Guide;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.item;

/**
 * Created by victor plaza on 10-SEP-2019.
 */
public class CustomListAdapterEntryGuideCheck extends BaseAdapter {
    private List<Guide> listData;
    private String saldo = "";
    int cant ;
    private Context mcontext;
    private LayoutInflater layoutInflater;

    Drawable myIcon = null;
    ColorFilter filter = null;
    String gColorActivo, gColorPendiente, gColorProcesado ;



    @SuppressLint("ResourceType")
    public CustomListAdapterEntryGuideCheck(Context aContext, List<Guide> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
        mcontext = aContext;
        gColorActivo = mcontext.getString(R.color.gColorVerdeDark);
        gColorPendiente = mcontext.getString(R.color.gColorGrisAzul_n500);
        gColorProcesado = mcontext.getString(R.color.gColorCyan_n700);
    }
    @Override
    public int getCount() {
        return listData.size();
    }
    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup vg) {


        ViewHolder holder ;
        if (v == null) {
            v = layoutInflater.inflate(R.layout.listrow_entry_guide_check, null);
            holder = new ViewHolder();
            holder.uNumGuia = (TextView) v.findViewById(R.id.tv_nGuia);
            holder.uCantidad = (TextView) v.findViewById(R.id.tv_cantidad);
            holder.uSaldo = (TextView)v.findViewById(R.id.tv_saldo);
            //holder.uEstado = (TextView) v.findViewById(R.id.tvCol3);
            holder.uImagen = (ImageView) v.findViewById(R.id.imgListView);

            v.setTag(holder);
        } else{
            holder = (ViewHolder) v.getTag();
        }

        cant = listData.get(position).getCantidad();

        // if(listData.get(position).getSaldo() > 0 && cant != listData.get(position).getSaldo()){

        if(listData.get(position).getSaldo() > 0 ){
            //v.setBackgroundColor(Color.parseColor("#DCFCEA"));
            saldo = "por validar: "+listData.get(position).getSaldo()+"";
        }
        else {
            saldo = "";
        }

        String estado = String.valueOf(listData.get(position).getDescripcion()).toLowerCase();

        if(estado.trim().equals("activo") && listData.get(position).getSaldo() == 0 ){
            saldo = "VALIDADO";
            holder.uSaldo.setTextColor(Color.parseColor(gColorActivo));
        }
        holder.uNumGuia.setText(listData.get(position).getNumero());
        holder.uCantidad.setText("cant: "+listData.get(position).getCantidad()+"");
        holder.uSaldo.setText(saldo);
        //holder.uEstado.setText( String.valueOf(listData.get(position).getDescripcion()) );



        myIcon = mcontext.getDrawable( R.drawable.ic_list_status );

        /*holder.uImagen.setImageResource(R.drawable.ic_list_status);*/


        if(estado.trim().equals("activo")){
            filter = new LightingColorFilter( Color.BLACK, Color.parseColor(gColorActivo));
        }
        if(estado.trim().equals("pendiente")){
            /*filter = new LightingColorFilter( Color.BLACK, Color.parseColor("#607d8b"));*/
            filter = new LightingColorFilter( Color.BLACK, Color.parseColor(gColorPendiente));

        }
        if(estado.trim().equals("procesado")){
            filter = new LightingColorFilter( Color.BLACK,Color.parseColor(gColorProcesado));
        }
        myIcon.setColorFilter(filter);
        holder.uImagen.setImageDrawable(myIcon);

        return v;
    }



    static class ViewHolder {

        TextView uNumGuia;
        TextView uCantidad;
        TextView uSaldo;
        //TextView uEstado;
        ImageView uImagen;
    }
}