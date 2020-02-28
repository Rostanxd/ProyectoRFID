package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

import java.io.Serializable;
import java.util.List;

public class LocatedInvData implements Serializable {
    private String ordenCompra;
    private String numeroGuia;
    private String docOrigen;
    private String docDestino;
    private String motivo;
    private String itemSku;
    private List<String> epcs;
    private SkuData skuData;

    public String getOrdenCompra() {
        return ordenCompra;
    }

    public void setOrdenCompra(String ordenCompra) {
        this.ordenCompra = ordenCompra;
    }

    public String getNumeroGuia() {
        return numeroGuia;
    }

    public void setNumeroGuia(String numeroGuia) {
        this.numeroGuia = numeroGuia;
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

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getItemSku() {
        return itemSku;
    }

    public void setItemSku(String itemSku) {
        this.itemSku = itemSku;
    }

    public List<String> getEpcs() {
        return epcs;
    }

    public void setEpcs(List<String> epcs) {
        this.epcs = epcs;
    }

    public SkuData getSkuData() {
        return skuData;
    }

    public void setSkuData(SkuData skuData) {
        this.skuData = skuData;
    }
}
