package co.kr.bluebird.newrfid.app.bbrfidbtdemo.service;

import android.content.Context;
import android.provider.ContactsContract;
import android.widget.Switch;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.transport.HttpsServiceConnectionSE;
import org.w3c.dom.Document;

import java.lang.reflect.Array;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.DataSourceDto;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.DataSourceDtoEx;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.DespatchGuide;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EGDData;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EGData;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EGDetailResponse;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EGTagsResponseItem;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EntryGuide;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EntryGuideDetail;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.Etiqueta;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ExceptionData;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.Garment;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.GarmentSale;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.GarmentSaleObj;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.GarmentSize;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.GenericSpinnerDto;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.Guide;
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
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ResidueExternDetail;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.SendTags;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.StoreExistence;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.WSException;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.CustomMsgExceptions;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility.ParamRfidIteration;

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

    public RfidService()
    {
        paramRfidIteration = null;
        paramLectorRfid_ =  null;
    }

    public RfidService(Context mContext_)
    {
        paramRfidIteration = new ParamRfidIteration(mContext_);
        paramLectorRfid_ =  paramRfidIteration.ConsultarParametros();
        paramLogin_ = paramRfidIteration.ConsultarParametrosLogin();
        globalContext = mContext_;

        gUsuario = paramLogin_ != null ? paramLogin_.getUsuario() :"";
    }

    /*public RfidService(Context mContext_, String [] WSConsumo)
    {
        paramRfidIteration = new ParamRfidIteration();
        paramLectorRfid_ =  paramRfidIteration.ConsultarParametros(mContext_);
        globalContext = mContext_;

        if(WSConsumo != null && WSConsumo.length > 0){
            SOAP_ACTION_ = WSConsumo[0];
            METHOD_NAME_ = WSConsumo[1];
            NAMESPACE_ = WSConsumo[2];
            URL_ = paramLectorRfid_.getHostPort()+ WSConsumo[3];
        }

    }*/


    // WS GUIA DE ENTRADA
    public EntryGuide GuiaEntradaService(String numOrdenCompra)
    {
        /*String SOAP_ACTION = "WebSithaction/AWSRFIDGUIAENTRADAOC.Execute";
        String METHOD_NAME = "WsRfidGuiaEntradaOC.Execute";
        String NAMESPACE = "WebSith";
        String URL = "http://info.thgye.com.ec/awsrfidguiaentradaoc.aspx";*/


        DataSourceDto geEstado;
        EGData data_;
        Guide guide;
        List<Guide> guides;
        EntryGuide ResponseGuide = new EntryGuide(null,null);

        String numero;
        String estadoCodigo;
        String estadoNombre;
        int cantidad;
        String CantidadTotal;

        try {

            SoapObject Request = new SoapObject(NAMESPACE_,METHOD_NAME_);
            Request.addProperty("Ordencompranumero",numOrdenCompra);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;

            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transportSE = new HttpTransportSE(paramLectorRfid_.getEndpoint()+URL_);
            transportSE.debug = true;


            transportSE.call(SOAP_ACTION_,soapEnvelope);
            /*String requestDump = transportSE.requestDump;
            String responseDump = transportSE.responseDump;*/

            SoapObject resultRequestSOAP = (SoapObject) soapEnvelope.getResponse();

            SoapPrimitive spEstado =(SoapPrimitive) ((SoapObject)resultRequestSOAP.getProperty("estado")).getProperty("codigo");
            SoapPrimitive spMensaje =(SoapPrimitive) ((SoapObject)resultRequestSOAP.getProperty("estado")).getProperty("mensaje");
            geEstado = new DataSourceDto(spEstado.getValue().toString(),spMensaje.getValue().toString(),null);
            if(spEstado.getValue().toString().equals("00")){

                CantidadTotal =  ((SoapObject)resultRequestSOAP.getProperty("data")).getPropertyAsString("cantidadTotal");

                guides = new ArrayList<Guide>();
                SoapObject soGuides = ((SoapObject) ((SoapObject)resultRequestSOAP.getProperty("data")).getProperty("guias"));
                for (int x=0; x<soGuides.getPropertyCount();x++)
                {
                    SoapObject soGuide = (SoapObject) soGuides.getProperty(x);
                    numero = soGuide.getPropertyAsString("numero");
                    estadoCodigo = soGuide.getPropertyAsString("estadoCodigo");
                    estadoNombre = soGuide.getPropertyAsString("estadoNombre");
                    cantidad = Integer.parseInt(soGuide.getPropertyAsString("cantidad")) ;

                    guide = new Guide(numero,estadoCodigo,estadoNombre,cantidad);
                    guides.add(guide);
                }

                data_ = new EGData(Integer.parseInt(CantidadTotal) ,guides);
                ResponseGuide = new EntryGuide(geEstado,data_);

            }
            else {
                ResponseGuide = new EntryGuide( geEstado,null );
            }

        } catch (Exception ex){
            geEstado =  new DataSourceDto("9999", "Error en la invocacion al servicio: "+ex.getMessage(),null);
            ResponseGuide = new EntryGuide(geEstado, null);
        }

        return ResponseGuide;
    }

    public DataSourceDto WSGuiaEntradaProcesar(String numGuia){

        DataSourceDto sourceDto = null;
        SoapObject request = new SoapObject(NAMESPACE_,METHOD_NAME_ );
        request.addProperty("Guianumero", numGuia);
        request.addProperty("Usrcodigo", paramLogin_ != null ? paramLogin_.getUsuario(): "");


        SoapObject resultRequestSOAP = CallService(request,SOAP_ACTION_,  paramLectorRfid_.getEndpoint()+URL_,true, false);


        if(TriggerException == null && resultRequestSOAP != null && resultRequestSOAP.hasProperty("estado")){

            SoapObject soEstado = (SoapObject) resultRequestSOAP.getProperty("estado");

            String codigo = soEstado.getPropertyAsString("codigo");
            String mensaje = soEstado.getPropertyAsString("mensaje");

            sourceDto = new DataSourceDto(codigo, mensaje, null);

        }
        else {
            sourceDto = new DataSourceDto("9999", TriggerException, null);
        }

        return sourceDto;

    }

    public EGDetailResponse GuiaEntradaDetalleService2(String numGuia, List<String> ListEpc)
    {
        /*String SOAP_ACTION = "WebSithaction/AWSRFIDGUIAENTRADADETALLE.Execute";
        String METHOD_NAME = "WsRfidGuiaEntradaDetalle.Execute";
        String NAMESPACE = "WebSith";
        String URL = "http://info.thgye.com.ec/awsrfidguiaentradadetalle.aspx";*/

        SoapObject SOepc ;
        SoapObject SOtagEPC = new SoapObject(NAMESPACE_, "tag_read");
        SoapObject SOTagRequest = new SoapObject(NAMESPACE_, "tag_request");
        SOTagRequest.addProperty("dispositivoId", 0);
        for(String epc: ListEpc) {

            SOepc = new SoapObject(NAMESPACE_, "tag_epc");
            SOepc.addProperty("epc",epc);

            SOtagEPC.addProperty("etiqueta",SOepc);

        }
        SOTagRequest.addProperty("etiquetas",SOtagEPC );


        SoapObject SOPrincipal = new SoapObject(NAMESPACE_, METHOD_NAME_);
        SOPrincipal.addProperty("Guianumero", numGuia);
        SOPrincipal.addProperty("Sdtrfidetiquetasrequest",SOTagRequest);

        SoapObject resultRequestSOAP = CallService(SOPrincipal,SOAP_ACTION_,  paramLectorRfid_.getEndpoint()+URL_,true, false);


        DataSourceDto geEstado = null;
        EGDetailResponse egDetailResponse = new EGDetailResponse();
        EGTagsResponseItem egTagsResponseItem = null;
        List<EGTagsResponseItem> egTagsResponseItemList = new ArrayList<EGTagsResponseItem>();

        //SoapObject TagRespItem = null;

        String [] ItemValue =  new String[9];

        List<String> _tagNoRead;
        List<String> _tagRead;

        boolean GuiaEntradaProcesable = true;

        if(TriggerException == null)
        {
            try{

                SoapPrimitive spEstado =(SoapPrimitive) ((SoapObject)resultRequestSOAP.getProperty("estado")).getProperty("codigo");
                SoapPrimitive spMensaje =(SoapPrimitive) ((SoapObject)resultRequestSOAP.getProperty("estado")).getProperty("mensaje");
                geEstado = new DataSourceDto(spEstado.getValue().toString(),spMensaje.getValue().toString(),null);

                if(spEstado.getValue().toString().equals("00")){

                    SoapObject soTags = ((SoapObject) ((SoapObject)resultRequestSOAP.getProperty("data")).getProperty("items"));
                    for (int x=0; x<soTags.getPropertyCount();x++)
                    {
                        SoapObject TagRespItem = (SoapObject) soTags.getProperty(x);
                        //TagRespItem = (SoapObject) soTag.getProperty("SdtRfidEtiquetasResponse.SdtRfidEtiquetasResponseItem");
                        ItemValue[0] = TagRespItem.getPropertyAsString("itemCodigo");
                        ItemValue[1] = TagRespItem.getPropertyAsString("itemGrupo1");
                        ItemValue[2] = TagRespItem.getPropertyAsString("itemGrupo2");
                        ItemValue[3] = TagRespItem.getPropertyAsString("itemGrupo3");
                        ItemValue[4] = TagRespItem.getPropertyAsString("itemGrupo4");
                        ItemValue[5] = TagRespItem.getPropertyAsString("itemGrupo5");
                        ItemValue[6] = TagRespItem.getPropertyAsString("CantidadLeidos");
                        ItemValue[7] = TagRespItem.getPropertyAsString("CantidadNoLeidos");
                        ItemValue[8] = TagRespItem.getPropertyAsString("CantidadDoc");


                        _tagNoRead = new ArrayList<String>();
                        _tagRead = new ArrayList<String>();

                        SoapObject soLectura = null;

                        if(TagRespItem.hasProperty("data_leido")){

                            soLectura = (SoapObject) TagRespItem.getProperty("data_leido");

                            if(soLectura.hasProperty("EPCs")){

                                try {
                                    SoapPrimitive spLeidosEPCs = (SoapPrimitive) soLectura.getProperty("EPCs");

                                    if(!spLeidosEPCs.getValue().toString().equals("\n" + "\t\t\t\t\t\t\t\t")){

                                        SoapObject soLeidosEPCs = (SoapObject) soLectura.getProperty("EPCs");
                                        for(int y=0; y<soLeidosEPCs.getPropertyCount();y++){
                                            String epc = soLeidosEPCs.getPropertyAsString(y);
                                            // _tagNoRead.add(epc.getPropertyAsString("epc"));
                                            _tagRead.add(epc);

                                        }

                                    }
                                }
                                catch (Exception ex){
                                    SoapObject soLeidosEPCs = (SoapObject) soLectura.getProperty("EPCs");
                                    for(int y=0; y<soLeidosEPCs.getPropertyCount();y++){
                                        String epc = soLeidosEPCs.getPropertyAsString(y);
                                        // _tagNoRead.add(epc.getPropertyAsString("epc"));
                                        _tagRead.add(epc);

                                    }
                                }

                            }
                        }

                        if(TagRespItem.hasProperty("data_no_leido")){

                            soLectura = (SoapObject) TagRespItem.getProperty("data_no_leido");

                            if(soLectura.hasProperty("EPCs")){

                           /* SoapPrimitive spNoLeidosEPCs = (SoapPrimitive) soLectura.getProperty("EPCs");

                            if(!spNoLeidosEPCs.getValue().toString().equals("\n" + "\t\t\t\t\t\t\t\t")){
                                SoapObject soNoLeidosEPCs = (SoapObject) soLectura.getProperty("EPCs");

                                for(int y=0; y<soNoLeidosEPCs.getPropertyCount();y++){
                                    String epc = soNoLeidosEPCs.getPropertyAsString(y);
                                    // _tagNoRead.add(epc.getPropertyAsString("epc"));
                                    _tagNoRead.add(epc);

                                }
                            }*/

                                try {

                                    SoapPrimitive spNoLeidosEPCs = (SoapPrimitive) soLectura.getProperty("EPCs");

                                    if(!spNoLeidosEPCs.getValue().toString().equals("\n" + "\t\t\t\t\t\t\t\t")){
                                        SoapObject soNoLeidosEPCs = (SoapObject) soLectura.getProperty("EPCs");

                                        for(int y=0; y<soNoLeidosEPCs.getPropertyCount();y++){
                                            String epc = soNoLeidosEPCs.getPropertyAsString(y);
                                            // _tagNoRead.add(epc.getPropertyAsString("epc"));
                                            _tagNoRead.add(epc);

                                        }
                                    }

                                }catch (Exception ex){
                                    SoapObject soNoLeidosEPCs = (SoapObject) soLectura.getProperty("EPCs");

                                    for(int y=0; y<soNoLeidosEPCs.getPropertyCount();y++){
                                        String epc = soNoLeidosEPCs.getPropertyAsString(y);
                                        // _tagNoRead.add(epc.getPropertyAsString("epc"));
                                        _tagNoRead.add(epc);

                                    }
                                }

                            }
                        }

                        egTagsResponseItem = new EGTagsResponseItem(ItemValue[0],ItemValue[1],ItemValue[2],ItemValue[3],ItemValue[4],ItemValue[5], Integer.valueOf(ItemValue[6]) ,Integer.valueOf(ItemValue[7]),_tagRead,_tagNoRead);

                        egTagsResponseItem.setCantidadDoc(Integer.valueOf(ItemValue[8]));

                        // if(cant_leidos != cant_documentado)
                        if(Integer.valueOf(ItemValue[6]) != Integer.valueOf(ItemValue[8])){
                            GuiaEntradaProcesable = false;
                        }
                        egTagsResponseItemList.add(egTagsResponseItem);
                    }
                    egDetailResponse.setItems(egTagsResponseItemList);
                }
                egDetailResponse.setStatus(geEstado);

            }catch (Exception ex){
                egDetailResponse.setStatus(new DataSourceDto( "9999", ex.getMessage(), null));
            }
        }
        else {
            egDetailResponse.setStatus(new DataSourceDto("9999", TriggerException, null));
        }

        egDetailResponse.setProcesable(GuiaEntradaProcesable);

        return  egDetailResponse;
    }

    private SoapObject CallService(String SOAP_ACTION, String METHOD_NAME, String NAMESPACE, String URL, Map<String, String> PROPERTIES, Map<String, SoapObject> soapObjectProp, boolean dotNet, boolean IsimplicitTypes)
    {
        //List<Map<String, String>> PROPERTIES
        SoapObject resultRequestSOAP = null;
        String ExMensaje = "";
        TriggerException = null;
        try
        {

            SoapObject Request = new SoapObject(NAMESPACE,METHOD_NAME);

            //Request.addProperty("Guianumero",numGuia);

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
            //soapEnvelope.implicitTypes = true;
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
        }
        catch (SocketException se){
            TriggerException = se.getMessage();
        }
        catch (Exception ex)
        {
            TriggerException = ex.getMessage();
        }
        return resultRequestSOAP;
    }


    private SoapObject CallService(SoapObject Request, String SOAP_ACTION, String URL, boolean dotNet, boolean IsimplicitTypes)
    {
        //List<Map<String, String>> PROPERTIES
        SoapObject resultRequestSOAP = null;
        TriggerException = null;
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

    private void SetExceptionData(String typeException, String msgException, int statusCode){
        exceptionData = new ExceptionData();
        exceptionData.setTypeException(typeException);
        exceptionData.setMsgException(msgException);
        exceptionData.setMsgUsuario(msgExceptions.getCustomMsg(typeException, statusCode) );
        exceptionData.setStatusCode(statusCode);
    }


    public DespatchGuide WSBodegasOrMotivosService(boolean isBodegas, Context mContext, String procesoId, String codBodAlmacenamiento)
    {
        /*String SOAP_ACTION = "";
        String METHOD_NAME = "";
        String NAMESPACE = "";
        String URL = "";*/
        //paramRfidIteration = new ParamRfidIteration();
        Map<String, String> propiedades = new HashMap<>();
        String propiedadResponse;
        String nombreOrDesc ;

        //ParamLectorRfid paramLectorRfid =  paramRfidIteration.ConsultarParametros(mContext);
        //String codBodega = paramLectorRfid.getCodbodega();

        if(isBodegas){
            /*//SOAP_ACTION = "http://tempuri.org/action/AWSRFIDBODEGAS.Execute";
            SOAP_ACTION = "WebSithaction/AWSRFIDBODEGAS.Execute";
            METHOD_NAME = "WsRfidBodegas.Execute";
            //NAMESPACE = "http://tempuri.org/";
            NAMESPACE = "WebSith";
            URL = "http://info.thgye.com.ec/awsrfidbodegas.aspx";*/
            propiedades.put("Procesoid",procesoId);
            propiedades.put("Bodcodigo",paramLectorRfid_.getCodbodega());
            propiedadResponse = "bodegas";
            nombreOrDesc = "nombre";
        }
        else {
           /* SOAP_ACTION = "WebSithaction/AWSRFIDMOTIVOS.Execute";
            METHOD_NAME = "WsRfidMotivos.Execute";
            NAMESPACE = "WebSith";
            URL = "http://info.thgye.com.ec/awsrfidmotivos.aspx";*/
           if(codBodAlmacenamiento == null) {
               propiedades.put("Bodcodigo",paramLectorRfid_.getCodbodega());
           }
           else {
               propiedades.put("Bodcodigo",codBodAlmacenamiento);
           }

            propiedadResponse = "motivos";
            nombreOrDesc = "descripcion";
        }



        SoapObject resultRequestSOAP = CallService(SOAP_ACTION_,METHOD_NAME_,NAMESPACE_,paramLectorRfid_.getEndpoint()+URL_,propiedades, null, true, false);

        DespatchGuide despatchGuide = null;
        if(TriggerException == null){
            DataSourceDto gdEstado;
            DataSourceDto gdbodega;

            List<DataSourceDto> bodegas;
            despatchGuide = new DespatchGuide(null,null);

            try {

                String codigoBodega;
                String nombreBodega;

                SoapPrimitive spEstado =(SoapPrimitive) ((SoapObject)resultRequestSOAP.getProperty("estado")).getProperty("codigo");
                SoapPrimitive spMensaje =(SoapPrimitive) ((SoapObject)resultRequestSOAP.getProperty("estado")).getProperty("mensaje");
                gdEstado = new DataSourceDto(spEstado.getValue().toString(),spMensaje.getValue().toString(),null);
                if(spEstado.getValue().toString().equals("00")){

                    bodegas = new ArrayList<DataSourceDto>();
                    SoapObject soBodegas = ((SoapObject) ((SoapObject)resultRequestSOAP.getProperty("data")).getProperty(propiedadResponse));
                    for (int x=0; x<soBodegas.getPropertyCount();x++)
                    {
                        SoapObject soTag = (SoapObject) soBodegas.getProperty(x);
                        codigoBodega = soTag.getPropertyAsString("codigo");
                        nombreBodega = soTag.getPropertyAsString(nombreOrDesc);
                        gdbodega = new DataSourceDto(codigoBodega, nombreBodega, null);
                        bodegas.add(gdbodega);
                    }

                    despatchGuide = new DespatchGuide(gdEstado, bodegas);

                }
                else {
                    despatchGuide = new DespatchGuide( gdEstado,null );
                }

            } catch (Exception ex){
                despatchGuide = new DespatchGuide(new DataSourceDto("9999", ex.getMessage(),null), null);
            }
        }
        else {
            despatchGuide = new DespatchGuide(new DataSourceDto("9999", TriggerException,null), null);
        }

        return despatchGuide;
    }

    public GenericSpinnerDto WSTipoMovimientos()
    {
        /*String SOAP_ACTION = "WebSithaction/AWSRFIDTIPOMOVIMIENTOS.Execute";
        String METHOD_NAME = "WsRfidTipoMovimientos.Execute";
        String NAMESPACE = "WebSith";
        String URL = host+"/awsrfidtipomovimientos.aspx";
*/
        GenericSpinnerDto spinnerDto = null;
        Map<String, String> propiedades = new HashMap<>();


        propiedades.put("Bodcodigo", paramLectorRfid_.getCodbodega());
        SoapObject resultRequestSOAP = CallService(SOAP_ACTION_,METHOD_NAME_,NAMESPACE_,paramLectorRfid_.getEndpoint()+URL_,propiedades, null, true, false);

        if(TriggerException == null){
            spinnerDto = ResponseToGenericSpinnerDto(resultRequestSOAP, "tipos","nombre",false);
        }
        else {
            spinnerDto = new GenericSpinnerDto();
            spinnerDto.setEstado(new DataSourceDto("9999", TriggerException, null));

        }

        return spinnerDto;
    }

    private GenericSpinnerDto ResponseToGenericSpinnerDto(SoapObject response, String propName, String propName2, boolean addSeleccione)
    {
        GenericSpinnerDto spinnerDto = new GenericSpinnerDto();
        DataSourceDto dtoEstado = null;
        /*DataSourceDto dtoData = null;*/
        DataSourceDto dtoTipo = null;
        List<DataSourceDto> dtoTipos = null;
        String codigo = null, nombre = null;

        if(TriggerException == null) {


            if (response != null && response.hasProperty("estado")) {

                try {
                    SoapObject respEstado = (SoapObject) response.getProperty("estado");
                    dtoEstado = new DataSourceDto(respEstado.getPropertyAsString("codigo"), respEstado.getPropertyAsString("mensaje"), null);

                    if (dtoEstado.getCodigo().equals("00")) {
                        if (response.hasProperty("data")) {
                            SoapObject respData = (SoapObject) response.getProperty("data");
                            if (respData.hasProperty(propName)) {
                                SoapObject respTipos = (SoapObject) respData.getProperty(propName);

                                if (respTipos.getPropertyCount() > 0) {
                                    dtoTipos = new ArrayList<DataSourceDto>();
                                    if (addSeleccione) {
                                        dtoTipos.add(new DataSourceDto("0", "- Seleccione -", null));
                                    }
                                    for (int x = 0; x < respTipos.getPropertyCount(); x++) {
                                        SoapObject soTag = (SoapObject) respTipos.getProperty(x);
                                        codigo = soTag.getPropertyAsString("codigo");
                                        nombre = soTag.getPropertyAsString(propName2);
                                        dtoTipo = new DataSourceDto(codigo, nombre, null);
                                        dtoTipos.add(dtoTipo);
                                    }
                                }
                            }
                        }
                    }

                    spinnerDto.setEstado(dtoEstado);
                    spinnerDto.setColeccion(dtoTipos);
                }catch (Exception ex){
                    spinnerDto.setEstado(new DataSourceDto("9999",ex.getMessage(), null));
                }
            }
            else
            {
                spinnerDto.setEstado(new DataSourceDto("9999","El servicio no arrojo respuesta", null));
            }
        }
        else {
            spinnerDto.setEstado(new DataSourceDto("9999", TriggerException, null));

        }
        return spinnerDto;
    }


    public List<ReplenishmentWareResult> SimularReposicionService(){

        ResidueExternDetail residueExternDetail;
        ReplenishmentWareResult replenishmentWareResult;

        List<ResidueExternDetail> residueExternDetailList = new ArrayList<ResidueExternDetail>();
        residueExternDetail = new ResidueExternDetail("Shoes CK 10","Red", 4);
        residueExternDetailList.add(residueExternDetail);
        residueExternDetail = new ResidueExternDetail("Shirt CK 12","Blue", 10);
        residueExternDetailList.add(residueExternDetail);
        residueExternDetail = new ResidueExternDetail("Panty CK 5","Black", 8);
        residueExternDetailList.add(residueExternDetail);

        List<ReplenishmentWareResult> results = new ArrayList<>();
        replenishmentWareResult = new ReplenishmentWareResult(1, "Zapatos Mocacines", "541875", "15.........","30","52","1500000", residueExternDetailList);
        results.add(replenishmentWareResult);
        replenishmentWareResult = new ReplenishmentWareResult(1, "Zapatos Bruno Paqar", "154782", "17.......","30","52","7047", residueExternDetailList);
        results.add(replenishmentWareResult);
        replenishmentWareResult = new ReplenishmentWareResult(1, "Calvin Klein Boxer", "154782", "17.......","30","52","7047", residueExternDetailList);
        results.add(replenishmentWareResult);
        replenishmentWareResult = new ReplenishmentWareResult(1, "Tommy Lingar MX", "154782", "17.......","30","52","7047", residueExternDetailList);
        results.add(replenishmentWareResult);


        return  results;

    }


    //Guia de despacho and Envio de mercaderia

    public EGDetailResponse EPCHomologacionService(ArrayList<String> ListEpc)
    {
        //String SOAP_ACTION = "WebSithaction/AWSRFIDETIQUETASLEIDAS.Execute";
        String SOAP_ACTION = "WebSithaction/AWSRFIDEPCHOMOLOGACION.Execute";
        String METHOD_NAME = "WsRfidEpcHomologacion.Execute";
        /*String NAMESPACE = "http://tempuri.org/";*/
        String NAMESPACE = "WebSith";
        String URL =  paramLectorRfid_.getEndpoint()+"/aWsRfidEpcHomologacion.aspx";

        SoapObject SOepc ;
        SoapObject SOtagEPC = new SoapObject(NAMESPACE, "tag_read");
        SoapObject SOTagRequest = new SoapObject(NAMESPACE, "tag_request");
        SOTagRequest.addProperty("dispositivoId", "01");
        for(String epc: ListEpc) {

            SOepc = new SoapObject(NAMESPACE, "tag_epc");
            SOepc.addProperty("epc",epc);

            SOtagEPC.addProperty("etiqueta",SOepc);

        }
        SOTagRequest.addProperty("etiquetas",SOtagEPC );


        SoapObject SOPrincipal = new SoapObject(NAMESPACE, METHOD_NAME);
        SOPrincipal.addProperty("Sdtrfidetiquetasrequest",SOTagRequest);

        SoapObject resultRequestSOAP = CallService(SOPrincipal,SOAP_ACTION,URL,false, true);


        EGDetailResponse egDetailResponse = new EGDetailResponse();

        if(TriggerException == null){
            DataSourceDto geEstado = null;
            //egDetailResponse = new EGDetailResponse();
            EGTagsResponseItem egTagsResponseItem = null;
            List<EGTagsResponseItem> egTagsResponseItemList = new ArrayList<EGTagsResponseItem>();

            //SoapObject TagRespItem = null;

            String [] ItemValue =  new String[8];

            List<String> _tagNoRead;
            List<String> _tagRead;

            try{

                SoapPrimitive spEstado =(SoapPrimitive) ((SoapObject)resultRequestSOAP.getProperty("estado")).getProperty("codigo");
                SoapPrimitive spMensaje =(SoapPrimitive) ((SoapObject)resultRequestSOAP.getProperty("estado")).getProperty("mensaje");
                geEstado = new DataSourceDto(spEstado.getValue().toString(),spMensaje.getValue().toString(),null);

                if(spEstado.getValue().toString().equals("00")){

                    SoapObject soTags = ((SoapObject) ((SoapObject)resultRequestSOAP.getProperty("data")).getProperty("items"));
                    for (int x=0; x<soTags.getPropertyCount();x++)
                    {
                        SoapObject TagRespItem = (SoapObject) soTags.getProperty(x);
                        //TagRespItem = (SoapObject) soTag.getProperty("SdtRfidEtiquetasResponse.SdtRfidEtiquetasResponseItem");
                        ItemValue[0] = TagRespItem.getPropertyAsString("itemCodigo");
                        ItemValue[1] = TagRespItem.getPropertyAsString("itemGrupo1");
                        ItemValue[2] = TagRespItem.getPropertyAsString("itemGrupo2");
                        ItemValue[3] = TagRespItem.getPropertyAsString("itemGrupo3");
                        ItemValue[4] = TagRespItem.getPropertyAsString("itemGrupo4");
                        ItemValue[5] = TagRespItem.getPropertyAsString("itemGrupo5");
                        ItemValue[6] = TagRespItem.getPropertyAsString("CantidadLeidos");
                        ItemValue[7] = TagRespItem.getPropertyAsString("CantidadNoLeidos");

                        _tagNoRead = new ArrayList<String>();
                        _tagRead = new ArrayList<String>();

                        SoapObject soLectura = null;

                        if(TagRespItem.hasProperty("data_leido")){

                            soLectura = (SoapObject) TagRespItem.getProperty("data_leido");

                            if(soLectura.hasProperty("EPCs")){
                                SoapObject soLeidosEPCs = (SoapObject) soLectura.getProperty("EPCs");

                                for(int y=0; y<soLeidosEPCs.getPropertyCount();y++){
                                    String epc = soLeidosEPCs.getPropertyAsString(y);
                                    // _tagNoRead.add(epc.getPropertyAsString("epc"));
                                    _tagRead.add(epc);

                                }
                            }
                        }

                        if(TagRespItem.hasProperty("data_no_leido")){

                            soLectura = (SoapObject) TagRespItem.getProperty("data_no_leido");

                            if(soLectura.hasProperty("EPCs")){
                                SoapObject soNoLeidosEPCs = (SoapObject) soLectura.getProperty("EPCs");

                                for(int y=0; y<soNoLeidosEPCs.getPropertyCount();y++){
                                    String epc = soNoLeidosEPCs.getPropertyAsString(y);
                                    // _tagNoRead.add(epc.getPropertyAsString("epc"));
                                    _tagNoRead.add(epc);

                                }
                            }
                        }



                        egTagsResponseItem = new EGTagsResponseItem(ItemValue[0],ItemValue[1],ItemValue[2],ItemValue[3],ItemValue[4],ItemValue[5], Integer.valueOf(ItemValue[6]) ,Integer.valueOf(ItemValue[7]),_tagRead,_tagNoRead);
                        egTagsResponseItemList.add(egTagsResponseItem);
                    }

                    egDetailResponse.setStatus(geEstado);
                    egDetailResponse.setItems(egTagsResponseItemList);

                }

            }catch (Exception ex){
                egDetailResponse.setStatus(new DataSourceDto("9999","Ocurrio una excepcion:"+ ex.getMessage(), null));
            }
        }
        else {
            egDetailResponse.setStatus(new DataSourceDto("9999", TriggerException, null));
        }
        return  egDetailResponse;
    }

    public  DataSourceDto GuiaDespachoProcesarService(List<EGTagsResponseItem> items, String lGuiaEntrada, String lBodega)
    {
        /*String SOAP_ACTION = "WebSithaction/AWSRFIDGUIADESPACHOPROCESAR.Execute";
        String METHOD_NAME = "WsRfidGuiaDespachoProcesar.Execute";
        String NAMESPACE = "WebSith";
        String URL = "http://info.thgye.com.ec/awsrfidguiadespachoprocesar.aspx";*/


        SoapObject soNivel1 = new SoapObject(NAMESPACE_, METHOD_NAME_);
        SoapObject soNIvel5 = null;
        SoapObject soNivel4;
        SoapObject soNivel3 ;

        SoapObject soNivel2 = new SoapObject(NAMESPACE_, "nivel2");

        soNivel1.addProperty("Guiaentrada", lGuiaEntrada);
        soNivel1.addProperty("Bodcodigo", lBodega);
        soNivel1.addProperty("Usrcodigo", gUsuario);

        for(EGTagsResponseItem item: items) {

            soNivel4 = new SoapObject(NAMESPACE_, "data_leido");
            soNIvel5 = new SoapObject(NAMESPACE_,null);
            for (String epc : item.getDataLeido()) {
                soNIvel5.addProperty("item", epc);

            }
            soNivel4.addProperty("EPCs", soNIvel5);

            soNivel3 = new SoapObject(NAMESPACE_,"nivel3");

            soNivel3.addProperty("itemCodigo",item.itemCodigo);
            soNivel3.addProperty("itemGrupo1",item.itemGrupo1);
            soNivel3.addProperty("itemGrupo2",item.itemGrupo2);
            soNivel3.addProperty("itemGrupo3",item.itemGrupo3);
            soNivel3.addProperty("itemGrupo4",item.itemGrupo4);
            soNivel3.addProperty("itemGrupo5",item.itemGrupo5);
            soNivel3.addProperty("CantidadLeidos",item.getCantidadLeidos());
            soNivel3.addProperty("CantidadNoLeidos",item.getCantidadNoLeidos());

            soNivel3.addProperty("data_leido",soNivel4);

            soNivel2.addProperty("SdtRfidEtiquetasResponse.SdtRfidEtiquetasResponseItem", soNivel3);
        }
        soNivel1.addProperty("Sdtrfidetiquetasresponse", soNivel2);
        soNivel1.addProperty("Discodigo", paramLectorRfid_.getDispositivoid());

        SoapObject resultRequestSOAP = CallService(soNivel1,SOAP_ACTION_,paramLectorRfid_.getEndpoint()+URL_,true, false);

        DataSourceDto geEstado = null;

        if(TriggerException == null)
        {
            String lSecuenciaGuia = null;
            try {
                SoapPrimitive spEstado =(SoapPrimitive) ((SoapObject)resultRequestSOAP.getProperty("estado")).getProperty("codigo");
                SoapPrimitive spMensaje =(SoapPrimitive) ((SoapObject)resultRequestSOAP.getProperty("estado")).getProperty("mensaje");
                if(resultRequestSOAP.hasProperty("data"))
                {
                    SoapObject soData = (SoapObject) resultRequestSOAP.getProperty("data");
                    if(soData.hasProperty("secuenciaGuia"))
                    {
                        lSecuenciaGuia = soData.getPropertyAsString("secuenciaGuia");
                    }
                }
                geEstado = new DataSourceDto(spEstado.getValue().toString(),spMensaje.getValue().toString(),lSecuenciaGuia);
            }catch (Exception ex){
                geEstado = new DataSourceDto("9999", ex.getMessage(), null);
            }


        }
        else {
            geEstado = new DataSourceDto("9999", TriggerException, null);
        }


        return geEstado;
    }


    public DataSourceDtoEx WSEnvioMercaderiaProcesarService(Context mContext, List<EGTagsResponseItem> items, String pMotivo, String pNota)
    {
       /* String SOAP_ACTION = "http://tempuri.org/action/AWSRFIDENVIOMERCADERIAPROCESAR.Execute";
        String METHOD_NAME = "WsRfidEnvioMercaderiaProcesar.Execute";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://info.thgye.com.ec/awsrfidenviomercaderiaprocesar.aspx";*/



        /*String SOAP_ACTION = "WebSithaction/AWSRFIDENVIOMERCADERIAPROCESAR.Execute";
        String METHOD_NAME = "WsRfidEnvioMercaderiaProcesar.Execute";
        String NAMESPACE = "WebSith";
        String URL = paramLectorRfid_.getHostPort() +"/awsrfidenviomercaderiaprocesar.aspx";*/

        DataSourceDtoEx dtoEx = null;

        WSException wsException = new WSException();
        wsException.setExceptionExist(false);

        SoapObject soNivel1 = new SoapObject(NAMESPACE_, METHOD_NAME_);

        paramRfidIteration = new ParamRfidIteration(mContext);

        ParamLectorRfid paramLectorRfid =  paramRfidIteration.ConsultarParametros();
        String codBodega = paramLectorRfid.getCodbodega();

        soNivel1.addProperty("Bodcodigo", codBodega);
        soNivel1.addProperty("Motcodigo", pMotivo);
        soNivel1.addProperty("Trnnota", pNota);

        SoapObject resultRequestSOAP = ArmarRequest(mContext, soNivel1, SOAP_ACTION_,paramLectorRfid_.getEndpoint()+URL_,NAMESPACE_,items,false);

        DataSourceDto geEstado = null;
        String lSecuenciaGuia = null;
        try
        {
            SoapPrimitive spEstado =(SoapPrimitive) ((SoapObject)resultRequestSOAP.getProperty("estado")).getProperty("codigo");
            SoapPrimitive spMensaje =(SoapPrimitive) ((SoapObject)resultRequestSOAP.getProperty("estado")).getProperty("mensaje");
            //SoapPrimitive spsecuenciaGuia =(SoapPrimitive) ((SoapObject)resultRequestSOAP.getProperty("data")).getProperty("secuenciaGuia");


            if(resultRequestSOAP.hasProperty("data")){
                SoapObject soData = (SoapObject) resultRequestSOAP.getProperty("data");
                if(soData.hasProperty("contenidoQR")){
                    lSecuenciaGuia = soData.getPropertyAsString("contenidoQR");
                }
            }
            geEstado = new DataSourceDto(spEstado.getValue().toString(),spMensaje.getValue().toString(),lSecuenciaGuia);

        } catch (Exception ex){
            wsException.setExceptionExist(true);
            wsException.setExceptionMessage("Error en el  servicio:: "+ex.getMessage());
        }
        dtoEx = new DataSourceDtoEx();
        dtoEx.setHandlerException(wsException);
        dtoEx.setInformationDto(geEstado);

        return dtoEx;
    }

    private SoapObject ArmarRequest(Context mContext,SoapObject soNivel1,String SOAP_ACTION, String URL, String NAMESPACE, List<EGTagsResponseItem> items, boolean requiereDataNoLeida){

        SoapObject soNIvel5 = null;
        SoapObject soNivel4 = null;
        SoapObject soNivel3 ;

        SoapObject soNivel2 = new SoapObject(NAMESPACE, "nivel2");

        paramRfidIteration = new ParamRfidIteration(mContext);

        ParamLectorRfid paramLectorRfid =  paramRfidIteration.ConsultarParametros();
        String codBodega = paramLectorRfid.getCodbodega();


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

        SoapObject resultRequestSOAP = CallService(soNivel1,SOAP_ACTION,URL,true, false);

        return resultRequestSOAP;
    }

    // WS RECEPCION DE MERCADERIA
    public ReceiveWareDetail WSRecepcionMercaderiaDetail(Context mContext, String doc_origen, boolean isNotExistQr, String[] dataNoQr)
    {
        ReceiveWareDetail receiveWareDetail = new ReceiveWareDetail();
        EGTagsResponseItem responseItem;
        List<EGTagsResponseItem> responseItemList = null;

        /*String SOAP_ACTION = "WebSithaction/AWSRFIDRECEPCIONDETALLE.Execute";
        String METHOD_NAME = "WsRfidRecepcionDetalle.Execute";
        String NAMESPACE = "WebSith";
        String URL = "http://info.thgye.com.ec/awsrfidrecepciondetalle.aspx";*/


        SoapObject soRequest = new SoapObject(NAMESPACE_, METHOD_NAME_);

        paramRfidIteration = new ParamRfidIteration(mContext);

        ParamLectorRfid paramLectorRfid =  paramRfidIteration.ConsultarParametros();
        String codBodega = paramLectorRfid.getCodbodega();

        if(isNotExistQr && dataNoQr != null )
        {
            soRequest.addProperty("Bodcodigo", codBodega);
            soRequest.addProperty("Tiporigen", dataNoQr[0]);
            soRequest.addProperty("Bodorigen", dataNoQr[1]);
            soRequest.addProperty("Aniorigen", dataNoQr[2]);
            soRequest.addProperty("Numorigen", dataNoQr[3]);
        }
        else
        {
            soRequest.addProperty("Bodcodigo", codBodega);
            soRequest.addProperty("Docorigen", doc_origen);
        }



        SoapObject resultRequestSOAP = CallService(soRequest,SOAP_ACTION_,paramLectorRfid_.getEndpoint()+URL_,true,false);

        DataSourceDto geEstado = null;
        String lDocOrigen = null, lDocDestino = null, lMotivo = null;
        int CantidadTotal = 0;
        if(TriggerException == null)
        {
            try
            {
                if(resultRequestSOAP != null){
                    if(resultRequestSOAP.hasProperty("estado")){
                        SoapObject soEstado = (SoapObject) resultRequestSOAP.getProperty("estado");
                        geEstado = new DataSourceDto(soEstado.getPropertyAsString("codigo"), soEstado.getPropertyAsString("mensaje"), null);
                    }
                    else {
                        geEstado = new DataSourceDto("00", "Exitoso", null);
                    }
                    if(resultRequestSOAP.hasProperty("data"))
                    {
                        SoapObject soData = (SoapObject) resultRequestSOAP.getProperty("data");
                        if(soData.hasProperty("docOrigen")){
                            lDocOrigen = soData.getPropertyAsString("docOrigen");
                        }
                        if(soData.hasProperty("docDestino")){
                            lDocDestino = soData.getPropertyAsString("docDestino");
                        }
                        if(soData.hasProperty("motDescription")){
                            lMotivo = soData.getPropertyAsString("motDescription");
                        }

                        if(geEstado.getCodigo().equals("00")){
                            if(soData.hasProperty("cantidadTotal")){
                                CantidadTotal = Integer.parseInt(soData.getPropertyAsString("cantidadTotal")) ;
                            }
                            if(soData.hasProperty("itemsCantidades")){
                                SoapObject SoDataItems = (SoapObject) soData.getProperty("itemsCantidades");

                                String [] ItemValue =  new String[8];
                                responseItemList = new ArrayList<EGTagsResponseItem>();

                                for (int x=0; x<SoDataItems.getPropertyCount();x++)
                                {
                                    SoapObject TagRespItem = (SoapObject) SoDataItems.getProperty(x);
                                    //TagRespItem = (SoapObject) soTag.getProperty("SdtRfidEtiquetasResponse.SdtRfidEtiquetasResponseItem");
                                    ItemValue[0] = TagRespItem.getPropertyAsString("itemCodigo");
                                    ItemValue[1] = TagRespItem.getPropertyAsString("itemGrupo1");
                                    ItemValue[2] = TagRespItem.getPropertyAsString("itemGrupo2");
                                    ItemValue[3] = TagRespItem.getPropertyAsString("itemGrupo3");
                                    ItemValue[4] = TagRespItem.getPropertyAsString("itemGrupo4");
                                    ItemValue[5] = TagRespItem.getPropertyAsString("itemGrupo5");
                                    ItemValue[6] = TagRespItem.getPropertyAsString("CantidadLeidos");
                                    ItemValue[7] = TagRespItem.getPropertyAsString("CantidadNoLeidos");

                                    responseItem = new EGTagsResponseItem(ItemValue[0],ItemValue[1],ItemValue[2],ItemValue[3],ItemValue[4],ItemValue[5], Integer.valueOf(ItemValue[6]) ,Integer.valueOf(ItemValue[7]),null,null);
                                    responseItemList.add(responseItem);
                                }
                            }

                            receiveWareDetail.setCantidadTotal(CantidadTotal);
                            receiveWareDetail.setDetalle(responseItemList);
                        }



                        receiveWareDetail.setDocOrigen(lDocOrigen);
                        receiveWareDetail.setDocDestino(lDocDestino);
                        receiveWareDetail.setMotDescription(lMotivo);
                        /*receiveWareDetail.setCantidadTotal(CantidadTotal);
                        receiveWareDetail.setDetalle(responseItemList);*/

                    }

                }
                else {
                    geEstado = new DataSourceDto("-9999", "El servicio no arrojo, ninguna Respuesta", null);
                }
                receiveWareDetail.setEstado(geEstado);

            } catch (Exception ex){
                receiveWareDetail.setEstado(new DataSourceDto("9999" , ex.getMessage(), null));
            }
        }
        else {
            receiveWareDetail.setEstado(new DataSourceDto("9999", TriggerException, null));
        }

        return receiveWareDetail;
    }

    public EGDetailResponse WSRecepcionMercaderiaCompara(String Docorigen, List<String> epcs)
    {
        EGDetailResponse detailResponse = null;

        /*String SOAP_ACTION = "WebSithaction/AWSRFIDRECEPCIONCOMPARA.Execute";
        String METHOD_NAME = "WsRfidRecepcionCompara.Execute";
        String NAMESPACE = "WebSith";
        String URL = host+"/awsrfidrecepcioncompara.aspx";
*/
        SoapObject soNivel1 = new SoapObject(NAMESPACE_, METHOD_NAME_);
        soNivel1.addProperty("Docorigen", Docorigen);

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
        soNivel1.addProperty("Sdtrfidetiquetasrequest", soNivel2);

        SoapObject resultRequestSOAP = CallService(soNivel1,SOAP_ACTION_,paramLectorRfid_.getEndpoint()+URL_,true,false);

        if(TriggerException == null){
            detailResponse = ResponseRWtoObject(resultRequestSOAP);
        }
        else {
            detailResponse = new EGDetailResponse();
            detailResponse.setStatus(new DataSourceDto("9999", TriggerException, null));
        }


        return  detailResponse;
    }
    private EGDetailResponse ResponseRWtoObject(SoapObject response)
    {
        EGDetailResponse detailResponse =new EGDetailResponse();
        //DataSourceDto dtoEstado = null;
        if(response.hasProperty("estado"))
        {
            String codigo = null, mensaje = null;
            SoapObject soEstado = (SoapObject) response.getProperty("estado");
            if(soEstado.hasProperty("codigo"))
            {
                codigo = soEstado.getPropertyAsString("codigo");
            }
            if(soEstado.hasProperty("mensaje"))
            {
                mensaje = soEstado.getPropertyAsString("mensaje");
            }
            /*dtoEstado = new DataSourceDto(codigo,mensaje,null);*/
            detailResponse.setStatus(new DataSourceDto(codigo,mensaje,null));

        }
        if(response.hasProperty("data")){
            SoapObject soData = (SoapObject) response.getProperty("data");
            if(soData.hasProperty("items")){
                SoapObject soItems = (SoapObject) soData.getProperty("items");

                if(soItems.hasProperty("SdtRfidEtiquetasResponse.SdtRfidEtiquetasResponseItem"))
                {
                    EGTagsResponseItem responseItem;
                    List<String> _tagNoRead, _tagRead;
                    List<EGTagsResponseItem> responseItemList = new ArrayList<EGTagsResponseItem>();
                    //SoapObject SoDataItems = (SoapObject) response.getProperty("SdtRfidEtiquetasResponse.SdtRfidEtiquetasResponseItem");

                    String [] ItemValue =  new String[8];
                    responseItemList = new ArrayList<EGTagsResponseItem>();

                    for (int x=0; x<soItems.getPropertyCount();x++)
                    {
                        SoapObject TagRespItem = (SoapObject) soItems.getProperty(x);

                        ItemValue[0] = TagRespItem.getPropertyAsString("itemCodigo");
                        if(ItemValue[0] != null && !ItemValue[0].equalsIgnoreCase("anyType{}"))
                        {
                            ItemValue[0] = TagRespItem.getPropertyAsString("itemCodigo");
                            ItemValue[1] = TagRespItem.getPropertyAsString("itemGrupo1");
                            ItemValue[2] = TagRespItem.getPropertyAsString("itemGrupo2");
                            ItemValue[3] = TagRespItem.getPropertyAsString("itemGrupo3");
                            ItemValue[4] = TagRespItem.getPropertyAsString("itemGrupo4");
                            ItemValue[5] = TagRespItem.getPropertyAsString("itemGrupo5");
                            ItemValue[6] = TagRespItem.getPropertyAsString("CantidadLeidos");
                            ItemValue[7] = TagRespItem.getPropertyAsString("CantidadNoLeidos");
                            _tagNoRead = ResponseTolistEpc(TagRespItem, "data_no_leido");
                            _tagRead = ResponseTolistEpc(TagRespItem, "data_leido");



                            responseItem = new EGTagsResponseItem(ItemValue[0],ItemValue[1],ItemValue[2],ItemValue[3],ItemValue[4],ItemValue[5], Integer.valueOf(ItemValue[6]) ,Integer.valueOf(ItemValue[7]),_tagRead,_tagNoRead);
                            responseItemList.add(responseItem);
                        }

                    }
                    detailResponse.setItems(responseItemList);
                }

            }

        }
        /*if(response.hasProperty("SdtRfidEtiquetasResponse.SdtRfidEtiquetasResponseItem"))
        {
            EGTagsResponseItem responseItem;
            List<String> _tagNoRead, _tagRead;
            List<EGTagsResponseItem> responseItemList = new ArrayList<EGTagsResponseItem>();
            //SoapObject SoDataItems = (SoapObject) response.getProperty("SdtRfidEtiquetasResponse.SdtRfidEtiquetasResponseItem");

            String [] ItemValue =  new String[8];
            responseItemList = new ArrayList<EGTagsResponseItem>();

            for (int x=0; x<response.getPropertyCount();x++)
            {
                SoapObject TagRespItem = (SoapObject) response.getProperty(x);

                ItemValue[0] = TagRespItem.getPropertyAsString("itemCodigo");
                if(ItemValue[0] != null && !ItemValue[0].equalsIgnoreCase("anyType{}"))
                {
                    ItemValue[0] = TagRespItem.getPropertyAsString("itemCodigo");
                    ItemValue[1] = TagRespItem.getPropertyAsString("itemGrupo1");
                    ItemValue[2] = TagRespItem.getPropertyAsString("itemGrupo2");
                    ItemValue[3] = TagRespItem.getPropertyAsString("itemGrupo3");
                    ItemValue[4] = TagRespItem.getPropertyAsString("itemGrupo4");
                    ItemValue[5] = TagRespItem.getPropertyAsString("itemGrupo5");
                    ItemValue[6] = TagRespItem.getPropertyAsString("CantidadLeidos");
                    ItemValue[7] = TagRespItem.getPropertyAsString("CantidadNoLeidos");
                    _tagNoRead = ResponseTolistEpc(TagRespItem, "data_no_leido");
                    _tagRead = ResponseTolistEpc(TagRespItem, "data_leido");



                    responseItem = new EGTagsResponseItem(ItemValue[0],ItemValue[1],ItemValue[2],ItemValue[3],ItemValue[4],ItemValue[5], Integer.valueOf(ItemValue[6]) ,Integer.valueOf(ItemValue[7]),_tagRead,_tagNoRead);
                    responseItemList.add(responseItem);
                }

            }
            detailResponse.setItems(responseItemList);
        }*/
        return detailResponse;
    }


    //
    private List<String> ResponseTolistEpc(SoapObject TagRespItem, String tagName)
    {
        SoapObject soLectura;
        List<String> tags = null;

        if(TagRespItem.hasProperty(tagName)){

            soLectura = (SoapObject) TagRespItem.getProperty(tagName);

            if(soLectura.hasProperty("EPCs")){

                try {

                    SoapPrimitive spLeidosEPCs = (SoapPrimitive) soLectura.getProperty("EPCs");

                    if(!spLeidosEPCs.getValue().toString().equals("\n" + "\t\t\t\t\t\t\t\t")){

                        SoapObject soLeidosEPCs = (SoapObject) soLectura.getProperty("EPCs");
                        if (soLeidosEPCs.getPropertyCount() > 0) {
                            tags = new ArrayList<String>();
                            for(int y=0; y<soLeidosEPCs.getPropertyCount();y++){
                                String epc = soLeidosEPCs.getPropertyAsString(y);
                                tags.add(epc);

                            }
                        }



                    }
                    /*SoapObject soLeidosEPCs = (SoapObject) soLectura.getProperty("EPCs");


                    if (soLeidosEPCs.getPropertyCount() > 0) {
                        tags = new ArrayList<String>();
                    }
                    for (int y = 0; y < soLeidosEPCs.getPropertyCount(); y++) {
                        String epc = soLeidosEPCs.getPropertyAsString(y);
                        tags.add(epc);

                    }*/
                }
                catch (Exception ex){
                    SoapObject soLeidosEPCs = (SoapObject) soLectura.getProperty("EPCs");
                    if (soLeidosEPCs.getPropertyCount() > 0) {
                        tags = new ArrayList<String>();
                        for(int y=0; y<soLeidosEPCs.getPropertyCount();y++){
                            String epc = soLeidosEPCs.getPropertyAsString(y);
                            tags.add(epc);

                        }
                    }

                }
            }
        }
        return tags;
    }


    public   DataSourceDto WSRecepcionMercaderiaProcesar(String pDocOrigen, String pNota, List<EGTagsResponseItem> responseItems)
    {
        DataSourceDto dtoResponse = null;
        String codigo = null, mensaje = null;

        /*String SOAP_ACTION = "WebSithaction/AWSRFIDRECEPCIONPROCESAR.Execute";
        String METHOD_NAME = "WsRfidRecepcionProcesar.Execute";
        String NAMESPACE = "WebSith";
        String URL = host+"/awsrfidrecepcionprocesar.aspx";*/

        SoapObject soNivel1 = new SoapObject(NAMESPACE_, METHOD_NAME_);
        soNivel1.addProperty("Docorigen", pDocOrigen);
        soNivel1.addProperty("Nota", pNota);

        SoapObject resultRequestSOAP =ArmarRequest(globalContext, soNivel1, SOAP_ACTION_, paramLectorRfid_.getEndpoint()+URL_, NAMESPACE_, responseItems,true);

        if(resultRequestSOAP.hasProperty("estado")){
            SoapObject soEstado = (SoapObject) resultRequestSOAP.getProperty("estado");
            if(soEstado.hasProperty("codigo")){
                codigo = soEstado.getPropertyAsString("codigo");
            }
            if(soEstado.hasProperty("mensaje")){
                mensaje = soEstado.getPropertyAsString("mensaje");
            }
            dtoResponse = new DataSourceDto(codigo, mensaje, null);
        }

        return dtoResponse;
    }

    // WS TOMA DE iNVENTARIO...

    public GenericSpinnerDto WSConteo(){
        //http://info.thgye.com.ec/awsrfidconteos.aspx

        DataSourceDto dtoEstado = null;
        DataSourceDto dtoData = null;
        List<DataSourceDto> dtoList = null;
        GenericSpinnerDto genericSpinnerDto = null;

        SoapObject soRequest = new SoapObject(NAMESPACE_, METHOD_NAME_);
        soRequest.addProperty("Bodcodigo",paramLectorRfid_.codbodega);
        SoapObject resultRequestSOAP = CallService(soRequest,SOAP_ACTION_,paramLectorRfid_.getEndpoint()+URL_,true,false);

        genericSpinnerDto = new GenericSpinnerDto();
        if(TriggerException == null)
        {
            if(resultRequestSOAP != null) {

                try {

                    if (resultRequestSOAP.hasProperty("estado")) {
                        String codigo = null, mensaje = null;
                        SoapObject soEstado = (SoapObject) resultRequestSOAP.getProperty("estado");
                        if (soEstado.hasProperty("codigo")) {
                            codigo = soEstado.getPropertyAsString("codigo");
                        }
                        if (soEstado.hasProperty("mensaje")) {
                            mensaje = soEstado.getPropertyAsString("mensaje");
                        }
                        dtoEstado = new DataSourceDto(codigo, mensaje, null);
                    }
                    if (resultRequestSOAP.hasProperty("data")) {
                        SoapObject soData = (SoapObject) resultRequestSOAP.getProperty("data");
                        dtoList = new ArrayList<DataSourceDto>();
                        dtoList.add(new DataSourceDto("0", "- Seleccione -", null));

                        if (soData.hasProperty("conteos")) {
                            try {
                                SoapObject soConteos = (SoapObject) soData.getProperty("conteos");
                                for (int x = 0; x < soConteos.getPropertyCount(); x++) {
                                    dtoData = new DataSourceDto(String.valueOf(x + 1), soConteos.getPropertyAsString(x), null);
                                    dtoList.add(dtoData);
                                }
                            }catch (ClassCastException ex){
                                dtoEstado = new DataSourceDto("9998","No Existen Conteos", ex.getMessage() );
                            }
                        }

                    }
                    genericSpinnerDto.setEstado(dtoEstado);
                    genericSpinnerDto.setColeccion(dtoList);
                }catch (Exception ex){
                    genericSpinnerDto.setEstado(new DataSourceDto("9999", ex.getMessage(), null ));
                }

            }
            else {
                genericSpinnerDto.setEstado(new DataSourceDto("9999", "El Servicio no dio Respuesta", null ));
            }



        }
        else {
            genericSpinnerDto.setEstado(new DataSourceDto("9999", TriggerException, null));
        }



        return genericSpinnerDto;

    }

    public InventoryControl WSInventoryControlCountDetail(String pSecconteo){


        /*String SOAP_ACTION = "WebSithaction/AWSRFIDCONTEODETALLE.Execute";
        String METHOD_NAME = "WsRfidConteoDetalle.Execute";
        String NAMESPACE = "WebSith";
        String URL = paramLectorRfid_.getHostPort()+"/awsrfidconteodetalle.aspx";*/

        SoapObject soRequest = new SoapObject(NAMESPACE_, METHOD_NAME_);
        soRequest.addProperty("Bodcodigo",paramLectorRfid_.codbodega);
        soRequest.addProperty("Secconteo",pSecconteo);
        //SoapObject resultRequestSOAP = CallService(soRequest,SOAP_ACTION,URL,true,false);


        return responsetoObjectIC(CallService(soRequest,SOAP_ACTION_,paramLectorRfid_.getEndpoint()+URL_,true,false));

    }

    private InventoryControl responsetoObjectIC(SoapObject response)
    {
        DataSourceDto dtoEstado = null;
        ICSeccion seccion = null;
        List<ICSeccion> icSeccionList = null;

        InventoryControl inventoryControl = null;
        if(response.hasProperty("estado")){
            inventoryControl = new InventoryControl();
            SoapObject soEstado = (SoapObject) response.getProperty("estado");
            if(soEstado.hasProperty("codigo") && soEstado.hasProperty("mensaje")){
                dtoEstado = new DataSourceDto(soEstado.getPropertyAsString("codigo"),soEstado.getPropertyAsString("mensaje"),null);
            }

        }
        if(response.hasProperty("data")){
            if(inventoryControl == null)
            {
                inventoryControl = new InventoryControl();
            }
            SoapObject soData = (SoapObject) response.getProperty("data");

            if(soData.hasProperty("totalLeido")){
                inventoryControl.setTotalLeido(Integer.valueOf(soData.getPropertyAsString("totalLeido").equals("") ? "0" : soData.getPropertyAsString("totalLeido")) );
            }
            if(soData.hasProperty("totalEsperado")){
                inventoryControl.setTotalEsperado(Integer.valueOf(soData.getPropertyAsString("totalEsperado").equals("") ? "0" : soData.getPropertyAsString("totalEsperado")) );
            }

            if(soData.hasProperty("secciones")){
                SoapObject soSecciones = (SoapObject) soData.getProperty("secciones");
                if(soSecciones.getPropertyCount() > 0){
                    icSeccionList = new ArrayList<ICSeccion>();
                    for(int x=0; x<soSecciones.getPropertyCount();x++)
                    {
                        SoapObject soSeccion = (SoapObject) soSecciones.getProperty(x);
                        seccion = new ICSeccion();
                        seccion.setNombre(soSeccion.getPropertyAsString("nombre"));
                        seccion.setLeido(Integer.valueOf(soSeccion.getPropertyAsString("leido")));
                        seccion.setEsperado(Integer.valueOf(soSeccion.getPropertyAsString("esperado")));
                        seccion.setPorcentaje(Double.valueOf(soSeccion.getPropertyAsString("porcentaje")));

                        icSeccionList.add(seccion);
                    }
                }


            }

        }
        inventoryControl.setEstado(dtoEstado);
        inventoryControl.setSecciones(icSeccionList);
        return inventoryControl;
    }

    public GenericSpinnerDto WSUbicacion()
    {
        /*String SOAP_ACTION = "WebSithaction/AWSRFIDUBICACION.Execute";
        String METHOD_NAME = "WsRfidUbicacion.Execute";
        String NAMESPACE = "WebSith";
        String URL = paramLectorRfid_.getHostPort()+"/awsrfidubicacion.aspx";*/

        SoapObject soRequest = new SoapObject(NAMESPACE_, METHOD_NAME_);

        return ResponseToGenericSpinnerDto(CallService(soRequest,SOAP_ACTION_,paramLectorRfid_.getEndpoint()+URL_,true,false),"ubicaciones","descripcion",true);
    }

    public GenericSpinnerDto WSSeccion()
    {
        SoapObject soRequest = new SoapObject(NAMESPACE_, METHOD_NAME_);
        soRequest.addProperty("Bodcodigo",paramLectorRfid_.getCodbodega());

        return ResponseToGenericSpinnerDto(CallService(soRequest,SOAP_ACTION_,paramLectorRfid_.getEndpoint()+URL_,true,false),"secciones","descripcion",true);
    }

    // tomaInventarioProcesar -> se usaba para homologar la etiquetas leidas antes de invocar al awsrfidtomaprocesar
    /*public DataSourceDtoEx WSTomaInventarioProcesar(ArrayList<String> ListEpc, String pConteo, String pUbicacion)
    {
        EGDetailResponse detailResponse =  EPCHomologacionService(ListEpc);
        DataSourceDtoEx dtoEx = new DataSourceDtoEx();

        if(detailResponse != null ){
            WSException wsException = new WSException();

            if(detailResponse.getStatus() != null && detailResponse.getStatus().equals("9999"))
            {
                wsException.setExceptionMessage(detailResponse.getStatus().getDescripcion());
                wsException.setExceptionExist(true);
                dtoEx.setHandlerException(wsException);
            }
            else {

                DataSourceDto dto = null;

                SoapObject soRequest = new SoapObject(NAMESPACE_, METHOD_NAME_);
                soRequest.addProperty("Bodcodigo", paramLectorRfid_.getCodbodega());
                soRequest.addProperty("Secconteo", pConteo);
                soRequest.addProperty("Ubicodigo", pUbicacion);

                soRequest.addProperty("Dspid",paramLectorRfid_.getDispositivoid() );
                //soRequest.addProperty("Usrcodigo", gUsuario);

                wsException.setExceptionExist(false);
                try {
                    SoapObject resultRequestSOAP =ArmarRequest(globalContext, soRequest, SOAP_ACTION_, paramLectorRfid_.getEndpoint()+URL_, NAMESPACE_, detailResponse.getItems(),true);

                    if(TriggerException == null){
                        if(resultRequestSOAP.hasProperty("estado")){
                            SoapObject soEstado =(SoapObject) resultRequestSOAP.getProperty("estado");
                            if(soEstado.hasProperty("codigo") && soEstado.hasProperty("mensaje"))
                            {
                                dto = new DataSourceDto(soEstado.getPropertyAsString("codigo"),soEstado.getPropertyAsString("mensaje"),null );
                                dtoEx.setInformationDto(dto);
                            }
                        }
                    }
                    else {
                        wsException.setExceptionMessage(TriggerException);
                        wsException.setExceptionExist(true);
                        dtoEx.setHandlerException(wsException);
                    }

                }
                catch (Exception ex){
                    wsException.setExceptionExist(true);
                    wsException.setExceptionMessage(ex.getMessage());
                }
                dtoEx.setHandlerException(wsException);
            }


        }


        return dtoEx;
    }*/


    public DataSourceDto WSTomaInventarioProcesar2(ArrayList<String> ListEpc, String pConteo, String pUbicacion)
    {
        return   ResponseToDataSourceDto(CallService(GenerarRequestInvProcesar(ListEpc, pConteo,pUbicacion ),SOAP_ACTION_,paramLectorRfid_.getEndpoint()+URL_,true, false));

    }

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
    public Garment WSInventoryPerStore(String coditem)
    {
        Garment garment = null;

        /*String SOAP_ACTION = "WebSithaction/AWSRFIDITEMINFO.Execute";
        String METHOD_NAME = "WsRfidItemInfo.Execute";
        String NAMESPACE = "WebSith";
        String URL = paramLectorRfid_.getHostPort()+"/awsrfiditeminfo.aspx";*/

        SoapObject soRequest = new SoapObject(NAMESPACE_, METHOD_NAME_);
        soRequest.addProperty("Itmcodigo", coditem);

        SoapObject response = CallService(soRequest,SOAP_ACTION_,paramLectorRfid_.getEndpoint()+URL_,true,false);

        if(TriggerException == null){
            garment = ResponseToGarment(response);
        }
        else {
            garment = new Garment();
            garment.setEstado(new DataSourceDto("9999", TriggerException, null));
        }

        return garment;
    }

    private Garment ResponseToGarment(SoapObject response)
    {
        Garment garment =  new Garment();
        List<String> listImagenes = null;

        if(response.hasProperty("estado")){
            SoapObject soEstado = (SoapObject) response.getProperty("estado");
            if(soEstado.hasProperty("codigo") && soEstado.hasProperty("mensaje")){
                garment.setEstado(new DataSourceDto(soEstado.getPropertyAsString("codigo"),soEstado.getPropertyAsString("mensaje"),null));
            }
        }
        if(response.hasProperty("data")){

            SoapObject soData = (SoapObject) response.getProperty("data");
            //itmCodigo estilo linea producto temporada precioIva precioSinIva listImagenes

            if(soData.hasProperty("itmCodigo")){
                garment.setItmCodigo(soData.getPropertyAsString("itmCodigo"));
                garment.setEstado(new DataSourceDto("00", "Exitoso", null));
            }
            if(soData.hasProperty("estilo")){
                garment.setEstilo(soData.getPropertyAsString("estilo"));
            }
            if(soData.hasProperty("linea")){
                garment.setLinea(soData.getPropertyAsString("linea"));
            }
            if(soData.hasProperty("producto")){
                garment.setProducto(soData.getPropertyAsString("producto"));
            }
            if(soData.hasProperty("temporada")){
                garment.setTemporada(soData.getPropertyAsString("temporada"));
            }
            if(soData.hasProperty("precioIva")){
                garment.setPrecioIva(Double.parseDouble(soData.getPropertyAsString("precioIva")));
            }
            if(soData.hasProperty("precioSinIva")){
                garment.setPrecioSinIva(Double.parseDouble(soData.getPropertyAsString("precioSinIva")));
            }
            if(soData.hasProperty("listImagenes")){
                SoapObject soListImag = (SoapObject) soData.getProperty("listImagenes");

                if(soListImag.getPropertyCount() > 0){
                    listImagenes = new ArrayList<String>();
                }
                for(int y=0; y<soListImag.getPropertyCount();y++){
                    String item = soListImag.getPropertyAsString(y);
                    listImagenes.add(item);

                }
                garment.setListImagenes(listImagenes);
            }
        }
        return garment;
    }

    public GarmentSaleObj WSInventoryPerStoreSale(String pItmcodigo)
    {

        /*String SOAP_ACTION = "WebSithaction/AWSRFIDITEMSALDOS.Execute";
        String METHOD_NAME = "WsRfidItemSaldos.Execute";
        String NAMESPACE = "WebSith";
        String URL = paramLectorRfid_.getHostPort()+"/awsrfiditemsaldos.aspx";*/

        TriggerException = null;
        GarmentSaleObj garmentSaleObj = new GarmentSaleObj();

        SoapObject soRequest = new SoapObject(NAMESPACE_, METHOD_NAME_);
        //Bodcodigo
        soRequest.addProperty("Bodcodigo", paramLectorRfid_.getCodbodega());
        soRequest.addProperty("Itmcodigo", pItmcodigo);

        SoapObject response = CallService(soRequest,SOAP_ACTION_,paramLectorRfid_.getEndpoint()+ URL_,true,false);

        if(TriggerException == null){
            return ResponseToGarmentSale(response);
        }
        else {
            garmentSaleObj = new GarmentSaleObj();
            garmentSaleObj.setStatus(new DataSourceDto( "9999", TriggerException, null));
            return  garmentSaleObj;
        }


    }
    private GarmentSaleObj ResponseToGarmentSale(SoapObject response)
    {
        //817857318004LAR
        GarmentSale garmentSale = null;
        List<GarmentSale> garmentSaleList = null;
        GarmentSize garmentSize = null;
        List<GarmentSize> garmentSizeList = null;

        GarmentSaleObj garmentSaleObj = new GarmentSaleObj();
        int totstockLocal = 0, totstockOtros = 0, stocklocal = 0, stockotros = 0 ;

        if(response.hasProperty("estado")){
            SoapObject soEstado = (SoapObject) response.getProperty("estado");
            garmentSaleObj.setStatus(new DataSourceDto( soEstado.getPropertyAsString("codigo"), soEstado.getPropertyAsString("mensaje"), null));

        }
        if(response.hasProperty("data")){
            SoapObject soData = (SoapObject) response.getProperty("data");
            if(soData.hasProperty("saldos")){
                SoapObject soSaldos = (SoapObject) soData.getProperty("saldos");

                if(soSaldos.getPropertyCount() > 0){

                    garmentSaleList = new ArrayList<GarmentSale>();
                    for(int x=0; x<soSaldos.getPropertyCount();x++)
                    {
                        SoapObject soColor = (SoapObject) soSaldos.getProperty(x);
                        garmentSale = new GarmentSale();

                        garmentSale.setColorName(soColor.getPropertyAsString("nombre"));
                        if(soColor.hasProperty("tallas"))
                        {
                            SoapObject soTallas = (SoapObject)  soColor.getProperty("tallas");
                            garmentSizeList = new ArrayList<GarmentSize>();
                            int totalTallas = soTallas.getPropertyCount();

                            totstockLocal= 0;
                            totstockOtros = 0;

                            for(int y=0; y<soTallas.getPropertyCount();y++)
                            {
                                garmentSize = new GarmentSize();
                                SoapObject soTalla = (SoapObject) soTallas.getProperty(y);
                               /* if(y == 0){
                                    garmentSize.setItmCodigo("0");
                                    garmentSize.setNombre("Talla");
                                    garmentSize.setStockLocal("Local");
                                    garmentSize.setStockOtros("Otros");
                                    garmentSize.setBusca(false);
                                }*/
                                stocklocal = Integer.parseInt(soTalla.getPropertyAsString("stockLocal"));
                                stockotros = Integer.parseInt(soTalla.getPropertyAsString("stockOtros"));
                                totstockLocal += stocklocal;
                                totstockOtros += stockotros;

                                garmentSize.setItmCodigo(soTalla.getPropertyAsString("itmCodigo"));
                                garmentSize.setNombre(soTalla.getPropertyAsString("nombre"));
                                garmentSize.setStockLocal(stocklocal );
                                garmentSize.setStockOtros(stockotros);
                                garmentSize.setBusca(Boolean.parseBoolean(soTalla.getPropertyAsString("busca")) );

                                garmentSizeList.add(garmentSize);
                                if(y == (totalTallas-1)){
                                    garmentSize = new GarmentSize();
                                    garmentSize.setItmCodigo("00");
                                    garmentSize.setNombre("Total");
                                    garmentSize.setStockLocal(totstockLocal);
                                    garmentSize.setStockOtros(totstockOtros);
                                    garmentSize.setBusca(false);
                                    garmentSizeList.add(garmentSize);
                                }
                            }
                            garmentSale.setSizes(garmentSizeList);
                        }


                        garmentSaleList.add(garmentSale);
                        //garmentSale.setSizes();

                    }
                }
            }
        }
        if(garmentSaleList != null && garmentSaleList.size() > 0){

            //garmentSaleObj.setStatus(new DataSourceDto( "00", "Exitoso", null));
            garmentSaleObj.setGarmentSaleList(garmentSaleList);

        }
        /*else {
            garmentSaleObj.setStatus(new DataSourceDto( "01", "Error en la Respuesta en el Servicio", null));
        }*/

        return  garmentSaleObj;
    }

    public List<StoreExistence>  WSInventoryPerStoreExistencePlace(String pItmcodigo){

        SoapObject soRequest = new SoapObject(NAMESPACE_, METHOD_NAME_);
        //Bodcodigo
        soRequest.addProperty("Bodcodigo", paramLectorRfid_.getCodbodega());
        soRequest.addProperty("Itmcodigo", pItmcodigo);

        //SoapObject response = CallService(soRequest,SOAP_ACTION_,URL_,true,false);

        return ResponseToStoreExistence(CallService(soRequest,SOAP_ACTION_,paramLectorRfid_.getEndpoint()+URL_,true,false));

    }

    private List<StoreExistence> ResponseToStoreExistence(SoapObject response)
    {

        SoapObject soLocal = null;
        StoreExistence existence = null;
        List<StoreExistence> existenceList = null;
        if(response.hasProperty("data")){
            SoapObject soData = (SoapObject) response.getProperty("data");
            if(soData.hasProperty("locales")){
                SoapObject soLocales = (SoapObject) soData.getProperty("locales");
                existenceList = new ArrayList<StoreExistence>();
                for(int x=0; x<soLocales.getPropertyCount();x++)
                {
                    soLocal = (SoapObject) soLocales.getProperty(x);
                    existence = new StoreExistence();
                    existence.setNameplace(soLocal.getPropertyAsString("nombre"));
                    existence.setStock(Integer.parseInt(soLocal.getPropertyAsString("stock")) );

                    existenceList.add(existence);
                }
            }
        }
        return existenceList;
    }

    public LoginData WSLogin(String user, String pass, Boolean isAdministrador)
    {

        TriggerException = null;
        LoginData loginData = null;
        SoapObject soRequest = new SoapObject(NAMESPACE_, METHOD_NAME_);


        String url = null;
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

        SoapObject soResponse = CallService(soRequest,SOAP_ACTION_,url,true,false);
        if(TriggerException == null){
            return  ResponseToLoginDataObj(soResponse);
        }
        else {
            loginData = new LoginData();
            loginData.setEstado(new DataSourceDto("9999", TriggerException, null));
            return  loginData;
        }

    }

    public boolean ExistDataBase(){
        if(paramLectorRfid_ != null){
            return true;
        }
        else {
            return false;
        }
    }

    private LoginData ResponseToLoginDataObj(SoapObject response)
    {
        LoginData loginData = null;
        DataSourceDto dtoEstado = null;
        DataSourceDto dtoUsuario = null;
        DataSourceDto dtoRol = null;
        LoginProgram loginProgram = null;

        SoapObject objSoap = null;


        List<LoginProgram> programList = null;
        if(response.hasProperty("estado")){
            objSoap = (SoapObject) response.getProperty("estado");
            if(objSoap.hasProperty("codigo") && objSoap.hasProperty("mensaje"))
            {
                dtoEstado = new DataSourceDto(objSoap.getPropertyAsString("codigo"),objSoap.getPropertyAsString("mensaje"), null);
            }
        }

        if(response.hasProperty("data")){
            objSoap = (SoapObject) response.getProperty("data");

            if(objSoap.hasProperty("usuario") ){
                SoapObject soUsuario = (SoapObject) objSoap.getProperty("usuario");
                if(soUsuario.hasProperty("id") && soUsuario.hasProperty("nombre")){
                    dtoUsuario = new DataSourceDto(soUsuario.getPropertyAsString("id"), soUsuario.getPrimitivePropertyAsString("nombre"), null);
                }
            }

            SoapObject objSoap2 = null;
            if(objSoap.hasProperty("rol")){

                objSoap2 = (SoapObject) objSoap.getProperty("rol");

                if(objSoap2.hasProperty("id") && objSoap2.hasProperty("nombre")){
                    dtoRol = new DataSourceDto(objSoap2.getPropertyAsString("id"), objSoap2.getPrimitivePropertyAsString("nombre"), null);
                }

            }

            if(objSoap.hasProperty("accesos")){
                objSoap2 = (SoapObject) objSoap.getProperty("accesos");

                /*if(soAccess.hasProperty("rol")){
                    SoapObject soRol = (SoapObject) soAccess.getProperty("rol");
                    if(soRol.hasProperty("id") && soRol.hasProperty("nombre")){
                        dtoRol = new DataSourceDto(soRol.getPropertyAsString("id"), soRol.getPrimitivePropertyAsString("nombre"), null);
                    }
                }*/

                programList = new ArrayList<LoginProgram>();
                for (int x=0; x<objSoap2.getPropertyCount();x++) {
                    SoapObject soPrograma = (SoapObject) objSoap2.getProperty(x);
                    if(soPrograma.hasProperty("nombre") && soPrograma.hasProperty("ruta")){
                        loginProgram = new LoginProgram();
                        loginProgram.setNombre(soPrograma.getPropertyAsString("nombre"));
                        loginProgram.setRuta(soPrograma.getPropertyAsString("ruta"));

                        programList.add(loginProgram);

                    }
                }



            }

        }

        loginData = new LoginData();
        loginData.setEstado(dtoEstado);
        loginData.setUsuario(dtoUsuario);
        loginData.setRol(dtoRol);
        loginData.setAccesos(programList);

        return loginData;
    }

    public Replenishments WSReposicion(List<String> listEpc, String Lincodigo, String Prdcodigo)
    {
        SoapObject soRequest = new SoapObject(NAMESPACE_, METHOD_NAME_);
        SoapObject soNivel4 ;
        SoapObject soNivel3 = new SoapObject(NAMESPACE_, "Nivel3");

        for (String epc:listEpc) {
            soNivel4 = new SoapObject(NAMESPACE_, null);
            soNivel4.addProperty("epc",epc);

            soNivel3.addProperty("etiquetas.etiqueta", soNivel4);
        }

        soRequest.addProperty("Etiquetas", soNivel3);
        soRequest.addProperty("Lincodigo", Lincodigo);
        soRequest.addProperty("Prdcodigo", Prdcodigo);
        soRequest.addProperty("Bodcodigo", paramLectorRfid_.getCodbodega());

        TriggerException = null;
        return  ResponseToObjReplenishment(CallService(soRequest,SOAP_ACTION_,paramLectorRfid_.getEndpoint()+URL_,true,false));

    }

    private Replenishments ResponseToObjReplenishment(SoapObject response)
    {
        SoapObject soItem;
        Replenishment replenishment;
        Replenishments replenishments = null;
        replenishments = new Replenishments();
        List<Replenishment> replenishmentList = null;
        DataSourceDtoEx sourceDtoEx;


        if(TriggerException == null)
        {
            if(response.hasProperty("estado")){
                sourceDtoEx = new DataSourceDtoEx();
                sourceDtoEx.setHandlerException(null);
                SoapObject soEstado = (SoapObject) response.getProperty("estado");
                sourceDtoEx.setInformationDto(new DataSourceDto(soEstado.getPropertyAsString("codigo"),soEstado.getPropertyAsString("mensaje"),null ));
                replenishments.setEstado(sourceDtoEx);
            }
            if(response.hasProperty("data"))
            {
                try {
                    SoapObject soData = (SoapObject) response.getProperty("data");
                    if(soData.hasProperty("rows"))
                    {
                        SoapObject soRows = (SoapObject) soData.getProperty("rows");
                        if(soRows.getPropertyCount() > 0)
                        {
                            replenishmentList = new ArrayList<Replenishment>();
                            for(int x=0; x<soRows.getPropertyCount();x++)
                            {
                                soItem = (SoapObject) soRows.getProperty(x);

                                replenishment = new Replenishment();
                                replenishment.setItmCodigo(soItem.getPropertyAsString("itmCodigo"));
                                replenishment.setCantidad_anterior( Integer.parseInt(soItem.getPropertyAsString("cantidad_anterior")));
                                replenishment.setDiferencia( Integer.parseInt(soItem.getPropertyAsString("diferencia")));
                                replenishment.setCantidad_egresos( Integer.parseInt(soItem.getPropertyAsString("cantidad_egresos")));
                                replenishment.setCantidad_ventas( Integer.parseInt(soItem.getPropertyAsString("cantidad_ventas")));
                                replenishment.setStock_otros( Integer.parseInt(soItem.getPropertyAsString("stock_otros")));
                                replenishment.setStock_local( Integer.parseInt(soItem.getPropertyAsString("stock_local")));

                                replenishmentList.add(replenishment);
                            }
                        }
                    }
                }
                catch (Exception ex){
                    TriggerException = ex.getMessage();
                }
            }
        }
        if(TriggerException != null) {
            WSException wsException = new WSException();
            replenishments = new Replenishments();
            sourceDtoEx = new DataSourceDtoEx();
            wsException.setExceptionExist(true);
            wsException.setExceptionMessage(TriggerException);

            sourceDtoEx.setHandlerException(wsException);
            sourceDtoEx.setInformationDto(null);

            replenishments.setEstado(sourceDtoEx);
        }

        replenishments.setReplenishments(replenishmentList);


        return replenishments;
    }

    public List<ReplenishmentSale> WsSimulationSaleDetail(){

        ReplenishmentSale replenishmentSale = new ReplenishmentSale();
        List<ReplenishmentSale> replenishmentSaleList = new ArrayList<ReplenishmentSale>();



        for(int i=0; i<10; i++){
            replenishmentSale = new ReplenishmentSale();
            replenishmentSale.setProduct("Sueter CK -Collection x"+i+"");
            replenishmentSale.setLocalname("San Marino");
            replenishmentSale.setCantidad(10+ i);
            replenishmentSaleList.add(replenishmentSale);
        }
        return replenishmentSaleList;
    }

    public List<ReplenishmentSale> WsReposicionSaldoDetalle(String Itmcodigo)
    {
        SoapObject soRequest = new SoapObject(NAMESPACE_, METHOD_NAME_);
        soRequest.addProperty("Itmcodigo", Itmcodigo);
        soRequest.addProperty("Bodcodigo", paramLectorRfid_.getCodbodega());

        TriggerException = null;
        return  ResponseToObjReplenishmentSale(CallService(soRequest,SOAP_ACTION_,paramLectorRfid_.getEndpoint()+URL_,true,false));

    }

    private List<ReplenishmentSale> ResponseToObjReplenishmentSale(SoapObject response)
    {
        ReplenishmentSale replenishmentSale = new ReplenishmentSale();
        SoapObject soItem;
        List<ReplenishmentSale> replenishmentSaleList = null;
        if (response.hasProperty("data"))
        {
            SoapObject soData = (SoapObject) response.getProperty("data");

            if(soData.hasProperty("locales_saldos")){
                SoapObject spLocales = (SoapObject) soData.getProperty("locales_saldos");


                if(spLocales.getPropertyCount() > 0)
                {
                    replenishmentSaleList = new ArrayList<ReplenishmentSale>();
                    for(int x=0; x<spLocales.getPropertyCount();x++)
                    {
                        soItem = (SoapObject) spLocales.getProperty(x);

                        replenishmentSale = new ReplenishmentSale();
                        replenishmentSale.setLocalname(soItem.getPropertyAsString("local"));
                        replenishmentSale.setCantidad( Integer.parseInt(soItem.getPropertyAsString("saldo")));

                        replenishmentSaleList.add(replenishmentSale);
                    }
                }

            }
        }
        return replenishmentSaleList;
    }

    public DataSourceDto WsSnapShot(List<String> listEpc ,String Lincodigo)
    {
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
        ///soRequest.addProperty("etiquetas", soNivel3);
        soRequest.addProperty("Lincodigo", Lincodigo);
        soRequest.addProperty("Prdcodigo", "");
        soRequest.addProperty("Bodcodigo", paramLectorRfid_.getCodbodega());

        TriggerException = null;
        return  ResponseToDataSourceDto(CallService(soRequest,SOAP_ACTION_,paramLectorRfid_.getEndpoint()+URL_,true,false));

    }

    private DataSourceDto ResponseToDataSourceDto(SoapObject response)
    {
        DataSourceDto sourceDto = null;
        String msj_error = null;
        if(TriggerException == null)
        {
            try {
                if(response != null ){

                    if(response.hasProperty("faultcode")){

                        msj_error = "Se ha producidon un error desconocido en el servicio";
                        String sfaultcode = response.getPropertyAsString("faultcode");

                        if(sfaultcode.equals("SOAP-ENV:Client")){
                            msj_error = "Se ha producidon un error del lado del cliente...";
                        }
                        else {
                            msj_error = "Se ha producidon un error del lado del servidor...";
                        }

                        sourceDto = new DataSourceDto("9998", msj_error, null);



                    }
                    else {
                        if(response.hasProperty("estado")){
                            SoapObject soEstado = (SoapObject)  response.getProperty("estado");
                            sourceDto = new DataSourceDto(soEstado.getPropertyAsString("codigo"), soEstado.getPropertyAsString("mensaje"), null);
                        }
                        else {
                            sourceDto = new DataSourceDto("9999", "El Servicio no devolvio ningun estado...", null);
                        }
                    }

                }
                else {
                    sourceDto = new DataSourceDto("9999", "Se ha producidon un error desconocido en el servicio...", null);
                }
            }
            catch (Exception ex){
                sourceDto = new DataSourceDto("9999", "Al Parecer la respuesta del servicio no es valida...", null);
            }

        }
        else {
            sourceDto = new DataSourceDto("9999", TriggerException, null);
        }
        return sourceDto;
    }
}
