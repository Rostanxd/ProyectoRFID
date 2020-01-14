package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

import java.util.List;

public class Garment {
    public DataSourceDto estado;
    public String itmCodigo;
    public String estilo;
    public String linea;
    public String producto;
    public String temporada;
    public double precioIva;
    public double precioSinIva;
    public List<String> listImagenes;

    public DataSourceDto getEstado() {
        return estado;
    }

    public void setEstado(DataSourceDto estado) {
        this.estado = estado;
    }

    public String getItmCodigo() {
        return itmCodigo;
    }

    public void setItmCodigo(String itmCodigo) {
        this.itmCodigo = itmCodigo;
    }

    public String getEstilo() {
        return estilo;
    }

    public void setEstilo(String estilo) {
        this.estilo = estilo;
    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getTemporada() {
        return temporada;
    }

    public void setTemporada(String temporada) {
        this.temporada = temporada;
    }

    public double getPrecioIva() {
        return precioIva;
    }

    public void setPrecioIva(double precioIva) {
        this.precioIva = precioIva;
    }

    public double getPrecioSinIva() {
        return precioSinIva;
    }

    public void setPrecioSinIva(double precioSinIva) {
        this.precioSinIva = precioSinIva;
    }

    public List<String> getListImagenes() {
        return listImagenes;
    }

    public void setListImagenes(List<String> listImagenes) {
        this.listImagenes = listImagenes;
    }
}
