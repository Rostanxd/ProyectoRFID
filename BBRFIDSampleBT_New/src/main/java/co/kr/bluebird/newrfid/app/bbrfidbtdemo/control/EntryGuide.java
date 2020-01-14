package co.kr.bluebird.newrfid.app.bbrfidbtdemo.control;

import java.util.List;

public class EntryGuide {

    /*public static class Estado{
        public String codigo;
        public String mensaje;

        public String getCodigo() {
            return codigo;
        }

        public void setCodigo(String codigo) {
            this.codigo = codigo;
        }

        public String getMensaje() {
            return mensaje;
        }

        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }

        public Estado(String codigo, String mensaje) {
            this.codigo = codigo;
            this.mensaje = mensaje;
        }
    }

    public static class Data{
        public int cantidadTotal;
        public List<Guia> guias;

        public int getCantidadTotal() {
            return cantidadTotal;
        }

        public void setCantidadTotal(int cantidadTotal) {
            this.cantidadTotal = cantidadTotal;
        }

        public List<Guia> getGuias() {
            return guias;
        }

        public void setGuias(List<Guia> guias) {
            this.guias = guias;
        }

        public Data(int cantidadTotal, List<Guia> guias) {
            this.cantidadTotal = cantidadTotal;
            this.guias = guias;
        }
    }

    public static class Guia{
        public String numero;
        public String estadoCodigo;
        public String estadoNombre;
        public int cantidad;

        public String getNumero() {
            return numero;
        }

        public void setNumero(String numero) {
            this.numero = numero;
        }

        public String getEstadoCodigo() {
            return estadoCodigo;
        }

        public void setEstadoCodigo(String estadoCodigo) {
            this.estadoCodigo = estadoCodigo;
        }

        public String getEstadoNombre() {
            return estadoNombre;
        }

        public void setEstadoNombre(String estadoNombre) {
            this.estadoNombre = estadoNombre;
        }

        public int getCantidad() {
            return cantidad;
        }

        public void setCantidad(int cantidad) {
            this.cantidad = cantidad;
        }

        public Guia(String numero, String estadoCodigo, String estadoNombre, int cantidad) {
            this.numero = numero;
            this.estadoCodigo = estadoCodigo;
            this.estadoNombre = estadoNombre;
            this.cantidad = cantidad;
        }
    }*/
    public String num_entrada;
    public String cantidad;
    public String status;

    public String getNum_entrada() {
        return num_entrada;
    }

    public void setNum_entrada(String num_entrada) {
        this.num_entrada = num_entrada;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public EntryGuide(String num_entrada, String cantidad, String status) {
        this.num_entrada = num_entrada;
        this.cantidad = cantidad;
        this.status = status;
    }
}
