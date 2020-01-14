package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

public class DataSourceDtoEx {
    public WSException handlerException;
    public DataSourceDto informationDto;

    public WSException getHandlerException() {
        return handlerException;
    }

    public void setHandlerException(WSException handlerException) {
        this.handlerException = handlerException;
    }

    public DataSourceDto getInformationDto() {
        return informationDto;
    }

    public void setInformationDto(DataSourceDto informationDto) {
        this.informationDto = informationDto;
    }
}
