package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

import java.util.List;

public class EGProcesado {
    public List<item> items;

    public int totalcant = 0;

    public int getTotalcant() {
        return totalcant;
    }
    public void setTotalcant(int totalcant) {
        this.totalcant = totalcant;
    }

    public EGProcesado(List<item> items, int totalcant) {
        this.items = items;
        this.totalcant = totalcant;
    }

    public List<item> getItems() {
        return items;
    }

    public void setItems(List<item> items) {
        this.items = items;
    }

    public EGProcesado(List<item> items) {
        this.items = items;
    }
}
