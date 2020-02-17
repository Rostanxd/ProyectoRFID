package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

import java.io.Serializable;
import java.util.List;

public class LocatedInvData implements Serializable {
    private String ordenCompra;
    private String numeroGuia;
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
