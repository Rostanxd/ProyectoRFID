package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

public class ResponseVal {
    private boolean validAccess;
    private boolean fullCollection;
    private String errorMsg;

    public boolean isValidAccess() {
        return validAccess;
    }

    public void setValidAccess(boolean validAccess) {
        this.validAccess = validAccess;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public boolean isFullCollection() {
        return fullCollection;
    }

    public void setFullCollection(boolean fullCollection) {
        this.fullCollection = fullCollection;
    }
}
