package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

public class Guide {

   public String numero;
   public String codigo;
   public String descripcion;
   public int cantidad;
   public  int saldo;


    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Guide(String numero, String codigo, String descripcion, int cantidad, int saldo) {
        this.numero = numero;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.saldo = saldo;
    }
}
