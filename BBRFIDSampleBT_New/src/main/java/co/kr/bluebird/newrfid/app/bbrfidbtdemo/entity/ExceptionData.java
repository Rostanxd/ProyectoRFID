package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

public class ExceptionData {
    public String typeException;
    public String msgException;
    public int statusCode;
    public String msgUsuario;
    public String auxiliar;

    public String getTypeException() {
        return typeException;
    }

    public void setTypeException(String typeException) {
        this.typeException = typeException;
    }

    public String getMsgException() {
        return msgException;
    }

    public void setMsgException(String msgException) {
        this.msgException = msgException;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getAuxiliar() {
        return auxiliar;
    }

    public void setAuxiliar(String auxiliar) {
        this.auxiliar = auxiliar;
    }

    public String getMsgUsuario() {
        return msgUsuario;
    }

    public void setMsgUsuario(String msgUsuario) {
        this.msgUsuario = msgUsuario;
    }
}
