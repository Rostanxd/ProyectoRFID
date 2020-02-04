package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

import java.util.List;

public class StoreExistenceObj {
    private DataSourceDto estado;
    private List<StoreExistence> existencias;

    public DataSourceDto getEstado() {
        return estado;
    }

    public void setEstado(DataSourceDto estado) {
        this.estado = estado;
    }

    public List<StoreExistence> getExistencias() {
        return existencias;
    }

    public void setExistencias(List<StoreExistence> existencias) {
        this.existencias = existencias;
    }
}
