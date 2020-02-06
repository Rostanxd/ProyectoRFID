package co.kr.bluebird.newrfid.app.bbrfidbtdemo.service;

import android.content.Context;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.transport.HttpsServiceConnectionSE;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.DataSourceDto;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.DataSourceDtoEx;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EGDetailResponse;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EGTagsResponseItem;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EntryGuide;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ExceptionData;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.Garment;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.GarmentSale;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.GarmentSaleObj;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.GarmentSize;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.GenericSpinnerDto;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ICSeccion;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.InventoryControl;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.LoginData;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.LoginProgram;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ParamLectorRfid;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ParamLogin;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ReceiveWareDetail;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.Replenishment;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ReplenishmentSale;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ReplenishmentWareResult;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.Replenishments;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ReplenismentSaleDetailsDto;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ResidueExternDetail;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.StoreExistence;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.StoreExistenceObj;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.WSException;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.CustomMsgExceptions;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.ParamRfidIteration;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.ResponseSoapToObject;

public class RfidService {

    private String gUsuario ;
    private ParamRfidIteration paramRfidIteration;
    private ParamLectorRfid paramLectorRfid_;
    private Context globalContext;
    private ParamLogin paramLogin_;

    private String TriggerException;
    private ExceptionData exceptionData = null;
    private CustomMsgExceptions msgExceptions = new CustomMsgExceptions();

    public String SOAP_ACTION_ = null;
    public String METHOD_NAME_ = null;
    public String NAMESPACE_ = null;
    public String URL_;
    private ResponseSoapToObject responseSoapToObject;

    public RfidService(Context mContext_)
    {
        paramRfidIteration = new ParamRfidIteration(mContext_);
        paramLectorRfid_ =  paramRfidIteration.ConsultarParametros();
        paramLogin_ = paramRfidIteration.ConsultarParametrosLogin();
        globalContext = mContext_;

        gUsuario = paramLogin_ != null ? paramLogin_.getUsuario() :"";

        responseSoapToObject = new ResponseSoapToObject();
    }

    /**
     * invoque a awsrfidguiaentradaoc (Lista de Guia de entrada de una orden de compra)
     * @param numOrdenCompra
     * @return
     */
    public EntryGuide WSGuiaEntradaByOrdenCompra(String numOrdenCompra)
    {
        SoapObject Request = new SoapObject(NAMESPACE_,METHOD_NAME_);
        Request.addProperty("Ordencompranumero",numOrdenCompra);
        return responseSoapToObject.GuiaEntradaOCResponse(CallService(Request, SOAP_ACTION_, paramLectorRfid_.getEndpoint()+URL_, true, false), exceptionData);
    }

    /**
     * invoca a awsrfidguiaentradaprocesar (finaliza el proceso de Guia de entrada)
     * @param numGuia
     * @return
     */
    public DataSourceDto WSGuiaEntradaProcesar(String numGuia, List<String> ListEpc)
    {
        /*SoapObject request = new SoapObject(NAMESPACE_,METHOD_NAME_ );
        request.addProperty("Guianumero", numGuia);
        request.addProperty("Usrcodigo", paramLogin_ != null ? paramLogin_.getUsuario(): "");*/
        SoapObject request = ArmarRequestGuiaEntradaDetalle(numGuia,ListEpc );
        request.addProperty("Usrcodigo", paramLogin_ != null ? paramLogin_.getUsuario(): "");

        return responseSoapToObject.ResponseToDatasourceDto(CallService(request,SOAP_ACTION_,  paramLectorRfid_.getEndpoint()+URL_,true, false), exceptionData);

    }

    /**
     * invoca a awsrfidguiaentradadetalle (homologar los tags leidos con los ingresados en el sistema)
     * @param numGuia
     * @param ListEpc
     * @return
     */
    public EGDetailResponse GuiaEntradaDetalleService2(String numGuia, List<String> ListEpc)
    {
        SoapObject response = ArmarRequestGuiaEntradaDetalle(numGuia,ListEpc );
        return responseSoapToObject.ResponseToEGDetailResponse(CallService(response,SOAP_ACTION_,  paramLectorRfid_.getEndpoint()+URL_,true, false), exceptionData, "GEN");
    }

    /**
     * *
     * crea la estructura del request del ws awsrfidguiaentradadetalle
     * @param numGuia numero de guia entrada
     * @param ListEpc lista de items(epc tags rfid) leidos
     * @return devuelve un soapObject que contiene el request
     */
    private SoapObject ArmarRequestGuiaEntradaDetalle(String numGuia,List<String> ListEpc){

        SoapObject SOepc ;
        SoapObject SOtagEPC = new SoapObject(NAMESPACE_, "tag_read");
        SoapObject SOTagRequest = new SoapObject(NAMESPACE_, "tag_request");
        SOTagRequest.addProperty("dispositivoId", paramLectorRfid_.getDispositivoid());
        for(String epc: ListEpc) {

            SOepc = new SoapObject(NAMESPACE_, "tag_epc");
            SOepc.addProperty("epc",epc);

            SOtagEPC.addProperty("etiqueta",SOepc);

        }
        SOTagRequest.addProperty("etiquetas",SOtagEPC );


        SoapObject SOPrincipal = new SoapObject(NAMESPACE_, METHOD_NAME_);
        SOPrincipal.addProperty("Guianumero", numGuia);
        SOPrincipal.addProperty("Sdtrfidetiquetasrequest",SOTagRequest);

        return SOPrincipal;
    }

    /**
     * Invoca al servicio  soap a traves de HttpTransportSE
     * @param SOAP_ACTION
     * @param METHOD_NAME
     * @param NAMESPACE
     * @param URL
     * @param PROPERTIES
     * @param soapObjectProp
     * @param dotNet
     * @param IsimplicitTypes
     * @return
     */
    private SoapObject CallService(String SOAP_ACTION, String METHOD_NAME, String NAMESPACE, String URL, Map<String, String> PROPERTIES, Map<String, SoapObject> soapObjectProp, boolean dotNet, boolean IsimplicitTypes)
    {
        //List<Map<String, String>> PROPERTIES
        SoapObject resultRequestSOAP = null;
        String ExMensaje = "";
        TriggerException = null;
        exceptionData = null;
        try
        {

            SoapObject Request = new SoapObject(NAMESPACE,METHOD_NAME);

            if(soapObjectProp != null){

                for(Map.Entry<String,SoapObject> map : soapObjectProp.entrySet())
                {
                    Request.addProperty(map.getKey(),map.getValue());
                }
            }
            else {
                if (PROPERTIES != null) {
                    for (Map.Entry<String, String> map : PROPERTIES.entrySet()) {
                        Request.addProperty(map.getKey(), map.getValue());
                    }
                }
            }
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            if(IsimplicitTypes){
                soapEnvelope.implicitTypes = true;
            }
            soapEnvelope.dotNet = dotNet;

            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transportSE = new HttpTransportSE(URL);
            transportSE.debug = true;


            transportSE.call(SOAP_ACTION,soapEnvelope);
            String requestDump = transportSE.requestDump;
            String responseDump = transportSE.responseDump;

            resultRequestSOAP = (SoapObject) soapEnvelope.getResponse();
        }
        catch (SocketTimeoutException ste)
        {
            TriggerException = "Al Parecer no hay conexion a internet, Cod Error:" + ste.getMessage();
            SetExceptionData("SocketTimeoutException", ste.getMessage(), 0);
        }
        catch (SocketException se){
            TriggerException = se.getMessage();
            SetExceptionData("SocketException", se.getMessage(), 0);
        }
        catch (UnknownHostException ue){
            TriggerException = "ERRORHOST";
            SetExceptionData("UnknownHostException", ue.getMessage(), 0 );
        }
        catch (HttpResponseException hex)
        {
            TriggerException = "ERRORHOST";
            SetExceptionData("HttpResponseException", hex.getMessage(), hex.getStatusCode());
        }

        catch (Exception ex)
        {
            TriggerException = ex.getMessage();
            SetExceptionData("Exception", ex.getMessage(), 0);
        }
        return resultRequestSOAP;
    }


    /**
     * Invoca al servicio  soap a traves de HttpTransportSE
     * @param Request
     * @param SOAP_ACTION
     * @param URL
     * @param dotNet
     * @param IsimplicitTypes
     * @return
     */
    private SoapObject CallService(SoapObject Request, String SOAP_ACTION, String URL, boolean dotNet, boolean IsimplicitTypes)
    {
        //List<Map<String, String>> PROPERTIES
        SoapObject resultRequestSOAP = null;
        TriggerException = null;
        exceptionData = null;
        try
        {
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            if(IsimplicitTypes){
                soapEnvelope.implicitTypes = true;
            }
            soapEnvelope.dotNet = dotNet;

            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transportSE = new HttpTransportSE(URL);
            transportSE.debug = true;


            transportSE.call(SOAP_ACTION,soapEnvelope);
            String requestDump = transportSE.requestDump;
            String responseDump = transportSE.responseDump;

            resultRequestSOAP = (SoapObject) soapEnvelope.getResponse();
        }
        catch (SocketTimeoutException ste)
        {
            TriggerException = "Al Parecer no hay conexion a internet, Cod Error:" + ste.getMessage();
            SetExceptionData("SocketTimeoutException", ste.getMessage(), 0);
        }
        catch (SocketException se){
            TriggerException = se.getMessage();
            SetExceptionData("SocketException", se.getMessage(), 0);
        }
        catch (UnknownHostException ue){
            TriggerException = "ERRORHOST";
            SetExceptionData("UnknownHostException", ue.getMessage(), 0 );
        }
        catch (HttpResponseException hex)
        {
            TriggerException = "ERRORHOST";
            SetExceptionData("HttpResponseException", hex.getMessage(), hex.getStatusCode());
        }

        catch (Exception ex)
        {
            TriggerException = ex.getMessage();
            SetExceptionData("Exception", ex.getMessage(), 0);
        }
        return resultRequestSOAP;
    }

    /**
     *
     * @param typeException
     * @param msgException
     * @param statusCode
     */
    private void SetExceptionData(String typeException, String msgException, int statusCode){
        exceptionData = new ExceptionData();
        exceptionData.setTypeException(typeException);
        exceptionData.setMsgException(msgException);
        exceptionData.setMsgUsuario(msgExceptions.getCustomMsg(typeException, statusCode, msgException) );
        exceptionData.setStatusCode(statusCode);
    }





    /*
    * metodos GenericSpinnerDto, fuente de datos de los controles spinners
    * ********************************************************************
    * ********************************************************************
    * ********************************************************************
    * */

    /**
     * *
     * Invoca awsrfidbodegas or awsrfidmotivos
     * @param isBodegas booleano que indica si lo que quiere traer son bodegas caso contrario Motivos
     * @param procesoId id del proceso que invoca el metodo (GDE, REC, ENV)
     * @param codBodAlmacenamiento
     * @return
     */
    public GenericSpinnerDto WSBodegasOrMotivosService(boolean isBodegas, String procesoId, String codBodAlmacenamiento)
    {

        Map<String, String> propiedades = new HashMap<>();
        String propiedadResponse;
        String nombreOrDesc ;

        if(isBodegas){

            propiedades.put("Procesoid",procesoId);
            propiedades.put("Bodcodigo",paramLectorRfid_.getCodbodega());
            propiedadResponse = "bodegas";
            nombreOrDesc = "nombre";
        }
        else {

           if(codBodAlmacenamiento == null) {
               propiedades.put("Bodcodigo",paramLectorRfid_.getCodbodega());
           }
           else {
               propiedades.put("Bodcodigo",codBodAlmacenamiento);
           }

            propiedadResponse = "motivos";
            nombreOrDesc = "descripcion";
        }

        return responseSoapToObject.ResponseToGenericSpinnerDto(CallService(SOAP_ACTION_,METHOD_NAME_,NAMESPACE_,paramLectorRfid_.getEndpoint()+URL_,propiedades, null, true, false), exceptionData, propiedadResponse, nombreOrDesc, true);
    }

    /**
     * Invoca a Ws awsrfidtipomovimientos
     * @return tipo de movimientos entre bodegas y locales
     */

    public GenericSpinnerDto WSTipoMovimientos()
    {
        GenericSpinnerDto spinnerDto = null;
        Map<String, String> propiedades = new HashMap<>();
        propiedades.put("Bodcodigo", paramLectorRfid_.getCodbodega());
        return responseSoapToObject.ResponseToGenericSpinnerDto(CallService(SOAP_ACTION_,METHOD_NAME_,NAMESPACE_,paramLectorRfid_.getEndpoint()+URL_,propiedades, null, true, false),exceptionData,"tipos","nombre",true);
    }

    /**
     * invoca a awsrfidubicacion
     * @return las ubicaciones como (piso de venta, Bodega Interna, etc)
     */
    public GenericSpinnerDto WSUbicacion()
    {
        SoapObject soRequest = new SoapObject(NAMESPACE_, METHOD_NAME_);
        return responseSoapToObject.ResponseToGenericSpinnerDto(CallService(soRequest,SOAP_ACTION_,paramLectorRfid_.getEndpoint()+URL_,true,false),exceptionData,"ubicaciones","descripcion",true);
    }

    /**
     * invoca a awsrfidsecciones
     * @return las diferentes secciones de prendas de un local(segun la bodega parametrizada en el dispositivo)
     */
    public GenericSpinnerDto WSSeccion()
    {
        SoapObject soRequest = new SoapObject(NAMESPACE_, METHOD_NAME_);
        soRequest.addProperty("Bodcodigo",paramLectorRfid_.getCodbodega());
        return responseSoapToObject.ResponseToGenericSpinnerDto(CallService(soRequest,SOAP_ACTION_,paramLectorRfid_.getEndpoint()+URL_,true,false),exceptionData,"secciones","descripcion",true);
    }

    /**
     * Invoca a awsrfidconteos
     * @return
     */
    public GenericSpinnerDto WSConteo(){
        SoapObject soRequest = new SoapObject(NAMESPACE_, METHOD_NAME_);
        soRequest.addProperty("Bodcodigo",paramLectorRfid_.getCodbodega());
        SoapObject response = CallService(soRequest,SOAP_ACTION_,paramLectorRfid_.getEndpoint()+URL_,true,false);
        return responseSoapToObject.ResponseToConteosGSDto(response, exceptionData);

    }









    //Guia de despacho and Envio de mercaderia  Homologacion

    /**
     * Invoca a aWsRfidEpcHomologacion, homologa las etiquetas leidas con la informacion del sistema
     * @param ListEpc
     * @return EGDetailResponse con la informacion homologada
     */
    public EGDetailResponse EPCHomologacionService(ArrayList<String> ListEpc){
        String SOAP_ACTION = "WebSithaction/AWSRFIDEPCHOMOLOGACION.Execute";
        String METHOD_NAME = "WsRfidEpcHomologacion.Execute";
        String NAMESPACE = "WebSith";
        String URL =  paramLectorRfid_.getEndpoint()+"/aWsRfidEpcHomologacion.aspx";
        SoapObject response = CallService(AsemblerRequestHomologacion(ListEpc, NAMESPACE, METHOD_NAME),SOAP_ACTION,URL,false, true);
        return responseSoapToObject.ResponseToEGDetailResponse(response,exceptionData, "GDE");
    }

    /**
     *
     * @param ListEpc
     * @param NAMESPACE
     * @param METHOD_NAME
     * @return
     */
    private SoapObject AsemblerRequestHomologacion(ArrayList<String> ListEpc, String NAMESPACE, String METHOD_NAME)
    {
        SoapObject so_request ;
        SoapObject so_tag_request;
        SoapObject so_etiqueta = new SoapObject(NAMESPACE, null);

        so_tag_request = new SoapObject(NAMESPACE,null);
        so_tag_request.addProperty("dispositivoId", paramLectorRfid_.getDispositivoid());
        for (String epc: ListEpc) {
            SoapObject so_epc = new SoapObject(NAMESPACE_, null);
            so_epc.addProperty("epc", epc);
            so_etiqueta.addProperty("etiqueta",so_epc );
        }
        so_tag_request.addProperty("etiquetas",so_etiqueta);
        so_request = new SoapObject(NAMESPACE, METHOD_NAME);
        so_request.addProperty("Sdtrfidetiquetasrequest",so_tag_request );
        return  so_request;

    }


    /**
     *
     * @param items
     * @param lGuiaEntrada
     * @param lBodega
     * @return
     */
    private SoapObject AsemblerRequestGDProcesar(List<EGTagsResponseItem> items, String lGuiaEntrada, String lBodega)
    {
        SoapObject so_request = new SoapObject(NAMESPACE_, METHOD_NAME_);
        so_request.addProperty("Guiaentrada", lGuiaEntrada);
        so_request.addProperty("Bodcodigo", lBodega);
        so_request.addProperty("Usrcodigo", gUsuario);

        so_request = AsemblerSdtrfidetiquetasresponse(so_request, items);
        so_request.addProperty("Discodigo", paramLectorRfid_.getDispositivoid());

        return so_request;

    }

    /**
     * Arma la trama del request "Sdtrfidetiquetasresponse"
     * @param so_request
     * @param items
     * @return
     */
    private SoapObject AsemblerSdtrfidetiquetasresponse(SoapObject so_request, List<EGTagsResponseItem> items ){

        SoapObject so_epcs, so_item , so_data_leido, so_ResponseItem;
        so_ResponseItem = new SoapObject(NAMESPACE_, null);
        for(EGTagsResponseItem item: items) {

            so_epcs = new SoapObject(NAMESPACE_, null);
            if(item.getDataLeido() != null && item.getDataLeido().size() > 0){

                so_item = new SoapObject(NAMESPACE_,null);
                for (String epc : item.getDataLeido()) {
                    so_item.addProperty("item", epc);

                }
                so_epcs.addProperty("EPCs", so_item);
            }


            so_data_leido = new SoapObject(NAMESPACE_,null);

            so_data_leido.addProperty("itemCodigo",item.itemCodigo);
            so_data_leido.addProperty("itemGrupo1",item.itemGrupo1);
            so_data_leido.addProperty("itemGrupo2",item.itemGrupo2);
            so_data_leido.addProperty("itemGrupo3",item.itemGrupo3);
            so_data_leido.addProperty("itemGrupo4",item.itemGrupo4);
            so_data_leido.addProperty("itemGrupo5",item.itemGrupo5);
            so_data_leido.addProperty("CantidadLeidos",item.getCantidadLeidos());
            so_data_leido.addProperty("CantidadNoLeidos",item.getCantidadNoLeidos());

            so_data_leido.addProperty("data_leido",so_epcs);

            so_ResponseItem.addProperty("SdtRfidEtiquetasResponse.SdtRfidEtiquetasResponseItem", so_data_leido);
        }
        so_request.addProperty("Sdtrfidetiquetasresponse", so_ResponseItem);
        /*so_request.addProperty("Discodigo", paramLectorRfid_.getDispositivoid());*/

        return  so_request;
    }


    /**
     * Invoca awsrfidguiadespachoprocesar
     * @param items
     * @param lGuiaEntrada
     * @param lBodega
     * @return
     */
    public  DataSourceDto GuiaDespachoProcesarService(List<EGTagsResponseItem> items, String lGuiaEntrada, String lBodega)
    {
        SoapObject request = AsemblerRequestGDProcesar(items, lGuiaEntrada, lBodega);
        SoapObject response = CallService(request,SOAP_ACTION_,paramLectorRfid_.getEndpoint()+URL_,true, false);
        return responseSoapToObject.ResponseToDataSourceWithSecuenceGuia(response, exceptionData, "secuenciaGuia");
    }

    // Envio de Mercaderia

    /**
     * Invoca awsrfidenviomercaderiaprocesar
     * @return Datasource con el estado de la solicitud y la secuencia a convertir en QRcode
     */
    public DataSourceDto WSEnvioMercaderiaProcesarService(List<EGTagsResponseItem> items, String pMotivo, String pNota){
        SoapObject request = AsemblerEnvioMercaderiaProcesar(items, pMotivo, pNota);
        SoapObject response = CallService(request,SOAP_ACTION_,paramLectorRfid_.getEndpoint()+URL_,true, false);
        return responseSoapToObject.ResponseToDataSourceWithSecuenceGuia(response, exceptionData, "contenidoQR");
    }

    /**
     * Armar el request que solicita el awsrfidenviomercaderiaprocesar
     * @param items
     * @param pMotivo
     * @param pNota
     * @return
     */
    private SoapObject AsemblerEnvioMercaderiaProcesar(List<EGTagsResponseItem> items, String pMotivo, String pNota)
    {
        SoapObject so_request = new SoapObject(NAMESPACE_, METHOD_NAME_);
        so_request.addProperty("Bodcodigo", paramLectorRfid_.getCodbodega());
        so_request.addProperty("Motcodigo", pMotivo);
        so_request.addProperty("Trnnota", pNota);

        so_request = AsemblerSdtrfidetiquetasresponse(so_request, items);
        so_request.addProperty("Usrcodigo", gUsuario);
        so_request.addProperty("Discodigo", paramLectorRfid_.getDispositivoid());

        return so_request;

    }


    private SoapObject AsemblerRequestRecepMercaderiaProcesar(SoapObject soNivel1,String SOAP_ACTION, String URL, String NAMESPACE, List<EGTagsResponseItem> items, boolean requiereDataNoLeida){

        SoapObject soNIvel5 ;
        SoapObject soNivel4 = null;
        SoapObject soNivel3 ;

        SoapObject soNivel2 = new SoapObject(NAMESPACE, "nivel2");

        for(EGTagsResponseItem item: items) {

            if(item.getDataLeido() != null && item.getDataLeido().size() > 0)
            {
                soNivel4 = new SoapObject(NAMESPACE, "data_leido");
                soNIvel5 = new SoapObject(NAMESPACE,null);
                for (String epc : item.getDataLeido()) {
                    soNIvel5.addProperty("item", epc);

                }
                soNivel4.addProperty("EPCs", soNIvel5);
            }


            soNivel3 = new SoapObject(NAMESPACE,"nivel3");

            soNivel3.addProperty("itemCodigo",item.itemCodigo);
            soNivel3.addProperty("itemGrupo1",item.itemGrupo1);
            soNivel3.addProperty("itemGrupo2",item.itemGrupo2);
            soNivel3.addProperty("itemGrupo3",item.itemGrupo3);
            soNivel3.addProperty("itemGrupo4",item.itemGrupo4);
            soNivel3.addProperty("itemGrupo5",item.itemGrupo5);
            soNivel3.addProperty("CantidadLeidos",item.getCantidadLeidos());
            soNivel3.addProperty("CantidadNoLeidos",item.getCantidadNoLeidos());

            if(soNivel4 != null){
                soNivel3.addProperty("data_leido",soNivel4);
            }


            if(requiereDataNoLeida)
            {
                if(item != null && item.getDataNoLeido() != null && item.getDataNoLeido().size() > 0)
                {
                    soNivel4 = new SoapObject(NAMESPACE, "data_no_leido");
                    soNIvel5 = new SoapObject(NAMESPACE,null);
                    for (String epc : item.getDataNoLeido()) {
                        soNIvel5.addProperty("item", epc);

                    }
                    soNivel4.addProperty("EPCs", soNIvel5);
                    soNivel3.addProperty("data_no_leido",soNivel4);
                }

            }


            soNivel2.addProperty("SdtRfidEtiquetasResponse.SdtRfidEtiquetasResponseItem", soNivel3);
        }
        soNivel1.addProperty("Sdtrfidetiquetasresponse", soNivel2);
        soNivel1.addProperty("Usrcodigo", gUsuario);
        soNivel1.addProperty("Discodigo", paramLectorRfid_.getDispositivoid());

        SoapObject resultRequestSOAP = CallService(soNivel1,SOAP_ACTION,URL,true, false);

        return resultRequestSOAP;
    }



    // WS RECEPCION DE MERCADERIA


    /**
     * Busca los detalles basicos de una salida de mercaderia segun el doc. origen escaneado o ingresado manualmente
     * @param doc_origen
     * @param isNotExistQr
     * @param dataNoQr
     * @return
     */
    public ReceiveWareDetail WSRecepcionMercaderiaDetail(String doc_origen, boolean isNotExistQr, String[] dataNoQr)
    {
        SoapObject response = CallService(AsemblerRequestRecepcionMercaderia(doc_origen, isNotExistQr, dataNoQr),SOAP_ACTION_,paramLectorRfid_.getEndpoint()+URL_,true,false);
        return responseSoapToObject.ResponseToReceiveWareDetail(response, exceptionData);
    }

    /**
     * Arma  el request q solicita el ws awsrfidrecepciondetalle
     * @param doc_origen
     * @param isNotExistQr
     * @param dataNoQr
     * @return
     */
    private SoapObject AsemblerRequestRecepcionMercaderia(String doc_origen, boolean isNotExistQr, String[] dataNoQr){

        SoapObject soRequest = new SoapObject(NAMESPACE_, METHOD_NAME_);
        soRequest.addProperty("Bodcodigo", paramLectorRfid_.getCodbodega());
        if(isNotExistQr && dataNoQr != null )
        {
            soRequest.addProperty("Tiporigen", dataNoQr[0]);
            soRequest.addProperty("Bodorigen", dataNoQr[1]);
            soRequest.addProperty("Aniorigen", dataNoQr[2]);
            soRequest.addProperty("Numorigen", dataNoQr[3]);
        }
        else
        {
            soRequest.addProperty("Docorigen", doc_origen);
        }
        return soRequest;
    }


    /**
     * Invoca awsrfidrecepcioncompara
     * @param Docorigen
     * @param epcs
     * @return
     */
    public EGDetailResponse WSRecepcionMercaderiaCompara(String Docorigen, List<String> epcs)
    {
        SoapObject request =  AsemblerRequestRecepCompare(Docorigen, epcs);
        SoapObject response = CallService(request,SOAP_ACTION_,paramLectorRfid_.getEndpoint()+URL_,true,false);
        return responseSoapToObject.ResponseToEGDetailResponse(response, exceptionData, "REC");
    }

    /**
     * Arma request del ws awsrfidrecepcioncompara
     * @param Docorigen
     * @param epcs
     * @return
     */
    private SoapObject AsemblerRequestRecepCompare(String Docorigen, List<String> epcs){

        SoapObject request = new SoapObject(NAMESPACE_, METHOD_NAME_);
        request.addProperty("Docorigen", Docorigen);
        request.addProperty("Bodcodori", paramLectorRfid_.getCodbodega());

        SoapObject soNivel2 = new SoapObject(NAMESPACE_, "soNivel2");
        soNivel2.addProperty("dispositivoId",paramLectorRfid_.getDispositivoid());


        SoapObject soNivel3 = new SoapObject(NAMESPACE_, "soNivel3");;
        SoapObject soNivel4 = null;

        for (String epc:epcs) {

            soNivel4 = new SoapObject(NAMESPACE_,null);
            soNivel4.addProperty("epc",epc);
            soNivel3.addProperty("etiqueta", soNivel4);
        }

        soNivel2.addProperty("etiquetas",soNivel3);
        request.addProperty("Sdtrfidetiquetasrequest", soNivel2);

        return request;
    }


    /**
     * Invoque awsrfidrecepcionprocesar
     * @param pDocOrigen
     * @param pNota
     * @param responseItems
     * @return
     */
    public   DataSourceDto WSRecepcionMercaderiaProcesar(String pDocOrigen, String pNota, List<EGTagsResponseItem> responseItems)
    {
        SoapObject soNivel1 = new SoapObject(NAMESPACE_, METHOD_NAME_);
        soNivel1.addProperty("Bodcodori", paramLectorRfid_.getCodbodega());
        soNivel1.addProperty("Docorigen", pDocOrigen);
        soNivel1.addProperty("Nota", pNota);
        SoapObject response =AsemblerRequestRecepMercaderiaProcesar( soNivel1, SOAP_ACTION_, paramLectorRfid_.getEndpoint()+URL_, NAMESPACE_, responseItems,true);
        return responseSoapToObject.ResponseToDatasourceDto(response, exceptionData);

    }



    // WS TOMA DE iNVENTARIO...

    /**
     * Invoque awsrfidconteodetalle
     * @param pSecconteo
     * @return
     */
    public InventoryControl WSInventoryControlCountDetail(String pSecconteo){


        SoapObject soRequest = new SoapObject(NAMESPACE_, METHOD_NAME_);
        soRequest.addProperty("Bodcodigo",paramLectorRfid_.codbodega);
        soRequest.addProperty("Secconteo",pSecconteo);
        SoapObject response = CallService(soRequest,SOAP_ACTION_,paramLectorRfid_.getEndpoint()+URL_,true,false);
        return responseSoapToObject.ResponsetoInventoryControl(response, exceptionData);

        //return responsetoObjectIC(CallService(soRequest,SOAP_ACTION_,paramLectorRfid_.getEndpoint()+URL_,true,false));

    }

    /**
     * Invoque awsrfidtomaprocesar
     * @param ListEpc
     * @param pConteo
     * @param pUbicacion
     * @return
     */
    public DataSourceDto WSTomaInventarioProcesar2(ArrayList<String> ListEpc, String pConteo, String pUbicacion)
    {
        //return   ResponseToDataSourceDto(CallService(GenerarRequestInvProcesar(ListEpc, pConteo,pUbicacion ),SOAP_ACTION_,paramLectorRfid_.getEndpoint()+URL_,true, false));
        SoapObject response = CallService(GenerarRequestInvProcesar(ListEpc, pConteo,pUbicacion ),SOAP_ACTION_,paramLectorRfid_.getEndpoint()+URL_,true, false);
        return responseSoapToObject.ResponseToDatasourceDto(response, exceptionData);
    }

    /**
     * Arma request de awsrfidtomaprocesar
     * @param ListEpc
     * @param pConteo
     * @param pUbicacion
     * @return
     */
    private SoapObject GenerarRequestInvProcesar(ArrayList<String> ListEpc, String pConteo, String pUbicacion){
        SoapObject soRequest = new SoapObject(NAMESPACE_, METHOD_NAME_);
        SoapObject soNivel4;
        SoapObject soNivel3 = new SoapObject(NAMESPACE_, null);
        SoapObject soSdtrfidetiquetasrequest = new SoapObject(NAMESPACE_, null);

        for (String epc:ListEpc) {
            soNivel4 = new SoapObject(NAMESPACE_, null);
            soNivel4.addProperty("epc",epc);

            soNivel3.addProperty("etiqueta", soNivel4);
        }

        soSdtrfidetiquetasrequest.addProperty("dispositivoId",paramLectorRfid_.getDispositivoid() );
        soSdtrfidetiquetasrequest.addProperty("etiquetas", soNivel3);


        soRequest.addProperty("Bodcodigo", paramLectorRfid_.getCodbodega());
        soRequest.addProperty("Secconteo", pConteo);
        soRequest.addProperty("Ubicodigo", pUbicacion);
        soRequest.addProperty("Sdtrfidetiquetasrequest",soSdtrfidetiquetasrequest );
        soRequest.addProperty("Usrcodigo",paramLogin_ != null ? paramLogin_.getUsuario() : "");

        return soRequest;
    }



    // WS Inventario por tienda...

    /**
     * Invoque awsrfiditeminfo
     * @param coditem
     * @return
     */
    public Garment WSInventoryPerStore(String coditem)
    {
        SoapObject soRequest = new SoapObject(NAMESPACE_, METHOD_NAME_);
        soRequest.addProperty("Itmcodigo", coditem);
        SoapObject response = CallService(soRequest,SOAP_ACTION_,paramLectorRfid_.getEndpoint()+URL_,true,false);
        return responseSoapToObject.ResponseToGarment(response, exceptionData);
    }


    /**
     * Invoca a awsrfiditemsaldos
     * @param pItmcodigo
     * @return
     */
    public GarmentSaleObj WSInventoryPerStoreSale(String pItmcodigo)
    {
        SoapObject soRequest = new SoapObject(NAMESPACE_, METHOD_NAME_);
        soRequest.addProperty("Bodcodigo", paramLectorRfid_.getCodbodega());
        soRequest.addProperty("Itmcodigo", pItmcodigo);
        SoapObject response = CallService(soRequest,SOAP_ACTION_,paramLectorRfid_.getEndpoint()+ URL_,true,false);
        return responseSoapToObject.ResponseToGarmentSale(response, exceptionData);


    }


    /**
     * Invoque awsrfiditemsaldoslocales
     * @param pItmcodigo
     * @return
     */
    public StoreExistenceObj WSInventoryPerStoreExistencePlace(String pItmcodigo){

        SoapObject soRequest = new SoapObject(NAMESPACE_, METHOD_NAME_);
        soRequest.addProperty("Bodcodigo", paramLectorRfid_.getCodbodega());
        soRequest.addProperty("Itmcodigo", pItmcodigo);
        return responseSoapToObject.ResponseToStoreExistenceObj(CallService(soRequest,SOAP_ACTION_,paramLectorRfid_.getEndpoint()+URL_,true,false), exceptionData);

    }

    public LoginData WSLogin(String user, String pass, Boolean isAdministrador)
    {
        SoapObject response = AsemblerRequestLogin_InvoqueWS(user,pass, isAdministrador);
        return responseSoapToObject.ResponseToLoginData(response, exceptionData);

    }

    private SoapObject AsemblerRequestLogin_InvoqueWS(String user, String pass, Boolean isAdministrador){
        SoapObject soRequest = new SoapObject(NAMESPACE_, METHOD_NAME_);


        String url;
        if(!isAdministrador){
            url = paramLectorRfid_.getEndpoint()+URL_;

            soRequest.addProperty("Usrcodigo", user.trim());
            soRequest.addProperty("Usrclave", pass.trim());
        }
        else {
            url = paramLectorRfid_.getEndpointExt()+URL_;
            soRequest.addProperty("Usuario", user.trim());
            soRequest.addProperty("Clave", pass.trim());
            soRequest.addProperty("Dispositivoid", paramLectorRfid_.getDispositivoid());
        }

        return CallService(soRequest,SOAP_ACTION_,url,true,false);

    }


    public Replenishments WSReposicion(List<String> listEpc, String Lincodigo, String Prdcodigo)
    {
        SoapObject soRequest =  AsemblerRequestReposicion(listEpc, Lincodigo, Prdcodigo);

        SoapObject response = CallService(soRequest,SOAP_ACTION_,paramLectorRfid_.getEndpoint()+URL_,true,false);
        return  responseSoapToObject.ResponseToObjReplenishment(response, exceptionData);
    }

    private SoapObject AsemblerRequestReposicion(List<String> listEpc, String Lincodigo, String Prdcodigo){
        SoapObject soRequest = new SoapObject(NAMESPACE_, METHOD_NAME_);
        SoapObject soNivel4 ;
        SoapObject soNivel3 = new SoapObject(NAMESPACE_, "Nivel3");
        SoapObject soSdtrfidetiquetasrequest = new SoapObject(NAMESPACE_, "Sdtrfidetiquetasrequest");

        for (String epc:listEpc) {
            soNivel4 = new SoapObject(NAMESPACE_, null);
            soNivel4.addProperty("epc",epc);

            soNivel3.addProperty("etiqueta", soNivel4);
        }

        soSdtrfidetiquetasrequest.addProperty("dispositivoId", paramLectorRfid_.getDispositivoid() );
        soSdtrfidetiquetasrequest.addProperty("etiquetas", soNivel3 );
        soRequest.addProperty("Sdtrfidetiquetasrequest", soSdtrfidetiquetasrequest );
        soRequest.addProperty("Lincodigo", Lincodigo);
        soRequest.addProperty("Prdcodigo", Prdcodigo);
        soRequest.addProperty("Bodcodigo", paramLectorRfid_.getCodbodega());

        return soRequest;
    }


    // falta agregar estado
    /**
     * Invoque awsrfidsaldosotroslocales
     * @param Itmcodigo
     * @return
     */
    public ReplenismentSaleDetailsDto WsReposicionSaldoDetalle(String Itmcodigo)
    {
        SoapObject soRequest = new SoapObject(NAMESPACE_, METHOD_NAME_);
        soRequest.addProperty("Itmcodigo", Itmcodigo);
        soRequest.addProperty("Bodcodigo", paramLectorRfid_.getCodbodega());
        return  responseSoapToObject.ResponseToObjReplenishmentSale(CallService(soRequest,SOAP_ACTION_,paramLectorRfid_.getEndpoint()+URL_,true,false),exceptionData);

    }

    public DataSourceDto WsSnapShot(List<String> listEpc ,String Lincodigo)
    {
        SoapObject soRequest = AsemblerRequestSnapShot(listEpc, Lincodigo);
        return responseSoapToObject.ResponseToDatasourceDto(CallService(soRequest,SOAP_ACTION_,paramLectorRfid_.getEndpoint()+URL_,true,false), exceptionData);

    }

    private SoapObject AsemblerRequestSnapShot(List<String> listEpc ,String Lincodigo){

        SoapObject soRequest = new SoapObject(NAMESPACE_, METHOD_NAME_);
        SoapObject soNivel2 = new SoapObject(NAMESPACE_,"Nivel2");
        SoapObject soNivel4 ;
        SoapObject soNivel3 = new SoapObject(NAMESPACE_, "Nivel3");

        for (String epc:listEpc) {
            soNivel4 = new SoapObject(NAMESPACE_, null);
            soNivel4.addProperty("epc",epc);
            soNivel3.addProperty("etiqueta", soNivel4);
        }

        soNivel2.addProperty("dispositivoId",paramLectorRfid_.getDispositivoid());
        soNivel2.addProperty("etiquetas",soNivel3);

        soRequest.addProperty("Sdtrfidetiquetasrequest", soNivel2);
        soRequest.addProperty("Lincodigo", Lincodigo);
        soRequest.addProperty("Prdcodigo", "");
        soRequest.addProperty("Bodcodigo", paramLectorRfid_.getCodbodega());
        soRequest.addProperty("Usrcodigo", gUsuario);

        return  soRequest;
    }
}
