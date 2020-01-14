package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

public class RfidAutentication {
    public DataSourceDto estado;
    public RAData data_;

    public DataSourceDto getEstado() {
        return estado;
    }

    public void setEstado(DataSourceDto estado) {
        this.estado = estado;
    }

    public RAData getData_() {
        return data_;
    }

    public void setData_(RAData data_) {
        this.data_ = data_;
    }

    public RfidAutentication(DataSourceDto estado, RAData data_) {
        this.estado = estado;
        this.data_ = data_;
    }
}
