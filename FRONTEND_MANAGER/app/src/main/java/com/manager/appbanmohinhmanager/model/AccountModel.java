package com.manager.appbanmohinhmanager.model;

import java.util.List;

public class AccountModel {
    boolean success;
    String message;
    List<Account> result;

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

    public List<Account> getResult() {
        return result;
    }

    public void setResult(List<Account> result) {
        this.result = result;
    }
}
