package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

import java.util.List;

public class LoginData {

    public DataSourceDto estado;
    public DataSourceDto usuario;
    public DataSourceDto rol;
    public List<LoginProgram> accesos;


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

    public DataSourceDto getRol() {
        return rol;
    }

    public void setRol(DataSourceDto rol) {
        this.rol = rol;
    }

    public List<LoginProgram> getAccesos() {
        return accesos;
    }

    public void setAccesos(List<LoginProgram> accesos) {
        this.accesos = accesos;
    }
}
