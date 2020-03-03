/*
package co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility;

import android.widget.BaseAdapter;

public class CustomListAdapterEntryGuide extends BaseAdapter {


}
*/

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
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.item;

/**
 * Created by victor plaza on 10-SEP-2019.
 */
public class CustomListAdapterEntryGuide extends BaseAdapter {
    private List<item> listData;
    private LayoutInflater layoutInflater;
    public CustomListAdapterEntryGuide(Context aContext, List<item> listData) {
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
            v = layoutInflater.inflate(R.layout.listrow_inventory_control_eg, null);
            holder = new ViewHolder();
            holder.uCodItem = (TextView) v.findViewById(R.id.tvCol1);
            holder.uCantidad = (TextView) v.findViewById(R.id.tvCol2);
            holder.uLeidos = (TextView) v.findViewById(R.id.tvCol3);


            v.setTag(holder);
        } else{
            holder = (ViewHolder) v.getTag();
        }

        holder.uCodItem.setText(listData.get(position).getItemCodigo());
        holder.uCantidad.setText(String.valueOf(listData.get(position).getCantidad()));
        holder.uLeidos.setText( String.valueOf(listData.get(position).getCantidadLeidos()) );

        return v;
    }



    static class ViewHolder {

        TextView uCodItem;
        TextView uCantidad;
        TextView uLeidos;

    }
}