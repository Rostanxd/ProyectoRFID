package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

import java.util.List;

public class GarmentSaleObj {
    public DataSourceDto status;
    public List<GarmentSale> GarmentSaleList;

    public DataSourceDto getStatus() {
        return status;
    }

    public void setStatus(DataSourceDto status) {
        this.status = status;
    }

    public List<GarmentSale> getGarmentSaleList() {
        return GarmentSaleList;
    }

    public void setGarmentSaleList(List<GarmentSale> garmentSaleList) {
        GarmentSaleList = garmentSaleList;
    }
}
