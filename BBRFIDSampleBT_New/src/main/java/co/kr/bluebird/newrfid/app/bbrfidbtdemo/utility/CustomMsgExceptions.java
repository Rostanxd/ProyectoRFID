package co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility;

import android.widget.Switch;

public class CustomMsgExceptions {

    public String getCustomMsg(String typeException, int statuscode, String msjExeption){
        return TypeExcepctionMsg(typeException,statuscode, msjExeption );
    }

    private String TypeExcepctionMsg(String exception, int statuscode, String msjExeption){
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
                message = "Ocurrio un error desconocido,Mensaje_exepcion: "+msjExeption;
                break;
            /*case "":
                message = "";
                break;*/

        }

        return message;
    }

    private String HttpStatusCode(int statuscode){

        String msg ;
        switch (statuscode){
            case 305:
                msg = "(Error http: 305)Error de Redireccion: problemas en el proxy en el proxy";
                break;

            case 400:
                msg = "(Error-Cliente http: 400)No pudo interpretar la solicitud dada una sintaxis inválida";
                break;
            case 401:
                msg = "(Error-Cliente http: 401)No tiene autorizacion para acceder a este recurso";
                break;

            case 403:
                msg = "(Error-Cliente http: 403)No tiene permiso a este contenido";
                break;
            case 404:
                msg = "(Error-Cliente http: 404)El servidor no pudo encontrar el servicio, quizas este ya no exista";
                break;
            case 405:
                msg = "(Error-Cliente http: 405)El recurso solicitado ha sido deshabilitado";
                break;
            case 406:
                msg = "(Error-Cliente http: 406)No se encuentra ningun contenido seguido por los criterios dados";
                break;
            case 407:
                msg = "(Error-Cliente http: 407) Se necesita una auntenticación Proxy";
                break;
            case 408:
                msg = "(Error-Cliente http: 408) El servidor ha cerrado la conexión";
                break;
            case 409:
                msg = "(Error-Cliente http: 409) El la peticion realizada tiene algun tipo de conflicto";
                break;
            case 410:
                msg = "(Error-Cliente http: 410) El contenido solicitado ha sido borrado del servidor";
                break;
            case 423:
                msg = "(Error-Cliente http: 423) El recurso que está siendo accedido está bloqueado";
                break;
            case 500:
                msg = "(Error-Server http: 500) Error interno del servidor";
                break;
            case 501:
                msg = "(Error-Server http: 501) El servicio solicitado parece que ha sido dado de baja";
                break;

            case 503:
                msg = "(Error-Server http: 503) El servidor no esta listo para manejar la peticion";
                break;
            case 504:
                msg = "(Error-Server http: 504) No se pudo mantener una respuesta a tiempo";
                break;

            case 506:
                msg = "(Error-Server http: 506) El servidor tiene un problema de configuración interna";
                break;

                default:
                    if(statuscode >= 300 && statuscode <= 399){
                        msg = "(Error-Redireccionamiento http: "+statuscode+")";
                    }
                    else if(statuscode >= 400 && statuscode <= 499){
                        msg = "(Error-Cliente http: "+statuscode+")";
                    }
                    else if(statuscode >= 500 && statuscode <= 599){
                        msg = "(Error-Server http: "+statuscode+")";
                    }
                    else {
                        msg = "Error Http desconocido, Codigo de estado Http:"+ statuscode;
                    }

                    break;

        }

        return msg;

    }
}
