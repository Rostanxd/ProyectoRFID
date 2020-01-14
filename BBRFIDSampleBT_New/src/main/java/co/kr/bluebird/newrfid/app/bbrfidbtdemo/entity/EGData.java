package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

import java.util.List;

public class EGData {
    public int cantidadTotal;
    public List<Guide> guias;

    public int getCantidadTotal() {
        return cantidadTotal;
    }

    public void setCantidadTotal(int cantidadTotal) {
        this.cantidadTotal = cantidadTotal;
    }

    public List<Guide> getGuias() {
        return guias;
    }

    public void setGuias(List<Guide> guias) {
        this.guias = guias;
    }

    public EGData(int cantidadTotal, List<Guide> guias) {
        this.cantidadTotal = cantidadTotal;
        this.guias = guias;
    }
}
