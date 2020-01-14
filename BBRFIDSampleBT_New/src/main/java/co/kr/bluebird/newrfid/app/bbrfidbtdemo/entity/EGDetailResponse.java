package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

import java.util.List;

public class EGDetailResponse {

    public DataSourceDto status;
    public List<EGTagsResponseItem> items;

    public DataSourceDto getStatus() {
        return status;
    }

    public void setStatus(DataSourceDto status) {
        this.status = status;
    }

    public List<EGTagsResponseItem> getItems() {
        return items;
    }

    public void setItems(List<EGTagsResponseItem> items) {
        this.items = items;
    }

    /*public EGDetailResponse(DataSourceDto status, List<EGTagsResponseItem> items) {
        this.status = status;
        this.items = items;
    }*/
}
