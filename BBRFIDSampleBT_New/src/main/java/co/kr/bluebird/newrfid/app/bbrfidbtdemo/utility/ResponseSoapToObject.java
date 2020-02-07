package co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.util.ArrayList;
import java.util.List;

import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.DataSourceDto;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.DespatchGuide;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EGData;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EGDetailResponse;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EGTagsResponseItem;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.EntryGuide;
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
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ReceiveWareDetail;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.Replenishment;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ReplenishmentSale;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.Replenishments;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ReplenismentSaleDetailsDto;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.StoreExistence;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.StoreExistenceObj;

public class ResponseSoapToObject {

    private AppMessage appMessage;
    boolean GuiaEntradaProcesable = true;

    public ResponseSoapToObject() {

        appMessage = new AppMessage();
    }

    public EntryGuide GuiaEntradaOCResponse(SoapObject response, ExceptionData exceptionData)
    {
        EntryGuide entryGuideResponse = null;
        DataSourceDto dtoEstado = null;
        int cantidadtotal = 0;
        EGData data_ = null;

        if(exceptionData == null){

            Guide guide;
            List<Guide> guides;

            dtoEstado = StatusTagToDatasourceDto(response, exceptionData);

            if(dtoEstado.getCodigo().equals("00")){
                try {
                    if(response.hasProperty("data")){
                        SoapObject soapObject = (SoapObject) response.getProperty("data");
                        String cantTotal = soapObject.getPropertyAsString("cantidadTotal");
                        cantidadtotal = (cantTotal != null && !cantTotal.equals("")) ? Integer.parseInt(cantTotal) : 0;

                        if(soapObject.hasProperty("guias")){

                            SoapObject so_guides = (SoapObject) soapObject.getProperty("guias");

                            if(so_guides.getPropertyCount() > 0){

                                guides = new ArrayList<Guide>();
                                SoapObject so_guide;
                                int cant;
                                int saldo = 0;
                                for(int x=0; x<so_guides.getPropertyCount();x++){
                                    so_guide = (SoapObject) so_guides.getProperty(x);
                                    cant = Integer.parseInt(!so_guide.getPropertyAsString("cantidad").equals("") ? so_guide.getPropertyAsString("cantidad") : "0");
                                    if(so_guide.hasProperty("saldo")){
                                        saldo = ! so_guide.getPropertyAsString("saldo").equals("") ? Integer.parseInt(so_guide.getPropertyAsString("saldo")) : 0;
                                    }

                                    guide = new Guide(so_guide.getPropertyAsString("numero"), so_guide.getPropertyAsString("estadoCodigo"), so_guide.getPropertyAsString("estadoNombre"), cant, saldo );
                                    guides.add(guide);
                                }

                                data_ = new EGData(cantidadtotal ,guides);
                            }
                        }
                    }
                }
                catch (Exception ex){
                    dtoEstado = new DataSourceDto("9999", appMessage.outputMsg(2) +" -Guia entrada OC-", null);
                }
            }

        }
        else {
            dtoEstado = new DataSourceDto("9999", exceptionData.getMsgUsuario() ,null);
        }
        entryGuideResponse = new EntryGuide(dtoEstado,data_);

        return entryGuideResponse;
    }

    public DataSourceDto ResponseToDatasourceDto(SoapObject response, ExceptionData exceptionData){
        return StatusTagToDatasourceDto(response, exceptionData);
    }

    public EGDetailResponse ResponseToEGDetailResponse(SoapObject response, ExceptionData exceptionData, String ProcessType){

        DataSourceDto geEstado ;
        EGDetailResponse egDetailResponse = new EGDetailResponse();

        if(exceptionData == null)
        {
            geEstado = StatusTagToDatasourceDto(response, exceptionData);
            egDetailResponse.setStatus(geEstado);


            if(geEstado.getCodigo().equals("00")){

                try {
                    if(response.hasProperty("data")){
                        SoapObject so_data = (SoapObject) response.getProperty("data");
                        if(so_data.hasProperty("items")){
                            SoapObject so_items = (SoapObject) so_data.getProperty("items");
                            egDetailResponse.setItems(DataItemHomologacionToListEGTagsResponseItem(so_items, ProcessType));
                            egDetailResponse.setProcesable(GuiaEntradaProcesable);
                        }
                    }
                }
                catch (Exception ex){
                    egDetailResponse.setStatus(new DataSourceDto("9999", appMessage.outputMsg(2), null));
                }
            }

        }
        else {
            egDetailResponse.setStatus(new DataSourceDto("9999", exceptionData.getMsgUsuario(), null));
        }



        return  egDetailResponse;

    }

    /**
     * metodo generico para convertir respuesta soap con lista de clave valor utilizado frecuentemente en los spinners
     * @param response soapObject obtenido tras invocar al HttpTransportSE(URL)
     * @param exceptionData objecto que representa si hubo alguna excepcion al consumir el ws
     * @param propName nombre del tag q contiene la collection
     * @param propName2 nombre del tag q hace referencia a la descripcion del item(nombre, descripcion)
     * @param addSeleccione si al collection quiere add un item -seleccione-
     * @return estado de la solicitud soap y un collection de items clave-valor
     */
    public GenericSpinnerDto ResponseToGenericSpinnerDto(SoapObject response, ExceptionData exceptionData, String propName, String propName2, boolean addSeleccione)
    {
        GenericSpinnerDto spinnerDto = new GenericSpinnerDto();
        DataSourceDto dtoEstado = null;

        if(exceptionData == null){
            dtoEstado = StatusTagToDatasourceDto(response, exceptionData);
            spinnerDto.setEstado(dtoEstado);

            if(dtoEstado.getCodigo().equals("00")){
                try {
                    spinnerDto.setColeccion(InvocarResponseToCollectionDataSourceDto(response,propName, propName2, addSeleccione, false ));
                }
                catch (Exception ex){
                    spinnerDto.setEstado(new DataSourceDto("9999", appMessage.outputMsg(2), null));
                }

            }
        }
        else {
            spinnerDto.setEstado(new DataSourceDto("9999", exceptionData.getMsgUsuario(),null));
        }
        return spinnerDto;
    }

    /**
     *
     * @param response
     * @param exceptionData
     * @return
     */
    public GenericSpinnerDto ResponseToConteosGSDto(SoapObject response, ExceptionData exceptionData){

        GenericSpinnerDto spinnerDto = new GenericSpinnerDto();
        DataSourceDto dtoEstado ;
        List<DataSourceDto> dtoList ;

        if(exceptionData == null){
            dtoEstado = StatusTagToDatasourceDto(response, exceptionData);
            spinnerDto.setEstado(dtoEstado);

            if(dtoEstado.getCodigo().equals("00")){
                try {
                    dtoList = InvocarResponseToCollectionDataSourceDto(response,"conteos", null, true,true );
                    spinnerDto.setColeccion(dtoList);
                }
                catch (ClassCastException ex){
                    spinnerDto.setEstado(new DataSourceDto("9999", appMessage.outputMsg(3), null));
                }
                catch (Exception ex){
                    spinnerDto.setEstado(new DataSourceDto("9999", appMessage.outputMsg(2), null));
                }

            }
        }
        else {
            spinnerDto.setEstado(new DataSourceDto("9999", exceptionData.getMsgUsuario(),null));
        }
        return spinnerDto;

    }


    public DataSourceDto ResponseToDataSourceWithSecuenceGuia(SoapObject response, ExceptionData exceptionData, String nombreTagSecuenciaQR){

        DataSourceDto geEstado = null;
        EGDetailResponse egDetailResponse = new EGDetailResponse();

        if(exceptionData == null)
        {
            geEstado = StatusTagToDatasourceDto(response, exceptionData);

            if(geEstado.getCodigo().equals("00")){

                try {
                    if(response.hasProperty("data")){
                        SoapObject so_data = (SoapObject) response.getProperty("data");
                        if(so_data.hasProperty(nombreTagSecuenciaQR)){
                            geEstado.setAuxiliar(so_data.getPropertyAsString(nombreTagSecuenciaQR));
                        }
                    }
                }
                catch (Exception ex){
                    egDetailResponse.setStatus(new DataSourceDto("9999", appMessage.outputMsg(2), null));
                }
            }

        }
        else {
            egDetailResponse.setStatus(new DataSourceDto("9999", exceptionData.getMsgUsuario(), null));
        }
        return  geEstado;
    }


    public ReceiveWareDetail ResponseToReceiveWareDetail(SoapObject response, ExceptionData exceptionData){

        DataSourceDto geEstado ;
        ReceiveWareDetail wareDetail = new ReceiveWareDetail() ;

        if(exceptionData == null)
        {
            geEstado = StatusTagToDatasourceDto(response, exceptionData);

            if(geEstado.getCodigo().equals("00")){

                try {
                    if(response.hasProperty("data")){
                        SoapObject so_data = (SoapObject) response.getProperty("data");
                        wareDetail = ResponseToReceiveWareDetailData(so_data, wareDetail);
                    }
                }
                catch (Exception ex){
                    geEstado = new DataSourceDto("9999", appMessage.outputMsg(2), null);
                }
            }

        }
        else {
            geEstado = new DataSourceDto("9999", exceptionData.getMsgUsuario(), null);
        }

        wareDetail.setEstado(geEstado);

        return  wareDetail;
    }


    public InventoryControl ResponsetoInventoryControl(SoapObject response, ExceptionData exceptionData){
        InventoryControl inventoryControl = new InventoryControl();
        DataSourceDto geEstado = null;

        if(exceptionData == null)
        {
            geEstado = StatusTagToDatasourceDto(response, exceptionData);

            if(geEstado.getCodigo().equals("00")){

                try {
                    if(response.hasProperty("data")){
                        SoapObject so_data = (SoapObject) response.getProperty("data");
                        inventoryControl = ResponsetoInventoryControlData(so_data,inventoryControl );
                    }
                }
                catch (Exception ex){
                    geEstado = new DataSourceDto("9999", appMessage.outputMsg(2), null);
                }
            }

        }
        else {
            geEstado = new DataSourceDto("9999", exceptionData.getMsgUsuario(), null);
        }
        inventoryControl.setEstado(geEstado);
        return  inventoryControl;

    }

    public Garment ResponseToGarment(SoapObject response, ExceptionData exceptionData){

        DataSourceDto geEstado = null;
        Garment garment = new Garment();

        if(exceptionData == null)
        {
            geEstado = StatusTagToDatasourceDto(response, exceptionData);

            if(geEstado.getCodigo().equals("00")){

                try {
                    if(response.hasProperty("data")){
                        SoapObject so_data = (SoapObject) response.getProperty("data");
                        garment = ResponseToGarmentData(so_data, garment);
                    }
                }
                catch (Exception ex){
                    geEstado = new DataSourceDto("9999", appMessage.outputMsg(2), null);
                }
            }

        }
        else {
            geEstado = new DataSourceDto("9999", exceptionData.getMsgUsuario(), null);
        }
        garment.setEstado(geEstado);

        return garment;
    }


    public GarmentSaleObj ResponseToGarmentSale(SoapObject response, ExceptionData exceptionData){

        GarmentSaleObj garmentSaleObj = new GarmentSaleObj();
        DataSourceDto geEstado = null;
        List<GarmentSale> garmentSaleList = null;
        if(exceptionData == null)
        {
            geEstado = StatusTagToDatasourceDto(response, exceptionData);

            if(geEstado.getCodigo().equals("00")){

                try {
                    if(response.hasProperty("data")){
                        SoapObject so_data = (SoapObject) response.getProperty("data");
                        garmentSaleList = ResponseToGarmentSale(so_data);
                    }
                }
                catch (Exception ex){
                    geEstado = new DataSourceDto("9999", appMessage.outputMsg(2), null);
                }
            }

        }
        else {
            geEstado = new DataSourceDto("9999", exceptionData.getMsgUsuario(), null);
        }
        if(garmentSaleList != null && garmentSaleList.size() > 0){
            garmentSaleObj.setGarmentSaleList(garmentSaleList);
        }
        garmentSaleObj.setStatus(geEstado);

        return  garmentSaleObj;
    }

    // falta pedir a robert q envie un estado

    public StoreExistenceObj ResponseToStoreExistenceObj(SoapObject response, ExceptionData exceptionData){

        DataSourceDto geEstado = null;
        StoreExistenceObj storeExistenceObj = new StoreExistenceObj();
        List<StoreExistence> storeExistenceList = null ;

        if(exceptionData == null)
        {
            geEstado = StatusTagToDatasourceDto(response, exceptionData);

            if(geEstado.getCodigo().equals("00")){

                try {
                    if(response.hasProperty("data")){
                        SoapObject so_data = (SoapObject) response.getProperty("data");
                        storeExistenceList = ResponseToStoreExistence(so_data);
                    }
                }
                catch (Exception ex){
                    geEstado = new DataSourceDto("9999", appMessage.outputMsg(2), null);
                }
            }

        }
        else {
            geEstado = new DataSourceDto("9999", exceptionData.getMsgUsuario(), null);
        }
        storeExistenceObj.setEstado(geEstado);
        storeExistenceObj.setExistencias(storeExistenceList);

        return  storeExistenceObj;
    }

    public LoginData ResponseToLoginData(SoapObject response, ExceptionData exceptionData){

        DataSourceDto geEstado = null;
        LoginData loginData = new LoginData();

        if(exceptionData == null)
        {
            geEstado = StatusTagToDatasourceDto(response, exceptionData);

            if(geEstado.getCodigo().equals("00")){

                try {
                    if(response.hasProperty("data")){
                        SoapObject so_data = (SoapObject) response.getProperty("data");
                        loginData = ResponseToLoginTagData(so_data, loginData);
                    }
                }
                catch (Exception ex){
                    geEstado = new DataSourceDto("9999", appMessage.outputMsg(2), null);
                }
            }

        }
        else {
            geEstado = new DataSourceDto("9999", exceptionData.getMsgUsuario(), null);
        }
        loginData.setEstado(geEstado);

        return loginData;
    }

    public Replenishments ResponseToObjReplenishment(SoapObject response , ExceptionData exceptionData){

        DataSourceDto geEstado;
        Replenishments replenishments = new Replenishments();
        List<Replenishment> replenishmentList = null;

        if(exceptionData == null)
        {
            geEstado = StatusTagToDatasourceDto(response, exceptionData);

            if(geEstado.getCodigo().equals("00")){

                try {
                    if(response.hasProperty("data")){
                        SoapObject so_data = (SoapObject) response.getProperty("data");
                        replenishmentList = ResponseToObjReplenishmentData(so_data);
                    }
                }
                catch (Exception ex){
                    geEstado = new DataSourceDto("9999", appMessage.outputMsg(2), null);
                }
            }

        }
        else {
            geEstado = new DataSourceDto("9999", exceptionData.getMsgUsuario(), null);
        }

        replenishments.setEstado(geEstado);
        replenishments.setReplenishments(replenishmentList);

        return replenishments;
    }

    public ReplenismentSaleDetailsDto ResponseToObjReplenishmentSale(SoapObject response, ExceptionData exceptionData)
    {
        ReplenismentSaleDetailsDto replenismentSaleDetailsDto = new ReplenismentSaleDetailsDto();
        DataSourceDto geEstado;


        if(exceptionData == null)
        {
            geEstado = StatusTagToDatasourceDto(response, exceptionData);
            if(geEstado.getCodigo().equals("00")){

                try {
                    replenismentSaleDetailsDto.setReplenishmentSales(getListReplenishmentSale(response));
                }
                catch (Exception ex){
                    geEstado = new DataSourceDto("9999", appMessage.outputMsg(2), null);
                }
            }
        }
        else {
            geEstado = new DataSourceDto("9999", exceptionData.getMsgUsuario(), null);
        }

        replenismentSaleDetailsDto.setEstado(geEstado);


        return replenismentSaleDetailsDto;
    }
    /*metodos privados*/

    private List<ReplenishmentSale> getListReplenishmentSale(SoapObject response){

        ReplenishmentSale replenishmentSale ;
        List<ReplenishmentSale> replenishmentSaleList = null;
        SoapObject soItem;


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

    private List<Replenishment> ResponseToObjReplenishmentData(SoapObject soData ){

        SoapObject soItem;
        Replenishment replenishment;
        List<Replenishment> replenishmentList = null;

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
        return replenishmentList;
    }


    private LoginData  ResponseToLoginTagData(SoapObject objSoap, LoginData loginData ){

        DataSourceDto dtoUsuario = null;
        DataSourceDto dtoRol = null;
        LoginProgram loginProgram ;
        List<LoginProgram> programList = null;

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


        loginData.setUsuario(dtoUsuario);
        loginData.setRol(dtoRol);
        loginData.setAccesos(programList);

        return loginData;

    }

    private InventoryControl ResponsetoInventoryControlData(SoapObject soData, InventoryControl inventoryControl )
    {
        ICSeccion seccion = null;
        List<ICSeccion> icSeccionList = null;

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
        return inventoryControl;
    }

    /**
     *
     * @param responseData
     * @param receiveWareDetail
     * @return
     */
    private ReceiveWareDetail ResponseToReceiveWareDetailData(SoapObject responseData, ReceiveWareDetail receiveWareDetail ){

        String lDocOrigen = null, lDocDestino = null, lMotivo = null;
        int CantidadTotal = 0;
        List<EGTagsResponseItem> responseItemList = null;
        EGTagsResponseItem responseItem ;
        //ReceiveWareDetail receiveWareDetail = new ReceiveWareDetail();


        if(responseData.hasProperty("docOrigen")){
            lDocOrigen = responseData.getPropertyAsString("docOrigen");
        }
        if(responseData.hasProperty("docDestino")){
            lDocDestino = responseData.getPropertyAsString("docDestino");
        }
        if(responseData.hasProperty("motDescription")){
            lMotivo = responseData.getPropertyAsString("motDescription");
        }

        if(responseData.hasProperty("cantidadTotal")){
            CantidadTotal = Integer.parseInt(responseData.getPropertyAsString("cantidadTotal")) ;
        }
        if(responseData.hasProperty("itemsCantidades")){
            SoapObject SoDataItems = (SoapObject) responseData.getProperty("itemsCantidades");

            String [] ItemValue =  new String[9];
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
                ItemValue[8] = TagRespItem.getPropertyAsString("CantidadDoc");

                responseItem = new EGTagsResponseItem(ItemValue[0],ItemValue[1],ItemValue[2],ItemValue[3],ItemValue[4],ItemValue[5], Integer.valueOf(ItemValue[6]) ,Integer.valueOf(ItemValue[7]),null,null);
                responseItem.setCantidadDoc(Integer.valueOf(ItemValue[8]));
                responseItemList.add(responseItem);
            }
        }

        receiveWareDetail.setCantidadTotal(CantidadTotal);
        receiveWareDetail.setDetalle(responseItemList);

        receiveWareDetail.setDocOrigen(lDocOrigen);
        receiveWareDetail.setDocDestino(lDocDestino);
        receiveWareDetail.setMotDescription(lMotivo);

        return receiveWareDetail;
    }

    /**
     *
     * @param response
     * @param exceptionData
     * @return
     */
    private DataSourceDto StatusTagToDatasourceDto(SoapObject response, ExceptionData exceptionData){
        DataSourceDto dto = null;
        SoapObject soapObject;

        try {
            if(exceptionData == null){
                DataSourceDto dtoFaultCode = ValidarErrorFaultCode(response);
                if(dtoFaultCode == null)
                {
                    if(response != null && response.hasProperty("estado")){
                        soapObject = (SoapObject) response.getProperty("estado");
                        dto = new DataSourceDto(soapObject.getPropertyAsString("codigo"), soapObject.getPropertyAsString("mensaje"), null);
                    }
                    else {
                        dto = new DataSourceDto("9999", appMessage.outputMsg(1), null );
                    }
                }
                else {
                    dto = dtoFaultCode;
                }

            }
            else {
                dto = new DataSourceDto("9999", exceptionData.getMsgUsuario() ,null);
            }
        }
        catch (Exception ex){
            dto = new DataSourceDto("9999", appMessage.outputMsg(2) , null);
        }
        return dto;
    }

    private DataSourceDto ValidarErrorFaultCode(SoapObject response){
        DataSourceDto sourceDto = null ;
        if(response != null){
            if(response.hasProperty("faultcode")){

                String msj_error;

                String sfaultcode = response.getPropertyAsString("faultcode");

                if(sfaultcode.equals("SOAP-ENV:Client")){
                    msj_error = "Se ha producidon un error del lado del cliente...";
                }
                else {
                    msj_error = "Se ha producidon un error del lado del servidor...";
                }

                sourceDto = new DataSourceDto("9999", msj_error, null);

            }
        }
        else {
            sourceDto = new DataSourceDto("9999", "Se ha producidon un error desconocido en el servicio", null);
        }
        return sourceDto;
    }

    /**
     *
     * @param response
     * @return
     */
    private List<EGTagsResponseItem> DataItemHomologacionToListEGTagsResponseItem(SoapObject response, String ProcessType) {
        EGTagsResponseItem egTagsResponseItem;
        List<EGTagsResponseItem> egTagsResponseItemList = null;

        if(response != null && response.getPropertyCount() > 0){
            String [] ItemValue =  new String[11];
            List<String> _tagNoRead;
            List<String> _tagRead;
            GuiaEntradaProcesable = true;
            egTagsResponseItemList = new ArrayList<EGTagsResponseItem>();


            for (int x=0; x<response.getPropertyCount();x++)
            {
                SoapObject TagRespItem = (SoapObject) response.getProperty(x);

                ItemValue[0] = TagRespItem.getPropertyAsString("itemCodigo");
                if(ItemValue[0] != null && !ItemValue[0].equalsIgnoreCase("anyType{}")) {


                    /*SoapObject TagRespItem = (SoapObject) response.getProperty(x);*/
                    //ItemValue[0] = TagRespItem.getPropertyAsString("itemCodigo");
                    ItemValue[1] = TagRespItem.getPropertyAsString("itemGrupo1");
                    ItemValue[2] = TagRespItem.getPropertyAsString("itemGrupo2");
                    ItemValue[3] = TagRespItem.getPropertyAsString("itemGrupo3");
                    ItemValue[4] = TagRespItem.getPropertyAsString("itemGrupo4");
                    ItemValue[5] = TagRespItem.getPropertyAsString("itemGrupo5");
                    ItemValue[6] = TagRespItem.getPropertyAsString("CantidadLeidos");
                    ItemValue[7] = TagRespItem.getPropertyAsString("CantidadNoLeidos");
                    ItemValue[8] = TagRespItem.getPropertyAsString("CantidadDoc");


                    if((ProcessType == "GEN") && TagRespItem.hasProperty("EtiquetasDocActivas") && TagRespItem.hasProperty("EtiquetasDocPendientes") )
                    {
                        ItemValue[9] = TagRespItem.getPropertyAsString("EtiquetasDocActivas");
                        ItemValue[10] = TagRespItem.getPropertyAsString("EtiquetasDocPendientes");
                    }


                    _tagNoRead = new ArrayList<String>();
                    _tagRead = new ArrayList<String>();

                    SoapObject soLectura = null;

                    if (TagRespItem.hasProperty("data_leido")) {

                        soLectura = (SoapObject) TagRespItem.getProperty("data_leido");

                        if (soLectura.hasProperty("EPCs")) {

                            try {
                                SoapPrimitive spLeidosEPCs = (SoapPrimitive) soLectura.getProperty("EPCs");

                                if (!spLeidosEPCs.getValue().toString().equals("\n" + "\t\t\t\t\t\t\t\t")) {

                                    SoapObject soLeidosEPCs = (SoapObject) soLectura.getProperty("EPCs");
                                    for (int y = 0; y < soLeidosEPCs.getPropertyCount(); y++) {
                                        String epc = soLeidosEPCs.getPropertyAsString(y);
                                        _tagRead.add(epc);

                                    }

                                }
                            } catch (Exception ex) {
                                SoapObject soLeidosEPCs = (SoapObject) soLectura.getProperty("EPCs");
                                for (int y = 0; y < soLeidosEPCs.getPropertyCount(); y++) {
                                    String epc = soLeidosEPCs.getPropertyAsString(y);
                                    _tagRead.add(epc);

                                }
                            }

                        }
                    }

                    if (TagRespItem.hasProperty("data_no_leido")) {

                        soLectura = (SoapObject) TagRespItem.getProperty("data_no_leido");

                        if (soLectura.hasProperty("EPCs")) {

                            try {

                                SoapPrimitive spNoLeidosEPCs = (SoapPrimitive) soLectura.getProperty("EPCs");

                                if (!spNoLeidosEPCs.getValue().toString().equals("\n" + "\t\t\t\t\t\t\t\t")) {
                                    SoapObject soNoLeidosEPCs = (SoapObject) soLectura.getProperty("EPCs");

                                    for (int y = 0; y < soNoLeidosEPCs.getPropertyCount(); y++) {
                                        String epc = soNoLeidosEPCs.getPropertyAsString(y);
                                        _tagNoRead.add(epc);

                                    }
                                }

                            } catch (Exception ex) {
                                SoapObject soNoLeidosEPCs = (SoapObject) soLectura.getProperty("EPCs");

                                for (int y = 0; y < soNoLeidosEPCs.getPropertyCount(); y++) {
                                    String epc = soNoLeidosEPCs.getPropertyAsString(y);
                                    _tagNoRead.add(epc);

                                }
                            }

                        }
                    }

                    egTagsResponseItem = new EGTagsResponseItem(ItemValue[0], ItemValue[1], ItemValue[2], ItemValue[3], ItemValue[4], ItemValue[5], Integer.valueOf(ItemValue[6]), Integer.valueOf(ItemValue[7]), _tagRead, _tagNoRead);

                    if((ProcessType == "GEN") && ItemValue[9] != null && !ItemValue[9].equals("") && ItemValue[10] != null && !ItemValue[10].equals("") )
                    {
                        egTagsResponseItem.setEtiquetasDocActivas(Integer.parseInt(ItemValue[9]));
                        egTagsResponseItem.setEtiquetasDocPendientes(Integer.parseInt(ItemValue[10]));
                    }



                    egTagsResponseItem.setCantidadDoc(Integer.valueOf(ItemValue[8]));

                    // if(cant_leidos != cant_documentado)
                    if (Integer.valueOf(ItemValue[6]) != Integer.valueOf(ItemValue[8])) {
                        GuiaEntradaProcesable = false;
                    }
                    if(ProcessType == "GEN"){
                        if(egTagsResponseItem.getEtiquetasDocPendientes() > 0 || ItemValue[0].equals("OTROS")){
                            egTagsResponseItemList.add(egTagsResponseItem);
                        }
                    }
                    else {
                        egTagsResponseItemList.add(egTagsResponseItem);
                    }
                }
            }
        }
        return egTagsResponseItemList;

    }

    /**
     *
     * @param response
     * @param propNombreCollection
     * @param propNombreItem
     * @param addItemSeleccine
     * @return
     */
    private List<DataSourceDto> InvocarResponseToCollectionDataSourceDto(SoapObject response, String propNombreCollection, String propNombreItem, boolean addItemSeleccine, boolean isConteos){

        List<DataSourceDto> dtoList = null;
        if(response.hasProperty("data")){
            SoapObject soapObject = (SoapObject) response.getProperty("data");

            if(soapObject.hasProperty(propNombreCollection)){
                soapObject = (SoapObject) soapObject.getProperty(propNombreCollection);
                dtoList = getGenericListDataSourceDto(soapObject, propNombreItem, addItemSeleccine, isConteos);

            }
        }
        return dtoList;
    }

    /**
     *
     * @param request
     * @param nombreOrDesc
     * @param addSeleccione
     * @return
     */
    private List<DataSourceDto> getGenericListDataSourceDto(SoapObject request, String nombreOrDesc, boolean addSeleccione, boolean isConteo){

        DataSourceDto dtoItem;
        List<DataSourceDto> dtoListItem = null ;

        if(request != null && request.getPropertyCount() > 0){
            dtoListItem = new ArrayList<>();
            if (addSeleccione) {
                dtoListItem.add(new DataSourceDto("0", "- Seleccione -", null));
            }
            if(isConteo){
                for (int x = 0; x < request.getPropertyCount(); x++) {
                    dtoItem = new DataSourceDto(String.valueOf(x + 1), request.getPropertyAsString(x), null);
                    dtoListItem.add(dtoItem);
                }
            }
            else {
                for (int x = 0; x < request.getPropertyCount(); x++) {
                    SoapObject so_item = (SoapObject) request.getProperty(x);
                    dtoItem = new DataSourceDto(so_item.getPropertyAsString("codigo"), so_item.getPropertyAsString(nombreOrDesc), null);
                    dtoListItem.add(dtoItem);
                }
            }

        }
        return  dtoListItem;
    }


    private Garment ResponseToGarmentData(SoapObject soData, Garment garment){

        List<String> listImagenes = null;

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
        return garment;
    }

    private List<GarmentSale> ResponseToGarmentSale(SoapObject soData ){

        List<GarmentSale> garmentSaleList = null;
        GarmentSale garmentSale;
        GarmentSize garmentSize;
        List<GarmentSize> garmentSizeList;
        int totstockLocal = 0, totstockOtros = 0, stocklocal = 0, stockotros = 0 ;

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
                }
            }
        }
        return garmentSaleList;
    }

    private List<StoreExistence> ResponseToStoreExistence(SoapObject soData)
    {

        SoapObject soLocal;
        StoreExistence existence ;
        List<StoreExistence> existenceList = null;


        if(soData.hasProperty("locales")){
            SoapObject soLocales = (SoapObject) soData.getProperty("locales");
            if(soLocales.getPropertyCount() > 0){
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
}
