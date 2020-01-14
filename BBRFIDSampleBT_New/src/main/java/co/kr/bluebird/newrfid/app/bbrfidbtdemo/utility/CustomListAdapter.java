/*
package co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility;

public class CustomListAdapter {
}
*/


package co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.kr.bluebird.newrfid.app.bbrfidbtdemo.R;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ReplenishmentWareResult;

/**
 * Created by victor plaza on 09-SEP-2019.
 */
public class CustomListAdapter extends BaseAdapter {
    private List<ReplenishmentWareResult> listData;
    private LayoutInflater layoutInflater;
    public CustomListAdapter(Context aContext, List<ReplenishmentWareResult> listData) {
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
            v = layoutInflater.inflate(R.layout.list_row_reposicion, null);
            holder = new ViewHolder();
            holder.uItems = (TextView) v.findViewById(R.id.tvItemsREP);
            holder.uReplenishmentCount = (TextView) v.findViewById(R.id.tvCantReREP);
            holder.uSales = (TextView) v.findViewById(R.id.tvVentREP);
            holder.uExpenses = (TextView) v.findViewById(R.id.tvEgresoREP);
            holder.uResidueWarehouse = (TextView) v.findViewById(R.id.tvSaldoBodIntREP);
            holder.uResidueExtern = (TextView) v.findViewById(R.id.tvSaldoOtrosREP);

            v.setTag(holder);
        } else{
            holder = (ViewHolder) v.getTag();
        }

        holder.uItems.setText(listData.get(position).getItems());
        holder.uReplenishmentCount.setText(listData.get(position).getReplenishmentCount());
        holder.uSales.setText(listData.get(position).getSales());
        holder.uExpenses.setText(listData.get(position).getExpenses());
        holder.uResidueWarehouse.setText(listData.get(position).getResidueWarehouse());
        holder.uResidueExtern.setText(listData.get(position).getResidueExtern());
        return v;
    }


     /*   holder..setText(listData.get(position).getName());
        holder.uDesignation.setText(listData.get(position).getDesignation());
        holder.uLocation.setText(listData.get(position).getLocation());
        return v;*/

    static class ViewHolder {

        TextView uItems;
        TextView uReplenishmentCount;
        TextView uSales;
        TextView uExpenses;
        TextView uResidueWarehouse;
        TextView uResidueExtern;
        //TextView residueExternDetails;
    }
}


