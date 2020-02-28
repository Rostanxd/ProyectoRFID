/*
package co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility;

public class CustomListAdapterTakInventoryControl {
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
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ICSeccion;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.InventoryControl;

/**
 * Created by victor plaza on 10-SEP-2019.
 */
public class CustomListAdapterTakInventoryControl extends BaseAdapter {
    private List<ICSeccion> listData;
    private LayoutInflater layoutInflater;
    public CustomListAdapterTakInventoryControl(Context aContext, List<ICSeccion>  listData) {
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
            v = layoutInflater.inflate(R.layout.listrow_inventory_control, null);
            holder = new ViewHolder();
            holder.uSeccion = (TextView) v.findViewById(R.id.tvCol1);
            holder.uLeidos = (TextView) v.findViewById(R.id.tvCol2);
            holder.uStock = (TextView) v.findViewById(R.id.tvCol3);
            holder.uPorcentaje = (TextView) v.findViewById(R.id.tvCol4);


            v.setTag(holder);
        } else{
            holder = (ViewHolder) v.getTag();
        }

        holder.uSeccion.setText(listData.get(position).getNombre());
        holder.uLeidos.setText(String.valueOf(listData.get(position).getLeido()));
        holder.uStock.setText(String.valueOf(listData.get(position).getEsperado()));
        holder.uPorcentaje.setText( String.valueOf(listData.get(position).getPorcentaje()) );

        return v;
    }


     /*   holder..setText(listData.get(position).getName());
        holder.uDesignation.setText(listData.get(position).getDesignation());
        holder.uLocation.setText(listData.get(position).getLocation());
        return v;*/

    static class ViewHolder {

        TextView uSeccion;
        TextView uLeidos;
        TextView uStock;
        TextView uPorcentaje;

    }
}



