package co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility;

public class AppMessage {
    public String outputMsg(int codigo){
        String msg = null;
        switch (codigo){
            case 1:
                msg = "El servicio no devolvio ningun estado";
                break;
            case 2:
                msg = "Error al interpretar respuesta del servicio";
                break;
            case 3:
                msg = "No Existen Conteos";
                break;
            case 4:
                msg = "Error inesperado";
                break;
        }
        return msg;
    }
    public String msjToast(int codigo){
        String msg = null;
        switch (codigo){
            case 1:
                msg = "El servicio no devolvio ningun estado";
                break;
            case 2:
                msg = "Error al interpretar respuesta del servicio";
                break;
        }
        return msg;
    }
}
