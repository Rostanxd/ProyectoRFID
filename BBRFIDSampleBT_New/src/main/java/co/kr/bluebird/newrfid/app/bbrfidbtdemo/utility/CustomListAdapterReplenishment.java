package co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import co.kr.bluebird.newrfid.app.bbrfidbtdemo.R;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.Replenishment;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ReplenishmentWareResult;

public class CustomListAdapterReplenishment extends BaseAdapter {

    private List<Replenishment> listData;
    private LayoutInflater layoutInflater;

    public CustomListAdapterReplenishment(Context aContext, List<Replenishment> listData) {
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
        CustomListAdapterReplenishment.ViewHolder holder ;
        if (v == null) {
            v = layoutInflater.inflate(R.layout.list_row_reposicion3, null);
            holder = new CustomListAdapterReplenishment.ViewHolder();
            holder.uitmCodigo = (TextView) v.findViewById(R.id.tvItemsREP);
            holder.ucantidad_anterior = (TextView) v.findViewById(R.id.tvCantAntREP);
            holder.udiferencia = (TextView) v.findViewById(R.id.tvDiferenciaREP);
            holder.ucantidad_actual = (TextView) v.findViewById(R.id.tvCantActualREP);
            holder.ucantidad_egresos = (TextView) v.findViewById(R.id.tvCantEgresoREP);
            holder.ucantidad_ventas = (TextView) v.findViewById(R.id.tvCantVentaREP);
            holder.ustock_otros = (TextView) v.findViewById(R.id.tvSaldoOtrosREP);
            holder.ustock_local = (TextView) v.findViewById(R.id.tvSaldoBodIntREP);

            v.setTag(holder);
        } else{
            holder = (CustomListAdapterReplenishment.ViewHolder) v.getTag();
        }

        holder.uitmCodigo.setText(listData.get(position).getItmCodigo());
        holder.ucantidad_anterior.setText(listData.get(position).getCantidad_anterior()+"");
        holder.udiferencia.setText(listData.get(position).getDiferencia()+"");
        holder.ucantidad_actual.setText(listData.get(position).getCantidad_actual()+"");
        holder.ucantidad_egresos.setText(listData.get(position).getCantidad_egresos()+"");
        holder.ucantidad_ventas.setText(listData.get(position).getCantidad_ventas()+"");
        holder.ustock_otros.setText(listData.get(position).getStock_otros()+"");
        holder.ustock_local.setText(listData.get(position).getStock_local()+"");

        return v;
    }

    static class ViewHolder {

        TextView uitmCodigo;
        TextView ucantidad_anterior;
        TextView udiferencia;
        TextView ucantidad_actual;
        TextView ucantidad_egresos;
        TextView ucantidad_ventas;
        TextView ustock_otros;
        TextView ustock_local;
        //TextView residueExternDetails;
    }

}
