package co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

import co.kr.bluebird.newrfid.app.bbrfidbtdemo.R;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.StoreExistence;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.item;

/**
 * Created by victor plaza on 12-Oct-2019.
 */
public class CustomListAdapterInvPerStoreOtherSale extends BaseAdapter {
    private List<StoreExistence> listData;
    private LayoutInflater layoutInflater;
    public CustomListAdapterInvPerStoreOtherSale(Context aContext, List<StoreExistence> listData) {
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
            v = layoutInflater.inflate(R.layout.listrow_2column, null);
            holder = new ViewHolder();
            holder.uNamePlace = (TextView) v.findViewById(R.id.tvCol1);
            holder.uStock = (TextView) v.findViewById(R.id.tvCol2);
            v.setTag(holder);
        } else{
            holder = (ViewHolder) v.getTag();
        }

        holder.uNamePlace.setText(listData.get(position).getNameplace());
        holder.uStock.setText(String.valueOf(listData.get(position).getStock()));
        return v;
    }



    static class ViewHolder {

        TextView uNamePlace;
        TextView uStock;

    }
}
