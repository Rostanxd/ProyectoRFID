package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

public class item {
    public String itemCodigo;
    public int cantidad;
    public int cantidadLeidos;
    public TagNoRead tagsNoLeidos;

    public String getItemCodigo() {
        return itemCodigo;
    }

    public void setItemCodigo(String itemCodigo) {
        this.itemCodigo = itemCodigo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getCantidadLeidos() {
        return cantidadLeidos;
    }

    public void setCantidadLeidos(int cantidadLeidos) {
        this.cantidadLeidos = cantidadLeidos;
    }

    public TagNoRead getTagsNoLeidos() {
        return tagsNoLeidos;
    }

    public void setTagsNoLeidos(TagNoRead tagsNoLeidos) {
        this.tagsNoLeidos = tagsNoLeidos;
    }

    public item(String itemCodigo, int cantidad, int cantidadLeidos, TagNoRead tagsNoLeidos) {
        this.itemCodigo = itemCodigo;
        this.cantidad = cantidad;
        this.cantidadLeidos = cantidadLeidos;
        this.tagsNoLeidos = tagsNoLeidos;
    }
}
