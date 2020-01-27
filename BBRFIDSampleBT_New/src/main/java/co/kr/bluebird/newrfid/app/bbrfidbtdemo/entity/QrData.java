package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

public class QrData {
    public String contenidoQr;
    public String motivo;
    public String usuario;
    public String imageQrBase64;
    public String cantidad;

    public String getContenidoQr() {
        return contenidoQr;
    }

    public void setContenidoQr(String contenidoQr) {
        this.contenidoQr = contenidoQr;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getImageQrBase64() {
        return imageQrBase64;
    }

    public void setImageQrBase64(String imageQrBase64) {
        this.imageQrBase64 = imageQrBase64;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
}
