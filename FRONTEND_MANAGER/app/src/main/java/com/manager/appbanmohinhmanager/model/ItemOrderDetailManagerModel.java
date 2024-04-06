package com.manager.appbanmohinhmanager.model;

import java.util.List;

public class ItemOrderDetailManagerModel {
    boolean success;
    String message;
    List<ItemOrderDetailManager> result;

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

    public List<ItemOrderDetailManager> getResult() {
        return result;
    }

    public void setResult(List<ItemOrderDetailManager> result) {
        this.result = result;
    }
}
