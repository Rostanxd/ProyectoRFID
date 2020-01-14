package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

public class ICSeccion {

    public String nombre;
    public int leido;
    public int esperado;
    public double porcentaje;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getLeido() {
        return leido;
    }

    public void setLeido(int leido) {
        this.leido = leido;
    }

    public int getEsperado() {
        return esperado;
    }

    public void setEsperado(int esperado) {
        this.esperado = esperado;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }
}
