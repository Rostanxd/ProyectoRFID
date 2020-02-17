package co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.R;


/**
 * Created by victor plaza on 14-FEB-2020.
 */
public class CustomListAdapterInvLocated extends BaseAdapter {
    private List<String> listData;
    private LayoutInflater layoutInflater;
    public CustomListAdapterInvLocated(Context aContext, List<String> epcs ) {
        this.listData = epcs;
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
            v = layoutInflater.inflate(R.layout.listrow_1column, null);
            holder = new ViewHolder();
            holder.uEpc = (TextView) v.findViewById(R.id.tvCol1);
            v.setTag(holder);
        } else{
            holder = (ViewHolder) v.getTag();
        }

        holder.uEpc.setText(listData.get(position));
        return v;
    }

    static class ViewHolder {
        TextView uEpc;
    }
}