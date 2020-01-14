package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

import java.util.List;

public class Replenishments {
    public DataSourceDtoEx estado;
    public List<Replenishment> replenishments;

    public DataSourceDtoEx getEstado() {
        return estado;
    }

    public void setEstado(DataSourceDtoEx estado) {
        this.estado = estado;
    }

    public List<Replenishment> getReplenishments() {
        return replenishments;
    }

    public void setReplenishments(List<Replenishment> replenishments) {
        this.replenishments = replenishments;
    }
}
