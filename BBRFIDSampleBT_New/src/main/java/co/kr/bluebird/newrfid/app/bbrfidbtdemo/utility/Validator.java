package co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility;

import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.DataSourceDto;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.GenericSpinnerDto;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.ResponseVal;

public class Validator {

    private ResponseVal responseVal;

    public ResponseVal getValidateGenericDto(GenericSpinnerDto spinnerDto){
        responseVal = new ResponseVal();
        if(spinnerDto != null && spinnerDto.getEstado() != null && spinnerDto.getEstado().getCodigo().equals("00")){
            responseVal.setValidAccess(true);
            if(spinnerDto.getColeccion() != null && spinnerDto.getColeccion().size() > 0){
                responseVal.setFullCollection(true);
            }
            else {
                responseVal.setFullCollection(false);
            }
        }
        else {
            String msg = null;
            responseVal.setValidAccess(false);
            if(spinnerDto != null && spinnerDto.getEstado() != null && spinnerDto.getEstado().getDescripcion() != null ){
                msg = spinnerDto.getEstado().getDescripcion();
            }
            else {
                AppMessage appMessage = new AppMessage();
                msg = appMessage.outputMsg(4);
            }
            responseVal.setErrorMsg(msg);
        }
        return responseVal;
    }

    public  ResponseVal getValidateDataSourceDto(DataSourceDto dto){

        ResponseVal responseVal = new ResponseVal();
        if(dto != null && dto.getCodigo() != null && dto.getCodigo().equals("00")){
            responseVal.setValidAccess(true);
        }
        else {
            responseVal.setValidAccess(false);
            if(dto != null && dto.getDescripcion() !=  null){
                responseVal.setErrorMsg(dto.getDescripcion());
            }
            else {
                AppMessage appMessage = new AppMessage();
                responseVal.setErrorMsg(appMessage.outputMsg(4));
            }
        }
        return responseVal;

    }
}
