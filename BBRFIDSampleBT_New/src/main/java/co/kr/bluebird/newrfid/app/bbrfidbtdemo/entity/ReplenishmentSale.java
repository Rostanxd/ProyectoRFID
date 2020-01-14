package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

public class ReplenishmentSale {
    public String product;
    public String localname;
    public int cantidad;

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getLocalname() {
        return localname;
    }

    public void setLocalname(String localname) {
        this.localname = localname;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
