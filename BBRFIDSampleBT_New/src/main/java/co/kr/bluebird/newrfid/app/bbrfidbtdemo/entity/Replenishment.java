package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

public class Replenishment {
    public String itmCodigo;
    public int cantidad_anterior;
    public int diferencia;
    public int cantidad_actual;
    public int cantidad_egresos;
    public int cantidad_ventas;
    public int stock_otros;
    public int stock_local;

    public String getItmCodigo() {
        return itmCodigo;
    }

    public void setItmCodigo(String itmCodigo) {
        this.itmCodigo = itmCodigo;
    }

    public int getCantidad_anterior() {
        return cantidad_anterior;
    }

    public void setCantidad_anterior(int cantidad_anterior) {
        this.cantidad_anterior = cantidad_anterior;
    }

    public int getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(int diferencia) {
        this.diferencia = diferencia;
    }

    public int getCantidad_actual() {
        return cantidad_actual;
    }

    public void setCantidad_actual(int cantidad_actual) {
        this.cantidad_actual = cantidad_actual;
    }

    public int getCantidad_egresos() {
        return cantidad_egresos;
    }

    public void setCantidad_egresos(int cantidad_egresos) {
        this.cantidad_egresos = cantidad_egresos;
    }

    public int getCantidad_ventas() {
        return cantidad_ventas;
    }

    public void setCantidad_ventas(int cantidad_ventas) {
        this.cantidad_ventas = cantidad_ventas;
    }

    public int getStock_otros() {
        return stock_otros;
    }

    public void setStock_otros(int stock_otros) {
        this.stock_otros = stock_otros;
    }

    public int getStock_local() {
        return stock_local;
    }

    public void setStock_local(int stock_local) {
        this.stock_local = stock_local;
    }
}
