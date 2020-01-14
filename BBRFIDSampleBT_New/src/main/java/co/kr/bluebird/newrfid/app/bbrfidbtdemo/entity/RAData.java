package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

public class RAData {
    public DataSourceDto usuario;
    public DataSourceDto rol;
    public RAAcceso acceso;

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

    public RAAcceso getAcceso() {
        return acceso;
    }

    public void setAcceso(RAAcceso acceso) {
        this.acceso = acceso;
    }

    public RAData(DataSourceDto usuario, DataSourceDto rol, RAAcceso acceso) {
        this.usuario = usuario;
        this.rol = rol;
        this.acceso = acceso;
    }
}
