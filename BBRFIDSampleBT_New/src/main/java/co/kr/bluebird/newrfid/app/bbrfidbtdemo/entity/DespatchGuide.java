package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

import java.util.List;

public class DespatchGuide {
    public DataSourceDto estado;
    public List<DataSourceDto> bodegas;

    public DataSourceDto getEstado() {
        return estado;
    }

    public void setEstado(DataSourceDto estado) {
        this.estado = estado;
    }

    public List<DataSourceDto> getBodegas() {
        return bodegas;
    }

    public void setBodegas(List<DataSourceDto> bodegas) {
        this.bodegas = bodegas;
    }

    public DespatchGuide(DataSourceDto estado, List<DataSourceDto> bodegas) {
        this.estado = estado;
        this.bodegas = bodegas;
    }
}
