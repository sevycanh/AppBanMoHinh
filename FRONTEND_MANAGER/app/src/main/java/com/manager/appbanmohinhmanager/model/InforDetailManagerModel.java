package com.manager.appbanmohinhmanager.model;

import java.util.List;

public class InforDetailManagerModel {
    boolean success;
    String message;
    List<InforDetailManager> result;

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

    public List<InforDetailManager> getResult() {
        return result;
    }

    public void setResult(List<InforDetailManager> result) {
        this.result = result;
    }
}
