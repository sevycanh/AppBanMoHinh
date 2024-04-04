package com.manager.appbanmohinhmanager.model;

import java.util.List;

public class StatisticalModel {
    boolean success;
    List<Statistical> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Statistical> getResult() {
        return result;
    }

    public void setResult(List<Statistical> result) {
        this.result = result;
    }
}
