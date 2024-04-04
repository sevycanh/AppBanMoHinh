package com.manager.appbanmohinhmanager.model;

import java.util.List;

public class CategoryManagerModel {
    boolean success;

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

    public List<CategoryManager> getResult() {
        return result;
    }

    public void setResult(List<CategoryManager> result) {
        this.result = result;
    }

    String message;
    List<CategoryManager> result;

}
