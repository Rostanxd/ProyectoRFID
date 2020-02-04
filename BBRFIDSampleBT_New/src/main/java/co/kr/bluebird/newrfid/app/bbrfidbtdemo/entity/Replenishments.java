package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

import java.util.List;

public class Replenishments {
    public DataSourceDto estado;
    public List<Replenishment> replenishments;

    public DataSourceDto getEstado() {
        return estado;
    }

    public void setEstado(DataSourceDto estado) {
        this.estado = estado;
    }

    public List<Replenishment> getReplenishments() {
        return replenishments;
    }

    public void setReplenishments(List<Replenishment> replenishments) {
        this.replenishments = replenishments;
    }
}
