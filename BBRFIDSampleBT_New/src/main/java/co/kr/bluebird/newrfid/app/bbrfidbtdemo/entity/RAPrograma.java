package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

public class RAPrograma {
    public String nombre;
    public String ruta;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public RAPrograma(String nombre, String ruta) {
        this.nombre = nombre;
        this.ruta = ruta;
    }
}
