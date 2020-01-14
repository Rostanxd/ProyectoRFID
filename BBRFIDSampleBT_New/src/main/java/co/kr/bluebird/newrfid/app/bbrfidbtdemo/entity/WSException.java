package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

public class WSException {
    public boolean exceptionExist;
    public String exceptionMessage;

    public boolean isExceptionExist() {
        return exceptionExist;
    }

    public void setExceptionExist(boolean exceptionExist) {
        this.exceptionExist = exceptionExist;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
}
