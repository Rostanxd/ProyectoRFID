/*
package co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility;

public class CustomListAdapterReplenishmentSale {

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
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ReplenishmentSale;

public class CustomListAdapterReplenishmentSale extends BaseAdapter {

    private List<ReplenishmentSale> listData;
    private LayoutInflater layoutInflater;

    public CustomListAdapterReplenishmentSale(Context aContext, List<ReplenishmentSale> listData) {
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
        CustomListAdapterReplenishmentSale.ViewHolder holder ;
        if (v == null) {
            v = layoutInflater.inflate(R.layout.list_row_reposicion_sale, null);
            holder = new CustomListAdapterReplenishmentSale.ViewHolder();
            /*holder.uproducto = (TextView) v.findViewById(R.id.tvProducto);*/
            holder.ulocal = (TextView) v.findViewById(R.id.tvLocal);
            holder.ucantidad = (TextView) v.findViewById(R.id.tvCantidad);

            v.setTag(holder);
        } else{
            holder = (CustomListAdapterReplenishmentSale.ViewHolder) v.getTag();
        }

        /*holder.uproducto.setText(listData.get(position).getProduct());*/
        holder.ulocal.setText(listData.get(position).getLocalname());
        holder.ucantidad.setText(listData.get(position).getCantidad()+"");


        return v;
    }

    static class ViewHolder {

        //TextView uproducto;
        TextView ulocal;
        TextView ucantidad;

    }

}
