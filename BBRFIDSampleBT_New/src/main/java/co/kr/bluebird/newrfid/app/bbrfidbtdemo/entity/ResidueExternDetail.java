package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

public class ResidueExternDetail {
    public String model;
    public String color;
    public int stock;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public ResidueExternDetail(String model, String color, int stock) {
        this.model = model;
        this.color = color;
        this.stock = stock;
    }
}
