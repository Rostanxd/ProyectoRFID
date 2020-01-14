package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

public class EntryGuideDetail {
    public DataSourceDto estado;
    public EGDData data_;

    public DataSourceDto getEstado() {
        return estado;
    }

    public void setEstado(DataSourceDto estado) {
        this.estado = estado;
    }

    public EGDData getData_() {
        return data_;
    }

    public void setData_(EGDData data_) {
        this.data_ = data_;
    }

    public EntryGuideDetail(DataSourceDto estado, EGDData data_) {
        this.estado = estado;
        this.data_ = data_;
    }
}
