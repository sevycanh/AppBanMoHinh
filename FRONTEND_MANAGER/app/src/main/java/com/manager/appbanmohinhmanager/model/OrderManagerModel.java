package com.manager.appbanmohinhmanager.model;

import java.util.List;

public class OrderManagerModel {
    boolean success;
    String message;
    List<OrderManager> result;

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

    public List<OrderManager> getResult() {
        return result;
    }

    public void setResult(List<OrderManager> result) {
        this.result = result;
    }
}
