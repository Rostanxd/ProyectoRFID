/*
package co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility;

public class CustomListAdapterEntryGuideCheck {
}
*/

package co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility;


import android.content.Context;
import android.graphics.Color;
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
    private LayoutInflater layoutInflater;
    public CustomListAdapterEntryGuideCheck(Context aContext, List<Guide> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
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



        /*holder.uCantidad.setText(String.valueOf(listData.get(position).getCantidad()));*/

        /*if(listData.get(position).getSaldo() > 0 && cant != listData.get(position).getSaldo() ){
            cantidad = cant+" [" + listData.get(position).getSaldo()+"]";
        }
        else
        {
            cantidad = cant+"";
        }*/

        if(listData.get(position).getSaldo() > 0 && cant != listData.get(position).getSaldo()){
            //v.setBackgroundColor(Color.parseColor("#DCFCEA"));
            saldo = "saldo: "+listData.get(position).getSaldo()+"";
        }
        else {
            saldo = "";
        }

        holder.uNumGuia.setText(listData.get(position).getNumero());
        holder.uCantidad.setText("cant: "+listData.get(position).getCantidad()+"");
        holder.uSaldo.setText(saldo);
        //holder.uEstado.setText( String.valueOf(listData.get(position).getDescripcion()) );
        if(String.valueOf(listData.get(position).getDescripcion()).toLowerCase().contains("pendiente"))
        {
            holder.uImagen.setImageResource(R.drawable.ic_procesado);
        }else{
            holder.uImagen.setImageResource(R.drawable.ic_activo);
        }

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