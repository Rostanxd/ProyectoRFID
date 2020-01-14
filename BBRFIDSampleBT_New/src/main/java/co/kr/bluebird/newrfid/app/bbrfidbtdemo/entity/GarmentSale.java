package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

import java.util.List;

public class GarmentSale {

    public String colorName;
    public List<GarmentSize> sizes;

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public List<GarmentSize> getSizes() {
        return sizes;
    }

    public void setSizes(List<GarmentSize> sizes) {
        this.sizes = sizes;
    }
}
