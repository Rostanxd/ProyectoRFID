package co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility;

import android.widget.Switch;

public class CustomMsgExceptions {

    public String getCustomMsg(String typeException, int statuscode){
        return TypeExcepctionMsg(typeException,statuscode );
    }

    private String TypeExcepctionMsg(String exception, int statuscode){
        String message = null ;

        switch (exception){

            case "SocketTimeoutException":
                message = "Se agotó el tiempo de espera";
                break;
            case "SocketException":
                message = "No se pudo conectar al servidor, puede ser itermitencia en la red...";
                break;
            case  "UnknownHostException":
                message = "No se pudo determinar la direccion del Host, parece que el end-point esta incorrecto o ha sido cambiado";
                break;
            case "HttpResponseException":
                if(statuscode != 0){
                    message = HttpStatusCode(statuscode);
                }

                break;

            case "Exception":
                message = "Ocurrio un error desconocido";
                break;
            /*case "":
                message = "";
                break;*/

        }

        return message;
    }

    private String HttpStatusCode(int statuscode){

        String msg = null;
        switch (statuscode){
            case 400:
                msg = "No pudo interpretar la solicitud dada una sintaxis inválida";
                break;
            case 401:
                msg = "No tiene autorizacion para acceder a este recurso";
                break;

            case 403:
                msg = "No tiene permiso a este contenido";
                break;
            case 404:
                msg = "El servidor no pudo encontrar el servicio, quizas este ya no exista";
                break;

        }

        return msg;

    }
}
