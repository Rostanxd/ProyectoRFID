package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

import java.util.List;

public class GenericSpinnerDto {
    public DataSourceDto estado;
    public List<DataSourceDto> coleccion;

    public DataSourceDto getEstado() {
        return estado;
    }

    public void setEstado(DataSourceDto estado) {
        this.estado = estado;
    }

    public List<DataSourceDto> getColeccion() {
        return coleccion;
    }

    public void setColeccion(List<DataSourceDto> coleccion) {
        this.coleccion = coleccion;
    }
}
