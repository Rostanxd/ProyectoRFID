package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

public class GarmentSize {
    public String itmCodigo;
    public String nombre;
    public int stockLocal;
    public int stockOtros;
    public boolean busca;

    public String getItmCodigo() {
        return itmCodigo;
    }

    public void setItmCodigo(String itmCodigo) {
        this.itmCodigo = itmCodigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getStockLocal() {
        return stockLocal;
    }

    public void setStockLocal(int stockLocal) {
        this.stockLocal = stockLocal;
    }

    public int getStockOtros() {
        return stockOtros;
    }

    public void setStockOtros(int stockOtros) {
        this.stockOtros = stockOtros;
    }

    public boolean isBusca() {
        return busca;
    }

    public void setBusca(boolean busca) {
        this.busca = busca;
    }
}
