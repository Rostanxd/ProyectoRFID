package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

public class ParamLogin {
    public String startdate;
    public String usuario;
    public int estado;
    public boolean isValidseccion;

    public boolean isValidseccion() {
        return isValidseccion;
    }

    public void setValidseccion(boolean validseccion) {
        isValidseccion = validseccion;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

}
