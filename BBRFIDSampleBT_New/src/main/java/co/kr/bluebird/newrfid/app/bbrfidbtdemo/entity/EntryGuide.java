package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

import java.io.Serializable;

public class EntryGuide implements Serializable {
    public DataSourceDto estado;
    public EGData data_;

    public DataSourceDto getEstado() {
        return estado;
    }

    public void setEstado(DataSourceDto estado) {
        this.estado = estado;
    }

    public EGData getData_() {
        return data_;
    }

    public void setData_(EGData data_) {
        this.data_ = data_;
    }

    public EntryGuide(DataSourceDto estado, EGData data_) {
        this.estado = estado;
        this.data_ = data_;
    }
}
