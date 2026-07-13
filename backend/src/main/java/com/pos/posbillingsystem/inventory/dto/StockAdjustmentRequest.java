package com.pos.posbillingsystem.inventory.dto;

public class StockAdjustmentRequest {

    private Integer productId;
    private Integer adjustedStock;
    private String remarks;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getAdjustedStock() {
        return adjustedStock;
    }

    public void setAdjustedStock(Integer adjustedStock) {
        this.adjustedStock = adjustedStock;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}