package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

import java.util.List;

public class ReceiveWareDetail {

    public DataSourceDto estado;
    public String docOrigen;
    public String docDestino;
    public String motDescription;
    public int cantidadTotal;
    public List<EGTagsResponseItem> detalle;

    public DataSourceDto getEstado() {
        return estado;
    }

    public void setEstado(DataSourceDto estado) {
        this.estado = estado;
    }

    public String getDocOrigen() {
        return docOrigen;
    }

    public void setDocOrigen(String docOrigen) {
        this.docOrigen = docOrigen;
    }

    public String getDocDestino() {
        return docDestino;
    }

    public void setDocDestino(String docDestino) {
        this.docDestino = docDestino;
    }

    public String getMotDescription() {
        return motDescription;
    }

    public void setMotDescription(String motDescription) {
        this.motDescription = motDescription;
    }

    public int getCantidadTotal() {
        return cantidadTotal;
    }

    public void setCantidadTotal(int cantidadTotal) {
        this.cantidadTotal = cantidadTotal;
    }

    public List<EGTagsResponseItem> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<EGTagsResponseItem> detalle) {
        this.detalle = detalle;
    }
}
