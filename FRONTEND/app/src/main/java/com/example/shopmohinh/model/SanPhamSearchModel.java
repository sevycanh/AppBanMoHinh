package com.example.shopmohinh.model;

import java.util.List;

public class SanPhamSearchModel {
    boolean success;
    String message;
    List<SanPhamSearch> result;

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

    public List<SanPhamSearch> getResult() {
        return result;
    }

    public void setResult(List<SanPhamSearch> result) {
        this.result = result;
    }
}
