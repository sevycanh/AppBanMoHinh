package com.example.shopmohinh.model;

import java.util.List;

public class CouponModel {
    boolean success;
    String message;

    List<Coupon> result;

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

    public List<Coupon> getResult() {
        return result;
    }

    public void setResult(List<Coupon> result) {
        this.result = result;
    }
}
