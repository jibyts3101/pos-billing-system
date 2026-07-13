package com.pos.posbillingsystem.billing.dto;

import java.util.List;

import com.pos.posbillingsystem.billing.enums.PaymentMethod;

public class BillRequest {

    private Integer customerId;

    private Integer userId;

    private PaymentMethod paymentMethod;

    private List<BillItemRequest> items;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public List<BillItemRequest> getItems() {
        return items;
    }

    public void setItems(List<BillItemRequest> items) {
        this.items = items;
    }
}