package com.assignments.toystore.model;

import java.util.List;

public class ProductModel {
    boolean success;
    List<Product> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Product> getResult() {
        return result;
    }

    public void setResult(List<Product> result) {
        this.result = result;
    }
}
