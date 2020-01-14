package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

import java.util.List;

public class LoginAcceso {
    public DataSourceDto rol;
    public List<LoginProgram> listaprogramas;

    public DataSourceDto getRol() {
        return rol;
    }

    public void setRol(DataSourceDto rol) {
        this.rol = rol;
    }

    public List<LoginProgram> getListaprogramas() {
        return listaprogramas;
    }

    public void setListaprogramas(List<LoginProgram> listaprogramas) {
        this.listaprogramas = listaprogramas;
    }
}
