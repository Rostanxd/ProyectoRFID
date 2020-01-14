package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

import java.util.List;

public class LoginData {

    public DataSourceDto estado;
    public DataSourceDto usuario;
    public List<LoginAcceso> accesos;

    public DataSourceDto getEstado() {
        return estado;
    }

    public void setEstado(DataSourceDto estado) {
        this.estado = estado;
    }

    public DataSourceDto getUsuario() {
        return usuario;
    }

    public void setUsuario(DataSourceDto usuario) {
        this.usuario = usuario;
    }

    public List<LoginAcceso> getAccesos() {
        return accesos;
    }

    public void setAccesos(List<LoginAcceso> accesos) {
        this.accesos = accesos;
    }
}
