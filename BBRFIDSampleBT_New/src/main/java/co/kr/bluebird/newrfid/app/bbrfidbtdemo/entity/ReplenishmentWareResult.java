package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

import java.util.List;

public class ReplenishmentWareResult {
    public int id;
    public String items;
    public String replenishmentCount;
    public String sales;
    public String expenses;
    public String residueWarehouse;
    public String residueExtern;
    public List<ResidueExternDetail> residueExternDetails;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getReplenishmentCount() {
        return replenishmentCount;
    }

    public void setReplenishmentCount(String replenishmentCount) {
        this.replenishmentCount = replenishmentCount;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getExpenses() {
        return expenses;
    }

    public void setExpenses(String expenses) {
        this.expenses = expenses;
    }

    public String getResidueWarehouse() {
        return residueWarehouse;
    }

    public void setResidueWarehouse(String residueWarehouse) {
        this.residueWarehouse = residueWarehouse;
    }

    public String getResidueExtern() {
        return residueExtern;
    }

    public void setResidueExtern(String residueExtern) {
        this.residueExtern = residueExtern;
    }

    public List<ResidueExternDetail> getResidueExternDetails() {
        return residueExternDetails;
    }

    public void setResidueExternDetails(List<ResidueExternDetail> residueExternDetails) {
        this.residueExternDetails = residueExternDetails;
    }

    public ReplenishmentWareResult(int id, String items, String replenishmentCount, String sales, String expenses, String residueWarehouse, String residueExtern, List<ResidueExternDetail> residueExternDetails) {
        this.id = id;
        this.items = items;
        this.replenishmentCount = replenishmentCount;
        this.sales = sales;
        this.expenses = expenses;
        this.residueWarehouse = residueWarehouse;
        this.residueExtern = residueExtern;
        this.residueExternDetails = residueExternDetails;
    }
}
