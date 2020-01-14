package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

public class DGDetalle {
    public String codigoItem;
    public String cantidad;

    public String getCodigoItem() {
        return codigoItem;
    }

    public void setCodigoItem(String codigoItem) {
        this.codigoItem = codigoItem;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public DGDetalle(String codigoItem, String cantidad) {
        this.codigoItem = codigoItem;
        this.cantidad = cantidad;
    }
}