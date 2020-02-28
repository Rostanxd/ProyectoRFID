package co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import co.kr.bluebird.newrfid.app.bbrfidbtdemo.R;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.GarmentSize;

public class ExpandableListAdapterInvPerStoreSaldos extends BaseExpandableListAdapter {

    private Context context;
    private List<String> ParentItem;
    private HashMap<String, List<GarmentSize>> ChildItem;


    public ExpandableListAdapterInvPerStoreSaldos(Context context, List<String> ParentItem,
                                 HashMap<String, List<GarmentSize>> ChildItem) {
        this.context = context;
        this.ParentItem = ParentItem;
        this.ChildItem = ChildItem;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.ChildItem.get(this.ParentItem.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        //final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        GarmentSize objGarmentSize =(GarmentSize) getChild(listPosition, expandedListPosition);

        LayoutInflater layoutInflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.child_itemsaldos_ips, null);

        /*if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.child_itemsaldos_ips, null);
        }*/
        TextView text1 = (TextView) convertView.findViewById(R.id.item1);
        TextView text2 = (TextView) convertView.findViewById(R.id.item2);
        TextView text3 = (TextView) convertView.findViewById(R.id.item3);
        TextView text4 = (TextView) convertView.findViewById(R.id.item4);
        //text1.setText(""+expandedListPosition);
        text1.setText(""+objGarmentSize.getNombre());
        text2.setText(""+objGarmentSize.getStockLocal());
        text3.setText(""+objGarmentSize.getStockOtros());
        text4.setText(""+objGarmentSize.getItmCodigo());

        if(objGarmentSize.getNombre().equalsIgnoreCase("Total")){
            if(convertView != null && convertView.getBackground() == null){
                convertView.setBackgroundColor(Color.parseColor("#c3e2e8"));
            }
        }
        else {
            if (convertView != null && convertView.getBackground() != null){
                convertView.setBackgroundColor(Color.WHITE);
            }
        }

        if(objGarmentSize.isBusca()){
            /*convertView.setBackgroundColor(Color.parseColor("#61bcc7"));*/
            convertView.setBackgroundColor(Color.parseColor("#F38428"));
        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.ChildItem.get(this.ParentItem.get(listPosition))
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.ParentItem.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.ParentItem.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.parent_itemsaldos_ips, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

}