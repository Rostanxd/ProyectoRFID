package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

public class SendTags {
    public String status;
    public String message;
    public int tags_quantity;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTags_quantity() {
        return tags_quantity;
    }

    public void setTags_quantity(int tags_quantity) {
        this.tags_quantity = tags_quantity;
    }

    public SendTags(String status, String message, int tags_quantity) {
        this.status = status;
        this.message = message;
        this.tags_quantity = tags_quantity;
    }
}
