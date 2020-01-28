package co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.DataSourceDto;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EGData;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EntryGuide;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ExceptionData;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.Guide;

public class ResponseSoapToObject {

    public EntryGuide GuiaEntradaOCResponse(SoapObject response, ExceptionData exceptionData)
    {
        EntryGuide entryGuideResponse = null;
        DataSourceDto dtoEstado = null;
        int cantidadtotal = 0;
        EGData data_ = null;

        if(exceptionData == null){

            Guide guide;
            List<Guide> guides;

            try {
                if(response.hasProperty("estado")){
                    SoapObject soapObject = (SoapObject) response.getProperty("estado");
                    dtoEstado = new DataSourceDto (soapObject.getPropertyAsString("codigo"), soapObject.getPropertyAsString("mensaje"), null);
                }
                else {
                    dtoEstado = new DataSourceDto("9999", "El servicio no devolvio ningun estado", null );
                }
                if(response.hasProperty("data")){
                    SoapObject soapObject = (SoapObject) response.getProperty("data");
                    String cantTotal = soapObject.getPropertyAsString("cantidadTotal");
                    cantidadtotal = (cantTotal != null && !cantTotal.equals("")) ? Integer.parseInt(cantTotal) : 0;

                    if(soapObject.hasProperty("guias")){

                        SoapObject so_guides = (SoapObject) soapObject.getProperty("guias");

                        if(so_guides.getPropertyCount() > 0){

                            guides = new ArrayList<Guide>();
                            SoapObject so_guide;
                            int cant = 0;
                            for(int x=0; x<so_guides.getPropertyCount();x++){
                                so_guide = (SoapObject) so_guides.getProperty(x);
                                cant = Integer.parseInt(!so_guide.getPropertyAsString("cantidad").equals("") ? so_guide.getPropertyAsString("cantidad") : "0");
                                guide = new Guide(so_guide.getPropertyAsString("numero"), so_guide.getPropertyAsString("estadoCodigo"), so_guide.getPropertyAsString("estadoNombre"), cant);
                                guides.add(guide);
                            }

                            data_ = new EGData(cantidadtotal ,guides);
                        }
                    }
                }
            }
            catch (Exception ex){
                dtoEstado = new DataSourceDto("9999", "Error al interpretar respuesta del servicio -Guia entrada OC-", null);
            }

        }
        else {
            dtoEstado = new DataSourceDto("9999", exceptionData.getMsgUsuario() ,null);
        }
        entryGuideResponse = new EntryGuide(dtoEstado,data_);

        return entryGuideResponse;
    }
}
