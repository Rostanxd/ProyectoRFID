/*
package co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility;

public class CustomListAdapterEntryGuideCheck {
}
*/

package co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
    private String cantidad = "";
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
    public View getView(int position, View v, ViewGroup vg) {
        ViewHolder holder ;
        if (v == null) {
            v = layoutInflater.inflate(R.layout.listrow_3column, null);
            holder = new ViewHolder();
            holder.uNumGuia = (TextView) v.findViewById(R.id.tvCol1);
            holder.uCantidad = (TextView) v.findViewById(R.id.tvCol2);
            holder.uEstado = (TextView) v.findViewById(R.id.tvCol3);


            v.setTag(holder);
        } else{
            holder = (ViewHolder) v.getTag();
        }

        cant = listData.get(position).getCantidad();


        /*holder.uCantidad.setText(String.valueOf(listData.get(position).getCantidad()));*/
        if(listData.get(position).getSaldo() > 0 && cant != listData.get(position).getSaldo() ){
            cantidad = cant+" [" + listData.get(position).getSaldo()+"]";
        }
        else
        {
            cantidad = cant+"";
        }

        holder.uNumGuia.setText(listData.get(position).getNumero());
        holder.uCantidad.setText(cantidad);
        holder.uEstado.setText( String.valueOf(listData.get(position).getDescripcion()) );

        return v;
    }



    static class ViewHolder {

        TextView uNumGuia;
        TextView uCantidad;
        TextView uEstado;

    }
}