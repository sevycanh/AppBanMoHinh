package com.manager.appbanmohinhmanager.model;

import java.util.List;

public class ProductManagerModel {
    boolean success;
    String message;
    List<ProductManager> result;

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

    public List<ProductManager> getResult() {
        return result;
    }

    public void setResult(List<ProductManager> result) {
        this.result = result;
    }
}
