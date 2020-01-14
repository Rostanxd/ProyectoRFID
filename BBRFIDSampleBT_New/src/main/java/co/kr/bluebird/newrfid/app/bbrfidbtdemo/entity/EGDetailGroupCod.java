package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

import java.util.List;

public class EGDetailGroupCod {
    public String itemCodigo;
    public List<String> epc;

    public String getItemCodigo() {
        return itemCodigo;
    }

    public void setItemCodigo(String itemCodigo) {
        this.itemCodigo = itemCodigo;
    }

    public List<String> getEpc() {
        return epc;
    }

    public void setEpc(List<String> epc) {
        this.epc = epc;
    }

    public EGDetailGroupCod(String itemCodigo, List<String> epc) {
        this.itemCodigo = itemCodigo;
        this.epc = epc;
    }
}
