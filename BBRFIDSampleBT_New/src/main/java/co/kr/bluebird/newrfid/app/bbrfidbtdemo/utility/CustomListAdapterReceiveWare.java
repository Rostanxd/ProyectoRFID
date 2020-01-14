package co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

import co.kr.bluebird.newrfid.app.bbrfidbtdemo.R;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EGTagsResponseItem;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.item;

/**
 * Created by victor plaza on 27-SEP-2019.
 */
public class CustomListAdapterReceiveWare extends BaseAdapter {
    private List<EGTagsResponseItem> listData;
    private LayoutInflater layoutInflater;
    private boolean isComplete;
    public CustomListAdapterReceiveWare(Context aContext, List<EGTagsResponseItem> listData, boolean isComplete) {
        this.listData = listData;
        this.isComplete = isComplete;
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
            v = layoutInflater.inflate(R.layout.listrow_4column, null);
            holder = new ViewHolder();
            holder.uCodItem = (TextView) v.findViewById(R.id.tvCol1);
            holder.uCantidad = (TextView) v.findViewById(R.id.tvCol2);
            holder.uLeidos = (TextView) v.findViewById(R.id.tvCol3);
            holder.uDif = (TextView) v.findViewById(R.id.tvCol4);


            v.setTag(holder);
        } else{
            holder = (ViewHolder) v.getTag();
        }
        int cantidadtotal = listData.get(position).getCantidadLeidos() + listData.get(position).getCantidadNoLeidos();
        int cantidadLeidos = listData.get(position).getCantidadLeidos();

        holder.uCodItem.setText(listData.get(position).getItemCodigo());
        holder.uCantidad.setText(String.valueOf(listData.get(position).getCantidadLeidos() + listData.get(position).getCantidadNoLeidos()));
       /* holder.uLeidos.setText( null );
        holder.uDif.setText( null);*/

        if(isComplete)
        {
            holder.uLeidos.setText( String.valueOf( listData.get(position).getCantidadLeidos()));
            holder.uDif.setText(String.valueOf(cantidadLeidos- cantidadtotal) );
        }
        else
        {
            holder.uLeidos.setText( null );
            holder.uDif.setText( null);
        }

        return v;
    }



    static class ViewHolder {

        TextView uCodItem;
        TextView uCantidad;
        TextView uLeidos;
        TextView uDif;

    }
}

