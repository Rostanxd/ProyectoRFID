package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

public class Etiqueta {
    public String epc;
    public String codigo;
    public String grupo1;
    public String grupo2;
    public String grupo3;
    public String grupo4;
    public String grupo5;

    public String getEpc() {
        return epc;
    }

    public void setEpc(String epc) {
        this.epc = epc;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getGrupo1() {
        return grupo1;
    }

    public void setGrupo1(String grupo1) {
        this.grupo1 = grupo1;
    }

    public String getGrupo2() {
        return grupo2;
    }

    public void setGrupo2(String grupo2) {
        this.grupo2 = grupo2;
    }

    public String getGrupo3() {
        return grupo3;
    }

    public void setGrupo3(String grupo3) {
        this.grupo3 = grupo3;
    }

    public String getGrupo4() {
        return grupo4;
    }

    public void setGrupo4(String grupo4) {
        this.grupo4 = grupo4;
    }

    public String getGrupo5() {
        return grupo5;
    }

    public void setGrupo5(String grupo5) {
        this.grupo5 = grupo5;
    }

    public Etiqueta(String epc, String codigo, String grupo1, String grupo2, String grupo3, String grupo4, String grupo5) {
        this.epc = epc;
        this.codigo = codigo;
        this.grupo1 = grupo1;
        this.grupo2 = grupo2;
        this.grupo3 = grupo3;
        this.grupo4 = grupo4;
        this.grupo5 = grupo5;
    }
}
