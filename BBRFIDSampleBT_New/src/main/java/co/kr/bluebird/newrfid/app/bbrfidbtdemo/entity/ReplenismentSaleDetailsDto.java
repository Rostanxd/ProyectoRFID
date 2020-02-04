package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

import java.util.List;

public class ReplenismentSaleDetailsDto {
    private DataSourceDto estado;
    private List<ReplenishmentSale> replenishmentSales;

    public DataSourceDto getEstado() {
        return estado;
    }

    public void setEstado(DataSourceDto estado) {
        this.estado = estado;
    }

    public List<ReplenishmentSale> getReplenishmentSales() {
        return replenishmentSales;
    }

    public void setReplenishmentSales(List<ReplenishmentSale> replenishmentSales) {
        this.replenishmentSales = replenishmentSales;
    }
}
