package com.example.shopmohinh.model;

import java.util.List;

public class InforDetailModel {
    boolean success;
    String message;
    List<InforDetail> result;

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

    public List<InforDetail> getResult() {
        return result;
    }

    public void setResult(List<InforDetail> result) {
        this.result = result;
    }
}
