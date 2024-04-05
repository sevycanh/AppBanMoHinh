package com.example.shopmohinh.model;

import java.util.List;

public class ItemOrderDetailModel {
    boolean success;
    String message;
    List<ItemOrderDetail> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ItemOrderDetail> getResult() {
        return result;
    }

    public void setResult(List<ItemOrderDetail> result) {
        this.result = result;
    }
}
