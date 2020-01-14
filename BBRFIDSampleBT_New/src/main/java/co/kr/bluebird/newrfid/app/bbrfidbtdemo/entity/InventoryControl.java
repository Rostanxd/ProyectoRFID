package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

import java.util.List;

public class InventoryControl {
    public DataSourceDto estado;
    public int totalLeido;
    public int totalEsperado;
    public List<ICSeccion> secciones;

    public DataSourceDto getEstado() {
        return estado;
    }

    public void setEstado(DataSourceDto estado) {
        this.estado = estado;
    }

    public int getTotalLeido() {
        return totalLeido;
    }

    public void setTotalLeido(int totalLeido) {
        this.totalLeido = totalLeido;
    }

    public int getTotalEsperado() {
        return totalEsperado;
    }

    public void setTotalEsperado(int totalEsperado) {
        this.totalEsperado = totalEsperado;
    }

    public List<ICSeccion> getSecciones() {
        return secciones;
    }

    public void setSecciones(List<ICSeccion> secciones) {
        this.secciones = secciones;
    }
}
