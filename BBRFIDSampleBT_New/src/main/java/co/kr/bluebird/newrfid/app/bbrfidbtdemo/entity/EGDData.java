package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

import java.util.List;

public class EGDData {
    public List<Etiqueta> etiquetas;

    public List<Etiqueta> getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(List<Etiqueta> etiquetas) {
        this.etiquetas = etiquetas;
    }

    public EGDData(List<Etiqueta> etiquetas) {
        this.etiquetas = etiquetas;
    }
}
